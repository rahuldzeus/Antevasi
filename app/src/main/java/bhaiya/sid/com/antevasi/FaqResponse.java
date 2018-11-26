package bhaiya.sid.com.antevasi;

public class FaqResponse {
    private String name,question,answer;
    FaqResponse(String name, String question, String answer){
        this.name=name;
        this.question=question;
        this.answer=answer;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
