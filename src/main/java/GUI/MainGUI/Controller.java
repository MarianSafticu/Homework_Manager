package GUI.MainGUI;

import GUI.AddUserGUI.ControllerAU;
import GUI.MainGUI.EditLogin.LoginController;
import GUI.RapoarteGUI.ControllerRap;
import GUI.StudentiGUI.View;
import Service.*;
import Utils.CassandraClient;
import Validator.ValidationException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class Controller {

    private Service s;
    private ServiceUsers su;
    private String tip;
    private String emailC;

    @FXML
    private Button studentiButton;

    @FXML
    private Button noteButton;

    @FXML
    private Button temeButton;

    @FXML
    private Button loginButton;
    @FXML
    private Button createAccountButton;
    @FXML
    private Button raportButton;
    @FXML
    private Label emailL;
    @FXML
    private Label passL;
    @FXML
    private TextField emailT;
    @FXML
    private PasswordField passwdF;
    @FXML
    private Label loginL;
    @FXML
    private Button editLogin;

    private String userName;

    private Stage thisStage;
    public void setThisStage(Stage s){
        thisStage = s;
    }



    @FXML
    private void loginButtonHandler(){
        try {

            tip = su.tryLogin(emailT.getText(), passwdF.getText());
            passL.setVisible(false);
            passwdF.setVisible(false);
            emailL.setVisible(false);
            emailT.setVisible(false);
            loginButton.setVisible(false);
            loginL.setVisible(false);
            raportButton.setVisible(true);
            if(tip.equals("admin")){
                createAccountButton.setVisible(true);
            }
            studentiButton.setVisible(true);
            noteButton.setVisible(true);
            temeButton.setVisible(true);
            editLogin.setVisible(true);
            userName = su.getName(emailT.getText());
            emailC = emailT.getText();
//            System.out.println(tip);
        }catch (ValidationException e){
            Alert a = new Alert(Alert.AlertType.ERROR);
            DialogPane dp = a.getDialogPane();
            dp.getStylesheets().add(
                    getClass().getResource("mydialog.css").toExternalForm());
            dp.getStyleClass().add("myDialog");
            a.setHeaderText("Error");
            a.setContentText(e.getMessage());
            a.showAndWait();
        }
    }

    @FXML
    private void raportBHandler(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Controller.class.getResource("rapoarteTab.fxml"));
            Parent nroot = loader.load();

            ControllerRap c = loader.getController();
            c.setService(s);
            c.setPrevStage(thisStage);
            Stage ns = new Stage();
            ns.setTitle("Rapoarte");
            ns.setScene(new Scene(nroot));
            c.setStage(ns);
            c.init();
            ns.show();
            thisStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void addAccountBHandler(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Controller.class.getResource("addUser.fxml"));
            Parent nroot = loader.load();

            ControllerAU c = loader.getController();
            c.setServiceUser(su);
            Stage ns = new Stage();
            ns.setTitle("Rapoarte");
            ns.setScene(new Scene(nroot));
            c.setStage(ns);
            c.init();
            ns.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void init(){

        studentiButton.setVisible(false);
        noteButton.setVisible(false);
        temeButton.setVisible(false);
        editLogin.setVisible(false);
        raportButton.setVisible(false);
        createAccountButton.setVisible(false);
        /*loginButton.setStyle("-fx-text-fill: #006464;\n" +
                "  -fx-background-color: #DFB951;\n" +
                "  -fx-border-radius: 20;\n" +
                "  -fx-background-radius: 20;\n" +
                "  -fx-padding: 5;\n" +
                "  -fx-background-color: linear-gradient(from 0% 0% to 100% 100% , #8b0000 0%, #00008b 100%);");
        */
    }

    @FXML
    private void exitHandler(){
        if(passL.isVisible()){
            thisStage.close();
        }else{

            passwdF.clear();
            emailT.clear();
            passL.setVisible(true);
            passwdF.setVisible(true);
            emailL.setVisible(true);
            emailT.setVisible(true);
            loginButton.setVisible(true);
            loginL.setVisible(true);
            studentiButton.setVisible(false);
            noteButton.setVisible(false);
            temeButton.setVisible(false);
            editLogin.setVisible(false);
            raportButton.setVisible(false);
            createAccountButton.setVisible(false);
        }
    }

    @FXML
    private void editLoginHandler(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Controller.class.getResource("EditLogin.fxml"));
            Parent nroot = loader.load();

            LoginController c = loader.getController();
            c.set(su,emailC,tip);
            Stage ns = new Stage();
            ns.setTitle("Change Password");
            ns.setScene(new Scene(nroot));
            c.setCurrentStage(ns);
            ns.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void stBHandler(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Controller.class.getResource("StudentiWindow.fxml"));

            Parent nroot = loader.load();

            GUI.StudentiGUI.Controller c = loader.getController();

            c.setService(s);
            c.setTipUser(tip);
            c.setServiceU(su);

            Stage ns = new Stage();
            ns.setTitle("Note App");
            ns.setScene(new Scene(nroot));
            c.setPrevStage(thisStage);
            c.setCurentStage(ns);
            c.init();
            thisStage.close();
            ns.show();


        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            System.out.println("EROARE!!");
        }
    }

    @FXML
    private void noteBHandler(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Controller.class.getResource("noteApp.fxml"));

            Parent nroot = loader.load();

            GUI.NoteGui.Controller c = loader.getController();

            c.setService(s);
            c.addTipUser(tip);
            c.setPrevStage(thisStage);
            c.init();
            c.setName(userName);
            Stage ns = new Stage();
            ns.setTitle("Note App");
            ns.setScene(new Scene(nroot));
            thisStage.close();
            ns.show();

            c.setCurentStage(ns);


        } catch (IOException e) {


        }
    }
    @FXML
    private void temeBHandler(){
        try {
//            Alert a = new Alert(Alert.AlertType.INFORMATION);
//            a.setHeaderText("FEATURE IN LUCRU");
//            a.setContentText("va rugam incercati mai tarziu\nAcest feature este in lucru\n(probabil pana vineri :) )");
//            a.show();
//            if (true){
//                return;
//            }
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Controller.class.getResource("TemeView.fxml"));

            Parent nroot = loader.load();

            GUI.TemeGUI.Controller c = loader.getController();

            c.setService(s);
            c.setTipUser(tip);

            c.init();
            Stage ns = new Stage();
            ns.setTitle("Note App");
            ns.setScene(new Scene(nroot));
            c.setCurentStage(ns);
            c.setPrevStage(thisStage);
            thisStage.close();
            ns.show();


        } catch (IOException e) {


        }
    }

    public void setServices(Service s, ServiceUsers su){
        this.s = s;
        this.su = su;
    }
}
