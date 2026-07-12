package quiz;

import java.io.Serializable;
import java.util.ArrayList;
import models.Question;
import models.QuizResult;

public abstract class QuizSession implements Serializable {
    private static final long serialVersionUID = 1L;

    protected ArrayList<Question> questions;
    protected int currentQuestionIndex;
    protected int score;
    protected int totalQuestions;
    protected String studentRegNumber;
    protected String studentName;
    protected String subjectCode;
    protected String subjectName;
    protected String difficulty;
    protected boolean isActive;
    protected long startTime;
    protected int timeLimitSeconds;
    protected ArrayList<Boolean> answerResults;
    protected boolean quizEnded;

    public QuizSession(String studentRegNumber, String studentName, String subjectCode,
                       String subjectName, String difficulty) {
        this.studentRegNumber = studentRegNumber;
        this.studentName = studentName;
        this.subjectCode = subjectCode;
        this.subjectName = subjectName;
        this.difficulty = difficulty;
        this.questions = new ArrayList<>();
        this.currentQuestionIndex = 0;
        this.score = 0;
        this.totalQuestions = 0;
        this.isActive = false;
        this.quizEnded = false;
        this.answerResults = new ArrayList<>();
        this.timeLimitSeconds = 0;
    }

    public abstract void startSession();
    public abstract boolean submitAnswer(int selectedOption);
    public abstract boolean isQuizComplete();
    public abstract QuizResult endSession();
    public abstract String getHint();
    public abstract boolean canContinue();
    public abstract int getRemainingAttempts();
    protected abstract String getModeName();

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
        this.totalQuestions = questions.size();
    }

    public Question getCurrentQuestion() {
        if (currentQuestionIndex < questions.size()) {
            return questions.get(currentQuestionIndex);
        }
        return null;
    }

    public void moveToNextQuestion() {
        if (currentQuestionIndex < questions.size() - 1) {
            currentQuestionIndex++;
        }
    }

    public int getCurrentQuestionIndex() { return currentQuestionIndex; }
    public int getTotalQuestions() { return totalQuestions; }
    public int getScore() { return score; }
    public int getCorrectAnswers() { return score; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public void startTimer() {
        startTime = System.currentTimeMillis();
    }

    public long getElapsedTimeSeconds() {
        if (startTime == 0) return 0;
        return (System.currentTimeMillis() - startTime) / 1000;
    }

    public boolean isTimeUp() {
        if (timeLimitSeconds <= 0) return false;
        return getElapsedTimeSeconds() >= timeLimitSeconds;
    }

    public void setTimeLimitSeconds(int seconds) {
        this.timeLimitSeconds = seconds;
    }

    public int getTimeLimitSeconds() { return timeLimitSeconds; }

    protected void recordAnswer(boolean isCorrect) {
        answerResults.add(isCorrect);
        if (isCorrect) {
            score++;
        }
    }

    protected QuizResult createResult(int timeTakenSeconds) {
        return new QuizResult(
            studentRegNumber, studentName, subjectCode, subjectName,
            getModeName(), difficulty, totalQuestions, score, timeTakenSeconds
        );
    }

    public ArrayList<Boolean> getAnswerResults() { return answerResults; }
    public boolean isQuizEnded() { return quizEnded; }
    protected void setQuizEnded(boolean ended) { this.quizEnded = ended; }

    public void reset() {
        currentQuestionIndex = 0;
        score = 0;
        answerResults.clear();
        startTime = 0;
        quizEnded = false;
    }
}