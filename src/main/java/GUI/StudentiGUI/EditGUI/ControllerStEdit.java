package GUI.StudentiGUI.EditGUI;

import Domain.Student;
import Service.*;
import Validator.ValidationException;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.Optional;


public class ControllerStEdit {
    private ServiceUsers su;
    private Service s;
    private Stage thisStage;

    public void setThisStage(Stage s){
        thisStage = s;
    }
    public void setService(Service s){
        this.s = s;
    }
    public void setServiceU(ServiceUsers su){
        this.su = su;
    }

    @FXML
    private TextField idF;
    @FXML
    private TextField numeF;
    @FXML
    private TextField grupaF;
    @FXML
    private TextField emailF;
    @FXML
    private PasswordField passwdF;

    @FXML
    private void addHandler(){
        try {
            Alert a = new Alert(Alert.AlertType.CONFIRMATION);
            a.setContentText("ID: "+idF.getText()+"\nNume: "+numeF.getText()+"\nGrupa: "+grupaF.getText()+"\nEmail: "+emailF.getText()+"\nParola cont: "+passwdF.getText());
            DialogPane dp = a.getDialogPane();
            dp.getStylesheets().add(
                    getClass().getResource("../../MainGui/mydialog.css").toExternalForm());
            dp.getStyleClass().add("myDialog");
            Optional<ButtonType> result = a.showAndWait();
            if(result.get() == ButtonType.OK) {
                su.save(emailF.getText(), passwdF.getText(), "student", numeF.getText());
                s.addStudent(idF.getText(), numeF.getText(), grupaF.getText(), emailF.getText());
            }
            thisStage.close();
        } catch (ValidationException e){
            Alert a = new Alert(Alert.AlertType.ERROR);
            DialogPane dp = a.getDialogPane();
            dp.getStylesheets().add(
                    getClass().getResource("../../MainGui/mydialog.css").toExternalForm());
            dp.getStyleClass().add("myDialog");
            a.setContentText(e.getMessage());
            a.showAndWait();
        }
    }
    @FXML
    private void updateHandler(){
        try{
            Student st = s.updateStudent(idF.getText(),numeF.getText(),grupaF.getText(),emailF.getText());

            if (!st.getEmail().equals(emailF.getText())){
                su.delete(st.getEmail());
                su.save(emailF.getText(),passwdF.getText(),"student",numeF.getText());
            }
            thisStage.close();

        } catch (ValidationException e){
            Alert a = new Alert(Alert.AlertType.ERROR);
            DialogPane dp = a.getDialogPane();
            dp.getStylesheets().add(
                    getClass().getResource("../../MainGui/mydialog.css").toExternalForm());
            dp.getStyleClass().add("myDialog");
            a.setContentText(e.getMessage());
            a.showAndWait();
        }
    }
    @FXML
    private void deleteHandler(){
        try{
            Student st =s.deleteStudent(idF.getText());
            if (st == null){
                Alert a = new Alert(Alert.AlertType.ERROR);
                DialogPane dp = a.getDialogPane();
                dp.getStylesheets().add(
                        getClass().getResource("../../MainGui/mydialog.css").toExternalForm());
                dp.getStyleClass().add("myDialog");
                a.setContentText("Nu exista nici un student cu acest id !!!");
                a.showAndWait();
            }
            su.delete(st.getEmail());
            thisStage.close();
        } catch (ValidationException e){
            Alert a = new Alert(Alert.AlertType.ERROR);
            DialogPane dp = a.getDialogPane();
            dp.getStylesheets().add(
                    getClass().getResource("../../MainGui/mydialog.css").toExternalForm());
            dp.getStyleClass().add("myDialog");
            a.setContentText(e.getMessage());
            a.showAndWait();
        }catch ( IllegalArgumentException e){
            Alert a = new Alert(Alert.AlertType.ERROR);
            DialogPane dp = a.getDialogPane();
            dp.getStylesheets().add(
                    getClass().getResource("../../MainGui/mydialog.css").toExternalForm());
            dp.getStyleClass().add("myDialog");
            a.setContentText(e.getMessage());
            a.showAndWait();
        }
    }
    @FXML
    private void cancelHandler(){
        thisStage.close();
    }

}
