package gui;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import models.Student;
import models.Teacher;
import models.Question;
import models.Subject;
import data.StudentDatabase;
import data.QuestionBank;
import filehandler.FileManager;
import utils.AppConstants;

public class TeacherFrame extends JFrame implements ActionListener {

    private Teacher teacher;
    private StudentDatabase studentDB;
    private QuestionBank questionBank;
    private FileManager fileManager;

    private JTextField txtRegNo, txtStudentName;
    private JTable studentTable;
    private DefaultTableModel studentModel;
    private JComboBox<String> cmbSubject, cmbDifficulty;
    private JTextField txtQuestion, txtOption1, txtOption2, txtOption3, txtOption4, txtHint;
    private JComboBox<Integer> cmbCorrectOption;
    private JButton btnAddQuestion, btnImportFile;
    private JFileChooser fileChooser;

    private JComboBox<String> cmbReportStudent;
    private JTextArea reportArea;
    private JComboBox<String> cmbSemester;
    private JTabbedPane tabbedPane;

    public TeacherFrame(Teacher teacher) {
        this.teacher = teacher;
        this.studentDB = StudentDatabase.getInstance();
        this.questionBank = QuestionBank.getInstance();
        this.fileManager = FileManager.getInstance();
        initComponents();
        loadStudentTable();
        loadSubjectComboBox();
        loadReportStudentComboBox();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void initComponents() {
        setTitle(AppConstants.APP_NAME + " - Teacher Dashboard");
        setSize(1200, 800);
        setMinimumSize(new Dimension(1000, 700));
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
        topPanel.setPreferredSize(new Dimension(1200, 90));
        topPanel.setOpaque(false);

        JLabel lblWelcome = new JLabel("Welcome, " + teacher.getFullName() + " (" + teacher.getTeacherId() + ")");
        lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblWelcome.setForeground(Color.WHITE);
        topPanel.add(lblWelcome, BorderLayout.WEST);

        JButton btnLogout = createStyledButton("Logout", new Color(244, 67, 54), new Dimension(120, 40));
        btnLogout.addActionListener(this);
        JPanel logoutPanel = new JPanel();
        logoutPanel.setOpaque(false);
        logoutPanel.add(btnLogout);
        topPanel.add(logoutPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // Tabbed Pane with Custom Colors
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabbedPane.setBackground(AppConstants.COLOR_BACKGROUND);

        tabbedPane.addTab("Manage Students", createStudentPanel());
        tabbedPane.addTab("Manage Questions", createQuestionPanel());
        tabbedPane.addTab("View Reports", createReportPanel());

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createStudentPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(AppConstants.COLOR_BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        // Form Panel with Card-like appearance
        JPanel formPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        formPanel.setBackground(new Color(255, 255, 255, 230));
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        // Reg No
        JLabel lblRegNo = createStyledLabel("Reg No:", AppConstants.COLOR_PRIMARY);
        formPanel.add(lblRegNo);
        txtRegNo = createStyledTextField(15);
        formPanel.add(txtRegNo);

        // Name
        JLabel lblName = createStyledLabel("Name:", AppConstants.COLOR_PRIMARY);
        formPanel.add(lblName);
        txtStudentName = createStyledTextField(20);
        formPanel.add(txtStudentName);

        // Semester Dropdown
        JLabel lblSemester = createStyledLabel("Semester:", AppConstants.COLOR_PRIMARY);
        formPanel.add(lblSemester);
        cmbSemester = new JComboBox<>(new String[]{"1", "2"});
        cmbSemester.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbSemester.setBackground(Color.WHITE);
        cmbSemester.setPreferredSize(new Dimension(70, 35));
        formPanel.add(cmbSemester);

        // Add Button
        JButton btnAddStudent = createStyledButton("Add Student", new Color(76, 175, 80), new Dimension(140, 38));
        btnAddStudent.addActionListener(e -> addStudent());
        formPanel.add(btnAddStudent);

        // Delete Button
        JButton btnDeleteStudent = createStyledButton("Delete Student", new Color(244, 67, 54), new Dimension(140, 38));
        btnDeleteStudent.addActionListener(e -> deleteStudent());
        formPanel.add(btnDeleteStudent);

        // Table with enhanced styling
        String[] columns = {" Reg Number", " Full Name", " Semester", " Quizzes Taken", " Avg Score"};
        studentModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        studentTable = new JTable(studentModel);
        studentTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        studentTable.setRowHeight(32);
        studentTable.setIntercellSpacing(new Dimension(10, 5));
        studentTable.setShowGrid(true);
        studentTable.setGridColor(new Color(230, 230, 230));

        // Table Header Styling
        JTableHeader header = studentTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(124, 77, 255));
        header.setForeground(Color.BLACK);
        header.setPreferredSize(new Dimension(header.getWidth(), 40));

        // Alternating row colors
        studentTable.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
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

        JScrollPane scrollPane = new JScrollPane(studentTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        scrollPane.getViewport().setBackground(Color.WHITE);

        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createQuestionPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(AppConstants.COLOR_BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(255, 255, 255, 240));
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        // Subject
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(createStyledLabel("Subject:", AppConstants.COLOR_PRIMARY), gbc);
        gbc.gridx = 1;
        cmbSubject = new JComboBox<>();
        cmbSubject.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbSubject.setBackground(Color.WHITE);
        cmbSubject.setPreferredSize(new Dimension(250, 35));
        formPanel.add(cmbSubject, gbc);

        // Difficulty
        gbc.gridx = 2;
        formPanel.add(createStyledLabel("Difficulty:", AppConstants.COLOR_WARNING), gbc);
        gbc.gridx = 3;
        cmbDifficulty = new JComboBox<>(new String[]{"Easy", "Medium", "Hard"});
        cmbDifficulty.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbDifficulty.setBackground(Color.WHITE);
        cmbDifficulty.setPreferredSize(new Dimension(150, 35));
        formPanel.add(cmbDifficulty, gbc);

        // Question
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(createStyledLabel("❓ Question:", AppConstants.COLOR_PRIMARY), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        txtQuestion = createStyledTextField(50);
        txtQuestion.setPreferredSize(new Dimension(600, 40));
        formPanel.add(txtQuestion, gbc);

        // Options
        gbc.gridwidth = 1;
        gbc.gridy = 2;
        gbc.gridx = 0;
        formPanel.add(createStyledLabel("Option 1:", new Color(33, 150, 243)), gbc);
        gbc.gridx = 1;
        txtOption1 = createStyledTextField(30);
        formPanel.add(txtOption1, gbc);

        gbc.gridx = 2;
        formPanel.add(createStyledLabel("Option 2:", new Color(33, 150, 243)), gbc);
        gbc.gridx = 3;
        txtOption2 = createStyledTextField(30);
        formPanel.add(txtOption2, gbc);

        gbc.gridy = 3;
        gbc.gridx = 0;
        formPanel.add(createStyledLabel("Option 3:", new Color(33, 150, 243)), gbc);
        gbc.gridx = 1;
        txtOption3 = createStyledTextField(30);
        formPanel.add(txtOption3, gbc);

        gbc.gridx = 2;
        formPanel.add(createStyledLabel("Option 4:", new Color(33, 150, 243)), gbc);
        gbc.gridx = 3;
        txtOption4 = createStyledTextField(30);
        formPanel.add(txtOption4, gbc);

        // Correct Option & Hint
        gbc.gridy = 4;
        gbc.gridx = 0;
        formPanel.add(createStyledLabel("Correct Option:", new Color(76, 175, 80)), gbc);
        gbc.gridx = 1;
        cmbCorrectOption = new JComboBox<>(new Integer[]{1, 2, 3, 4});
        cmbCorrectOption.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cmbCorrectOption.setBackground(Color.WHITE);
        cmbCorrectOption.setPreferredSize(new Dimension(80, 35));
        formPanel.add(cmbCorrectOption, gbc);

        gbc.gridx = 2;
        formPanel.add(createStyledLabel("Hint:", new Color(255, 152, 0)), gbc);
        gbc.gridx = 3;
        txtHint = createStyledTextField(30);
        formPanel.add(txtHint, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setOpaque(false);

        btnAddQuestion = createStyledButton("Add Question", new Color(33, 150, 243), new Dimension(160, 45));
        btnAddQuestion.addActionListener(this);

        btnImportFile = createStyledButton("Import from CSV/TXT", new Color(76, 175, 80), new Dimension(200, 45));
        btnImportFile.addActionListener(this);

        buttonPanel.add(btnAddQuestion);
        buttonPanel.add(btnImportFile);

        gbc.gridy = 5;
        gbc.gridx = 0;
        gbc.gridwidth = 4;
        formPanel.add(buttonPanel, gbc);

        panel.add(formPanel, BorderLayout.NORTH);

        // Info Area with better styling
        JTextArea infoArea = new JTextArea(
                "📋 CSV Format: question,opt1,opt2,opt3,opt4,correctOption(1-4),difficulty,subjectCode,hint\n" +
                        "📄 TXT Format: Question: text\\n Option1: text\\nOption2: text\\nOption3: text\\nOption4: text\\n" +
                        "Answer: 1-4\\nDifficulty: Easy/Medium/Hard\\nSubjectCode: code\\nHint: text\\n---"
        );
        infoArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
        infoArea.setBackground(new Color(255, 250, 240));
        infoArea.setForeground(new Color(100, 100, 100));
        infoArea.setEditable(false);
        infoArea.setLineWrap(true);
        infoArea.setWrapStyleWord(true);

        // Set border with insets for JTextArea
        infoArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(new Color(255, 152, 0)),
                        "File Format Help",
                        TitledBorder.LEFT,
                        TitledBorder.TOP,
                        new Font("Segoe UI", Font.BOLD, 12),
                        new Color(255, 152, 0)
                ),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JScrollPane infoScroll = new JScrollPane(infoArea);
        infoScroll.setPreferredSize(new Dimension(900, 150));
        infoScroll.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        panel.add(infoScroll, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createReportPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(AppConstants.COLOR_BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        topPanel.setBackground(new Color(255, 255, 255, 230));
        topPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        topPanel.add(createStyledLabel("Select Student:", AppConstants.COLOR_PRIMARY));
        cmbReportStudent = new JComboBox<>();
        cmbReportStudent.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbReportStudent.setBackground(Color.WHITE);
        cmbReportStudent.setPreferredSize(new Dimension(300, 38));
        topPanel.add(cmbReportStudent);

        JButton btnViewReport = createStyledButton("View Report", new Color(33, 150, 243), new Dimension(140, 40));
        btnViewReport.addActionListener(this);
        topPanel.add(btnViewReport);

        JButton btnExportReport = createStyledButton("Export Report", new Color(76, 175, 80), new Dimension(140, 40));
        btnExportReport.addActionListener(this);
        topPanel.add(btnExportReport);

        JButton btnRefresh = createStyledButton("Refresh", new Color(255, 152, 0), new Dimension(120, 40));
        btnRefresh.addActionListener(e -> loadReportStudentComboBox());
        topPanel.add(btnRefresh);

        reportArea = new JTextArea();
        reportArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        reportArea.setEditable(false);
        reportArea.setBackground(new Color(255, 255, 255));
        reportArea.setMargin(new Insets(15, 15, 15, 15));

        JScrollPane scrollPane = new JScrollPane(reportArea);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    // Helper Methods for Styled Components
    private JLabel createStyledLabel(String text, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(color);
        return label;
    }

    private JTextField createStyledTextField(int columns) {
        JTextField field = new JTextField(columns);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        field.setBackground(Color.WHITE);
        return field;
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

    private void loadStudentTable() {
        studentModel.setRowCount(0);
        ArrayList<Student> students = studentDB.getAllStudents();
        for (Student s : students) {
            studentModel.addRow(new Object[]{
                    s.getRegistrationNumber(),
                    s.getFullName(),
                    s.getSemester(),
                    s.getQuizHistory().size(),
                    String.format("%.2f%%", s.calculateScore())
            });
        }
    }

    private void loadSubjectComboBox() {
        cmbSubject.removeAllItems();
        ArrayList<Subject> subjects = questionBank.getAllSubjects();
        if (subjects.isEmpty()) {
            cmbSubject.addItem("No subjects available");
        } else {
            for (Subject s : subjects) {
                cmbSubject.addItem(s.getSubjectCode() + " - " + s.getSubjectName());
            }
        }
    }

    private void loadReportStudentComboBox() {
        cmbReportStudent.removeAllItems();
        ArrayList<Student> students = studentDB.getAllStudents();
        if (students.isEmpty()) {
            cmbReportStudent.addItem("No students available");
        } else {
            for (Student s : students) {
                cmbReportStudent.addItem(s.getRegistrationNumber() + " - " + s.getFullName());
            }
        }
    }

    private void addStudent() {
        String regNo = txtRegNo.getText().trim();
        String name = txtStudentName.getText().trim();

        int semester = 1;
        if (cmbSemester != null && cmbSemester.getSelectedItem() != null) {
            semester = Integer.parseInt(cmbSemester.getSelectedItem().toString());
        }

        if (regNo.isEmpty() || name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠ Please fill all fields!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (studentDB.studentExists(regNo)) {
            JOptionPane.showMessageDialog(this, "⚠ Student already exists!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Student newStudent = new Student(regNo, name, semester);
        studentDB.addStudent(newStudent);
        teacher.addStudent(newStudent);

        txtRegNo.setText("");
        txtStudentName.setText("");
        if (cmbSemester != null) {
            cmbSemester.setSelectedIndex(0);
        }

        loadStudentTable();
        loadReportStudentComboBox();
        JOptionPane.showMessageDialog(this, "✅ Student added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void deleteStudent() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a student to delete", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String regNo = studentModel.getValueAt(selectedRow, 0).toString();
        int confirm = JOptionPane.showConfirmDialog(this, "Delete student " + regNo + "?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            studentDB.removeStudent(regNo);
            loadStudentTable();
            loadReportStudentComboBox();
            JOptionPane.showMessageDialog(this, "✅ Student deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void addQuestion() {
        // Check if subject is selected
        if (cmbSubject.getSelectedItem() == null || cmbSubject.getSelectedItem().toString().equals("No subjects available")) {
            JOptionPane.showMessageDialog(this, "⚠ No subject available! Please ensure subjects are loaded.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String subjectItem = cmbSubject.getSelectedItem().toString();
        String subjectCode = subjectItem.split(" - ")[0];
        String difficulty = cmbDifficulty.getSelectedItem().toString();
        String questionText = txtQuestion.getText().trim();
        String[] options = {
                txtOption1.getText().trim(),
                txtOption2.getText().trim(),
                txtOption3.getText().trim(),
                txtOption4.getText().trim()
        };
        int correctOption = (int) cmbCorrectOption.getSelectedItem() - 1;
        String hint = txtHint.getText().trim();

        if (questionText.isEmpty() || options[0].isEmpty() || options[1].isEmpty() ||
                options[2].isEmpty() || options[3].isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠ Please fill all question fields!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (hint.isEmpty()) {
            hint = "No hint available";
        }

        Question q = new Question(questionText, options, correctOption, difficulty, subjectCode, hint);
        questionBank.addQuestion(q, subjectCode);
        teacher.addQuestionManually(q);

        // Clear all fields
        txtQuestion.setText("");
        txtOption1.setText("");
        txtOption2.setText("");
        txtOption3.setText("");
        txtOption4.setText("");
        txtHint.setText("");
        cmbCorrectOption.setSelectedIndex(0);

        JOptionPane.showMessageDialog(this, "✅ Question added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void importQuestions() {
        // Check if subject is selected
        if (cmbSubject.getSelectedItem() == null || cmbSubject.getSelectedItem().toString().equals("No subjects available")) {
            JOptionPane.showMessageDialog(this, " Please select a valid subject before importing questions!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (fileChooser == null) {
            fileChooser = new JFileChooser(".");
        }

        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                ArrayList<Question> importedQuestions;
                if (file.getName().endsWith(".csv")) {
                    importedQuestions = fileManager.importQuestionsFromCSV(file.getAbsolutePath());
                } else if (file.getName().endsWith(".txt")) {
                    importedQuestions = fileManager.importQuestionsFromTXT(file.getAbsolutePath());
                } else {
                    JOptionPane.showMessageDialog(this, " Only .csv or .txt files supported!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String subjectItem = cmbSubject.getSelectedItem().toString();
                String subjectCode = subjectItem.split(" - ")[0];

                for (Question q : importedQuestions) {
                    q.setSubjectCode(subjectCode);
                    questionBank.addQuestion(q, subjectCode);
                    teacher.addQuestionManually(q);
                }

                JOptionPane.showMessageDialog(this, "✅ " + importedQuestions.size() + " questions imported successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, " Import failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void viewReport() {
        if (cmbReportStudent.getItemCount() == 0 || cmbReportStudent.getSelectedItem().toString().equals("No students available")) {
            JOptionPane.showMessageDialog(this, " No students available!", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String selected = cmbReportStudent.getSelectedItem().toString();
        String regNo = selected.split(" - ")[0];
        Student student = studentDB.findStudentByRegNumber(regNo);

        if (student != null) {
            reportArea.setText(student.generateReport());
        } else {
            reportArea.setText(" Student not found!");
        }
    }

    private void exportReport() {
        if (reportArea.getText().isEmpty() || reportArea.getText().equals(" Student not found!")) {
            JOptionPane.showMessageDialog(this, " No valid report to export!", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (fileChooser == null) {
            fileChooser = new JFileChooser(".");
        }

        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                fileManager.exportStudentReport(reportArea.getText(), fileChooser.getSelectedFile().getAbsolutePath());
                JOptionPane.showMessageDialog(this, " Report exported successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, " Export failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        if (cmd.equals("Add Student") || cmd.equals("Add Student")) {
            addStudent();
        } else if (cmd.equals("Delete Student") || cmd.equals("Delete Student")) {
            deleteStudent();
        } else if (cmd.equals("Add Question") || cmd.equals(" Add Question")) {
            addQuestion();
        } else if (cmd.equals("Import from CSV/TXT") || cmd.equals(" Import from CSV/TXT")) {
            importQuestions();
        } else if (cmd.equals("View Report") || cmd.equals(" View Report")) {
            viewReport();
        } else if (cmd.equals("Export Report") || cmd.equals(" Export Report")) {
            exportReport();
        } else if (cmd.equals("Logout") || cmd.equals("Logout")) {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                this.dispose();
                new LoginFrame().setVisible(true);
            }
        }
    }
}