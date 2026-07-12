package quiz;

import models.Question;
import models.QuizResult;

public class SurvivalMode extends QuizSession {
    private static final long serialVersionUID = 1L;
    
    private int wrongAnswersCount;
    private final int MAX_WRONG = 3;
    private int streakCorrect;
    private int maxStreak;
    private boolean quizFinished;
    
    public SurvivalMode(String studentRegNumber, String studentName, String subjectCode,
                        String subjectName, String difficulty) {
        super(studentRegNumber, studentName, subjectCode, subjectName, difficulty);
        this.wrongAnswersCount = 0;
        this.streakCorrect = 0;
        this.maxStreak = 0;
        this.quizFinished = false;
        setTimeLimitForDifficulty(difficulty);
    }
    
    private void setTimeLimitForDifficulty(String difficulty) {
        switch (difficulty.toLowerCase()) {
            case "easy": this.timeLimitSeconds = 450; break;
            case "medium": this.timeLimitSeconds = 360; break;
            case "hard": this.timeLimitSeconds = 300; break;
            default: this.timeLimitSeconds = 450;
        }
    }
    
    @Override
    public void startSession() {
        this.isActive = true;
        this.quizEnded = false;
        this.quizFinished = false;
        this.currentQuestionIndex = 0;
        this.score = 0;
        this.wrongAnswersCount = 0;
        this.streakCorrect = 0;
        this.maxStreak = 0;
        this.answerResults.clear();
        startTimer();
    }
    
    @Override
    public boolean submitAnswer(int selectedOption) {
        if (!isActive || quizEnded || quizFinished) {
            return false;
        }
        
        if (isTimeUp()) {
            this.quizFinished = true;
            this.isActive = false;
            this.quizEnded = true;
            return false;
        }
        
        if (wrongAnswersCount >= MAX_WRONG) {
            this.quizFinished = true;
            this.isActive = false;
            this.quizEnded = true;
            return false;
        }
        
        Question currentQ = getCurrentQuestion();
        if (currentQ == null) {
            return false;
        }
        
        boolean isCorrect = currentQ.isCorrect(selectedOption);
        
        if (isCorrect) {
            // Correct answer
            score++;
            streakCorrect++;
            answerResults.add(true);
            if (streakCorrect > maxStreak) {
                maxStreak = streakCorrect;
            }
        } else {
            // Wrong answer - lose a life
            wrongAnswersCount++;
            streakCorrect = 0;
            answerResults.add(false);
        }
        
        // Check if game over due to wrong answers
        if (wrongAnswersCount >= MAX_WRONG) {
            this.quizFinished = true;
            this.isActive = false;
            this.quizEnded = true;
            return isCorrect;
        }
        
        // Check if this was the last question
        boolean wasLastQuestion = (currentQuestionIndex + 1 >= totalQuestions);
        
        if (wasLastQuestion) {
            this.quizFinished = true;
            this.isActive = false;
            this.quizEnded = true;
        } else {
            // Move to next question regardless of correct/wrong
            currentQuestionIndex++;
        }
        
        return isCorrect;
    }
    
    @Override
    public boolean isQuizComplete() {
        return quizFinished || quizEnded || isTimeUp() || wrongAnswersCount >= MAX_WRONG;
    }
    
    @Override
    public QuizResult endSession() {
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
    
    @Override
    public String getHint() {
        return "No hints in Survival Mode";
    }
    
    @Override
    public boolean canContinue() {
        return !quizFinished && !quizEnded && !isTimeUp() && wrongAnswersCount < MAX_WRONG;
    }
    
    @Override
    public int getRemainingAttempts() {
        return MAX_WRONG - wrongAnswersCount;
    }
    
    @Override
    protected String getModeName() {
        return "Survival Mode";
    }
    
    public int getRemainingLives() {
        return MAX_WRONG - wrongAnswersCount;
    }
    
    public int getStreakCorrect() {
        return streakCorrect;
    }
    
    public int getMaxStreak() {
        return maxStreak;
    }
}