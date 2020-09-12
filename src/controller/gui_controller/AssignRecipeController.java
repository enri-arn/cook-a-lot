package controller.gui_controller;

import controller.bl_controller.TaskMenuController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Font;
import model.businesslogic.Cook;
import model.businesslogic.Item;
import model.businesslogic.Recipe;
import model.businesslogic.Shift;
import model.businesslogic.exception.UseCaseLogicException;
import model.services.CookALotServiceProvider;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Arnaudo Enrico, Bonino Samuele, Burdisso Enrico.
 */
public class AssignRecipeController implements Initializable {

    /**
     * Istanza statica del controller e puntatore al controller della business logic.
     */
    private static AssignRecipeController instance;
    private TaskMenuController ctrl;

    /**
     * Componenti dell'innterfaccia grafica.
     */
    @FXML
    public Label refMenuLabel;
    @FXML
    public Label recipeLabel;
    @FXML
    public Label descriptionLabel;
    @FXML
    public Label timeLabel;
    @FXML
    public TilePane cookNameTag;
    @FXML
    public Button assignBtn;
    @FXML
    public TilePane shiftNameTag;
    @FXML
    public AnchorPane assignViewMainPane;
    @FXML
    public ImageView goBackArrow;
    @FXML
    public AnchorPane assignCenterPane;
    @FXML
    public Button deleteBtn;

    /**
     * Tabelle di associazione oggetti business - interfaccia grafica.
     */
    private Map<Label, Boolean> cooksLabel = new HashMap<>();
    private Map<GridPane, Boolean> shiftsTags = new HashMap<>();
    private HashMap<GridPane, Shift> gridShift = new HashMap<>();
    private HashMap<Label, Cook> lblCook = new HashMap<>();

    /**
     * Puntatori alla ricetta corrente, turno e cuoco selezionato.
     */
    private Recipe currentRecipe;
    private Shift selectedShift;
    private Cook selectedCook;

    /**
     * Metodo derivato dal pattern Singleton per ottenere l'istanza dell'attuale classe.
     *
     * @return un istanza di {@link AssignRecipeController}.
     */
    static AssignRecipeController getInstance() {
        if (instance == null) {
            instance = new AssignRecipeController();
        }
        return instance;
    }

    /**
     * Ritorna alla schermata del menu' rimuovendo dal pannello principale il pannello centrale sostituendolo
     * con la schermata "menu_view.fxml".
     */
    public void goBack() {
        Controller.getInstance().getMainPane().getChildren().remove(2);
        try {
            Controller.getInstance().getMainPane().setCenter(FXMLLoader.load(getClass().getResource("../../view/gui2/menu_view.fxml")));
            MenuViewController.getInstance().showExpandedMenu();
        } catch (IOException e) {
            e.printStackTrace();
        }
        MenuViewController.getInstance().showExpandedMenu();
    }

    /**
     * Metodo derivato dall'interfaccia {@link Initializable} la quale garantisce l'assegnazione dell'istanza statica del
     * controller all'attuale classe e l'assegnamento al puntatore del {@link TaskMenuController}  alla sua istanza
     * ottenuta tramite pattern Singleton.
     *
     * @param location  Il percorso utilizzato per risolvere i percorsi relativi per l'oggetto radice o null se la posizione
     *                  non e' nota.
     * @param resources Le risorse utilizzate per localizzare l'oggetto radice o null se l'oggetto radice non e'
     *                  stato localizzato.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instance = this;
        ctrl = TaskMenuController.getInstance();
    }

    /**
     * Imposta i componenti dell'interfaccia grafica inserendo il nome del menu', il nome della ricetta  la descrizione
     * e il tempo di attivita'.
     * Imposta il puntatore alla ricetta corrente alla ricetta selezionata.
     * Aggiunge graficamente i tag dei cuochi e dei turni.
     *
     * @param recipe ricetta selezionata.
     * @param item   voce della sezione selezionata usata per ottenere la descrizione.
     */
    void setComponent(Recipe recipe, Item item) {
        refMenuLabel.setText(ctrl.getCurrentMenu().getTitle());
        recipeLabel.setText(recipe.getName());
        descriptionLabel.setText(item.getDescription());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(recipe.getActivityTime());
        timeLabel.setText(calendar.get(Calendar.HOUR_OF_DAY) + " ore "
                + calendar.get(Calendar.MINUTE) + " min "
                + calendar.get(Calendar.SECOND) + " sec");
        currentRecipe = recipe;
        addCookNameTag();
        addShiftTag();
    }

    /**
     * Assegna uno stile alla Label selezionata indicandone il font family, il colore di sfondo, la dimensione del testo,
     * il raggio dello sfondo e il colore del testo.
     * Aggiunge poi un padding di 5pt alla Label.
     *
     * @param label      Label a cui applicare lo stile.
     * @param background colore doi sfondo espresso in notazione esadecimale.
     * @param radius     raggio da applicare allo sfondo.
     * @param textColor  colore el testo espresso in notazione esadecimale.
     */
    private void setLabelStyle(Label label, String background, int radius, String textColor) {
        label.setStyle("-fx-font-family: 'Trebuchet MS'; -fx-background-color: " + background + ";" +
                " -fx-background-radius: " + radius + ";" +
                "-fx-text-fill: " + textColor + ";");
        label.setFont(Font.font((double) 16));
        label.setPadding(new Insets(5, 5, 5, 5));
    }

    /**
     * Aggiunge i tag dei turni contenenti l'id del turno, la sua data di inizio e di fine.
     * I tag sono costituiti da un GridPane con una sola colonna e più righe nelle quali sono alloggiate le varie
     * Label con i rispettivi contenuti.
     * La GridPane verra' mappata nella tabella gridShift per mantenere un riferimento al turno.
     */
    private void addShiftTag() {
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE dd MMMM");
        SimpleDateFormat formatter1 = new SimpleDateFormat(" HH:mm ");
        List<Shift> shifts = CookALotServiceProvider.getInstance().getDataManager().getAllShifts();
        shifts.forEach(shift -> {
            GridPane gridPane = new GridPane();
            gridPane.setStyle("-fx-background-color: #d7d7d7;-fx-background-radius: 5;");
            Label id = new Label("\t\tTURNO " + shift.getId().substring(12));
            setLabelStyle(id, "#D7D7D7", 0, "#010101");
            Label dateTime = new Label("  " + formatter.format(shift.getStartTime()));
            setLabelStyle(dateTime, "#D7D7D7", 0, "#000000");
            Label startTime = new Label("  Dalle:   " + formatter1.format(shift.getStartTime()));
            setLabelStyle(startTime, "#D7D7D7", 0, "#000000");
            Label endTime = new Label("  Alle:     " + formatter1.format(shift.getEndTime()));
            setLabelStyle(endTime, "#D7D7D7", 0, "#000000");
            Label type = new Label("Tipo: " + (shift.getLine() ? "Linea" : "Servizio"));
            setLabelStyle(type, "#D7D7D7", 0, "#000000");
            gridPane.add(id, 0, 0);
            gridPane.add(dateTime, 0, 1);
            gridPane.add(startTime, 0, 2);
            gridPane.add(endTime, 0, 3);
            gridPane.add(type, 0, 4);
            gridPane.setPrefSize(200, 150);
            gridPane.setOnMouseClicked(e -> setSelectedShift(gridPane));
            gridShift.put(gridPane, shift);
            shiftNameTag.getChildren().add(gridPane);
            shiftsTags.put(gridPane, false);
        });
    }

    /**
     * Aggiunge i tag dei cuochi contenenti il nome del cuoco.
     * I tag sono costituiti da una Label.
     * La Label verra' mappata nella tabella lblCook per mantenere un riferimento al cuoco.
     */
    private void addCookNameTag() {
        CookALotServiceProvider.getInstance().getDataManager().getAllCooks().forEach(cook -> {
            Label label = new Label(cook.getName());
            setLabelStyle(label, "#D7D7D7", 20, "#000000");
            label.setPrefSize(200, 50);
            label.setOnMouseClicked(e -> setSelected(label));
            lblCook.put(label, cook);
            cooksLabel.put(label, false);
            cookNameTag.getChildren().add(label);
        });
    }

    /**
     * Imposta un turno come selezionato andando ad evidenziare con un colore la GridPane.
     * Il turno selezionato viene ricavato dalla mappa shiftsTags avendo come chiave la corrispettiva GridPane.
     *
     * @param gridPane chiave della mappa per ottenere il turno associatole.
     */
    private void setSelectedShift(GridPane gridPane) {
        for (Map.Entry<GridPane, Boolean> entry : shiftsTags.entrySet()) {
            if (entry.getKey().equals(gridPane)) {
                entry.getKey().setStyle("-fx-background-color:" + randomColor() + ";-fx-background-radius: 5;");
                entry.getKey().getChildren().forEach(node -> node.setStyle("-fx-text-fill: #FFFFFF;-fx-font-family: 'Trebuchet MS';"));
                shiftsTags.put(gridPane, true);
            } else {
                entry.getKey().setStyle("-fx-background-color: #d7d7d7;-fx-background-radius: 5;");
                entry.getKey().getChildren().forEach(node -> node.setStyle("-fx-font-family: 'Trebuchet MS';"));
                shiftsTags.put(gridPane, false);
            }
        }
        selectedShift = gridShift.get(gridPane);
    }

    /**
     * Imposta un cuoco come selezionato andando ad evidenziare con un colore la Label.
     * Il cuoco selezionato viene ricavato dalla mappa cooksLabel avendo come chiave la corrispettiva Label.
     *
     * @param label chiave della mappa per ottenere il cuoco associato.
     */
    private void setSelected(Label label) {
        for (Map.Entry<Label, Boolean> entry : cooksLabel.entrySet()) {
            if (entry.getKey().equals(label)) {
                setLabelStyle(entry.getKey(), randomColor(), 20, "#FFFFFF");
                cooksLabel.put(label, true);
            } else {
                setLabelStyle(entry.getKey(), "#D7D7D7", 20, "#000000");
                cooksLabel.put(label, false);
            }
        }
        selectedCook = lblCook.get(label);
    }

    /**
     * Genera un colore casuale in forma esadecimale andandolo a selezionare dalla lista di colori
     * dello {@link ShiftChooserController}.
     *
     * @return una Stringa rappresentante un colore espresso in esadecimale.
     */
    private String randomColor() {
        return ShiftChooserController.getInstance().getColor();
    }

    /**
     * Assegna un cuoco ad un turno.
     * Se il cuoco scelto o il turno scelto sono nulli verra' mostrata una SnackBar tramite lo {@link SnakbarController}
     * con il corrispettivo errore.
     * Se cuoco e turno sono selezionati verra' richiesto al {@link TaskMenuController} di provvedere all'assegnamento.
     * L'operazione può generare tre eccezioni diverse in caso in cui il cuoco non sia nel turno scelto, nel caso in cui
     * il cuoco sia sovraccarico o nel caso in cui il tempo di attivita' del turno supera il tempo di attivita' della
     * ricetta.
     * Se tutte le operazioni vanno a buon fine si tornera' alla vista del menu'.
     */
    public void assignCookRecipe() {
        if (selectedCook != null && selectedShift != null) {
            try {
                ctrl.assign(currentRecipe, selectedCook, selectedShift);
                goBack();
            } catch (UseCaseLogicException ex) {
                SnakbarController.getInstance().showSnakBar(ex.getMessage(), assignViewMainPane);
            }
        } else {
            SnakbarController.getInstance().showSnakBar("Assicurati di aver selezionato \nun cuoco e un turno", assignViewMainPane);
        }
    }
}
