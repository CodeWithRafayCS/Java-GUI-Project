package utils;

import java.awt.Color;
import java.awt.Font;

public class AppConstants {
    
    // Application Info
    public static final String APP_NAME = "Quiz & Exam Simulator";
    
    // Difficulty Levels
    public static final String[] DIFFICULTY_LEVELS = {"Easy", "Medium", "Hard"};
    
    // Exam Modes
    public static final String[] EXAM_MODES = {"Practice Mode", "Exam Mode", "Survival Mode"};
    
    // Semesters
    public static final String SEMESTER_1 = "1st Semester";
    public static final String SEMESTER_2 = "2nd Semester";
    
    // GUI Dimensions
    public static final int LOGIN_WIDTH = 500;
    public static final int LOGIN_HEIGHT = 550;
    
    // GUI Colors
    public static final Color COLOR_PRIMARY = new Color(124, 77, 255);
    public static final Color COLOR_SUCCESS = new Color(76, 175, 80);
    public static final Color COLOR_WARNING = new Color(255, 193, 7);
    public static final Color COLOR_DANGER = new Color(244, 67, 54);
    public static final Color COLOR_BACKGROUND = new Color(240, 242, 245);
    public static final Color COLOR_LIGHT = new Color(250, 250, 250);
    
    // GUI Fonts
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font FONT_NORMAL = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_BUTTON = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_QUESTION = new Font("Segoe UI", Font.BOLD, 16);
    
    // Default Credentials
    public static final String DEFAULT_TEACHER_ID = "TCH001";
    public static final String DEFAULT_TEACHER_PASSWORD = "admin123";
    public static final String DEFAULT_TEACHER_NAME = "Admin Teacher";
    
    // Messages
    public static final String MSG_EMPTY_FIELDS = "Please fill all fields!";
    public static final String MSG_LOGIN_FAILED = "Invalid credentials!";
    
    // Button Texts
    public static final String BTN_LOGIN = "Login";
    public static final String BTN_LOGOUT = "Logout";
    public static final String BTN_EXIT = "Exit";
    public static final String BTN_ADD = "Add";
    public static final String BTN_DELETE = "Delete";
    public static final String BTN_SEARCH = "Search";
    public static final String BTN_VIEW_REPORT = "View Report";
    public static final String BTN_START_QUIZ = "Start Quiz";
    public static final String BTN_REFRESH = "Refresh";
}