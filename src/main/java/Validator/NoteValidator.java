package Validator;

import Domain.Nota;

public class NoteValidator implements Validator<Nota> {

    @Override
    public void validate(Nota n) throws ValidationException{
        String err = "";
        if(n.getGrade()>10 || n.getGrade() < 1){
            err += "Nota trebuie sa fie intre 1 si 10\n";
        }if(n.getProfLab().equals("")){
            err += "numele profesorului nu poate fi vid\n";
        }
        if(err.length()!=0){
            throw new ValidationException(err);
        }
    }
}
