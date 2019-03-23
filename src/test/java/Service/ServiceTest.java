//package Service;
//
//import Domain.Student;
//import Domain.TemaLab;
//import Repo.NoteRepo;
//import Repo.StudentRepo;
//import Repo.TemaRepo;
//import Validator.NoteValidator;
//import Validator.StudentValidator;
//import Validator.TemaLaboratorValidator;
//import org.junit.Before;
//import org.junit.Test;
//
//import static org.junit.Assert.*;
//
//public class ServiceTest {
//    Service s;
//    Student s1;
//    TemaLab t1;
//    @Before
//    public void setUp() throws Exception {
//        s = new Service(new StudentRepo(new StudentValidator()),new TemaRepo(new TemaLaboratorValidator()),new NoteRepo(new NoteValidator()));
//        s1= new Student(1,"ion",222,"a");
//    }
//
//    @Test
//    public void addStudent() {
//        s.addStudent(1,"ion",222,"a");
//        assert s.findStudent(1).equals(s1);
//    }
//
//    @Test
//    public void updateStudent() {
//        s.addStudent(1,"ion",222,"a");
//        s1.setNume("gigi");
//        s.updateStudent(1,"gigi",222,"a");
//        assert s.findStudent(1).getNume().equals("gigi");
//    }
//
//    @Test
//    public void deleteStudent() {
//        s.deleteStudent(1);
//        assert s.findStudent(1)==null;
//    }
//
//    @Test
//    public void addTemaLab() {
//        s.addTemaLab(1,"da",3,1);
//        TemaLab t= s.findTema(1);
//        assert t.getStartDate()==1;
//        assert t.getDeadline()==3;
//        assert t.getDescriere().equals("da");
//    }
//
//    @Test
//    public void prelungireTermen() {
//        s.addTemaLab(1,"da",3,1);
//        s.prelungireTermen(1,4);
//        TemaLab t= s.findTema(1);
//        assert t.getStartDate()==1;
//        assert t.getDeadline()==7;
//    }
//}