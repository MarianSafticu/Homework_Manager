package GUI.RapoarteGUI;

import Domain.Student;
import Domain.TemaLab;
import Service.Service;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ControllerRap {

    private Service s;
    private Stage thisStage;
    private Stage prevStage;
    public void setService(Service s){
        this.s = s;
    }
    public void setStage(Stage s){
        thisStage = s;
    }
    public void setPrevStage(Stage s){
        prevStage = s;
    }

    @FXML
    private PieChart pc;
    @FXML
    private ComboBox<String> raportCB;
    @FXML
    private Button generatePDFB;
    @FXML
    private TableView tv;

    private FileChooser fc = new FileChooser();

    private void addTableHeaderR1(PdfPTable table) {
        Stream.of("ID", "Nume", "Grupa","Media")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(1);
                    header.setPhrase(new Phrase(columnTitle));
                    table.addCell(header);
                });
    }
    private void addTableHeaderR2(PdfPTable table) {
        Stream.of("ID", "Descriere", "Deadline","Media")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(1);
                    header.setPhrase(new Phrase(columnTitle));
                    table.addCell(header);
                });
    }
    private void addTableHeaderR3(PdfPTable table) {
        Stream.of("ID", "Nume","Grupa")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(1);
                    header.setPhrase(new Phrase(columnTitle));
                    table.addCell(header);
                });
    }

    private void addRowsR1(PdfPTable table,String id,String nume,String grupa,String media) {
        table.addCell(id);
        table.addCell(nume);
        table.addCell(grupa);
        if (media.length() > 6) {
            table.addCell(media.subSequence(0, 5).toString());
        }else{
            table.addCell(media);
        }
    }
    private void addRowsR2(PdfPTable table,String id,String nume,String grupa) {
        table.addCell(id);
        table.addCell(nume);
        table.addCell(grupa);
    }


    @FXML
    public void generarePDFHandler(){
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Pdf",".pdf"));
        fc.setInitialFileName("raport"+(raportCB.getSelectionModel().getSelectedIndex()+1)+".pdf");
        File saveF= fc.showSaveDialog(null);
        if(saveF == null){
            return;
        }
        try {

            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(saveF));

            document.open();

            Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);

            Paragraph title;
            PdfPTable table;
            switch (raportCB.getSelectionModel().getSelectedIndex()) {
                case 0:
                    title = new Paragraph("Mediile Studentilor:");
                    title.setAlignment(Element.ALIGN_CENTER);
                    document.add(title);
                    document.add(new Paragraph(""));
                    document.add(new Paragraph(""));
                    table = new PdfPTable(4);
                    addTableHeaderR1(table);
                    List<Pair<Student, Double>> all1 = s.medieSt();
                    all1.sort((x, y) -> {
                        return y.getValue().compareTo(x.getValue());
                    });
                    all1.forEach(x -> {
                        addRowsR1(table, x.getKey().getID().toString(), x.getKey().getNume(), x.getKey().getGrupa().toString(), x.getValue().toString());
                    });
                    break;
                case 1:
                    title = new Paragraph("Mediile Notelor de Laborator:");
                    title.setAlignment(Element.ALIGN_CENTER);
                    document.add(title);
                    document.add(new Paragraph(""));
                    document.add(new Paragraph(""));
                    table = new PdfPTable(4);
                    addTableHeaderR2(table);
                    List<Pair<Integer,Double>> all2 = s.medieNoteFiecareLaborator();
                    all2.sort((x, y) -> {
                        return x.getValue().compareTo(y.getValue());
                    });
                    all2.forEach(x -> {
                        addRowsR1(table, s.findTema(x.getKey()).getID().toString(), s.findTema(x.getKey()).getDescriere(), s.findTema(x.getKey()).getDeadline()+"", x.getValue().toString());
                    });
                    break;
                case 2:
                    title = new Paragraph("Studentii care pot intra in examen:");
                    title.setAlignment(Element.ALIGN_CENTER);
                    document.add(title);
                    document.add(new Paragraph(""));
                    document.add(new Paragraph(""));
                    table = new PdfPTable(3);
                    addTableHeaderR3(table);
                    List<Student> all3 = s.valiziExamen();
                    all3.sort((x, y) -> {
                        return x.getNume().compareTo(y.getNume());
                    });
                    all3.forEach(x -> {
                        addRowsR2(table,x.getID().toString(),x.getNume(),x.getGrupa().toString() );
                    });
                    break;
                default:
                    title = new Paragraph("Studentii fara intarzieri la predare:");
                    title.setAlignment(Element.ALIGN_CENTER);
                    document.add(title);
                    document.add(new Paragraph(""));
                    document.add(new Paragraph(""));
                    table = new PdfPTable(3);
                    addTableHeaderR3(table);
                    List<Student> all = s.studentiFaraIntarziere();
                    all.sort((x, y) -> {
                        return x.getNume().compareTo(y.getNume());
                    });
                    all.forEach(x -> {
                        addRowsR2(table, x.getID().toString(),x.getNume(),x.getGrupa().toString());
                    });
                    break;

            }
            document.add(table);
            document.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initTable(){
        tv.getColumns().clear();
        switch (raportCB.getSelectionModel().getSelectedIndex()) {
            case 0:
                TableColumn<Pair<Student,Double>,Integer> id1C= new TableColumn<>("ID");
                TableColumn<Pair<Student,Double>,String> nume1C= new TableColumn<>("Nume");
                TableColumn<Pair<Student,Double>,Integer> grupa1C= new TableColumn<>("Grupa");
                TableColumn<Pair<Student,Double>, Double> medie1C = new TableColumn<>("medie");

                id1C.setCellValueFactory(x->{
                    SimpleObjectProperty<Integer> n = new SimpleObjectProperty<>();
                    n.setValue(x.getValue().getKey().getID());
                    return n;
                });
                medie1C.setCellValueFactory(new PropertyValueFactory<>("value"));
                nume1C.setCellValueFactory(x->{
                    SimpleObjectProperty<String> n = new SimpleObjectProperty<>();
                    n.setValue(x.getValue().getKey().getNume());
                    return n;
                });
                grupa1C.setCellValueFactory(x->{
                    SimpleObjectProperty<Integer> n = new SimpleObjectProperty<>();
                    n.setValue(x.getValue().getKey().getGrupa());
                    return n;
                });

                tv.getColumns().addAll(id1C,nume1C,grupa1C,medie1C);
                tv.setItems(FXCollections.observableList(s.medieSt()));
                break;
            case 1:
                TableColumn<Pair<Integer,Double>,Integer> idC= new TableColumn<>("ID Tema");
                TableColumn<Pair<Integer,Double>,String> descC= new TableColumn<>("descriere");
                TableColumn<Pair<Integer,Double>,Integer> deadlineC= new TableColumn<>("deadline");
                TableColumn<Pair<Integer,Double>, Double> medieC = new TableColumn<>("medie");

                idC.setCellValueFactory(new PropertyValueFactory<>("key"));
                medieC.setCellValueFactory(new PropertyValueFactory<>("value"));
                descC.setCellValueFactory(x->{
                    SimpleObjectProperty<String> n = new SimpleObjectProperty<>();
                    n.setValue(s.findTema(x.getValue().getKey()).getDescriere());
                    return n;
                });
                deadlineC.setCellValueFactory(x->{
                    SimpleObjectProperty<Integer> n = new SimpleObjectProperty<>();
                    n.setValue(s.findTema(x.getValue().getKey()).getDeadline());
                    return n;
                });

                tv.getColumns().addAll(idC,descC,deadlineC,medieC);
                tv.setItems(FXCollections.observableList(s.medieNoteFiecareLaborator()));
                break;
            case 2:
                TableColumn<Student,Integer> idSC= new TableColumn<>("ID");
                TableColumn<Student,String> numeSC= new TableColumn<>("Nume");
                TableColumn<Student,Integer> grupaSC= new TableColumn<>("Grupa");

                idSC.setCellValueFactory(new PropertyValueFactory<>("ID"));
                numeSC.setCellValueFactory(new PropertyValueFactory<>("nume"));
                grupaSC.setCellValueFactory(new PropertyValueFactory<>("grupa"));


                tv.getColumns().addAll(idSC,numeSC,grupaSC);
                tv.setItems(FXCollections.observableList(s.valiziExamen()));
                break;
            case 3:
                idSC= new TableColumn<>("ID");
                numeSC= new TableColumn<>("nume");
                grupaSC = new TableColumn<>("grupa");

                idSC.setCellValueFactory(new PropertyValueFactory<>("ID"));
                numeSC.setCellValueFactory(new PropertyValueFactory<>("nume"));
                grupaSC.setCellValueFactory(new PropertyValueFactory<>("grupa"));


                tv.getColumns().addAll(idSC,numeSC,grupaSC);
                tv.setItems(FXCollections.observableList(s.studentiFaraIntarziere()));
                break;
        }
    }

    public void init(){
        initTable();

        //initializare filtre
        raportCB.setItems(FXCollections.observableArrayList("Note Laborator","Cea mai grea tema","Eligibili examen","Predare la timp"));
        raportCB.getSelectionModel().select(0);
        raportCBHandler();
        fc = new FileChooser();
    }
    @FXML
    private void raportCBHandler(){
        switch (raportCB.getSelectionModel().getSelectedIndex()){
            case 0:
                ObservableList<PieChart.Data> pieChartData = FXCollections.observableList(s.mediiScurteStudenti().stream()
                        .map((Pair<Integer,Integer> x)->new PieChart.Data(x.getKey().toString(),x.getValue())).collect(Collectors.toList()));
                pc.setData(pieChartData);
                initTable();
                break;
            case 1:
                ObservableList<PieChart.Data> pieChartData2 = FXCollections.observableList(s.medieNoteFiecareLaborator().stream()
                        .map(x->new PieChart.Data("lab "+x.getKey(),1/(x.getValue()*100))).collect(Collectors.toList()));
                pc.setData(pieChartData2);
                initTable();
                break;
            case 2:
                ObservableList<PieChart.Data> pieChartData3 = FXCollections.observableArrayList();
                long eligibili = s.valiziExamen().stream().count();
                long toti = s.allStudents().spliterator().estimateSize();
                pieChartData3.add(new PieChart.Data("Pot intra in examen",eligibili));
                pieChartData3.add(new PieChart.Data("nu pot intra",toti-eligibili));
                pc.setData(pieChartData3);
                initTable();
                break;
            case 3:
                ObservableList<PieChart.Data> pieChartData4 = FXCollections.observableArrayList();
                long latimp = s.studentiFaraIntarziere().stream().count();
                long toti2 = s.allStudents().spliterator().estimateSize();
                pieChartData4.add(new PieChart.Data("Fara intarziere",latimp));
                pieChartData4.add(new PieChart.Data("cu intarziere",toti2-latimp));
                pc.setData(pieChartData4);
                initTable();
                break;
        }
    }
    @FXML
    private void backBHandler(){
        thisStage.close();
        prevStage.show();
    }

}
