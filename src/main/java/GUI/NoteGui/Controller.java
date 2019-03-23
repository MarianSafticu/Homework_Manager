package GUI.NoteGui;

import Domain.Nota;
import Domain.Student;
import GUI.NoteGui.AddWindow.ControllerADD;
import Service.Service;
import Validator.ValidationException;
import com.sun.javafx.collections.ObservableListWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;
import sun.swing.plaf.synth.Paint9Painter;

import javax.swing.text.html.Option;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
//import GUI.NoteGui.AddWindow.*;

public class Controller {
    private Service s;
    private ContextMenu entriesPopup;
    private String tipUser;

    public void setService(Service s){
        this.s = s;
    }
    public void addTipUser(String s){this.tipUser = s;}

    private Stage prevStage;
    private Stage thisStage;

    public void setPrevStage(Stage s){
        prevStage = s;
    }
    public void setCurentStage(Stage s){
        thisStage = s;
    }

    @FXML
    private Button backB;

//    @FXML
//    private RadioButton absenteB;
    @FXML
    private TableView tabel;
    @FXML
    private Label saptCurenta;
    private FileChooser fc;
    private String userName;

    public void setName(String name){
        userName = name;
    }
    @FXML
    private PieChart pc;
    @FXML
    private ComboBox<String> raportCB;
    @FXML
    private Button generatePDFB;

    @FXML
    private Button addButton;



    //filtre
    @FXML
    private ComboBox<String> laboratorCBFilter;
    @FXML
    private Label labCBLabel;
    @FXML
    private ComboBox<String> grupaCBFilter;
    @FXML
    private Label grupaCBLabel;
    @FXML
    private TextField numeStFieldFilter;
    @FXML
    private Label numeStLabel;
    @FXML
    private ComboBox<String> filterCB;


    @FXML
    private void backButtonHandler(){
        thisStage.close();
        prevStage.show();
    }

    @FXML
    private void addButtonHandler(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(GUI.MainGUI.Controller.class.getResource("addGUI.fxml"));

            Parent nroot = loader.load();

            ControllerADD c = loader.getController();

            c.setService(s);
            //c.setTipUser(tipUser);

            c.setCtrl(this);
            Stage ns = new Stage();
            ns.setTitle("Note App");
            ns.setScene(new Scene(nroot));
            c.setPrevStage(thisStage);
            c.setCurentStage(ns);
            c.init(Integer.parseInt(saptCurenta.getText()));
            c.setNume(userName);
            //thisStage.close();
            ns.show();


        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            System.out.println("EROARE!!");
        }

        initTable();
    }
    public void init(){
        fc = new FileChooser();
        initLabCB(laboratorCBFilter);
        initTable();
        grupaCBFilter.setItems(FXCollections.observableList(s.allGroups()));
        grupaCBFilter.getSelectionModel().select(0);

        //initializare filtre
        filterCB.setItems(FXCollections.observableArrayList("Toate notele","Note Student","Note Grupa la tema","Note Grupa","Note Laborator"));
        filterCB.getSelectionModel().select(0);

        numeStFieldFilter.setVisible(false);
        numeStLabel.setVisible(false);
        grupaCBFilter.setVisible(false);
        grupaCBLabel.setVisible(false);
        laboratorCBFilter.setVisible(false);
        labCBLabel.setVisible(false);

        if(!tipUser.equals("profesor")){
            addButton.setVisible(false);
        }

    }
    @FXML
    private void raportCBHandler(){
        switch (raportCB.getSelectionModel().getSelectedIndex()){
            case 0:
                ObservableList<PieChart.Data> pieChartData = FXCollections.observableList(s.medieSt().stream()
                .map(x->new PieChart.Data(x.getValue().toString(),1)).collect(Collectors.toList()));
                pc.setData(pieChartData);

                break;
            case 1:
                ObservableList<PieChart.Data> pieChartData2 = FXCollections.observableList(s.medieNoteFiecareLaborator().stream()
                        .map(x->new PieChart.Data("lab "+x.getKey(),1/(x.getValue()*100))).collect(Collectors.toList()));
                pc.setData(pieChartData2);
                break;
            case 2:
                ObservableList<PieChart.Data> pieChartData3 = FXCollections.observableArrayList();
                long eligibili = s.valiziExamen().stream().count();
                long toti = s.allStudents().spliterator().estimateSize();
                pieChartData3.add(new PieChart.Data("Pot intra in examen",eligibili));
                pieChartData3.add(new PieChart.Data("nu pot intra",toti-eligibili));
                pc.setData(pieChartData3);
                break;
            case 3:
                ObservableList<PieChart.Data> pieChartData4 = FXCollections.observableArrayList();
                long latimp = s.studentiFaraIntarziere().stream().count();
                long toti2 = s.allStudents().spliterator().estimateSize();
                pieChartData4.add(new PieChart.Data("Fara intarziere",latimp));
                pieChartData4.add(new PieChart.Data("cu cel putin o intarziere",toti2-latimp));
                pc.setData(pieChartData4);
                break;
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

        ObservableList<String> ol = FXCollections.observableList(list);
        cb.setItems(ol);
        cb.getSelectionModel().select(saptCurenta.getText());
    }
    @FXML
    public void generarePDFHandler(){
        fc.setInitialDirectory(new File("."));
        fc.setInitialFileName("raport"+raportCB.getSelectionModel().getSelectedIndex()+1+".pdf");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("pdf",".pdf"));

        System.out.println(fc.showSaveDialog(null));
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        DialogPane dp = a.getDialogPane();
        dp.getStylesheets().add(
                getClass().getResource("../MainGui/mydialog.css").toExternalForm());
        dp.getStyleClass().add("myDialog");
        a.setContentText("INCA NEIMPLEMENTAT\nINCERCATI SA FOLOSITI ACEST FEATURE MAI TARZIU");
        a.show();
    }
    public void initTable(){
        tabel.getColumns().clear();

        TableColumn<Nota, String> studentIdC = new TableColumn<>("Student");
        TableColumn<Nota, String> temaIdC = new TableColumn<>("Tema");
        TableColumn<Nota, String> profesorC = new TableColumn<>("Profesor");
        TableColumn<Nota, Double> notaC = new TableColumn<>("Nota");

        studentIdC.setCellValueFactory(p->{
            SimpleObjectProperty<String> prop = new SimpleObjectProperty<>();
            Student st = s.findStudent((Integer) p.getValue().getID().getFirst());
            if(st != null) {
                prop.setValue(st.getNume()+" ("+st.getID()+")");
                return prop;
            }else{
                return null;
            }
        });
        temaIdC.setCellValueFactory(p->{
            SimpleObjectProperty<String> prop = new SimpleObjectProperty<>();
            prop.setValue("Laboratorul "+p.getValue().getID().getSecond());
            return prop;
        });
        profesorC.setCellValueFactory(new PropertyValueFactory<>("profLab"));
        notaC.setCellValueFactory(p->{
            SimpleObjectProperty<Double> prop = new SimpleObjectProperty<>();
            prop.setValue(p.getValue().getGrade());
            return prop;
        });
        tabel.getColumns().addAll(studentIdC, temaIdC, profesorC, notaC);

        //final List<Nota> l= new ArrayList<>();
        //s.allGrades().forEach(System.out::println);
        ObservableList<Nota> model = FXCollections.observableList(StreamSupport.stream(s.allGrades().spliterator(), false).collect(Collectors.toList()));

        tabel.setItems(model);
    }



    @FXML
    private void filterCBHandler(){
        switch (filterCB.getSelectionModel().getSelectedIndex()){
            case 0:
                initTable();
                numeStFieldFilter.setVisible(false);
                numeStLabel.setVisible(false);
                grupaCBFilter.setVisible(false);
                grupaCBLabel.setVisible(false);
                laboratorCBFilter.setVisible(false);
                labCBLabel.setVisible(false);
                break;
            case 1:
                f2ButtonHandler();
                numeStFieldFilter.setVisible(true);
                numeStLabel.setVisible(true);
                grupaCBFilter.setVisible(false);
                grupaCBLabel.setVisible(false);
                laboratorCBFilter.setVisible(false);
                labCBLabel.setVisible(false);
                break;
            case 2:
                f3ButtonHandler();
                numeStFieldFilter.setVisible(false);
                numeStLabel.setVisible(false);
                grupaCBFilter.setVisible(true);
                grupaCBLabel.setVisible(true);
                laboratorCBFilter.setVisible(true);
                labCBLabel.setVisible(true);
                break;

            case 3:
                f4ButtonHandler();
                numeStFieldFilter.setVisible(false);
                numeStLabel.setVisible(false);
                grupaCBFilter.setVisible(true);
                grupaCBLabel.setVisible(true);
                laboratorCBFilter.setVisible(false);
                labCBLabel.setVisible(false);
                break;

            case 4:
                f1ButtonHandler();
                numeStFieldFilter.setVisible(false);
                numeStLabel.setVisible(false);
                grupaCBFilter.setVisible(false);
                grupaCBLabel.setVisible(false);
                laboratorCBFilter.setVisible(true);
                labCBLabel.setVisible(true);
                break;
        }

    }
    @FXML
    private void labCBHandler(){
        if (filterCB.getSelectionModel().getSelectedIndex() == 2){
            f3ButtonHandler();
        }
        else {
            f1ButtonHandler();
        }
    }
    @FXML
    private void grupaCBHandler(){
        if (filterCB.getSelectionModel().getSelectedIndex() == 2){
            f3ButtonHandler();
        }
        else {
            f4ButtonHandler();
        }
    }


    @FXML
    private void f1ButtonHandler(){
        tabel.getColumns().clear();
        TableColumn<Nota,String> numeC = new TableColumn<>("Student");
        TableColumn<Nota,String> labC = new TableColumn<>("Laborator");
        TableColumn<Nota,String> grupaC = new TableColumn<>("Grupa");
        TableColumn<Nota,String> notaC = new TableColumn<>("Nota");

        numeC.setCellValueFactory(n->{
            SimpleObjectProperty<String> nume = new SimpleObjectProperty<>();
            nume.setValue(s.findStudent(n.getValue().getStudentID()).getNume());
            return nume;
        });
        grupaC.setCellValueFactory(n->{
            SimpleObjectProperty<String> nume = new SimpleObjectProperty<>();
            nume.setValue(s.findStudent(n.getValue().getStudentID()).getGrupa().toString());
            return nume;
        });
        notaC.setCellValueFactory(n->{
            SimpleObjectProperty<String> nota = new SimpleObjectProperty<>();
            nota.setValue(n.getValue().getGrade().toString());
            return nota;
        });
        labC.setCellValueFactory(n->{
            SimpleObjectProperty<String> nota = new SimpleObjectProperty<>();
            nota.setValue(s.findTema(n.getValue().getTemaID()).getDescriere());
            return nota;
        });

        tabel.getColumns().addAll(numeC,labC,grupaC,notaC);
        tabel.setItems(FXCollections.observableList(s.noteAtLab(laboratorCBFilter.getSelectionModel().getSelectedIndex()+1)));

    }
    @FXML
    private void f2ButtonHandler(){
        tabel.getColumns().clear();
        TableColumn<Nota,String> numeC = new TableColumn<>("Student");
        TableColumn<Nota,String> labC = new TableColumn<>("Laborator");
        TableColumn<Nota,String> grupaC = new TableColumn<>("Grupa");
        TableColumn<Nota,String> notaC = new TableColumn<>("Nota");

        grupaC.setCellValueFactory(n->{
            SimpleObjectProperty<String> nume = new SimpleObjectProperty<>();
            nume.setValue(s.findStudent(n.getValue().getStudentID()).getGrupa().toString());
            return nume;
        });
        labC.setCellValueFactory(n->{
            SimpleObjectProperty<String> nota = new SimpleObjectProperty<>();
            nota.setValue(s.findTema(n.getValue().getTemaID()).getDescriere());
            return nota;
        });
        numeC.setCellValueFactory(n->{
            SimpleObjectProperty<String> nume = new SimpleObjectProperty<>();
            nume.setValue(s.findStudent(n.getValue().getStudentID()).getNume());
            return nume;
        });
        notaC.setCellValueFactory(n->{
            SimpleObjectProperty<String> nota = new SimpleObjectProperty<>();
            nota.setValue(n.getValue().getGrade().toString());
            return nota;
        });


        tabel.getColumns().addAll(numeC,labC,grupaC,notaC);
        tabel.setItems(FXCollections.observableList(s.noteForStudent(numeStFieldFilter.getText())));
    }
    @FXML
    private void f3ButtonHandler(){
        tabel.getColumns().clear();
        TableColumn<Nota,String> numeC = new TableColumn<>("Student");
        TableColumn<Nota,String> labC = new TableColumn<>("Laborator");
        TableColumn<Nota,String> grupaC = new TableColumn<>("Grupa");
        TableColumn<Nota,String> notaC = new TableColumn<>("Nota");

        grupaC.setCellValueFactory(n->{
            SimpleObjectProperty<String> nume = new SimpleObjectProperty<>();
            nume.setValue(s.findStudent(n.getValue().getStudentID()).getGrupa().toString());
            return nume;
        });
        labC.setCellValueFactory(n->{
            SimpleObjectProperty<String> nota = new SimpleObjectProperty<>();
            nota.setValue(s.findTema(n.getValue().getTemaID()).getDescriere());
            return nota;
        });

        numeC.setCellValueFactory(n->{
            SimpleObjectProperty<String> nume = new SimpleObjectProperty<>();
            nume.setValue(s.findStudent(n.getValue().getStudentID()).getNume());
            return nume;
        });
        notaC.setCellValueFactory(n->{
            SimpleObjectProperty<String> nota = new SimpleObjectProperty<>();
            nota.setValue(n.getValue().getGrade().toString());
            return nota;
        });

        tabel.getColumns().addAll(numeC,labC,grupaC,notaC);
        tabel.setItems(FXCollections.observableList(s.noteForGrupaAtTema(grupaCBFilter.getSelectionModel().getSelectedItem(),laboratorCBFilter.getSelectionModel().getSelectedIndex()+1)));
    }

    private TableColumn< javafx.util.Pair<String,List<Double> > ,String> addColF4(Integer poz){
        TableColumn<javafx.util.Pair<String,List<Double> > ,String> notal1C = new TableColumn<>("Lab"+poz.toString());

        notal1C.setCellValueFactory(n->{
            SimpleObjectProperty<String> nota = new SimpleObjectProperty<>();
            nota.setValue(n.getValue().getValue().get(poz-1).toString());
            return nota;
        });
        return notal1C;
    }

    @FXML
    private void f4ButtonHandler(){
        tabel.getColumns().clear();
        TableColumn< javafx.util.Pair<String,List<Double>>,String > numeC = new TableColumn<>("Student");
        TableColumn< javafx.util.Pair<String,List<Double>>,String > notal1C = addColF4(1);
        TableColumn< javafx.util.Pair<String,List<Double>>,String > notal2C = addColF4(2);
        TableColumn< javafx.util.Pair<String,List<Double>>,String > notal3C = addColF4(3);
        TableColumn< javafx.util.Pair<String,List<Double>>,String > notal4C = addColF4(4);
        TableColumn< javafx.util.Pair<String,List<Double>>,String > notal5C = addColF4(5);
        TableColumn< javafx.util.Pair<String,List<Double>>,String > notal6C = addColF4(6);
        TableColumn< javafx.util.Pair<String,List<Double>>,String > notal7C = addColF4(7);
        TableColumn< javafx.util.Pair<String,List<Double>>,String > notal8C = addColF4(8);
        TableColumn< javafx.util.Pair<String,List<Double>>,String > notal9C = addColF4(9);
        TableColumn< javafx.util.Pair<String,List<Double>>,String > notal10C = addColF4(10);
        TableColumn< javafx.util.Pair<String,List<Double>>,String > notal11C = addColF4(11);
        TableColumn< javafx.util.Pair<String,List<Double>>,String > notal12C = addColF4(12);

        numeC.setCellValueFactory(( n)->{
            SimpleObjectProperty<String> nume = new SimpleObjectProperty<>();
            nume.setValue(n.getValue().getKey());
            return nume;
        });

        tabel.getColumns().add(numeC);
        tabel.getColumns().add(notal1C);
        tabel.getColumns().add(notal2C);
        tabel.getColumns().add(notal3C);
        tabel.getColumns().add(notal4C);
        tabel.getColumns().add(notal5C);
        tabel.getColumns().add(notal6C);
        tabel.getColumns().add(notal7C);
        tabel.getColumns().add(notal8C);
        tabel.getColumns().add(notal9C);
        tabel.getColumns().add(notal10C);
        tabel.getColumns().add(notal11C);
        tabel.getColumns().add(notal12C);
        tabel.setItems(FXCollections.observableList(s.studentiGrupa(Integer.parseInt(grupaCBFilter.getSelectionModel().getSelectedItem()))));
    }
    @FXML
    private void backBHandler(){
        thisStage.close();
        prevStage.show();
    }

}
