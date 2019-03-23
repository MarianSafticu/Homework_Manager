package Repo;

import Domain.Nota;
import Domain.Pair;
import Domain.Student;
import Utils.CassandraClient;
import Validator.*;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;

public class NoteDBRepo extends NoteRepo {
    private CassandraClient cc;
    public NoteDBRepo(Validator<Nota> v, CassandraClient c) {
        super(v);
        cc = c;
        load();

    }



    private void load(){
        for (Row r:cc.exec("select * from \"MapApp\".\"Note\";")){
            Nota e = new Nota(r.getInt("stid"),r.getInt("tid"),r.getDouble("valoare"),r.getString("profesor"),r.getDate("data").toString());
            this.elems.put(e.getID(),e);
        }
    }




    @Override
    public Nota save(Nota s) throws ValidationException {
        ResultSet r = cc.exec("select * from \"MapApp\".\"Note\" where stid = "+s.getStudentID()+" and tid = "+s.getTemaID()+";");
        if(r.all().isEmpty()){
            Nota e = super.save(s);
            cc.exec("insert into \"MapApp\".\"Note\"(stid,tid,valoare,profesor,data) values ("+s.getStudentID()+","+s.getTemaID()+","+s.getGrade()+",'"+s.getProfLab()+"','"+s.getDate()+"');");
            return e;
        }
        return s;
    }

    @Override
    public Nota delete(Pair<Integer, Integer> id) {
        Nota e =super.delete(id);
        cc.exec("delete from \"MapApp\".\"Note\" where stid = "+id.getFirst()+" and tid = "+id.getSecond()+";");
        return e;
    }

    @Override
    public Nota update(Nota s) {
        Nota e = super.update(s);
        ResultSet r = cc.exec("select * from \"MapApp\".\"Note\" where stid = "+s.getStudentID()+" and tid = "+s.getTemaID()+";");
        if(!r.all().isEmpty()) {
            cc.exec("insert into \"MapApp\".\"Note\"(stid,tid,valoare,profesor,data) values ("+s.getStudentID()+","+s.getTemaID()+","+s.getGrade()+",'"+s.getProfLab()+"','"+s.getDate()+"');");
        }
        return e;
    }
}
