package Validator;

import Domain.TemaLab;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TemaLaboratorValidatorTest {

    TemaLab t;
    TemaLaboratorValidator v;
    @Before
    public void setUp() throws Exception {
        t = new TemaLab(1,"tema",4,3);
        v = new TemaLaboratorValidator();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void valid() {
        try{
            v.validate(t);
            assert true;
        }catch (ValidationException e){
            assert false;
        }
    }
    @Test
    public void badNr() {
        t.setID(-1);
        try{
            v.validate(t);
            assert false;
        }catch (ValidationException e){
            assert e.getMessage().equals("numarul temei de laborator invalid, trebuie sa fie un numar intre 1 si 14\n");
        }
        t.setID(2);
    }
    @Test
    public void badDescriere() {
        t.setDescriere("");
        try{
            v.validate(t);
            assert false;
        }catch (ValidationException e){
            assert e.getMessage().equals("descriera nu poate fi vida \n");
        }
        t.setDescriere("ceva");

    }
    @Test
    public void InvalidDeadline() {
        t.setDeadline(-1);
        try{
            v.validate(t);
            assert false;
        }catch (ValidationException e){
            assert e.getMessage().equals("termenul predarii invalid, trebuie sa fie un nunmar intre 1 si 14\n");
        }
        t.setDeadline(3);
    }
    @Test
    public void BadDeadline() {
        t.setDeadline(2);
        try{
            v.validate(t);
            assert false;
        }catch (ValidationException e){
            assert e.getMessage().equals("termenul de predare nu poate fi inainte de data primirii temei\n");
        }
        t.setDeadline(5);
    }
    @Test
    public void badStartDate() {
        t.setStartDate(-2);
        try{
            v.validate(t);
            assert false;
        }catch (ValidationException e){
            assert e.getMessage().equals("saptamana primiri temei invalida, trebuie sa fie un nunmar intre 1 si 14\n");
        }
    }
    @Test
    public void badNrAndDescriere() {
        try{
            v.validate(t);
            assert true;
        }catch (ValidationException e){
            assert e.getMessage().equals("numarul temei de laborator invalid, trebuie sa fie un numar intre 1 si 14\ndescriera nu poate fi vida \n");
        }
    }

}