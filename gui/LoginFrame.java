package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import data.StudentDatabase;
import models.Student;
import models.Teacher;
import utils.AppConstants;
import utils.Validator;

public class LoginFrame extends JFrame implements ActionListener {

    private JComboBox<String> cmbUserType;
    private JTextField txtRegNo;
    private JTextField txtName;
    private JTextField txtTeacherId;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnExit;
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private JPanel mainContentPanel;

    private Teacher defaultTeacher;

    public LoginFrame() {
        initComponents();
        loadDefaultTeacher();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void initComponents() {
        setTitle(AppConstants.APP_NAME + " - Login");
        setSize(AppConstants.LOGIN_WIDTH, AppConstants.LOGIN_HEIGHT);
        setMinimumSize(new Dimension(500, 550));
        setResizable(true);
        getContentPane().setBackground(AppConstants.COLOR_BACKGROUND);

        // Main panel with GridBagLayout for flexible resizing
        mainContentPanel = new JPanel(new GridBagLayout());
        mainContentPanel.setBackground(AppConstants.COLOR_BACKGROUND);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;

        // Create a centered container panel
        JPanel centerContainer = new JPanel(new BorderLayout());
        centerContainer.setBackground(AppConstants.COLOR_BACKGROUND);
        centerContainer.setOpaque(true);

        // Title Panel
        JPanel titlePanel = createTitlePanel();
        centerContainer.add(titlePanel, BorderLayout.NORTH);

        // Card Panel for Student/Teacher forms
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setBackground(AppConstants.COLOR_BACKGROUND);
        cardPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        cardPanel.add(createStudentPanel(), "Student");
        cardPanel.add(createTeacherPanel(), "Teacher");

        centerContainer.add(cardPanel, BorderLayout.CENTER);

        // Bottom Panel - User Type Selection
        JPanel bottomPanel = createBottomPanel();
        centerContainer.add(bottomPanel, BorderLayout.SOUTH);

        // Add center container with constraints
        mainContentPanel.add(centerContainer, gbc);
        add(mainContentPanel);
    }

    private JPanel createTitlePanel() {
        JPanel titlePanel = new JPanel(new GridBagLayout());
        titlePanel.setBackground(AppConstants.COLOR_PRIMARY);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;

        JLabel titleLabel = new JLabel(AppConstants.APP_NAME);
        titleLabel.setFont(AppConstants.FONT_TITLE);
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel, gbc);

        // Add a subtle subtitle
        gbc.gridy = 1;
        JLabel subtitleLabel = new JLabel("Student Management System");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(230, 230, 230));
        titlePanel.add(subtitleLabel, gbc);

        return titlePanel;
    }

    private JPanel createStudentPanel() {
    JPanel panel = new JPanel(new GridBagLayout());
    panel.setBackground(AppConstants.COLOR_BACKGROUND);

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 10, 10, 10);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    // Title
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 2;
    gbc.anchor = GridBagConstraints.CENTER;
    JLabel studentTitle = new JLabel("Student Login");
    studentTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
    studentTitle.setForeground(AppConstants.COLOR_PRIMARY);
    panel.add(studentTitle, gbc);

    // Registration Number
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.gridwidth = 1;
    gbc.anchor = GridBagConstraints.WEST;
    JLabel lblRegNo = new JLabel("Reg No:");
    lblRegNo.setFont(AppConstants.FONT_NORMAL);
    panel.add(lblRegNo, gbc);

    gbc.gridx = 1;
    txtRegNo = new JTextField(20);
    txtRegNo.setFont(AppConstants.FONT_NORMAL);
    txtRegNo.setPreferredSize(new Dimension(250, 35));
    panel.add(txtRegNo, gbc);

    // Full Name
    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.anchor = GridBagConstraints.WEST;
    JLabel lblName = new JLabel("Full Name:");
    lblName.setFont(AppConstants.FONT_NORMAL);
    panel.add(lblName, gbc);

    gbc.gridx = 1;
    txtName = new JTextField(20);
    txtName.setFont(AppConstants.FONT_NORMAL);
    txtName.setPreferredSize(new Dimension(250, 35));
    panel.add(txtName, gbc);

    // Note
    gbc.gridx = 0;
    gbc.gridy = 3;
    gbc.gridwidth = 2;
    gbc.anchor = GridBagConstraints.CENTER;
    JLabel noteLabel = new JLabel(
        "<html><div style='text-align: center;'>" +
        "<font color='#FF6B6B'>ℹ️ Note:</font> You can only login if teacher has added you." +
        "</div></html>"
    );
    noteLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
    panel.add(noteLabel, gbc);

    // Buttons
    gbc.gridx = 0;
    gbc.gridy = 4;
    gbc.gridwidth = 2;
    gbc.insets = new Insets(20, 10, 10, 10);
    gbc.anchor = GridBagConstraints.CENTER;
    JPanel buttonPanel = createButtonPanel();
    panel.add(buttonPanel, gbc);

    return panel;
}


    private JPanel createTeacherPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(AppConstants.COLOR_BACKGROUND);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title for teacher panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel teacherTitle = new JLabel("Teacher Login");
        teacherTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        teacherTitle.setForeground(AppConstants.COLOR_PRIMARY);
        teacherTitle.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(teacherTitle, gbc);

        // Teacher ID
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        JLabel lblTeacherId = new JLabel("Teacher ID:");
        lblTeacherId.setFont(AppConstants.FONT_NORMAL);
        panel.add(lblTeacherId, gbc);

        gbc.gridx = 1;
        txtTeacherId = new JTextField(20);
        txtTeacherId.setFont(AppConstants.FONT_NORMAL);
        txtTeacherId.setPreferredSize(new Dimension(250, 35));
        panel.add(txtTeacherId, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setFont(AppConstants.FONT_NORMAL);
        panel.add(lblPassword, gbc);

        gbc.gridx = 1;
        txtPassword = new JPasswordField(20);
        txtPassword.setFont(AppConstants.FONT_NORMAL);
        txtPassword.setPreferredSize(new Dimension(250, 35));
        panel.add(txtPassword, gbc);

        // Note
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        JLabel noteLabel = new JLabel("<html><div style='text-align: center;'>Default ID: <b>" + AppConstants.DEFAULT_TEACHER_ID +
                "</b> | Password: <b>" + AppConstants.DEFAULT_TEACHER_PASSWORD + "</b></div></html>");
        noteLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        noteLabel.setForeground(new Color(100, 100, 100));
        noteLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(noteLabel, gbc);

        // Buttons
        gbc.gridy = 4;
        gbc.insets = new Insets(20, 10, 10, 10);
        JPanel buttonPanel = createButtonPanel();
        panel.add(buttonPanel, gbc);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(AppConstants.COLOR_BACKGROUND);

        btnLogin = new JButton(AppConstants.BTN_LOGIN);
        btnExit = new JButton(AppConstants.BTN_EXIT);

        styleButton(btnLogin, AppConstants.COLOR_SUCCESS);
        styleButton(btnExit, AppConstants.COLOR_DANGER);

        btnLogin.addActionListener(this);
        btnExit.addActionListener(this);

        buttonPanel.add(btnLogin);
        buttonPanel.add(btnExit);

        return buttonPanel;
    }

    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 15));
        bottomPanel.setBackground(AppConstants.COLOR_BACKGROUND);
        bottomPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 200, 200)));

        JLabel lblUserType = new JLabel("Login as:");
        lblUserType.setFont(AppConstants.FONT_NORMAL);

        cmbUserType = new JComboBox<>(new String[]{"Student", "Teacher"});
        cmbUserType.setFont(AppConstants.FONT_NORMAL);
        cmbUserType.setPreferredSize(new Dimension(120, 30));
        cmbUserType.addActionListener(e -> cardLayout.show(cardPanel, (String) cmbUserType.getSelectedItem()));
        
        bottomPanel.add(lblUserType);
        bottomPanel.add(cmbUserType);

        return bottomPanel;
    }

    private void styleButton(JButton button, Color bgColor) {
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(AppConstants.FONT_BUTTON);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(130, 42));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.darker());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
    }

    private void loadDefaultTeacher() {
        defaultTeacher = new Teacher(
                AppConstants.DEFAULT_TEACHER_ID,
                AppConstants.DEFAULT_TEACHER_NAME
        );
        defaultTeacher.setHashedPassword(AppConstants.DEFAULT_TEACHER_PASSWORD);
    }

    private void handleStudentLogin() {
        String regNumber = txtRegNo.getText().trim();
        String fullName = txtName.getText().trim();

        if (Validator.isEmpty(regNumber) || Validator.isEmpty(fullName)) {
            JOptionPane.showMessageDialog(this, AppConstants.MSG_EMPTY_FIELDS, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        StudentDatabase db = StudentDatabase.getInstance();

        if (db.validateStudentLogin(regNumber, fullName)) {
            Student student = db.findStudentByRegNumber(regNumber);
            JOptionPane.showMessageDialog(this, "Welcome " + student.getFullName() + "!", "Login Success", JOptionPane.INFORMATION_MESSAGE);
            this.dispose();
            new StudentFrame(student).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials or student not added by teacher!\n\nPlease contact your teacher to add you first.", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleTeacherLogin() {
        String teacherId = txtTeacherId.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (Validator.isEmpty(teacherId) || Validator.isEmpty(password)) {
            JOptionPane.showMessageDialog(this, AppConstants.MSG_EMPTY_FIELDS, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (defaultTeacher.getTeacherId().equals(AppConstants.DEFAULT_TEACHER_ID) && defaultTeacher.getHashedPassword().equals(AppConstants.DEFAULT_TEACHER_PASSWORD)) {
            JOptionPane.showMessageDialog(this, "Welcome " + defaultTeacher.getFullName() + "!", "Login Success", JOptionPane.INFORMATION_MESSAGE);
            this.dispose();
            new TeacherFrame(defaultTeacher).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, AppConstants.MSG_LOGIN_FAILED, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();
        String userType = (String) cmbUserType.getSelectedItem();

        if (action.equals(AppConstants.BTN_LOGIN)) {
            if (userType.equals("Student")) {
                handleStudentLogin();
            } else {
                handleTeacherLogin();
            }
        } else if (action.equals(AppConstants.BTN_EXIT)) {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to exit?", "Confirm Exit",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        }
    }
}