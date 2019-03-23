package Repo;

import Domain.Nota;
import Domain.Pair;
import Domain.User;
import Utils.CassandraClient;
import Validator.Validator;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;

import java.util.ArrayList;
import java.util.List;

public class UsersRepo {
    private CassandraClient cc;
    public UsersRepo(CassandraClient c) {
        cc = c;
    }



//    private void load(){
//        for (Row r:cc.exec("select * from \"MapApp\".\"Note\";")){
//            Nota e = new Nota(r.getInt("stid"),r.getInt("tid"),r.getDouble("valoare"),r.getString("profesor"),r.getDate("data").toString());
//        }
//    }

    public boolean save(User s) {
        ResultSet r = cc.exec("select * from \"MapApp\".users where username = '"+s.getUsername()+"';");
        if(r.all().isEmpty()){
            cc.exec("insert into \"MapApp\".users(username,passwd,usertype,name) values ('"+s.getUsername()+"','"+s.getPasswd()+"','"+s.getTip()+"','"+s.getName()+"');");
            return true;
        }
        return false;
    }

    public boolean delete(String username) {
        ResultSet r = cc.exec("select * from \"MapApp\".users where username = '"+username+"';");
        if(!r.all().isEmpty()) {
            cc.exec("delete from \"MapApp\".users where username = '" + username + "';");
            return true;
        }
        return false;
    }

    public boolean update(User s) {
        ResultSet r = cc.exec("select * from \"MapApp\".users where username = '"+s.getUsername()+"';");
        if(!r.all().isEmpty()) {
            cc.exec("insert into \"MapApp\".users(username,passwd,usertype,name) values ('"+s.getUsername()+"','"+s.getPasswd()+"','"+s.getTip()+"','"+s.getName()+"');");
            return true;
        }
        return false;
    }
    public User findOne(String username){
        ResultSet rows = cc.exec("select * from \"MapApp\".users where username = '"+username+"';");
        List<Row> row = rows.all();
        if(row.size() == 0){
            return null;
        }
        Row r= row.get(0);
        if(r != null){
            return new User( r.getString("username"),r.getString("passwd"),r.getString("usertype"),r.getString("name"));
        }
        return null;
    }
    public List<User> findAll(String username){
        ResultSet rows = cc.exec("select * from \"MapApp\".users;");
        List<User> all = new ArrayList<>();
        for(Row r:rows){
            all.add(new User(r.getString("username"),r.getString("passwd"),r.getString("usertype"),r.getString("name")));
        }
        return all;
    }
}
