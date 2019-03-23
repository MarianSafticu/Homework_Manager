package GUI.MainGUI.EditLogin;

import Service.ServiceUsers;
import Validator.ValidationException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

public class LoginController {
    private ServiceUsers su;
    private String userName;
    private String tip;
    private Stage thisStage;

    public void setCurrentStage(Stage s){
        thisStage = s;
    }
    public void set(ServiceUsers s,String emailU,String tipu){
        su =s;
        userName = emailU;
        tip = tipu;
    }

    @FXML
    private PasswordField oldPw;
    @FXML
    private PasswordField newPw;

    @FXML
    private void cancelHandler(){
        thisStage.close();
    }

    @FXML
    private void saveHandler(){
        try {
            su.tryLogin(userName, oldPw.getText());
            if(su.updatePasswd(userName,newPw.getText(),tip)){
                Alert a = new Alert(Alert.AlertType.INFORMATION);
                a.setContentText("Parola a fost schimbata cu succes ");
                a.showAndWait();
            }
            thisStage.close();
        }catch (ValidationException e){
            Alert a = new Alert(Alert.AlertType.ERROR);
            DialogPane dp = a.getDialogPane();
            dp.getStylesheets().add(
                    getClass().getResource("../mydialog.css").toExternalForm());
            dp.getStyleClass().add("myDialog");
            a.setContentText(e.getMessage());
            a.showAndWait();
        }
    }

}
