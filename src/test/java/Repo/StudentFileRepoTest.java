package Repo;

import Domain.Student;
import Validator.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class StudentFileRepoTest {

    private Validator<Student> mockV = new StudentValidator();
    private String fName;

    private StudentFileRepo studentFileRepoUnderTest;

    @Before
    public void setUp() {
        fName = "testSRepo.txt";
        studentFileRepoUnderTest = new StudentFileRepo(mockV, fName);
    }

    @After
    public void tearDown(){
       try (BufferedWriter out = new BufferedWriter(new FileWriter(fName))){
            out.write("");
       } catch (IOException e) {
           e.printStackTrace();
       }
    }

    @Test
    public void testSave() {
        // Setup
        final Student e = new Student(1,"ionel",223,"ion@mail");
        final Student expectedResult = null;

        // Run the test
        final Student result = studentFileRepoUnderTest.save(e);

        // Verify the results
        assertEquals(expectedResult, result);

    }

    @Test
    public void testDelete() {
        // Setup
        final Integer id = 0;
        final Student expectedResult = null;

        // Run the test
        final Student result = studentFileRepoUnderTest.delete(id);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    @Test
    public void testUpdate() {
        // Setup
        final Student s = new Student(1,"ionel",223,"ion@mail");
        final Student expectedResult = null;

        // Run the test
        Student result = studentFileRepoUnderTest.update(s);

        // Verify the results
        assertEquals(expectedResult, result);
    }

}
