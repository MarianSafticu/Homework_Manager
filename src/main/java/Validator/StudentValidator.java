package Validator;

import Domain.Student;

public class StudentValidator implements Validator<Student> {

    @Override
    public void validate(Student s) throws ValidationException{
        String err = "";
        if(s.getID()<=0){
            err += "Id invalid (Id-ul nu poate fi negativ)\n";
        }
        if(s.getNume().equals("")){
            err += "Nume invalid (numele nu poate fi vid) \n";
        }
        if(s.getGrupa()< 100 || s.getGrupa() >=1000) {
            err += "Grupa invalida (grupa trebuie sa fie un numar pozitiv format din 3 cifre)\n";
        }
        if(s.getEmail().equals("")){
            err += "Email-ul nu poate fi vid\n";
        }
        if(err.length()!=0){
            throw new ValidationException(err);
        }
    }

}
