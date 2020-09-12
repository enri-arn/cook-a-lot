package controller.gui_controller;

import javafx.fxml.FXML;
import model.businesslogic.Shift;
import controller.bl_controller.TaskMenuController;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import model.services.CookALotServiceProvider;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Arnaudo Enrico, Bonino Samuele, Burdisso Enrico.
 */
public class ShiftChooserController implements Initializable {

    /**
     * Istanza statica del controller.
     */
    private static ShiftChooserController instance;

    /**
     * Componenti dell'interfaccia grafica
     */
    @FXML
    public AnchorPane shiftsCenterPane;
    @FXML
    public ImageView goBackArrow;
    @FXML
    public GridPane shiftGridPane;
    @FXML
    public Label shiftDate;

    /**
     * Liste di colori e generatore di di numeri random.
     */
    private List<String> colors = new ArrayList<>();
    private int idColor = 0;
    private List<Shift> shiftsDM;
    private Calendar calendar;
    private CalendarController c;

    /**
     * Metodo derivato dal pattern Singleton per ottenere l'istanza dell'attuale classe.
     *
     * @return un istanza di {@link ShiftChooserController}.
     */
    public static ShiftChooserController getInstance() {
        if (instance == null) {
            instance = new ShiftChooserController();
        }
        return instance;
    }

    /**
     * Ritorna alla schermata del menu' rimuovendo dal pannello principale il pannello centrale sostituendolo
     * con la schermata "center_main_pane.fxml".
     */
    public void goBack() {
        Controller.getInstance().getMainPane().getChildren().remove(2);
        try {
            Controller.getInstance().getMainPane().setCenter(FXMLLoader.load(getClass().getResource("../../view/gui2/center_main_pane.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metodo derivato dall'interfaccia {@link Initializable} la quale garantisce l'assegnazione dell'istanza statica del
     * controller all'attuale classe.
     * Inizializza la lista di colori e aggiunge le ore, i colori e i turni alla gridpane principale.
     *
     * @param location  Il percorso utilizzato per risolvere i percorsi relativi per l'oggetto radice o null se la posizione
     *                  non e' nota.
     * @param resources Le risorse utilizzate per localizzare l'oggetto radice o null se l'oggetto radice non e'
     *                  stato localizzato.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instance = this;
        colors.add("#F44336");
        colors.add("#FF4081");
        colors.add("#9C27B0");
        colors.add("#7C4DFF");
        colors.add("#3F51B5");
        colors.add("#448AFF");
        colors.add("#03A9F4");
        colors.add("#00BCD4");
        colors.add("#F44336");
        colors.add("#009688");
        colors.add("#3F51B5");
        colors.add("#4CAF50");
        colors.add("#8BC34A");
        colors.add("#CDDC39");
        colors.add("#FFC107");
        colors.add("#FF9800");
        colors.add("#FF5722");
        colors.add("#795548");
        colors.add("#9E9E9E");
        colors.add("#607D8B");
        calendar = Calendar.getInstance();
        c = CalendarController.getInstance();
        shiftsDM = CookALotServiceProvider.getInstance().getDataManager().getAllShifts(c.getDay(), c.getMonth(), c.getYear());
        if (shiftsDM.size() == 0) {
            loadEmptyScene();
        } else {
            addHoursToGridPane();
            addColorToRows();
            addShiftToRows();
        }
    }

    /**
     * Aggiunge al GridPane principale i turni mostrandoli nelle fasce orarie appropriate.
     */
    private void addShiftToRows() {
        //noinspection MagicConstant
        calendar.set(c.getYear(), c.getMonth(), c.getDay());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM YYYY");
        String date = dateFormat.format(calendar.getTime());
        shiftDate.setText(date.substring(0, 1).toUpperCase() + date.substring(1).toLowerCase());
        shiftsDM.forEach(s -> {
            AnchorPane pane = new AnchorPane();
            String color = getColor();
            pane.setStyle("-fx-background-color: " + color + ";");
            Label label = new Label("TURNO " + s.getId().substring(12));
            setLabelStyle(label, 20);
            Label shiftType = new Label("SERVIZIO");
            setLabelStyle(shiftType, 10);
            setBackground(shiftType);
            Label assignCount = new Label("assegnamenti: " + s.getAssignementsCount() + "\ncuochi: " + s.getCooks().size());
            setLabelStyle(assignCount, 14);
            if (!s.getLine()) {
                Button showMenuBtn = new Button("CONSULTA MENU'");
                setButtonStyle(showMenuBtn);
                showMenuBtn.setOnAction(e -> showMenu(s));
                AnchorPane.setRightAnchor(showMenuBtn, 10.00);
                AnchorPane.setTopAnchor(showMenuBtn, 10.0);
                pane.getChildren().add(showMenuBtn);

            } else {
                shiftType.setText("LINEA");
            }
            AnchorPane.setLeftAnchor(shiftType, 5.0);
            AnchorPane.setTopAnchor(shiftType, 5.0);
            pane.getChildren().add(shiftType);
            int startHour = getStartHour(s.getStartTime());
            int rowSpan = getRowSpan(s.getEndTime(), s.getStartTime());
            shiftGridPane.add(pane, 1, startHour, 3, rowSpan);
            shiftGridPane.add(label, 1, startHour, 3, rowSpan);
            shiftGridPane.add(assignCount, 2, startHour, 3, rowSpan);
        });
    }

    /**
     * Carica la schermata di turni vuoti nel caso in cui non ce ne siano.
     */
    private void loadEmptyScene() {
        shiftsCenterPane.getChildren().clear();
        try {
            shiftsCenterPane.getChildren().add(FXMLLoader.load(getClass().getResource("../../view/gui2/shift_chooser_empty.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Ottiene un colore dalla lista.
     *
     * @return una stringa rappresentante il colore in esadecimale.
     */
    public String getColor() {
        String color = colors.get(idColor);
        idColor++;
        if (idColor >= colors.size()) {
            idColor = 0;
        }
        return color;
    }

    /**
     * Ottiene l'ora da una data.
     *
     * @param date data selezionata.
     * @return l'ora della data.
     */
    private int getStartHour(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * Ottiene la differenza in ore tra due date.
     *
     * @param date1 data di fine.
     * @param date2 data di inizio.
     * @return la differenza tra data fine e data inizio.
     */
    private int getRowSpan(Date date1, Date date2) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date1);
        int endHour = calendar.get(Calendar.HOUR_OF_DAY);
        calendar.setTime(date2);
        int startHour = calendar.get(Calendar.HOUR_OF_DAY);
        return endHour - startHour;
    }

    /**
     * Aggiunge lo stile alla Label.
     *
     * @param label label da stilizzare.
     * @param font  grandezza del testo.
     */
    private void setLabelStyle(Label label, int font) {
        label.setStyle("-fx-font-family: 'Trebuchet MS'");
        label.setFont(new Font(font));
        label.setAlignment(Pos.CENTER_RIGHT);
        label.setTextFill(Paint.valueOf("#FFFFFF"));
        label.setPadding(new Insets(10, 10, 10, 10));
    }

    /**
     * Aggiunge lo stile alla Label.
     *
     * @param label label da stilizzare.
     */
    private void setBackground(Label label) {
        String fontColor = "#000000";
        if (label.getText().equalsIgnoreCase("L")) {
            fontColor = "#FF0000";
        }
        label.setTextFill(Paint.valueOf(fontColor));
        label.setPrefHeight(13);
        label.setPrefWidth(50);
        label.setAlignment(Pos.CENTER);
        label.setPadding(new Insets(3, 5, 3, 5));
        label.setStyle("-fx-font-family: 'Trebuchet MS';" +
                "-fx-background-color: #FFFFFF;" +
                "-fx-background-position: center center;" +
                "-fx-background-radius: 12;" +
                "-fx-opacity: 0.6;");
    }

    /**
     * Aggiunge lo stile ad un bottone.
     *
     * @param button bottone a cui applicare lo stile.
     */
    private void setButtonStyle(Button button) {
        button.setStyle("-fx-background-color: #FFFFFF;" +
                "-fx-font-family: 'Trebuchet MS';" +
                "-fx-text-fill: #000000;" +
                "-fx-font-size: 18;");
        button.setPrefWidth(250);
        button.setPrefHeight(50);
        button.setMaxHeight(50);
    }

    /**
     * Aggiunge i colori al GridPane principale.
     */
    private void addColorToRows() {
        for (int i = 0; i < 24; i++) {
            Pane pane1 = new Pane();
            Pane pane2 = new Pane();
            Pane pane3 = new Pane();
            pane1.setPrefHeight(50);
            pane2.setPrefHeight(50);
            pane3.setPrefHeight(50);
            if (i % 2 == 0) {
                pane1.setStyle("-fx-background-color: #ffffff;");
                pane2.setStyle("-fx-background-color: #ffffff;");
                pane3.setStyle("-fx-background-color: #ffffff;");
            } else {
                pane1.setStyle("-fx-background-color: #dedede;");
                pane2.setStyle("-fx-background-color: #dedede;");
                pane3.setStyle("-fx-background-color: #dedede;");
            }
            shiftGridPane.add(pane1, 1, i);
            shiftGridPane.add(pane2, 2, i);
            shiftGridPane.add(pane3, 3, i);
        }
    }

    /**
     * Aggiunge le ore al GridPane principale.
     * Le ore sono rappresentante da una Label.
     */
    private void addHoursToGridPane() {
        for (int i = 0; i < 24; i++) {
            String text = String.valueOf(i);
            Label label = new Label(text);
            label.setPrefWidth(60);
            label.setPrefHeight(50);
            label.setStyle("-fx-font-family: 'Trebuchet MS'");
            label.setFont(new Font(18));
            label.setAlignment(Pos.CENTER_RIGHT);
            label.setTextFill(Paint.valueOf("#FFFFFF"));
            label.setPadding(new Insets(0, 10, 0, 0));
            label.setStyle("-fx-background-color: #212121;");
            shiftGridPane.add(label, 0, i);
        }
    }

    /**
     * Mostra il menu' del turno selezionato.
     * Viene caricato nel pannello centrale la schermata "menu_view.fxml" e richiamato il metodo showExpandedMenu del
     * controller {@link MenuViewController}.
     *
     * @param shift turno selezionato.
     */
    private void showMenu(Shift shift) {
        TaskMenuController.getInstance().setCurrentShift(shift);
        Controller.getInstance().getMainPane().getChildren().remove(2);
        try {
            Controller.getInstance().getMainPane().setCenter(FXMLLoader.load(getClass().getResource("../../view/gui2/menu_view.fxml")));
            MenuViewController.getInstance().showExpandedMenu();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
