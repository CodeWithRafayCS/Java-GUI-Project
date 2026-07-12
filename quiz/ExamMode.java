package quiz;

import models.Question;
import models.QuizResult;

public class ExamMode extends QuizSession {
    private static final long serialVersionUID = 1L;
    
    private boolean autoSubmitted;
    private boolean quizFinished;
    
    public ExamMode(String studentRegNumber, String studentName, String subjectCode,
                    String subjectName, String difficulty) {
        super(studentRegNumber, studentName, subjectCode, subjectName, difficulty);
        this.autoSubmitted = false;
        this.quizFinished = false;
        setTimeLimitForDifficulty(difficulty);
    }
    
    private void setTimeLimitForDifficulty(String difficulty) {
        switch (difficulty.toLowerCase()) {
            case "easy": this.timeLimitSeconds = 300; break;
            case "medium": this.timeLimitSeconds = 240; break;
            case "hard": this.timeLimitSeconds = 180; break;
            default: this.timeLimitSeconds = 300;
        }
    }
    
    @Override
    public void startSession() {
        this.isActive = true;
        this.quizEnded = false;
        this.quizFinished = false;
        this.autoSubmitted = false;
        this.currentQuestionIndex = 0;
        this.score = 0;
        this.answerResults.clear();
        startTimer();
    }
    
    @Override
    public boolean submitAnswer(int selectedOption) {
        if (!isActive || quizEnded || quizFinished) {
            return false;
        }
        
        if (isTimeUp()) {
            autoSubmit();
            return false;
        }
        
        Question currentQ = getCurrentQuestion();
        if (currentQ == null) {
            return false;
        }
        
        boolean isCorrect = currentQ.isCorrect(selectedOption);
        
        // Record answer and increment score if correct
        answerResults.add(isCorrect);
        if (isCorrect) {
            score++;
        }
        
        // Check if this was the last question
        boolean wasLastQuestion = (currentQuestionIndex + 1 >= totalQuestions);
        
        if (wasLastQuestion) {
            // Quiz finished normally
            this.quizFinished = true;
            this.isActive = false;
            this.quizEnded = true;
        } else {
            // Move to next question
            currentQuestionIndex++;
        }
        
        return isCorrect;
    }
    
    @Override
    public boolean isQuizComplete() {
        return quizFinished || quizEnded || isTimeUp();
    }
    
    @Override
    public QuizResult endSession() {
        if (quizFinished && !autoSubmitted) {
            // Already finished normally
        }
        
        this.isActive = false;
        this.quizEnded = true;
        this.quizFinished = true;
        
        long elapsedTime = getElapsedTimeSeconds();
        int timeTaken = (int) Math.min(elapsedTime, timeLimitSeconds);
        
        QuizResult result = new QuizResult(
            studentRegNumber, studentName, subjectCode, subjectName,
            getModeName(), difficulty, totalQuestions, score, timeTaken
        );
        
        return result;
    }
    
    public void autoSubmit() {
        if (!isActive || quizFinished || quizEnded) {
            return;
        }
        
        this.autoSubmitted = true;
        this.isActive = false;
        this.quizEnded = true;
        this.quizFinished = true;
    }
    
    @Override
    public String getHint() {
        return "No hints in Exam Mode";
    }
    
    @Override
    public boolean canContinue() {
        return !quizFinished && !quizEnded && !isTimeUp();
    }
    
    @Override
    public int getRemainingAttempts() {
        return 1;
    }
    
    @Override
    protected String getModeName() {
        return "Exam Mode";
    }
}