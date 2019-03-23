package Repo;

import Domain.TemaLab;
import Validator.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.assertEquals;

public class TemaFileRepoTest {

    private Validator<TemaLab> mockV;
    private String fName;

    private TemaFileRepo temaFileRepoUnderTest;

    @Before
    public void setUp() {
        mockV = new TemaLaboratorValidator();
        fName = "testT.txt";
        temaFileRepoUnderTest = new TemaFileRepo(mockV, fName);
    }
    @After
    public void tearDown(){
        File file;
        try(BufferedWriter out = new BufferedWriter(new FileWriter(fName))){
            out.write("");
        } catch (IOException e) {
            e.printStackTrace();
        }
        ;
    }

    @Test
    public void testSave() {
        // Setup
        final TemaLab e = new TemaLab(1,"tema",4,2);
        final TemaLab expectedResult = null;

        // Run the test
        TemaLab result = temaFileRepoUnderTest.save(e);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    @Test
    public void testDelete() {
        // Setup
        final Integer id = 0;
        final TemaLab expectedResult = null;

        // Run the test
        final TemaLab result = temaFileRepoUnderTest.delete(id);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    @Test
    public void testUpdate() {
        // Setup
        final TemaLab s = new TemaLab(1,"tema",4,2);
        final TemaLab expectedResult = null;

        // Run the test
            final TemaLab result = temaFileRepoUnderTest.update(s);

        // Verify the results
        assertEquals(expectedResult, result);
    }
}
