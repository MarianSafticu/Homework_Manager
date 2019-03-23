package Repo;

import Domain.Student;
import Validator.StudentValidator;
import Validator.Validator;

public class StudentRepo extends Repository<Integer, Student> {
    public StudentRepo(Validator<Student> v){
        super (v);
    }
}
