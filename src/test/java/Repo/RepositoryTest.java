package Repo;

import Domain.Student;
import Validator.StudentValidator;
import Validator.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class RepositoryTest {

    Repository<Integer, Student> repo;
    Student s1;
    Student s2;
    @Before
    public void setUp() throws Exception {
        repo = new StudentRepo(new StudentValidator());
        s1 = new Student(12,"Ion",222,"ion@ionel.com");
        s2 = new Student(13,"Gigi",223,"ion@ionel.com");
    }

    @Test
    public void findNone() {
        assert repo.findOne(12)==null;
    }

    @Test
    public void badFindOne() {
        try{
            repo.findOne(null);
            assert false;
        }catch (IllegalArgumentException e){
            assert true;
        }
    }

    @Test
    public void save() {
        assert repo.save(s1)==null;
        assert repo.save(s2)==null;
    }

    @Test
    public void badSave() {
        repo.save(s1);
        repo.save(s2);

        assert repo.save(s1).equals(s1);
        try{
            repo.save(new Student(-1,"",2,""));
            assert false;
        }catch (ValidationException e){
            assert true;
        }
    }

    @Test
    public void findOne() {
        repo.save(s1);
        repo.save(s2);

        assert repo.findOne(12).equals(s1);
    }

    @Test
    public void findAll() {
        int i=0;
        for(Student s:repo.findAll()){
            if(i==0){
               assert s.equals(s1);
            }else if(i==1){
                assert  s.equals(s1);
            }else{
                assert false;
            }
            i++;
        }
    }

    @Test
    public void delete() {
        repo.save(s1);
        repo.save(s2);

        assert repo.delete(17)==null;
        assert repo.delete(13).equals(s2);
    }

    @Test
    public void badDelete() {
        try {
            repo.delete(null);
            assert false;
        }catch (IllegalArgumentException e){
            assert true;
        }
    }

    @Test
    public void badUpdate() {
        try {
            repo.update(null);
            assert false;
        }catch (IllegalArgumentException e){
            assert true;
        }
    }

    @Test
    public void update() {
        repo.save(s1);
        repo.save(s2);
        assert repo.update(new Student(1,"v",222,"d")) == null;
        assert repo.update(new Student(12,"v",222,"d")).equals(s1);
    }

    @Test
    public void illegalUpdate() {
        try{
            assert repo.update(new Student(12,"",-2,"d")) == s1;
            assert false;
        }catch (ValidationException e){
            assert true;
        }
    }
}