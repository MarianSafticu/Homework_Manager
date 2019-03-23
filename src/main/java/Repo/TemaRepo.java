package Repo;

import Domain.TemaLab;
import Validator.TemaLaboratorValidator;
import Validator.Validator;

public class TemaRepo extends Repository<Integer, TemaLab> {
    public TemaRepo(Validator<TemaLab> v){
        super(v);
    }
}
