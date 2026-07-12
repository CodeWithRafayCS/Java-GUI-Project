package quiz;

import models.Question;
import models.QuizResult;

public class PracticeMode extends QuizSession {
    private static final long serialVersionUID = 1L;
    
    private int hintsUsed;
    private boolean[] hintUsedForQuestion;
    private boolean quizFinished;
    
    public PracticeMode(String studentRegNumber, String studentName, String subjectCode,
                        String subjectName, String difficulty) {
        super(studentRegNumber, studentName, subjectCode, subjectName, difficulty);
        this.hintsUsed = 0;
        this.quizFinished = false;
        this.timeLimitSeconds = 0;
    }
    
    @Override
    public void startSession() {
        this.isActive = true;
        this.quizEnded = false;
        this.quizFinished = false;
        this.currentQuestionIndex = 0;
        this.score = 0;
        this.hintsUsed = 0;
        this.answerResults.clear();
        
        if (questions != null && !questions.isEmpty()) {
            this.hintUsedForQuestion = new boolean[questions.size()];
            for (int i = 0; i < hintUsedForQuestion.length; i++) {
                hintUsedForQuestion[i] = false;
            }
        }
        startTimer();
    }
    
    @Override
    public boolean submitAnswer(int selectedOption) {
        if (!isActive || quizEnded || quizFinished) {
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
            answerResults.add(true);
            
            // Check if this was the last question
            boolean wasLastQuestion = (currentQuestionIndex + 1 >= totalQuestions);
            
            if (wasLastQuestion) {
                this.quizFinished = true;
                this.isActive = false;
                this.quizEnded = true;
            } else {
                currentQuestionIndex++;
            }
        } else {
            // Wrong answer - stay on same question, record as wrong attempt
            answerResults.add(false);
            // Don't move to next question, don't increment score
        }
        
        return isCorrect;
    }
    
    @Override
    public boolean isQuizComplete() {
        return quizFinished || quizEnded;
    }
    
    @Override
    public QuizResult endSession() {
        this.isActive = false;
        this.quizEnded = true;
        this.quizFinished = true;
        
        long elapsedTime = getElapsedTimeSeconds();
        
        QuizResult result = new QuizResult(
            studentRegNumber, studentName, subjectCode, subjectName,
            getModeName(), difficulty, totalQuestions, score, (int) elapsedTime
        );
        result.setHintsUsed(hintsUsed);
        
        return result;
    }
    
    @Override
    public String getHint() {
        if (currentQuestionIndex >= 0 && hintUsedForQuestion != null && 
            currentQuestionIndex < hintUsedForQuestion.length) {
            if (!hintUsedForQuestion[currentQuestionIndex]) {
                hintUsedForQuestion[currentQuestionIndex] = true;
                hintsUsed++;
                Question q = getCurrentQuestion();
                return q != null ? q.getHint() : "No hint available";
            } else {
                return "You already used the hint for this question";
            }
        }
        return "No hint available";
    }
    
    @Override
    public boolean canContinue() {
        return !quizFinished && !quizEnded;
    }
    
    @Override
    public int getRemainingAttempts() {
        return Integer.MAX_VALUE;
    }
    
    @Override
    protected String getModeName() {
        return "Practice Mode";
    }
    
    public int getHintsUsed() {
        return hintsUsed;
    }
}