package excell;

/**
 * Created by dokuchaev on 08.12.16.
 */
public class Question{
    private String question;
    private String code;
    private String answer;

    public Question(String question, String code, String answer){
        this.question = question;
        this.code = code;
        this.answer = answer;
    }

    public Question(){
    }

    public String getQuestion(){
        return question;
    }

    public void setQuestion(String question){
        this.question = question;
    }

    public String getCode(){
        return code;
    }

    public void setCode(String code){
        this.code = code;
    }

    public String getAnswer(){
        return answer;
    }

    public void setAnswer(String answer){
        this.answer = answer;
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (!(o instanceof Question)) return false;

        Question question1 = (Question) o;

        if (!getQuestion().equals(question1.getQuestion())) return false;
        if (getCode() != null ? !getCode().equals(question1.getCode()) : question1.getCode() != null) return false;
        return getAnswer().equals(question1.getAnswer());
    }

    @Override
    public int hashCode(){
        int result = getQuestion().hashCode();
        result = 31 * result + (getCode() != null ? getCode().hashCode() : 0);
        result = 31 * result + getAnswer().hashCode();
        return result;
    }
}
