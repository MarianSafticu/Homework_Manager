package Service;


import javafx.util.Pair;
import Domain.*;

import GUI.ChangeEventType;
import Utils.Observable;
import Utils.Observer;
import GUI.StudentEvent;
import Repo.Repository;
import Validator.ValidationException;
import jdk.nashorn.internal.parser.DateParser;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;


/**
 * the class for app service
 * contains 3 repository for students, homework and grades
 */
public class Service implements Observable<StudentEvent> {
    static int saptamana_curenta=1;
    private ArrayList<Observer<StudentEvent>> observers;
    Repository<Integer, Student> studenti;
    Repository<Integer, TemaLab> teme_laborator;
    Repository<Domain.Pair<Integer,Integer>,Nota> note;

    public Service(Repository<Integer, Student> studenti, Repository<Integer, TemaLab> teme_laborator, Repository<Domain.Pair<Integer, Integer>, Nota> note) {
        this.studenti = studenti;
        this.teme_laborator = teme_laborator;
        this.note = note;
        observers = new ArrayList<>();
    }

    /**
     * adds a student to student repository
     * @param id id of the student
     * @param nume name of the student
     * @param grupa group of the student
     * @param email email of the student
     * @throws ValidationException
     * @return null if the student has been added to repository
     * otherwise it return the student
     * if the student is invalid then the function throws Validation esxception
     */
    public Student addStudent(String id, String nume, String grupa,String email) throws ValidationException {
        try {
            Student s = new Student(Integer.parseInt(id), nume, Integer.parseInt(grupa), email);
            Student ret = studenti.save(s);
            if (ret == null) {
                notifyObservers(new StudentEvent(null, s, ChangeEventType.ADD));
            }
            return ret;
        }
        catch (NumberFormatException e){
            throw new ValidationException("Grupa si/sau ID-ul trebuie sa fie numere !!1 \n");
        }
    }

    /**
     * update a student from student repository
     * @param id the ID of the student
     * @param nume the name of the student
     * @param grupa the group of student
     * @param email the email of student
     * @return the old version of entity - if the entity is updated,
     * otherwise returns the entity - (e.g id does not exist).
     */
    public Student updateStudent(String id, String nume, String grupa,String email){
        try {
            Student s = new Student(Integer.parseInt(id), nume, Integer.parseInt(grupa), email);
            Student ret = studenti.update(s);
            if (ret.hashCode() != s.hashCode()) {
                notifyObservers(new StudentEvent(ret, s, ChangeEventType.UPDATE));
            }
            return ret;
        }catch (NumberFormatException e){
            throw new ValidationException("ID-ul si (sau) grupa trebuie sa fie numere intregi");
        }
    }

    /**
     * find a student from student repository
     * @param id is the ID of the student that we searched for
     *           id must not be null
     * @return the student with that id, if is found
     *          null if a student with searched id does not exist
     */
    public Student findStudent(int id){
        return studenti.findOne(id);
    }

    /**
     * find a student from homework repository
     * @param id is the ID of the homework that we searched for
     *           id must not be null
     * @return the homework with that id, if is found
     *          null if a homework with searched id does not exist
     */
    public TemaLab findTema(int id){
        return teme_laborator.findOne(id);
    }


    /**S
     * delete a student from student repository
     * @param id is the ID of the student that will be deleted
     * @return the removed student or null if the student was not in repository
     */
    public Student deleteStudent(String id){
        try {
            Student ret = studenti.delete(Integer.parseInt(id));
            note.findAll().forEach(n->{
                if(n.getStudentID() == Integer.parseInt(id)){
                    note.delete(new Domain.Pair<>(n.getStudentID(),n.getTemaID()));
                }
            });
            if (ret != null) {
                notifyObservers(new StudentEvent(ret, null, ChangeEventType.DELETE));
            }
            return ret;
        }catch (NumberFormatException e){
            throw new ValidationException("Id-ul trebuie sa fie un numar intreg");
        }
    }

    /**
     * add a laboratory homework to homework repository
     * @param nr the number of the homework
     * @param desc the description of the homework
     * @param deadline the deadline week for the homework
     * @param start the start week for homework
     * @return null if the homework has been added to repository
     * otherwise it return the homework
     * @throws ValidationException if homework is invalid
     */
    public TemaLab addTemaLab(int nr,String desc,int deadline,int start)throws ValidationException{
        TemaLab t=new TemaLab(nr,desc,deadline,start);
        return teme_laborator.save(t);
    }

    /**
     * extend the deadline of the homework
     * @param id the number of homework that will have an extend deadline
     * @param nrSaptamani the number of weeks with which will be extended the homework
     * @return -1 if the nr of weeks plus the deadline is greater than 14
     *          0 otherwise
     */
    public int prelungireTermen(int id,int nrSaptamani){
        if(saptamana_curenta<=teme_laborator.findOne(id).getDeadline() && saptamana_curenta+nrSaptamani <= 14) {
            TemaLab t = teme_laborator.findOne(id);
            t.setDeadline(t.getDeadline() + nrSaptamani);
            teme_laborator.update(t);
            return 0;
        }
        return -1;
    }

    /**
     *  function to get all students
     * @return all students from repository
     */
    public Iterable<Student> allStudents(){
        return studenti.findAll();
    }
    /**
     *  function to get all homeworks
     * @return all grades from repository
     */
    public Iterable<TemaLab> allHomeworks(){
        return teme_laborator.findAll();
    }
    /**
     *  function to get all grades
     * @return all grades from repository
     */
    public Iterable<Nota> allGrades(){
        return note.findAll();
    }

    /**
     *
     * function that assign a grade to a student at a specific homework
     * if the student was late the the grade will be lower-ed
     * @param stid id of the student
     * @param tid id of the homework
     * @param grade the grade
     * @param prof the teacher that assigned the grade to the student
     * @param fb the feedback for the homework
     * @param saptamana_curenta the current week
     * @param data the current data (in format of \"yyyy-MM-dd HH:mm:ss\" ")
     * @return the grade assigned to the student
     */
    public Nota assignGrade(int stid,int tid,double grade,String prof,String fb , int saptamana_curenta , String data,boolean special){
        TemaLab t = findTema(tid);
        Student s = findStudent(stid);
        if( t == null){
            throw new ValidationException("Tema inexistenta \n");
        }
        if( s == null){
            throw new ValidationException("Student inexistent \n");
        }
        if(!special) {
            if (saptamana_curenta == t.getDeadline() + 1) {
                grade -= 2.5;
                if (grade < 1) {
                    grade = 1;
                }
            } else if (saptamana_curenta == t.getDeadline() + 2) {
                grade -= 5;
            } else if (saptamana_curenta >= t.getDeadline() + 3) {
                grade = 1;
            }
            if (grade < 1) {
                grade = 1;
            }
        }

        Nota n = new Nota(stid,tid,grade,prof,data);
        Nota ret = note.save(n);
        if(ret == null)
            feedback(n,fb,saptamana_curenta);
        return ret;
    }


    /**
     * the function that write feedback in the feedback folder
     * @param nota the grade object (witch contains the IDs of the student and homework)
     * @see Nota
     * @param fb the feedback
     * @param saptamana_curenta is the currnet week
     */
    public void feedback(Nota nota,String fb,int saptamana_curenta){
        Student st = findStudent(nota.getStudentID());
        TemaLab t = findTema(nota.getTemaID());
        String nume = st.getNume();
        try(BufferedWriter br = new BufferedWriter(new FileWriter("data/feedback/"+nume+".txt",true))){
            br.write("Tema: "+t.getID());
            br.newLine();
            br.write("Nota: "+nota.getGrade());
            br.newLine();
            br.write("Predata in saptamana: "+saptamana_curenta);
            br.newLine();
            br.write("Deadline: "+t.getDeadline());
            br.newLine();
            br.write("Feedback:\r\n"+fb);
            br.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public <E> List<E> filterAndSort(E entity, Predicate<E> pr, Comparator<E> comp){
        List<E> rez = new ArrayList<>();
        return rez;
    }

    public Integer getSaptamana_curenta(LocalDate date){
        if (date.isBefore(LocalDate.parse("2018-10-08")))
            return 1;
        else if (date.isBefore(LocalDate.parse("2018-10-15"))){
            return 2;
        }else if (date.isBefore(LocalDate.parse("2018-10-22"))){
            return 3;
        }else if (date.isBefore(LocalDate.parse("2018-10-29"))){
            return 4;
        }else if (date.isBefore(LocalDate.parse("2018-11-05"))){
            return 5;
        }else if (date.isBefore(LocalDate.parse("2018-11-12"))){
            return 6;
        }else if (date.isBefore(LocalDate.parse("2018-11-19"))){
            return 7;
        }else if (date.isBefore(LocalDate.parse("2018-11-26"))){
            return 8;
        }else if (date.isBefore(LocalDate.parse("2018-12-03"))){
            return 9;
        }else if (date.isBefore(LocalDate.parse("2018-12-10"))){
            return 10;
        }else if (date.isBefore(LocalDate.parse("2018-12-17"))){
            return 11;
        }else if (date.isBefore(LocalDate.parse("2019-01-07"))){
            return 12;
        }else if (date.isBefore(LocalDate.parse("2018-01-14"))){
            return 13;
        }else{
            return 14;
        }
    }

    @Override
    public void addObserver(Observer<StudentEvent> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<StudentEvent> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(StudentEvent studentEvent) {
        observers.forEach(obs-> obs.update(studentEvent));
    }

    public List<String> allStudentsWith(String sub){
        List<String> rez = new ArrayList<>();
        allStudents().forEach(s -> {
            if(s.getNume().contains(sub))
                rez.add(s.getNume()+"\t("+s.getID()+")");
        });
        return rez;
    }

    public Integer getIdFromNume(String nume){
        if(nume.split("[(]")[1]!= null){
            return Integer.parseInt(nume.split("[(]")[1].split("[)]")[0]);
        }
        else{
            ArrayList<Integer> a = new ArrayList<>();
            allStudents().forEach(s -> {if ( s.getNume().equals(nume)) a.add(s.getID());});
            return a.get(0) ;
        }
    }

    public List<String> allGroups() {
        ArrayList<String> al = new ArrayList<>();

        studenti.findAll().forEach(s -> {
            if(!al.contains(s.getGrupa().toString())){
                al.add(s.getGrupa().toString());
            }
        });
        return al;
    }

    public List<Nota> noteAtLab(int labID) {
        List<Nota> rez = new ArrayList<>();
        note.findAll().forEach(n->{
            if(n.getTemaID() == labID)
                rez.add(n);
        });
        return rez;
    }

    public List<javafx.util.Pair<String,List<Double>>> studentiGrupa(int grupa) {
        Map<String,List<Double>> rez = new HashMap<>();
        studenti.findAll().forEach(s->{
            note.findAll().forEach(n->{
                if(s.getGrupa() == grupa && s.getID()==n.getStudentID()){
                    if(rez.containsKey(s.getNume())){
                        rez.get(s.getNume()).set(n.getTemaID()-1,n.getGrade());
                    }else{
                        rez.put(s.getNume(),new ArrayList<>());
                        rez.get(s.getNume()).add(0,0.d);
                        rez.get(s.getNume()).add(1,0d);
                        rez.get(s.getNume()).add(2,0d);
                        rez.get(s.getNume()).add(3,0d);
                        rez.get(s.getNume()).add(4,0d);
                        rez.get(s.getNume()).add(5,0d);
                        rez.get(s.getNume()).add(6,0d);
                        rez.get(s.getNume()).add(7,0d);
                        rez.get(s.getNume()).add(8,0d);
                        rez.get(s.getNume()).add(9,0d);
                        rez.get(s.getNume()).add(10,0d);
                        rez.get(s.getNume()).add(11,0d);

                        rez.get(s.getNume()).set(n.getTemaID()-1,n.getGrade());
                    }
                }
            });
        });
        return rez.entrySet().stream().map(e->new Pair<>(e.getKey(),e.getValue())).collect(Collectors.toList());
    }

    public List<Nota> noteForGrupaAtTema(String grupa, int temaid) {
        List<Nota> rez = new ArrayList<>();
        note.findAll().forEach(n->{
            if((findStudent(n.getStudentID()).getGrupa() == Double.parseDouble(grupa)) && n.getTemaID() == temaid )
                rez.add(n);
        });
        return rez;
    }

    public List<Nota> noteForStudent(String nume) {
        List<Nota> rez = new ArrayList<>();
        note.findAll().forEach(n->{
            if(findStudent(n.getStudentID()).getNume().contains(nume))
                rez.add(n);
        });
        return rez;


    }

    public List<Pair<Integer,Double>> medieNoteFiecareLaborator(){
        List<Pair<Integer,Double>> rez = new ArrayList<>();

        teme_laborator.findAll().forEach(l->{
            Integer m;
            List<Double> noteLab = new ArrayList<>();
            note.findAll().forEach(n->{
                if (n.getTemaID() == l.getID()){
                    noteLab.add(n.getGrade());
                }
            });
            Double medie;
            if(noteLab.size() == 0){
                medie = 0.1d;
            }
            else {
                medie = noteLab.stream().reduce((x, y) -> {
                    return x + y;
                }).get() / noteLab.size();
                rez.add(new Pair<>(l.getID(),medie));
            }
        });
        return rez;
    }


    public List<Pair<Student,Double>> medieSt(){
        List<Pair<Student,Double>> rez = new ArrayList<>();
        studenti.findAll().forEach(s->{
            List<Double> noteS = new ArrayList<>();
            note.findAll().forEach(n->{
                if(n.getStudentID() == s.getID()){
                    TemaLab t = teme_laborator.findOne(n.getTemaID());
                    for (int i = t.getStartDate(); i< t.getDeadline();i++){
                        noteS.add(n.getGrade());
                    }
                }
            });
            Double medie;
            if(!noteS.isEmpty()){
                medie = noteS.stream().reduce(0.00,(x,y)->{ return x+y; }).doubleValue()/noteS.size();
            }else{
                medie = 1.d;
            }
            rez.add(new Pair(s,medie));
        });
        return rez;
    }
    public List<Pair<Integer,Integer>> mediiScurteStudenti(){
        List<Pair<Integer,Integer>> rez = new ArrayList<>(11);
        rez.add(new Pair<>(1,0));
        rez.add(new Pair<>(2,0));
        rez.add(new Pair<>(3,0));
        rez.add(new Pair<>(4,0));
        rez.add(new Pair<>(5,0));
        rez.add(new Pair<>(6,0));
        rez.add(new Pair<>(7,0));
        rez.add(new Pair<>(8,0));
        rez.add(new Pair<>(9,0));
        rez.add(new Pair<>(10,0));
        medieSt().forEach(p->{
            int idx = (int)(Math.floor(p.getValue()));
            rez.set(idx-1,new Pair<>(idx,rez.get(idx-1).getValue()+1));
        });
        return rez;
    }

    public List<Student> valiziExamen(){
        List<Student> rez = new ArrayList<>();
        medieSt().forEach(p ->{
            if(p.getValue() >= 4)
                rez.add(p.getKey());
        });
        return rez;
    }


    public List<Student> studentiFaraIntarziere(){
        List<Student> rez = new ArrayList<>();
        studenti.findAll().forEach(s->{
            List<Boolean> a = new ArrayList<>();
            a.add(true);
            note.findAll().forEach(n->{
                if(n.getStudentID() == s.getID()){
                    if(a.get(0) && getSaptamana_curenta(LocalDate.parse(n.getDate())) >= teme_laborator.findOne(n.getTemaID()).getDeadline()){
                        a.set(0,false);
                    }
                }
            });
            if(a.get(0)) {
                rez.add(s);
            }
        });
        return rez;
    }

    public List<Student> studentiNumeGrupa(String nume,String grupa){
        List<Student> rez = new ArrayList<>();
        allStudents().forEach(s->{
            if(s.getNume().startsWith(nume) && s.getGrupa().toString().startsWith(grupa)){
                rez.add(s);
            }
        });
        return rez;
    }

    public List<TemaLab> temeDesc(String desc){
        List<TemaLab> rez = new ArrayList();
        allHomeworks().forEach(x->{
            if (x.getDescriere().contains(desc)){
                rez.add(x);
            }
        });
        return rez;
    }
}

