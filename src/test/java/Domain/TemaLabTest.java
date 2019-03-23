package Domain;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TemaLabTest {
    TemaLab t1,t2,t3;
    @Before
    public void setUp() throws Exception {
        t1 = new TemaLab(1,"ceva",2,1);
        t2 = new TemaLab(2,"altceva",3,2);
        t3 = new TemaLab(1,"ceva",1,2);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getID() {
        assert t1.getID()==1;
        assert t2.getID()==2;
    }

    @Test
    public void setID() {
        t2.setID(4);
        assert t2.getID()==4;
    }

    @Test
    public void getDescriere() {
        assert t1.getDescriere().equals("ceva");
        assert t2.getDescriere().equals("altceva");
    }

    @Test
    public void setDescriere() {
        t2.setDescriere("1");
        assert t2.getDescriere().equals("1");
    }

    @Test
    public void getDeadline() {
        assert t1.getDeadline()==2;
        assert t2.getDeadline()==3;
    }

    @Test
    public void setDeadline() {
        t2.setDeadline(6);
        assert t2.getDeadline()==6;
    }

    @Test
    public void getStartDate() {
        assert t1.getStartDate()==1;
        assert t2.getStartDate()==2;
    }

    @Test
    public void setStartDate() {
        t2.setStartDate(4);
        assert t2.getStartDate()==4;
    }
}