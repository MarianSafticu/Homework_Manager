package GUI.StudentiGUI;

import Domain.Student;
import GUI.ChangeEventType;
import GUI.StudentEvent;
import GUI.StudentiGUI.EditGUI.ControllerStEdit;
import Service.*;
import Utils.Observer;
import com.sun.javafx.scene.control.TableColumnComparatorBase;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;


import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Controller implements Observer<StudentEvent> {

    private Service s;
    private ObservableList<Student> obsl;
    private String tipUser;
    private ServiceUsers su;




    private Stage prevStage;
    private Stage thisStage;


    @FXML
    private TableView tw;
    @FXML
    private Button editStB;
    @FXML
    private Button back;
    @FXML
    private TextField NumeF;
    @FXML
    private TextField GrupaF;


    @FXML
    private void BackButtonHandler(){
        thisStage.close();
        prevStage.show();
    }

    @FXML
    private void EditButtonHandler(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Controller.class.getResource("../MainGUI/EditWindow.fxml"));
            Parent nroot =loader.load();

            ControllerStEdit c = loader.getController();
            c.setService(s);
            c.setServiceU(su);
            Stage ns = new Stage();
            ns.setScene(new Scene(nroot));
            ns.setTitle("Edit Sudenti");
            c.setThisStage(ns);
            ns.showAndWait();
            initTable();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @FXML
    private void GrupaFieldHandler(){
        tw.setItems(FXCollections.observableList(s.studentiNumeGrupa(NumeF.getText(),GrupaF.getText())));
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
    public void setTipUser(String tip){
        tipUser = tip;
    }
    public void setServiceU(ServiceUsers su){
        this.su = su;
    }

    public void init(){
        obsl = FXCollections.observableList(StreamSupport.stream(s.allStudents().spliterator(),false).collect(Collectors.toList()));
        this.s.addObserver(this);
        if(!tipUser.equals("admin")){
            editStB.setVisible(false);
        }
        initTable();
    }
    void initTable(){
        tw.getColumns().clear();
        TableColumn<Student,String> idC = new TableColumn<>("Id");
        TableColumn<Student,String> numeC = new TableColumn<>("Nume");
        TableColumn<Student,String> grupaC = new TableColumn<>("Grupa");
        TableColumn<Student,String> emailC = new TableColumn<>("Email");
        numeC.setCellValueFactory(new PropertyValueFactory<>("nume"));
        emailC.setCellValueFactory(new PropertyValueFactory<>("email"));
        idC.setCellValueFactory(new PropertyValueFactory<>("ID"));
        grupaC.setCellValueFactory(new PropertyValueFactory<>("grupa"));
//        grupaC.setCellValueFactory(p->{
//            SimpleObjectProperty<String> prop = new SimpleObjectProperty<>();
//            prop.setValue(p.getValue().getGrupa().toString());
//            return prop;
//        });

        tw.getColumns().addAll(idC,numeC,grupaC,emailC);
        tw.setItems(FXCollections.observableList(StreamSupport.stream(s.allStudents().spliterator(), false).collect(Collectors.toList())));

    }
//    public Controller(Service s,String tip){
//        this.s = s;
//        obsl = FXCollections.observableList(StreamSupport.stream(s.allStudents().spliterator(),false).collect(Collectors.toList()));
//        this.s.addObserver(this);
//        tipUser = tip;
//    }


    @Override
    public void update(StudentEvent studentEvent) {
        if(studentEvent.getType() == ChangeEventType.ADD){
            obsl.add(studentEvent.getData());
        }else if(studentEvent.getType() == ChangeEventType.UPDATE){
            obsl.set(obsl.indexOf(studentEvent.getOldData()),studentEvent.getData());
        }else if(studentEvent.getType() == ChangeEventType.DELETE){
            obsl.remove(studentEvent.getOldData());
        }
    }

    public ObservableList getObservers(){
        return obsl;
    }

    public void addSt(String id,String nume,String grupa,String email){
        s.addStudent(id,nume,grupa,email);
    }

    public void updateSt(String id,String nume,String grupa,String email){
        s.updateStudent(id,nume,grupa,email);
    }

    public void deleteSt(String id){
        s.deleteStudent(id);
    }
}
