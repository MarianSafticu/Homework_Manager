package Domain;

import Domain.Student;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class StudentTest {

    Student s1,s2,s3;
    @Before
    public void setUp() throws Exception {
        s1 = new Student(123,"Nume",246,"a@b.com");
        s2 = new Student(12,"Alt Nume",246,"a@c.com");
        s3 = new Student(123,"Nume",246,"a@b.com");

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getId() {
        assert s1.getID()==123;
        assert s2.getID()==12;
    }

    @Test
    public void setId() {
        s2.setID(453);
        assert s2.getID()==453;
    }

    @Test
    public void getNume() {
        assert s1.getNume()=="Nume";
        assert s2.getNume()=="Alt Nume";
    }

    @Test
    public void setNume() {
        s2.setNume("Ion");
        assert s2.getNume()=="Ion";
    }

    @Test
    public void getGrupa() {
        assert s1.getGrupa()==246;
        assert s2.getGrupa()==246;
    }

    @Test
    public void setGrupa() {
        assert s2.getGrupa()==246;
        s2.setGrupa(124);
        assert s2.getGrupa()==124;
    }

    @Test
    public void getEmail() {
        assert s1.getEmail().equals("a@b.com");
        assert s2.getEmail().equals("a@c.com");
    }

    @Test
    public void setEmail() {
        s2.setEmail("a@d.c");
        assert s2.getEmail().equals("a@d.c");
    }


    @Test
    public void equals() {
        assert s1.equals(s3);
    }
}