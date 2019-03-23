package GUI.StudentiGUI;

import Domain.Student;
import Validator.*;
import com.sun.org.apache.xml.internal.utils.Trie;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;

public class View {
    private TableView tv;
    private TextField idText;
    private TextField numeText;
    private TextField grupaText;
    private TextField emailText;
    private Controller ctrl;

    public View(Controller ctrl) {
        this.ctrl = ctrl;
        tv = new TableView<Student>();
    }

    public BorderPane getView(){
        BorderPane bp = new BorderPane();
        bp.setLeft(createTable());
        bp.setRight(createStudent());
        BackgroundSize bSize = new BackgroundSize(100, 100, true, true, false, true);
        Image image1 = new Image(""+View.class.getResource("bg.jpg"));

        bp.setBackground(new Background(new BackgroundImage(image1,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                bSize)));

        Trie a = new Trie();


        return bp;

    }

    private void initTable(){
        tv.setItems(ctrl.getObservers());
        TableColumn<Student, String > idC = new TableColumn<>("ID");
        TableColumn<Student, String > numeC = new TableColumn<>("Nume");
        TableColumn<Student, String > grupaC = new TableColumn<>("Grupa");
        TableColumn<Student, String > emailC = new TableColumn<>("Email");

        tv.getColumns().addAll(idC,numeC,grupaC,emailC);


        idC.setCellValueFactory(new PropertyValueFactory<>("ID"));
        numeC.setCellValueFactory(new PropertyValueFactory<>("Nume"));
        grupaC.setCellValueFactory(new PropertyValueFactory<>("Grupa"));
        emailC.setCellValueFactory(new PropertyValueFactory<>("Email"));

        BackgroundSize bSize = new BackgroundSize(100, 100, true, true, false, true);
        Image image1 = new Image(""+View.class.getResource("bg.jpg"));

        tv.setBackground(new Background(new BackgroundImage(image1,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                bSize)));

        tv.getSelectionModel().selectedItemProperty().addListener((observer,oldData,newData)->showDetails((Student) newData));


    }

    private void showDetails(Student s){
        if(s!= null){
           idText.setText(s.getID().toString());
           numeText.setText(s.getNume());
           grupaText.setText(s.getGrupa().toString());
           emailText.setText(s.getEmail());
        }
    }

    private StackPane createTable(){
        StackPane sp = new StackPane();
        sp.getChildren().add(tv);
        initTable();
        return sp;
    }

    private GridPane createStudent(){
        GridPane gp = new GridPane();
        Label idL = new Label("Id");
        idL.setTextFill(Paint.valueOf("blue"));
        idL.setStyle("-fx-font-weight: bold");

        Label numeL = new Label("Nume");
        numeL.setTextFill(Paint.valueOf("blue"));
        numeL.setStyle("-fx-font-weight: bold");

        Label grupaL = new Label("Grupa");
        grupaL.setTextFill(Paint.valueOf("blue"));
        grupaL.setStyle("-fx-font-weight: bold");

        Label mailL = new Label("Email");
        mailL.setTextFill(Paint.valueOf("blue"));
        mailL.setStyle("-fx-font-weight: bold");

        idText = new TextField();
//        idText.setStyle("-fx-background-color: black; -fx-text-fill: blue");

        numeText = new TextField();
//        numeText.setStyle("-fx-background-color: gray; -fx-text-fill: blue");

        grupaText   = new TextField();
//        grupaText.setStyle("-fx-background-color: black; -fx-text-fill: blue");

        emailText = new TextField();
//        emailText.setStyle("-fx-background-color: gray; -fx-text-fill: blue");

        gp.add(idL,0,0);
        gp.add(idText,1,0);
        gp.add(numeL,0,1);
        gp.add(numeText,1,1);
        gp.add(grupaL,0,2);
        gp.add(grupaText,1,2);
        gp.add(mailL,0,3);
        gp.add(emailText,1,3);

        HBox butBox = new HBox();
        Button addB = new Button("Add");
        addB.setOnAction(event -> {
            this.addHandler();
        });
        butBox.getChildren().add(addB);

        Button updateB = new Button("Update");
        updateB.setOnAction(event -> {
            this.updateHandler();
        });
        butBox.getChildren().add(updateB);

        Button deleteB = new Button("Delete");
        deleteB.setOnAction(event -> {
            this.deleteHandler();
        });
        butBox.getChildren().add(deleteB);

        Button clearB = new Button("Clear");
        clearB.setOnAction(event -> {
            this.clearHandler();
        });
        butBox.getChildren().add(clearB);

        gp.add(butBox,0,4,2,1);

        return gp;
    }

    private void addHandler() {
        try {
            ctrl.addSt(idText.getText(),numeText.getText(),grupaText.getText(),emailText.getText());
        } catch (ValidationException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Eroare adaugare");
            alert.setContentText(e.getMessage());
            alert.show();
        }
    }

    private void updateHandler() {
        try {

            ctrl.updateSt(idText.getText(),numeText.getText(),grupaText.getText(),emailText.getText());
        } catch (ValidationException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Eroare adaugare");
            alert.setContentText(e.getMessage());
            alert.show();
        }
    }

    private void deleteHandler() {
        try {

            ctrl.deleteSt(idText.getText());
        } catch (ValidationException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Eroare adaugare");
            alert.setContentText(e.getMessage());
            alert.show();
        }
    }

    private void clearHandler() {

        idText.setText("");
        numeText.setText("");
        grupaText.setText("");
        emailText.setText("");
    }

}