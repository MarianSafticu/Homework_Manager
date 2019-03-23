package Service;

import Domain.User;
import Repo.UsersRepo;
import Validator.ValidationException;

import javax.jws.soap.SOAPBinding;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ServiceUsers {
    UsersRepo users;
    MessageDigest md;
    public ServiceUsers(UsersRepo users) {
        this.users = users;
        try{
            md = md.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
    public boolean delete(String username){
        return users.delete(username);
    }
    public boolean save(String username,String passwd,String tip,String nume){
        byte[] criptedPasswd = md.digest(passwd.getBytes());
        char[] strCP = new char[criptedPasswd.length];
        for(int i=0;i<criptedPasswd.length;i++){
            strCP[i] = (char)criptedPasswd[i];
        }

        User u = new User(username,String.valueOf(strCP),tip,nume);
        if(! users.save(u)){
            throw new ValidationException("Exista deja un utilizator cu acest email in baza de date!!!");
        }
        return true;
    }

    public boolean updatePasswd(String username,String passwd,String tip){
        byte[] criptedPasswd = md.digest(passwd.getBytes());
        char[] strCP = new char[criptedPasswd.length];
        for(int i=0;i<criptedPasswd.length;i++){
            strCP[i] = (char)criptedPasswd[i];
        }
        String nume = users.findOne(username).getName();
        User u = new User(username,String.valueOf(strCP),tip,nume);
        return users.update(u);
    }

    public String tryLogin(String username,String passwd){
        if(username.length() == 0){
            throw new ValidationException("email invalid!!!");
        }
        byte[] criptedPasswd = md.digest(passwd.getBytes());
        char[] strCP = new char[criptedPasswd.length];
        for(int i=0;i<criptedPasswd.length;i++){
            strCP[i] = (char)criptedPasswd[i];
        }
        User s = users.findOne(username);
        if(s == null){
            throw new ValidationException("email invalid!!!");
        }else{
            if(!String.valueOf(strCP).equals(s.getPasswd())){
                throw new ValidationException("Parola gresita!!!");
            }
        }
        return s.getTip();
    }
    public String getName(String username){
        return users.findOne(username).getName();
    }

}
