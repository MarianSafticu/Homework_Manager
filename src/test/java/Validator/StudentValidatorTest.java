package Validator;

import Domain.Student;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class StudentValidatorTest {
    StudentValidator v;
    Student s1;
    @Before
    public void setUp() throws Exception {
        s1 = new Student(1,"Ion",213,"altceva");
        v = new StudentValidator();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void goodStudent() {
        try {
            v.validate(s1);
            assert true;
        }catch (ValidationException e){
            assert false;
        }
    }

    @Test
    public void badID(){
        s1.setID(-1);
        try{
            v.validate(s1);
            assert false;
        }catch (ValidationException e){
            assert e.getMessage().equals("Id invalid (Id-ul nu poate fi negativ)\n");
        }
        s1.setID(1);
    }
    @Test
    public void badName(){
        s1.setNume("");
        try{
            v.validate(s1);
            assert false;
        }catch (ValidationException e){
            assert e.getMessage().equals("Nume invalid (numele nu poate fi vid) \n");
        }
        s1.setNume("Ion");
    }
    @Test
    public void badGrupa(){
        s1.setGrupa(23);
        try{
            v.validate(s1);
            assert false;
        }catch (ValidationException e){
            assert e.getMessage().equals("Grupa invalida (grupa trebuie sa fie un numar pozitiv format din 3 cifre)\n");
        }
        s1.setGrupa(123);
    }
    @Test
    public void badEmail(){
        s1.setEmail("");
        try{
            v.validate(s1);
            assert false;
        }catch (ValidationException e){
            assert e.getMessage().equals("Email-ul nu poate fi vid\n");
        }
        s1.setEmail("a");
    }
    @Test
    public void badIdAndName(){
        s1.setID(-2);
        s1.setNume("");
        try{
            v.validate(s1);
            assert false;
        }catch (ValidationException e){
            assert e.getMessage().equals("Id invalid (Id-ul nu poate fi negativ)\nNume invalid (numele nu poate fi vid) \n");
        }
        s1.setNume("Ion");
    }
}