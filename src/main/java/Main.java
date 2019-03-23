import Domain.*;
import GUI.StudentiGUI.Controller;
import Utils.CassandraClient;
import Validator.*;
import Repo.*;
import Service.*;
//import UI.ConsoleUI;

import com.datastax.driver.core.exceptions.NoHostAvailableException;
import javafx.application.Application;
import javafx.beans.property.Property;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;


public class Main extends Application {
    @Override
    public void start(Stage ps){
        try {
            Validator<Student> vs = new StudentValidator();
            Validator<TemaLab> vt = new TemaLaboratorValidator();
            Validator<Nota> vn = new NoteValidator();
            try {

                CassandraClient client = new CassandraClient("127.0.0.1");
                Repository<Integer, Student> rs = new StudentDBRepo(vs, client);
                Repository<Integer, TemaLab> rt = new TemaDBRepo(vt, client);
                Repository<Pair<Integer, Integer>, Nota> rn = new NoteDBRepo(vn, client);
//                Repository<Integer, Student> rs = new StudentFileRepo(vs,"data/studenti.txt");
//                Repository<Integer, TemaLab> rt = new TemaFileRepo(vt, "data/teme.txt");
//                Repository<Pair<Integer, Integer>, Nota> rn = new NoteFileRepo(vn, "data/catalog.txt");
                Service s = new Service(rs, rt, rn);

                UsersRepo ur = new UsersRepo(client);
                ServiceUsers su = new ServiceUsers(ur);


                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(Main.class.getResource("GUI/MainGUI/mainApp.fxml"));
                Parent root = loader.load();
                GUI.MainGUI.Controller ctrl = loader.getController();

                ctrl.setServices(s,su);
                ctrl.init();
                ctrl.setThisStage(ps);
                ps.setTitle("Student app");
                Scene thisScene = new Scene(root);
                ps.setScene(thisScene);
                ps.show();
            } catch (NoHostAvailableException e) {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setHeaderText("Eroare la conexiunea la baza de date!!\n"+e.getMessage());
                a.show();
            }


        } catch (IOException e) {
            e.printStackTrace();
            ps.close();
        }
    }
    public static void main(String[] args) {
        launch(args);

        //ConsoleUI app=new ConsoleUI(s);
        //app.run();

    }
}
