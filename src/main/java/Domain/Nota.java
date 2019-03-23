package Domain;

import javax.swing.text.DateFormatter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Nota implements HasID<Pair<Integer,Integer>> {
    private Pair<Integer,Integer> StudentLab;
    private String st;
    private Integer lab;
    private Double nota;
    private String profLab;
    LocalDate data;

    public Nota(Integer StudentID,int LabID, Double nota,String profLab,String data){
        StudentLab=new Pair(StudentID,LabID);
        this.nota = nota;
        this.profLab = profLab;
        st = StudentID.toString();
        lab = LabID;
        this.data = LocalDate.parse(data,DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public String getDate(){
        return data.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public void setDate(String data){
        this.data = LocalDate.parse(data, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * getter for ID
     * @return student id
     */
    @Override
    public Pair getID(){
        return StudentLab;
    }
    /**
     * setter for ID
     * @param StudentLab is the new id for the student
     */
    @Override
    public void setID(Pair StudentLab){
        this.StudentLab=StudentLab;
    }
    /**
     * getter for laboratory teacher
     * @return the name of the laboratory teacher
     */
    public String getProfLab() {
        return profLab;
    }

    /**
     * gettter for student id
     * @return the id of the student who received the grade
     */
    public Integer getStudentID(){
        return StudentLab.getFirst();
    }

    /**
     * getter for homework id
     * @return the id of the homework witch was assigned with the grade
     */
    public Integer getTemaID(){
        return StudentLab.getSecond();
    }


    /**
     * setter for laboratory teacher
     * @param profLab is the new name of the laboratory teacher for student
     */
    public void setProfLab(String profLab) {
        this.profLab = profLab;
    }

    public Double getGrade(){
        return nota;
    }

    private Double getNota(){
        return nota;
    }
    private void setNota(double n){
        this.nota = n;
    }
    public boolean equals(Nota ot){
        return StudentLab.equals(ot.StudentLab);
    }


    @Override
    public String toString() {
        return ""+this.StudentLab+"   "+this.profLab+"  "+this.nota;
    }
}
