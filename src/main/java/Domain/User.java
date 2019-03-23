package Domain;

import java.util.Objects;

public class User {
    String username;
    String passwd;
    String tip;
    String nume;

    public User(String username, String passwd, String tip,String nume) {
        this.username = username;
        this.passwd = passwd;
        this.tip = tip;
        this.nume = nume;
    }

    public String getName(){
        return nume;
    }
    public void setName(String n){
        nume = n;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username);
    }

}
