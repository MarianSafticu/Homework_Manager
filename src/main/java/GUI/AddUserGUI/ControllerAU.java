package GUI.AddUserGUI;

import Service.Service;
import Service.ServiceUsers;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ServiceConfigurationError;

public class ControllerAU {

    private ServiceUsers su;
    private Stage thisStage;

    public void setServiceUser(ServiceUsers su){
        this.su = su;
    }
    public void setStage(Stage s){
        thisStage = s;
    }
    @FXML
    private ComboBox<String> tipCB;
    @FXML
    private TextField numeF;
    @FXML
    private PasswordField passwordF;
    @FXML
    private TextField emailF;

    public void init(){
        tipCB.setItems(FXCollections.observableArrayList("profesor","admin"));
        tipCB.getSelectionModel().select(0);
    }
    @FXML
    private void addHandler(){
        su.save(emailF.getText(),passwordF.getText(),tipCB.getSelectionModel().getSelectedItem(),numeF.getText());
        thisStage.close();
    }
    @FXML
    private void cancelHandler(){
        thisStage.close();
    }

}
