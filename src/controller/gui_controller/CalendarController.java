package controller.gui_controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.IntStream;

/**
 * @author Arnaudo Enrico, Bonino Samuele, Burdisso Enrico.
 */
public class CalendarController implements Initializable {

    /**
     * Elementi dell'interfaccia grafica.
     */
    @FXML
    public AnchorPane id;
    @FXML
    public GridPane calendarGrid;
    @FXML
    public Button consultShiftBtn;
    @FXML
    public Label yearLabel;
    @FXML
    public ImageView arrowLeft;
    @FXML
    public ImageView arrowRight;

    /**
     * Istanza statica del controller.
     */
    private static CalendarController instance;

    /**
     * Variabili e oggetti di supporto.
     */
    private Circle circle;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private List<Label> daysLabel;
    private Map<Label, Boolean> isSelected;
    private int month = 0;
    private int year = 0;
    private int day = 0;

    /**
     * Metodo derivato dall'interfaccia {@link Initializable} la quale garantisce l'assegnazione dell'istanza statica del
     * controller all'attuale classe.
     *
     * @param location  Il percorso utilizzato per risolvere i percorsi relativi per l'oggetto radice o null se la posizione
     *                  non e' nota.
     * @param resources Le risorse utilizzate per localizzare l'oggetto radice o null se l'oggetto radice non e'
     *                  stato localizzato.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instance = this;
        daysLabel = new ArrayList<>();
        isSelected = new HashMap<>();
        calendar = Calendar.getInstance();
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);
        dateFormat = new SimpleDateFormat("MMMM YYYY");
        String date = dateFormat.format(calendar.getTime());
        yearLabel.setText(date.substring(0, 1).toUpperCase() + date.substring(1).toLowerCase());
        showCalendar();
    }

    /**
     * Metodo derivato dal pattern Singleton per ottenere l'istanza dell'attuale classe.
     *
     * @return un istanza di {@link CalendarController}.
     */
    public static CalendarController getInstance() {
        if (instance == null) {
            instance = new CalendarController();
        }
        return instance;
    }

    /**
     * Ottiene il mese scelto.
     *
     * @return il mese scelto.
     */
    int getMonth() {
        return month;
    }

    /**
     * Ottiene l'anno scelto.
     *
     * @return l'anno scelto.
     */
    int getYear() {
        return year;
    }

    /**
     * Ottiene il giorno scelto.
     *
     * @return il giorno scelto.
     */
    int getDay() {
        return day;
    }

    /**
     * Elimina lo sfondo attorno alla label.
     *
     * @param label label selezionata.
     */
    private void deleteLabelShadow(Label label) {
        isSelected.forEach((lbl, isSelect) -> {
            if (lbl.equals(label) && !isSelected.get(lbl)) {
                label.setStyle("-fx-background-color: #FFFFFF;" +
                        "-fx-background-radius: 0;" +
                        "-fx-background-position: center center;" +
                        "-fx-font-size: 18;" +
                        "-fx-font-family: 'Trebuchet MS';");
                label.setTextFill(Paint.valueOf("#000000"));
            }
        });
    }

    /**
     * Aggiunge uno sfondo alla label selezionata.
     *
     * @param label label selezionata.
     */
    private void addLabelShadow(Label label) {
        isSelected.forEach((lbl, isSelect) -> {
            if (!(lbl.equals(label) && isSelected.get(lbl))) {
                label.setStyle("-fx-background-color: #D7D7D7;" +
                        "-fx-background-radius: 50;" +
                        "-fx-background-position: center center;" +
                        "-fx-font-size: 18;" +
                        "-fx-font-family: 'Trebuchet MS';");
                label.setTextFill(Paint.valueOf("#000000"));
            }
        });

    }

    /**
     * Seleziona una label e ne modifica lo sfondo.
     *
     * @param label label selezionata.
     */
    private void setSelectedLabel(Label label) {
        isSelected.forEach((lbl, isSelect) -> {
            isSelected.put(lbl, false);
            deleteLabelShadow(lbl);
        });
        isSelected.put(label, true);
        label.setStyle("-fx-background-color: #4499DD;" +
                "-fx-background-radius: 50;" +
                "-fx-background-position: center center;" +
                "-fx-font-size: 18;" +
                "-fx-font-family: 'Trebuchet MS';");
        label.setTextFill(Paint.valueOf("#FFFFFF"));
    }

    /**
     * Aggiunge alla GridPane della scena i numeri del mese ed il mese stesso.
     */
    private void showCalendar() {
        Calendar calendar = new GregorianCalendar(year, month, 1);

        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        int initSpace = 5;
        initSpace += IntStream.range(0, dayOfWeek).count();

        int j = initSpace % 7 + 1;
        int rowIndex = 1;
        int columIndex = j;
        while (j <= daysInMonth + initSpace) {
            if (j - initSpace > 0) {
                Label label = new Label(String.valueOf(j - initSpace));
                daysLabel.add(label);
                isSelected.put(label, false);
                setLabelStyle(label);
                calendarGrid.add(label, columIndex - 1, rowIndex);
            }
            if (j % 7 == 0) {
                columIndex = 0;
                rowIndex++;
            }
            columIndex++;
            j++;
        }
        daysLabel.forEach(label -> {
            label.setOnMouseClicked(e -> setSelectedLabel(label));
            label.setOnMouseEntered(e -> addLabelShadow(label));
            label.setOnMouseExited(e -> deleteLabelShadow(label));
        });
    }

    /**
     * Aggiunge uno stile ad una label.
     *
     * @param label label da stilizzare.
     */
    private void setLabelStyle(Label label) {
        label.setPrefWidth(50);
        label.setPrefHeight(50);
        label.setAlignment(Pos.CENTER);
        label.setStyle("-fx-font-size: 18px;" +
                "-fx-font-family: 'Trebuchet MS'");
    }

    /**
     * Aggiorna il calendario spostandolo di un mese indietro nel tempo.
     */
    public void goBackCalendar() {
        daysLabel.forEach(label -> calendarGrid.getChildren().remove(label));
        daysLabel.clear();
        isSelected.clear();
        month--;
        if (month < 0) {
            month = 11;
            year--;
        }
        //noinspection MagicConstant
        calendar.set(year, month, 1);
        String date = dateFormat.format(calendar.getTime());
        yearLabel.setText(date.substring(0, 1).toUpperCase() + date.substring(1).toLowerCase());
        showCalendar();
    }

    /**
     * Aggiunge un cerchio come ombra.
     *
     * @param circle cerchio da aggiungere.
     */
    private void addShadow(Circle circle) {
        circle.setFill(Paint.valueOf("#D7D7D7"));
        circle.setStroke(Paint.valueOf("#D7D7D7"));
        circle.setSmooth(true);
        circle.setOpacity(0.5);
        circle.setRadius(20.0);
    }

    /**
     * Aggiunge un cerchio come ombra alla freccia destra.
     */
    public void addShadowRight() {
        circle = new Circle();
        addShadow(circle);
        circle.setOnMouseClicked(e -> goForwardCalendar());
        AnchorPane.setTopAnchor(circle, 12.0);
        AnchorPane.setLeftAnchor(circle, 12.0);
        id.getChildren().add(circle);
    }

    /**
     * Rimuove il cerchio che fungeva da ombra.
     */
    public void deleteShadow() {
        id.getChildren().remove(circle);
    }

    /**
     * Aggiunge un cerchio come ombra alla freccia sinistra.
     */
    public void addShadowLeft() {
        circle = new Circle();
        addShadow(circle);
        circle.setOnMouseClicked(e -> goBackCalendar());
        AnchorPane.setTopAnchor(circle, 12.0);
        AnchorPane.setRightAnchor(circle, 28.0);
        id.getChildren().add(circle);
    }

    /**
     * Aggiorna il calendario spostandolo di un mese avanti nel tempo.
     */
    public void goForwardCalendar() {
        daysLabel.forEach(label -> calendarGrid.getChildren().remove(label));
        daysLabel.clear();
        isSelected.clear();
        month++;
        if (month == 12) {
            month = 0;
            year++;
        }
        //noinspection MagicConstant
        calendar.set(year, month, 1);
        String date = dateFormat.format(calendar.getTime());
        yearLabel.setText(date.substring(0, 1).toUpperCase() + date.substring(1).toLowerCase());
        showCalendar();
    }

    /**
     * Carica nel pannello centrale dello stage la schermata di selezione del turno.
     */
    public void showShifts() {
        final boolean[] goOn = {false};
        isSelected.forEach((k, v) -> {
            if (v) {
                day = Integer.parseInt(k.getText());
                goOn[0] = true;
            }
        });
        if (goOn[0]) {
            isSelected.clear();
            daysLabel.clear();
            Controller.getInstance().getMainPane().getChildren().remove(id);
            try {
                Controller.getInstance().getMainPane().setCenter(FXMLLoader.load(getClass().getResource("../../view/gui2/shifts_chooser_view.fxml")));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            SnakbarController.getInstance().showSnakBar("Assicurati di aver selezionato \nun giorno", id);
        }
    }
}
