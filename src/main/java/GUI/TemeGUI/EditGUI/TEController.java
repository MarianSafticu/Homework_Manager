package GUI.TemeGUI.EditGUI;

import Service.Service;
import Validator.ValidationException;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sun.plugin.com.event.COMEventHandler;

import java.text.ParseException;
import java.time.LocalDate;

public class TEController {
    @FXML
    private TextField idT;
    @FXML
    private TextField descT;
    @FXML
    private ComboBox deadlineCB;
    @FXML
    private ComboBox startCB;

    private Service s;
    private Stage thisStage;
    private Stage prevStage;
    private Integer saptCurenta;

    public void setStage(Stage s){
        thisStage = s;
    }
    public void setPrevStage(Stage s){
        prevStage = s;
    }
    public void setService(Service s){
        this.s = s;
    }


    public void init(){
        deadlineCB.setItems(FXCollections.observableArrayList("1","2","3","4","5","6","7","8","9","10","11","12","13","14"));
        startCB.setItems(FXCollections.observableArrayList("1","2","3","4","5","6","7","8","9","10","11","12","13","14"));
        saptCurenta = s.getSaptamana_curenta(LocalDate.now());
        startCB.getSelectionModel().select(saptCurenta - 1);
        deadlineCB.getSelectionModel().select(saptCurenta % 14);
    }
    @FXML
    private void addHandler(){
        try {
            s.addTemaLab(Integer.parseInt(idT.getText()), descT.getText(), deadlineCB.getSelectionModel().getSelectedIndex() + 1, startCB.getSelectionModel().getSelectedIndex() + 1);
            thisStage.close();
        }catch (ValidationException e){
            Alert a = new Alert(Alert.AlertType.ERROR);
            DialogPane dp = a.getDialogPane();
            dp.getStylesheets().add(
                    getClass().getResource("../../MainGui/mydialog.css").toExternalForm());
            dp.getStyleClass().add("myDialog");
            a.setHeaderText(e.getMessage());
            a.showAndWait();
        }catch (NumberFormatException e){
            Alert a = new Alert(Alert.AlertType.ERROR);
            DialogPane dp = a.getDialogPane();
            dp.getStylesheets().add(
                    getClass().getResource("../../MainGui/mydialog.css").toExternalForm());
            dp.getStyleClass().add("myDialog");
            a.setHeaderText("ID-ul trebuie sa fie un numar");
            a.showAndWait();
        }

    }
    @FXML
    private void cancelHandler(){
        thisStage.close();
    }

}
