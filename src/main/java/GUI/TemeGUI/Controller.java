package GUI.TemeGUI;

import Domain.TemaLab;
import GUI.StudentiGUI.EditGUI.ControllerStEdit;
import GUI.TemeGUI.EditGUI.TEController;
import GUI.TemeGUI.modify.modController;
import Service.Service;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Controller {
    Service s;
    String tipUser;
    Integer saptCurenta;
    private Stage prevStage;
    private Stage thisStage;

    @FXML
    private TableView tv;
    @FXML
    private TextField idText;
    @FXML
    private Button modB;
    @FXML
    private Button addB;



    public void setPrevStage(Stage s){
        prevStage = s;
    }
    public void setCurentStage(Stage s){
        thisStage = s;
    }
    public void setService(Service s){
        this.s = s;
    }
    public void setTipUser(String t){
        tipUser = t;
    }

    private void initTable(){
        tv.getColumns().clear();
        TableColumn<TemaLab,Integer> idC = new TableColumn<>("ID");
        TableColumn<TemaLab,String> descC = new TableColumn<>("Descriere");
        TableColumn<TemaLab,Integer> deadlineC = new TableColumn<>("Deadline");
        TableColumn<TemaLab,Integer> primireC = new TableColumn<>("Saptamana Primirii");

        idC.setCellValueFactory(new PropertyValueFactory<>("ID"));
        descC.setCellValueFactory(new PropertyValueFactory<>("descriere"));
        deadlineC.setCellValueFactory(new PropertyValueFactory<>("deadline"));
        primireC.setCellValueFactory(new PropertyValueFactory<>("startDate"));

        tv.getColumns().addAll(idC,descC,primireC,deadlineC);
        tv.setItems(FXCollections.observableList(StreamSupport.stream(s.allHomeworks().spliterator(), false).collect(Collectors.toList())));
    }


    public void init(){
        ObservableList<Integer> ol = FXCollections.observableArrayList();
        s.allHomeworks().forEach(x->ol.add(x.getID()));
        initTable();
        if(!tipUser.equals("profesor")){
            addB.setVisible(false);
            modB.setVisible(false);
        }
    }

    @FXML
    public void textFiler(){
        tv.getColumns().clear();
        TableColumn<TemaLab,Integer> idC = new TableColumn<>("ID");
        TableColumn<TemaLab,String> descC = new TableColumn<>("Descriere");
        TableColumn<TemaLab,Integer> deadlineC = new TableColumn<>("Deadline");
        TableColumn<TemaLab,Integer> primireC = new TableColumn<>("Saptamana Primirii");

        idC.setCellValueFactory(new PropertyValueFactory<>("ID"));
        descC.setCellValueFactory(new PropertyValueFactory<>("descriere"));
        deadlineC.setCellValueFactory(new PropertyValueFactory<>("deadline"));
        primireC.setCellValueFactory(new PropertyValueFactory<>("startDate"));

        tv.getColumns().addAll(idC,descC,primireC,deadlineC);

        tv.setItems(FXCollections.observableList(s.temeDesc(idText.getText())));
    }
    @FXML
    public void addHandler(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(GUI.StudentiGUI.Controller.class.getResource("../MainGUI/temeedit.fxml"));
            Parent nroot =loader.load();

            TEController c = loader.getController();
            c.setService(s);
            Stage ns = new Stage();
            ns.setScene(new Scene(nroot));
            ns.setTitle("Add Tema");
            c.setStage(ns);
            c.init();
            ns.showAndWait();
            initTable();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void backHandler(){
        thisStage.close();
        prevStage.show();
    }

    @FXML
    public void modTemaHandler(){
        try {
            if(tv.getSelectionModel().getSelectedItem() == null){
                Alert a = new Alert(Alert.AlertType.ERROR);
                DialogPane dp = a.getDialogPane();
                dp.getStylesheets().add(
                        getClass().getResource("../MainGui/mydialog.css").toExternalForm());
                dp.getStyleClass().add("myDialog");
                a.setHeaderText("Terebuie sa selectati o tema din tabel!!");
                a.showAndWait();
                return;
            }
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(GUI.StudentiGUI.Controller.class.getResource("../MainGUI/newDeadline.fxml"));
            Parent nroot =loader.load();

            modController c = loader.getController();
            c.setService(s);
            Stage ns = new Stage();
            ns.setScene(new Scene(nroot));
            ns.setTitle("Add Tema");
            c.setStage(ns);
            c.init(((TemaLab)tv.getSelectionModel().getSelectedItem()).getID());
            ns.showAndWait();
            initTable();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
