//package Validator;
//
//import Domain.Nota;
//import org.junit.Before;
//import org.junit.Test;
//
//import static org.junit.Assert.*;
//
//public class NoteValidatorTest {
//
//    Nota a;
//    NoteValidator nv;
//    @Before
//    public void setUp() throws Exception {
//        a = new Nota(1,1,8,"prof","2010-10-10 11:11:11");
//        nv = new NoteValidator();
//    }
//
//    @Test
//    public void badGrade(){
//        try {
//            nv.validate(new Nota(1, 1, -1, "asd00", "2010-10-10 10:10:10"));
//        }catch ( ValidationException e){
//            assertEquals(e.getMessage(),"Nota trebuie sa fie intre 1 si 10\n");
//        }
//    }
//
//    @Test
//    public void badProf(){
//        try {
//            nv.validate(new Nota(1, 1, 1, "", "2010-10-10 10:10:10"));
//        }catch ( ValidationException e){
//            assertEquals(e.getMessage(),"numele profesorului nu poate fi vid\n");
//        }
//    }
//    @Test
//    public void valid() {
//        try{
//            nv.validate(a);
//            assert true;
//        }catch (Exception e){
//            assert false;
//        }
//    }
//}