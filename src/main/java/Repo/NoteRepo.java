package Repo;

import Domain.Nota;
import Domain.Pair;
import Validator.Validator;

public class NoteRepo extends Repository<Pair<Integer,Integer>, Nota> {
    public NoteRepo(Validator<Nota> v){
        super(v);
    }
}
