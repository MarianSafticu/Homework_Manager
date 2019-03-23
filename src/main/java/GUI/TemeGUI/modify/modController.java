package GUI.TemeGUI.modify;

import Service.Service;
import Validator.ValidationException;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.nio.channels.SeekableByteChannel;

public class modController {
    private Service s;
    private Stage thisStage;
    private Integer id;
    @FXML
    private Slider nrSaptSlider;
    @FXML
    private Label nrSaptL;

    public void setService(Service s){
        this.s = s;
    }
    public void setStage(Stage s){
        thisStage = s;
    }

    public void init(Integer id){
        nrSaptSlider.setMax(13);
        nrSaptSlider.setMin(0);
        this.id = id;
    }

    @FXML
    private void sliderMoveHandler(){
        nrSaptL.setText("Tema va fi prelungita cu "+(int)Math.floor(nrSaptSlider.getValue())+" saptamani ");
    }
    @FXML
    private void cancelHandler(){
        thisStage.close();
    }
    @FXML
    private void modifiHandler(){
        try {
            s.prelungireTermen(id, (int) Math.floor(nrSaptSlider.getValue()));
            thisStage.close();
        }catch (ValidationException e){
            Alert a = new Alert(Alert.AlertType.ERROR);
            DialogPane dp = a.getDialogPane();
            dp.getStylesheets().add(
                    getClass().getResource("../../MainGui/mydialog.css").toExternalForm());
            dp.getStyleClass().add("myDialog");
            a.setHeaderText(e.getMessage());
            a.showAndWait();
        }
    }


}
