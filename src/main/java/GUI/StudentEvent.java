package GUI;

import Domain.Student;

public class StudentEvent {

    private Student old;
    private Student news;
    private ChangeEventType type;

    public StudentEvent(Student old,Student news,ChangeEventType type){
        this.old = old;
        this.news = news;
        this.type = type;
    }

    public ChangeEventType getType() {
        return type;
    }

    public Student getData() {
        return news;
    }

    public Student getOldData(){
        return old;
    }

    public void setType(ChangeEventType type){
        this.type = type;
    }



}
