package GUI.NoteGui.AddWindow;

import Domain.Nota;
import GUI.NoteGui.Controller;
import Service.*;
import Validator.ValidationException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;

public class ControllerADD {
    private Service s;
    private Stage prevStage;
    private Stage thisStage;
    private Integer saptCurenta;
    private ContextMenu entriesPopup;
    private Controller c;
    private String nume;

    public void setCtrl(Controller c){
        this.c = c;
    }
    public void setPrevStage(Stage s){
        prevStage = s;
    }
    public void setCurentStage(Stage s){
        thisStage = s;
    }
    public void setService(Service s){
        this.s = s;
    }

    @FXML
    private RadioButton absenteB;
    @FXML
    private RadioButton saptAntRB;
    @FXML
    private Label saptAntL;
    @FXML
    private TextField studentField;
    @FXML
    private ComboBox<String> laboratorCB;
    @FXML
    private TextField notaField;
    @FXML
    private ComboBox saptAntCB;
    @FXML
    private TextArea feedbackArea;

    public void setNume(String nume){
        this.nume = nume;
    }
    @FXML
    private void addBHandler(){
        try {
            Double.parseDouble(notaField.getText());
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmare Nota");
            alert.setHeaderText("Vericare date nota");
            DialogPane dp = alert.getDialogPane();
            dp.getStylesheets().add(
                    getClass().getResource("../../MainGui/mydialog.css").toExternalForm());
            dp.getStyleClass().add("myDialog");
            alert.setContentText("Student : " + studentField.getText() +"\nLaborator "+(laboratorCB.getSelectionModel().getSelectedIndex()+1)
                    +"\n Nota : " + notaField.getText()+"\nProfesor :" + nume);
            Optional<ButtonType> res = alert.showAndWait();
            if(res.get() == ButtonType.CANCEL){
                return;
            }else {
                try {
                    Nota result;
                    if (!saptAntRB.isSelected()) {
                        result = s.assignGrade(s.getIdFromNume(studentField.getText()), laboratorCB.getSelectionModel().getSelectedIndex() + 1, Double.parseDouble(notaField.getText()), nume, feedbackArea.getText(), saptCurenta, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), absenteB.isSelected());
                    } else {
                        result = s.assignGrade(s.getIdFromNume(studentField.getText()), laboratorCB.getSelectionModel().getSelectedIndex() + 1, Double.parseDouble(notaField.getText()), nume, feedbackArea.getText(), saptAntCB.getSelectionModel().getSelectedIndex() + 1, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), absenteB.isSelected());
                    }
                    if (result != null) {
                        Alert alert2 = new Alert(Alert.AlertType.ERROR);
                        dp = alert2.getDialogPane();
                        dp.getStylesheets().add(
                                getClass().getResource("../../MainGui/mydialog.css").toExternalForm());
                        dp.getStyleClass().add("myDialog");
                        alert2.setHeaderText("Exista deja o nota pentru student la laborator ");
                        alert2.show();
                        return;
                    } else {
                        c.initTable();
                        thisStage.close();
                    }
                }catch(ValidationException e){
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    dp = a.getDialogPane();
                    dp.getStylesheets().add(
                            getClass().getResource("../../MainGui/mydialog.css").toExternalForm());
                    dp.getStyleClass().add("myDialog");
                    a.setContentText(e.getMessage());
                    a.showAndWait();
                }catch (ArrayIndexOutOfBoundsException e){
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    dp = a.getDialogPane();
                    dp.getStylesheets().add(
                            getClass().getResource("../../MainGui/mydialog.css").toExternalForm());
                    dp.getStyleClass().add("myDialog");
                    a.setContentText("trebuie sa alegeti un nume !!");
                    a.showAndWait();
                }
            }
        }catch (NumberFormatException e){

        }
        catch (ValidationException e){

        }
    }
    private void initLabCB(ComboBox<String> cb){

        ArrayList<String> list = new ArrayList<>();
        list.add("1 ");
        list.add("2 ");
        list.add("3 ");
        list.add("4 ");
        list.add("5 ");
        list.add("6 ");
        list.add("7 ");
        list.add("8 ");
        list.add("9 ");
        list.add("10 ");
        list.add("11 ");
        list.add("12 ");
        list.add("13 ");
        list.add("14 ");

        ObservableList<String> ol = FXCollections.observableList(list);
        saptAntCB.setItems(ol);
        cb.setItems(ol);
        cb.getSelectionModel().select(saptCurenta-2);
        saptAntCB.getSelectionModel().select(saptCurenta-2);
    }
    public void init(Integer sap){
        saptCurenta = sap;
        saptAntCB.setVisible(false);
        saptAntL.setVisible(false);
        absenteB.setSelected(false);
        initLabCB(laboratorCB);
        motivatHandler();

    }

    @FXML
    private void saptAntHandler(){
        if(!saptAntRB.isSelected()){
            saptAntCB.setVisible(false);
            saptAntL.setVisible(false);
        }else{
            saptAntCB.setVisible(true);
            saptAntL.setVisible(true);
        }
        motivatHandler();
    }
    @FXML
    private void cancelBHandler(){
        thisStage.close();
    }

    @FXML
    private void motivatHandler(){
        if (absenteB.isSelected()){
            feedbackArea.setText("");
        }else{
            Double n;
            if(!saptAntRB.isSelected())
                n = (saptCurenta-laboratorCB.getSelectionModel().getSelectedIndex() - 2)*2.5;
            else
                n = (saptAntCB.getSelectionModel().getSelectedIndex() + 1 -laboratorCB.getSelectionModel().getSelectedIndex() - 2)*2.5;

            if(n > 0 && n <= 5)
                feedbackArea.setText("NOTA A FOST DIMINUATĂ CU "+ n +" PUNCTE DATORITĂ ÎNTÂRZIERILOR\n");
            else if(n>5)
                feedbackArea.setText("NOTA A FOST DIMINUATA LA 1 DIN CAUZA INTARZIERILOR\n");
            else
                feedbackArea.setText("");
        }
    }
    private void addMenuItem(ContextMenu cm, String text ,TextField st){

        MenuItem mi = new MenuItem(text);
        mi.setOnAction(p->{
            st.setText(mi.getText());
        });
        cm.getItems().add(mi);

    }
    private void autoCompleteNume(TextField textf,boolean id){
        if (entriesPopup != null) {
            entriesPopup.hide();
        }
        entriesPopup = new ContextMenu();
        entriesPopup.setOpacity(0.7);
        s.allStudentsWith(textf.getText()).forEach((String s) -> addMenuItem(entriesPopup, id?s:s.split("[(]")[0],textf));

        Bounds bs = textf.localToScreen(textf.getBoundsInLocal());
        entriesPopup.show(textf, bs.getMinX(), bs.getMaxY());
    }
    @FXML
    private void stChangeHadler() {
        autoCompleteNume(studentField,true);
    }
}
