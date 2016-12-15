package excell;

/**
 * Created by dokuchaev on 08.12.16.
 */
public class Question {

    private final String question;
    private final String code;
    private final String answer;

    public Question(String question, String code, String answer) {
        this.question = question;
        this.code = code;
        this.answer = answer;
    }

    public String getQuestion() {
        return question.trim();
    }

    public String getCode() {
        return code.trim();
    }

    String getAnswer() {
        return answer.trim();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Question)) return false;

        Question question1 = (Question) o;

        return getQuestion().equals(question1.getQuestion()) && (getCode() != null ? getCode()
                .equals(question1.getCode()) : question1.getCode() == null && getAnswer()
                .equals(question1.getAnswer()));
    }

    @Override
    public int hashCode() {
        int result = getQuestion().hashCode();
        result = 31 * result + (getCode() != null ? getCode().hashCode() : 0);
        result = 31 * result + getAnswer().hashCode();
        return result;
    }
}
