package Repo;

import Domain.HasID;
import Domain.Student;
import Validator.ValidationException;
import Validator.Validator;
import Validator.StudentValidator;

import java.util.Map;
import java.util.TreeMap;

public abstract class Repository<ID,E extends HasID<ID>> implements CrudRepository<ID,E> {
    protected Map<ID,E> elems;
    private Validator<E> v;
    public Repository(Validator<E> validator){
        elems = new TreeMap();
        v = validator;
    }


    @Override
    public E findOne(ID id) {
        if ( id == null ) throw new IllegalArgumentException("null ID\n");
        return elems.get(id);
    }

    @Override
    public Iterable<E> findAll() {
        return elems.values();
    }

    @Override
    public E save(E s) throws ValidationException {
        v.validate(s);
        if(elems.get(s.getID())==null) {
            elems.put(s.getID(),s);
            return null;
        }else{
            return s;
        }
    }

    @Override
    public E delete(ID id) {
        if(id==null) throw new IllegalArgumentException("null id\n");
        return elems.remove(id);
    }

    @Override
    public E update(E s) {
        if(s==null) throw new IllegalArgumentException("null entity\n");
        v.validate(s);
        return elems.replace(s.getID(),s);
    }
}
