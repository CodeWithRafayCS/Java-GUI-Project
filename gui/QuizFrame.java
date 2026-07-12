package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import models.Student;
import models.Question;
import models.QuizResult;
import quiz.*;
import data.QuestionBank;
import data.StudentDatabase;
import utils.AppConstants;

public class QuizFrame extends JFrame implements ActionListener {

    private Student student;
    private QuizSession quizSession;
    private QuestionBank questionBank;
    private StudentDatabase studentDB;

    private String subjectCode;
    private String subjectName;
    private String mode;
    private String difficulty;
    private ArrayList<Question> questions;

    private boolean quizEnded = false;

    private JLabel lblTimer, lblQuestionNo, lblScore, lblLives;
    private JTextArea txtQuestion;
    private JRadioButton[] optButtons;
    private ButtonGroup optionGroup;
    private JPanel optionsPanel;
    private JButton btnSubmit, btnNext, btnHint;
    private Timer timer;
    private int timeRemaining;

    public QuizFrame(Student student, String subjectCode, String subjectName, String mode, String difficulty) {
        this.student = student;
        this.subjectCode = subjectCode;
        this.subjectName = subjectName;
        this.mode = mode;
        this.difficulty = difficulty;
        this.questionBank = QuestionBank.getInstance();
        this.studentDB = StudentDatabase.getInstance();

        loadQuestions();
        createQuizSession();
        initComponents();
        startQuiz();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void loadQuestions() {
        questions = questionBank.getQuestionsBySubjectAndDifficulty(subjectCode, difficulty);
        if (questions.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No questions available!", "Error", JOptionPane.ERROR_MESSAGE);
            dispose();
        }
    }

    private void createQuizSession() {
        switch (mode) {
            case "Practice Mode":
                quizSession = new PracticeMode(student.getRegistrationNumber(), student.getFullName(),
                        subjectCode, subjectName, difficulty);
                break;
            case "Exam Mode":
                quizSession = new ExamMode(student.getRegistrationNumber(), student.getFullName(),
                        subjectCode, subjectName, difficulty);
                break;
            case "Survival Mode":
                quizSession = new SurvivalMode(student.getRegistrationNumber(), student.getFullName(),
                        subjectCode, subjectName, difficulty);
                break;
            default:
                quizSession = new PracticeMode(student.getRegistrationNumber(), student.getFullName(),
                        subjectCode, subjectName, difficulty);
        }

        quizSession.setQuestions(questions);
        quizSession.setTimeLimitSeconds(getTimeLimit());
    }

    private int getTimeLimit() {
        switch (difficulty) {
            case "Easy": return 300;
            case "Medium": return 240;
            case "Hard": return 180;
            default: return 300;
        }
    }

    private void initComponents() {
        setTitle(AppConstants.APP_NAME + " - " + mode + " - " + subjectName);
        setSize(900, 700);
        setMinimumSize(new Dimension(800, 600));
        setResizable(true);
        getContentPane().setBackground(AppConstants.COLOR_BACKGROUND);
        setLayout(new BorderLayout());

        // Top Panel with Gradient
        JPanel topPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(0, 0, AppConstants.COLOR_PRIMARY, getWidth(), 0, new Color(156, 39, 176));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        topPanel.setPreferredSize(new Dimension(900, 70));
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JPanel infoPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        infoPanel.setOpaque(false);

        lblTimer = new JLabel("⏱Time: --:--");
        lblTimer.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTimer.setForeground(Color.WHITE);

        lblQuestionNo = new JLabel("Question 1/" + questions.size());
        lblQuestionNo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblQuestionNo.setForeground(Color.WHITE);

        lblScore = new JLabel("Score: 0");
        lblScore.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblScore.setForeground(Color.WHITE);

        lblLives = new JLabel("");
        lblLives.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblLives.setForeground(Color.WHITE);

        infoPanel.add(lblTimer);
        infoPanel.add(lblQuestionNo);
        infoPanel.add(lblScore);
        infoPanel.add(lblLives);
        topPanel.add(infoPanel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        // Center Panel
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(AppConstants.COLOR_BACKGROUND);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 20, 30));

        // Question Panel with Card styling
        JPanel questionPanel = new JPanel(new BorderLayout());
        questionPanel.setBackground(Color.WHITE);
        questionPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        txtQuestion = new JTextArea();
        txtQuestion.setFont(new Font("Segoe UI", Font.BOLD, 16));
        txtQuestion.setWrapStyleWord(true);
        txtQuestion.setLineWrap(true);
        txtQuestion.setEditable(false);
        txtQuestion.setBackground(Color.WHITE);
        txtQuestion.setForeground(new Color(50, 50, 50));
        JScrollPane questionScroll = new JScrollPane(txtQuestion);
        questionScroll.setBorder(null);
        questionPanel.add(questionScroll, BorderLayout.CENTER);

        centerPanel.add(questionPanel, BorderLayout.NORTH);

        // Options Panel
        optionsPanel = new JPanel(new GridLayout(4, 1, 12, 12));
        optionsPanel.setBackground(AppConstants.COLOR_BACKGROUND);
        optionsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        optButtons = new JRadioButton[4];
        optionGroup = new ButtonGroup();
        for (int i = 0; i < 4; i++) {
            optButtons[i] = new JRadioButton();
            optButtons[i].setFont(new Font("Segoe UI", Font.PLAIN, 14));
            optButtons[i].setBackground(AppConstants.COLOR_BACKGROUND);
            optButtons[i].setCursor(new Cursor(Cursor.HAND_CURSOR));
            optionGroup.add(optButtons[i]);
            optionsPanel.add(optButtons[i]);
        }

        centerPanel.add(optionsPanel, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        // Bottom Panel
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        bottomPanel.setBackground(AppConstants.COLOR_BACKGROUND);
        bottomPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 200, 200)));

        btnSubmit = new JButton("Submit Answer");
        btnNext = new JButton("Next Question");
        btnHint = new JButton("Hint");

        styleButton(btnSubmit, new Color(76, 175, 80));
        styleButton(btnNext, new Color(33, 150, 243));
        styleButton(btnHint, new Color(255, 152, 0));

        btnSubmit.addActionListener(this);
        btnNext.addActionListener(this);
        btnHint.addActionListener(this);

        bottomPanel.add(btnSubmit);
        bottomPanel.add(btnNext);
        bottomPanel.add(btnHint);

        add(bottomPanel, BorderLayout.SOUTH);

        // Mode-specific settings (ORIGINAL LOGIC - UNCHANGED)
        if (mode.equals("Practice Mode")) {
            btnHint.setVisible(true);
        } else {
            btnHint.setVisible(false);
        }

        if (mode.equals("Survival Mode")) {
            lblLives.setVisible(true);
        } else {
            lblLives.setVisible(false);
        }

        if (mode.equals("Exam Mode")) {
            btnNext.setEnabled(false);
            btnNext.setVisible(false);
        }

        updateLivesDisplay();
    }

    private void styleButton(JButton button, Color bgColor) {
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(150, 42));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
    }

    private void startQuiz() {
        quizSession.startSession();
        startTimer();
        displayCurrentQuestion();
    }

    private void startTimer() {
        timeRemaining = quizSession.getTimeLimitSeconds();
        updateTimerDisplay();

        timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (!quizEnded && quizSession.isActive()) {
                    timeRemaining--;
                    SwingUtilities.invokeLater(() -> {
                        updateTimerDisplay();
                        if (timeRemaining <= 0 && !quizEnded) {
                            timer.cancel();
                            timeUp();
                        }
                    });
                }
            }
        }, 1000, 1000);
    }

    private void updateTimerDisplay() {
        int minutes = timeRemaining / 60;
        int seconds = timeRemaining % 60;
        lblTimer.setText(String.format("⏱Time: %02d:%02d", minutes, seconds));

        if (timeRemaining <= 30) {
            lblTimer.setForeground(Color.RED);
        } else if (timeRemaining <= 60) {
            lblTimer.setForeground(new Color(255, 152, 0));
        } else {
            lblTimer.setForeground(Color.WHITE);
        }
    }

    private void timeUp() {
        if (quizEnded) return;
        JOptionPane.showMessageDialog(this, "Time's Up! Quiz will be submitted.", "Time's Up", JOptionPane.WARNING_MESSAGE);

        if (quizSession instanceof ExamMode) {
            ((ExamMode) quizSession).autoSubmit();
        }
        endQuiz(true);
    }

    private void displayCurrentQuestion() {
        if (quizEnded) return;

        Question current = quizSession.getCurrentQuestion();
        if (current != null) {
            txtQuestion.setText(current.getQuestionText());
            String[] options = current.getOptions();
            for (int i = 0; i < 4; i++) {
                optButtons[i].setText((i+1) + ". " + options[i]);
                optButtons[i].setSelected(false);
            }
            optionGroup.clearSelection();

            lblQuestionNo.setText("Question " + (quizSession.getCurrentQuestionIndex() + 1) + "/" + quizSession.getTotalQuestions());
            lblScore.setText("Score: " + quizSession.getScore());
        }

        updateLivesDisplay();
    }

    private void updateLivesDisplay() {
        if (mode.equals("Survival Mode") && quizSession instanceof SurvivalMode) {
            SurvivalMode sm = (SurvivalMode) quizSession;
            int lives = sm.getRemainingLives();
            lblLives.setText("❤Lives: " + lives + "/3");
            if (lives == 1) {
                lblLives.setForeground(new Color(255, 152, 0));
            } else if (lives == 0) {
                lblLives.setForeground(Color.RED);
            } else {
                lblLives.setForeground(Color.WHITE);
            }
        }
    }

    private void submitAnswer() {
        if (quizEnded) return;

        int selected = -1;
        for (int i = 0; i < 4; i++) {
            if (optButtons[i].isSelected()) {
                selected = i;
                break;
            }
        }

        if (selected == -1) {
            JOptionPane.showMessageDialog(this, "Please select an answer!", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Submit the answer to the quiz session
        boolean isCorrect = quizSession.submitAnswer(selected);

        // Show feedback
        if (isCorrect) {
            JOptionPane.showMessageDialog(this, "Correct!", "Good Job", JOptionPane.INFORMATION_MESSAGE);
        } else {
            if (mode.equals("Practice Mode")) {
                JOptionPane.showMessageDialog(this, "Wrong answer! Try again.", "Incorrect", JOptionPane.ERROR_MESSAGE);
                // Clear selection for practice mode so user can retry
                optionGroup.clearSelection();
            } else {
                JOptionPane.showMessageDialog(this, "Wrong answer!", "Incorrect", JOptionPane.INFORMATION_MESSAGE);
            }
        }

        // Update display
        lblScore.setText("Score: " + quizSession.getScore());
        updateLivesDisplay();

        // Check if quiz is complete - ONLY ONE PLACE THAT CALLS endQuiz
        if (quizSession.isQuizComplete() && !quizEnded) {
            endQuiz(false);
        } else if (!quizEnded && !mode.equals("Practice Mode")) {
            // For non-practice modes, move to next question display
            displayCurrentQuestion();
        } else if (!quizEnded && mode.equals("Practice Mode") && isCorrect) {
            // For practice mode, only move to next question if answer was correct
            displayCurrentQuestion();
        }
    }

    private void nextQuestion() {
        if (quizEnded) return;
        if (quizSession.getCurrentQuestionIndex() + 1 < quizSession.getTotalQuestions()) {
            quizSession.moveToNextQuestion();
            displayCurrentQuestion();
        }
    }

    private void showHint() {
        if (mode.equals("Practice Mode") && quizSession instanceof PracticeMode) {
            String hint = quizSession.getHint();
            JOptionPane.showMessageDialog(this, "💡 Hint: " + hint, "Hint", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void endQuiz(boolean timeUp) {
        if (quizEnded) return;
        quizEnded = true;

        if (timer != null) {
            timer.cancel();
        }

        QuizResult result = quizSession.endSession();

        if (result != null){
            studentDB.addQuizResult(student.getRegistrationNumber(), result);

            String message = String.format(
                    "📊 Quiz Completed!\n\nSubject: %s\nMode: %s\nDifficulty: %s\n\nScore: %d/%d (%.1f%%)\n%s",
                    subjectName, mode, difficulty,
                    result.getCorrectAnswers(), result.getTotalQuestions(),
                    result.getScorePercentage(),
                    result.isPassed() ? "✅ PASSED" : "❌ FAILED"
            );

            if (timeUp) {
                message = "TIME'S UP!\n\n" + message;
            }

            JOptionPane.showMessageDialog(this, message, "Quiz Result", JOptionPane.INFORMATION_MESSAGE);
        }

        dispose();
        new StudentFrame(student).setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnSubmit) {
            submitAnswer();
        } else if (e.getSource() == btnNext) {
            nextQuestion();
        } else if (e.getSource() == btnHint) {
            showHint();
        }
    }
}