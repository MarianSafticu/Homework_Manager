package Repo;

import Domain.Student;
import Domain.TemaLab;
import Utils.CassandraClient;
import Validator.*;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;

public class TemaDBRepo extends TemaRepo {
    //private Session session;
    private CassandraClient cc;
    public TemaDBRepo(Validator<TemaLab> v, CassandraClient c) {
        super(v);
        cc = c;
        load();

    }



    private void load(){
        for (Row r:cc.exec("select * from \"MapApp\".\"Laboratoare\";")){
            this.elems.put(r.getInt("id"),new TemaLab(r.getInt("id"),r.getString("descriere"),r.getInt("deadline"),r.getInt("start")));
        }
    }

    @Override
    public TemaLab save(TemaLab s) throws ValidationException {
        ResultSet r = cc.exec("select * from \"MapApp\".\"Laboratoare\" where id ="+s.getID()+";");
        if(r.all().isEmpty()){
            TemaLab e = super.save(s);
            cc.exec("insert into \"MapApp\".\"Laboratoare\"(id,descriere,deadline,start) values ("+s.getID()+",'"+s.getDescriere()+"',"+s.getDeadline()+","+s.getStartDate()+");");
            return e;
        }
        return s;
    }

    @Override
    public TemaLab delete(Integer id) {
        TemaLab t = super.delete(id);
        cc.exec("delete from \"MapApp\".\"Laboratoare\" where id = "+id+";");
        return t;
    }

    @Override
    public TemaLab update(TemaLab s) {
        ResultSet r = cc.exec("select * from \"MapApp\".\"Laboratoare\" where id ="+s.getID()+";");
        if(!r.all().isEmpty()) {
            TemaLab e = super.update(s);
            cc.exec("insert into \"MapApp\".\"Laboratoare\"(id,descriere,deadline,start) values ("+s.getID()+",'"+s.getDescriere()+"',"+s.getDeadline()+","+s.getStartDate()+");");
            return e;
        }
        return null;
    }
}
