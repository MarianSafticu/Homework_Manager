package Repo;

import Domain.Student;
import Utils.CassandraClient;
import Utils.InactiveAction;
import Validator.Validator;
import Validator.ValidationException;
import com.datastax.driver.core.*;

public class StudentDBRepo extends StudentRepo{
    //private Session session;
    private CassandraClient cc;
    public StudentDBRepo(Validator<Student> v,CassandraClient c) {
        super(v);
        cc = c;
        load();

    }



    private void load(){
        for (Row r:cc.exec("select * from \"MapApp\".\"Studenti\";")){
            this.elems.put(r.getInt("id"),new Student(r.getInt("id"),r.getString("nume"),r.getInt("grupa"),r.getString("email")));
        }
    }

    @Override
    public Student save(Student s) throws ValidationException {
        ResultSet r = cc.exec("select * from \"MapApp\".\"Studenti\" where id ="+s.getID()+";");
        if(r.all().isEmpty()){
            Student e = super.save(s);
            cc.exec("insert into \"MapApp\".\"Studenti\"(id,nume,grupa,email) values ("+s.getID()+",'"+s.getNume()+"',"+s.getGrupa()+",'"+s.getEmail()+"');");

            return e;
        }
        return s;
    }

    @Override
    public Student delete(Integer id) {
        Student e = super.delete(id);
        cc.exec("delete from \"MapApp\".\"Studenti\" where id = "+id+";");
        cc.exec("delete from \"MapApp\".\"Note\" where stid = "+id+";");
        return e;
    }

    @Override
    public Student update(Student s) {
        ResultSet r = cc.exec("select * from \"MapApp\".\"Studenti\" where id ="+s.getID()+";");
        if(!r.all().isEmpty()) {
            Student e = super.update(s);
            cc.exec("insert into \"MapApp\".\"Studenti\"(id,nume,grupa,email) values (" + s.getID() + ",'" + s.getNume() + "'," + s.getGrupa() + ",'" + s.getEmail() + "');");
            return e;
        }
        return null;
    }
}
