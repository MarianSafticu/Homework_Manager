package Validator;

import Domain.TemaLab;

public class TemaLaboratorValidator implements Validator<TemaLab> {

    @Override
    public void validate(TemaLab t) throws ValidationException {
        String err="";
        if(t.getID()>14 || t.getID()<1){
            err += "numarul temei de laborator invalid, trebuie sa fie un numar intre 1 si 14\n";
        }
        if(t.getDescriere().equals("")){
            err += "descriera nu poate fi vida \n";
        }
        if(t.getDeadline()<1 || t.getDeadline()>14){
            err += "termenul predarii invalid, trebuie sa fie un nunmar intre 1 si 14\n";
        }else if(t.getDeadline()<t.getStartDate()){
            err += "termenul de predare nu poate fi inainte de data primirii temei\n";
        }
        if(t.getStartDate()<1 || t.getStartDate()>14){
            err += "saptamana primiri temei invalida, trebuie sa fie un nunmar intre 1 si 14\n";
        }
        if(!err.equals("") ){
            throw new ValidationException(err);
        }
    }
}
