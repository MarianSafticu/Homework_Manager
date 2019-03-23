//package Domain;
//
//import org.junit.Before;
//import org.junit.Test;
//
//import static org.junit.Assert.*;
//
//public class NotaTest {
//
//    Nota a,b;
//    @Before
//    public void setUp(){
//        a = new Nota(1,1,6,"prof","2018-10-18 10:59:23");
//        b = new Nota(1,1,6,"prof","2018-10-18 10:59:23");
//    }
//    @Test
//    public void getDate() {
//        assertEquals(a.getDate(),"2018-10-18 10:59:23");
//    }
//
//    @Test
//    public void setDate() {
//        a.setDate("2018-10-10 10:59:23");
//        assertEquals(a.getDate(),"2018-10-10 10:59:23");
//    }
//
//    @Test
//    public void getID() {
//        assert (a.getID().equals(new Pair<>(1,1)));
//    }
//
//    @Test
//    public void setID() {
//        a.setID(new Pair<>(2,2));
//        assert(a.getID().equals(new Pair<>(2,2)));
//    }
//
//    @Test
//    public void getProfLab() {
//        assertEquals(a.getProfLab(),"prof");
//    }
//
//    @Test
//    public void getStudentID() {
//        assert (b.getStudentID()==1);
//    }
//
//    @Test
//    public void getTemaID() {
//        assert b.getTemaID() == 1;
//    }
//
//    @Test
//    public void setProfLab() {
//        a.setProfLab("Profesor");
//        assertEquals(a.getProfLab(),"Profesor");
//    }
//
//    @Test
//    public void getGrade() {
//        assert a.getGrade() == 6;
//    }
//    @Test
//    public void equal(){
//        assert a.equals(b);
//    }
//}