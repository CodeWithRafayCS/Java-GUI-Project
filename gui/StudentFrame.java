package gui;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import models.Student;
import models.Subject;
import models.QuizResult;
import data.QuestionBank;
import utils.AppConstants;

public class StudentFrame extends JFrame implements ActionListener {

    private Student student;
    private QuestionBank questionBank;

    private JComboBox<String> cmbSemester;
    private JComboBox<String> cmbSubject;
    private JComboBox<String> cmbMode;
    private JComboBox<String> cmbDifficulty;
    private JButton btnStartQuiz;
    private JButton btnViewReport;
    private JButton btnLogout;
    private JLabel lblWelcome;
    private JTable reportTable;
    private DefaultTableModel reportModel;
    private JTabbedPane tabbedPane;

    public StudentFrame(Student student) {
        this.student = student;
        this.questionBank = QuestionBank.getInstance();
        initComponents();
        loadSubjects();
        loadReport();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void initComponents() {
        setTitle(AppConstants.APP_NAME + " - Student Dashboard");
        setSize(1000, 750);
        setMinimumSize(new Dimension(900, 650));
        setResizable(true);
        getContentPane().setBackground(AppConstants.COLOR_BACKGROUND);
        setLayout(new BorderLayout());

        // Top Panel with Gradient Effect
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
        topPanel.setPreferredSize(new Dimension(1000, 80));
        topPanel.setOpaque(false);

        lblWelcome = new JLabel(" Welcome, " + student.getFullName() + " (" + student.getRegistrationNumber() + ")");
        lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblWelcome.setForeground(Color.WHITE);
        topPanel.add(lblWelcome, BorderLayout.WEST);

        btnLogout = createStyledButton("Logout", new Color(244, 67, 54), new Dimension(110, 38));
        btnLogout.addActionListener(this);
        JPanel logoutPanel = new JPanel();
        logoutPanel.setOpaque(false);
        logoutPanel.add(btnLogout);
        topPanel.add(logoutPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // Tabbed Pane
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabbedPane.setBackground(AppConstants.COLOR_BACKGROUND);
        tabbedPane.addTab(" Take Quiz", createQuizPanel());
        tabbedPane.addTab(" My Reports", createReportPanel());

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createQuizPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(AppConstants.COLOR_BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 2;

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(255, 255, 255, 240));
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));

        GridBagConstraints fgbc = new GridBagConstraints();
        fgbc.insets = new Insets(8, 8, 8, 8);
        fgbc.fill = GridBagConstraints.HORIZONTAL;

        // Semester
        fgbc.gridx = 0; fgbc.gridy = 0;
        formPanel.add(createStyledLabel(" Semester:", AppConstants.COLOR_PRIMARY), fgbc);
        fgbc.gridx = 1;
        cmbSemester = new JComboBox<>(new String[]{"1st Semester", "2nd Semester"});
        cmbSemester.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbSemester.setBackground(Color.WHITE);
        cmbSemester.setPreferredSize(new Dimension(250, 35));
        cmbSemester.addActionListener(e -> loadSubjects());
        formPanel.add(cmbSemester, fgbc);

        // Subject
        fgbc.gridx = 0; fgbc.gridy = 1;
        formPanel.add(createStyledLabel(" Subject:", AppConstants.COLOR_PRIMARY), fgbc);
        fgbc.gridx = 1;
        cmbSubject = new JComboBox<>();
        cmbSubject.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbSubject.setBackground(Color.WHITE);
        cmbSubject.setPreferredSize(new Dimension(250, 35));
        formPanel.add(cmbSubject, fgbc);

        // Exam Mode
        fgbc.gridx = 0; fgbc.gridy = 2;
        formPanel.add(createStyledLabel("Exam Mode:", new Color(33, 150, 243)), fgbc);
        fgbc.gridx = 1;
        cmbMode = new JComboBox<>(new String[]{"Practice Mode", "Exam Mode", "Survival Mode"});
        cmbMode.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbMode.setBackground(Color.WHITE);
        cmbMode.setPreferredSize(new Dimension(250, 35));
        formPanel.add(cmbMode, fgbc);

        // Difficulty
        fgbc.gridx = 0; fgbc.gridy = 3;
        formPanel.add(createStyledLabel("Difficulty:", AppConstants.COLOR_WARNING), fgbc);
        fgbc.gridx = 1;
        cmbDifficulty = new JComboBox<>(new String[]{"Easy", "Medium", "Hard"});
        cmbDifficulty.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbDifficulty.setBackground(Color.WHITE);
        cmbDifficulty.setPreferredSize(new Dimension(250, 35));
        formPanel.add(cmbDifficulty, fgbc);

        // Start Button
        fgbc.gridx = 0; fgbc.gridy = 4;
        fgbc.gridwidth = 2;
        fgbc.anchor = GridBagConstraints.CENTER;
        btnStartQuiz = createStyledButton("Start Quiz", new Color(76, 175, 80), new Dimension(180, 45));
        btnStartQuiz.addActionListener(this);
        formPanel.add(btnStartQuiz, fgbc);

        // Info Panel
        JPanel infoPanel = new JPanel();
        infoPanel.setBackground(new Color(255, 250, 240));
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(255, 152, 0)),
                        "Exam Mode Information", TitledBorder.LEFT, TitledBorder.TOP,
                        new Font("Segoe UI", Font.BOLD, 12), new Color(255, 152, 0)),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setPreferredSize(new Dimension(500, 130));

        JLabel info1 = new JLabel("• Practice Mode: Unlimited attempts, hints available");
        JLabel info2 = new JLabel("• Exam Mode: Timed, no hints, one attempt");
        JLabel info3 = new JLabel("• Survival Mode: Quiz ends after 3 wrong answers");

        info1.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        info2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        info3.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        infoPanel.add(info1);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(info2);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(info3);

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(formPanel, gbc);
        gbc.gridy = 1;
        panel.add(infoPanel, gbc);

        return panel;
    }

    private JPanel createReportPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(AppConstants.COLOR_BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String[] columns = {" Date", " Subject", " Mode", "Difficulty", " Score", " Passed"};
        reportModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        reportTable = new JTable(reportModel);
        reportTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        reportTable.setRowHeight(30);
        reportTable.setShowGrid(true);
        reportTable.setGridColor(new Color(230, 230, 230));

        // Table Header Styling
        JTableHeader header = reportTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(new Color(124, 77, 255));
        header.setForeground(Color.BLACK);
        header.setPreferredSize(new Dimension(header.getWidth(), 35));

        // Alternating row colors
        reportTable.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 245, 255));
                }
                setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(reportTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        scrollPane.getViewport().setBackground(Color.WHITE);

        btnViewReport = createStyledButton(" View Full Report", new Color(33, 150, 243), new Dimension(160, 38));
        btnViewReport.addActionListener(this);

        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(AppConstants.COLOR_BACKGROUND);
        btnPanel.add(btnViewReport);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(btnPanel, BorderLayout.SOUTH);

        return panel;
    }

    // Helper Methods for Styled Components
    private JLabel createStyledLabel(String text, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(color);
        return label;
    }

    private JButton createStyledButton(String text, Color bgColor, Dimension size) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(size);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    private void loadSubjects() {
        cmbSubject.removeAllItems();
        String semNum = String.valueOf(student.getSemester());
        ArrayList<Subject> subjects = questionBank.getSubjectsBySemester(semNum);
        if (subjects.isEmpty()) {
            cmbSubject.addItem("No subjects available");
        } else {
            for (Subject s : subjects) {
                cmbSubject.addItem(s.getSubjectName());
            }
        }
    }

    private void loadReport() {
        reportModel.setRowCount(0);
        ArrayList<QuizResult> results = student.getQuizHistory();
        for (QuizResult r : results) {
            Object[] row = {
                    r.getFormattedDate(),
                    r.getSubjectName(),
                    r.getExamMode(),
                    r.getDifficulty(),
                    String.format("%.1f%%", r.getScorePercentage()),
                    r.isPassed() ? "✅ Yes" : "❌ No"
            };
            reportModel.addRow(row);
        }
    }

    private void startQuiz() {
        if (cmbSubject.getSelectedItem() == null || cmbSubject.getSelectedItem().toString().equals("No subjects available")) {
            JOptionPane.showMessageDialog(this, "No subjects available for your semester!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String semester = cmbSemester.getSelectedItem().toString();
        String subjectName = cmbSubject.getSelectedItem().toString();
        String mode = cmbMode.getSelectedItem().toString();
        String difficulty = cmbDifficulty.getSelectedItem().toString();

        String semNum = semester.equals("1st Semester") ? "1" : "2";
        ArrayList<Subject> subjects = questionBank.getSubjectsBySemester(semNum);
        String subjectCode = "";
        for (Subject s : subjects) {
            if (s.getSubjectName().equals(subjectName)) {
                subjectCode = s.getSubjectCode();
                break;
            }
        }

        ArrayList<models.Question> questions = questionBank.getQuestionsBySubjectAndDifficulty(subjectCode, difficulty);
        if (questions.isEmpty()) {
            JOptionPane.showMessageDialog(this, " No questions available for this subject and difficulty level!\nPlease ask teacher to add questions.", "No Questions", JOptionPane.WARNING_MESSAGE);
            return;
        }

        this.dispose();
        QuizFrame quizFrame = new QuizFrame(student, subjectCode, subjectName, mode, difficulty);
        quizFrame.setVisible(true);
    }

    private void showFullReport() {
        String report = student.generateReport();
        JTextArea textArea = new JTextArea(report);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textArea.setEditable(false);
        textArea.setMargin(new Insets(10, 10, 10, 10));
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(650, 500));
        JOptionPane.showMessageDialog(this, scrollPane, " Student Report", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnStartQuiz) {
            startQuiz();
        } else if (e.getSource() == btnViewReport) {
            showFullReport();
        } else if (e.getSource() == btnLogout) {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                this.dispose();
                new LoginFrame().setVisible(true);
            }
        }
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            loadReport();
        }
    }
}