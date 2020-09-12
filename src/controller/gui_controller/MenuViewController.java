package controller.gui_controller;

import controller.bl_controller.TaskMenuController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import model.businesslogic.*;
import model.utils.EventReceiver;
import model.utils.EventSource;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import static model.businesslogic.Shift.*;

/**
 * @author Arnaudo Enrico, Bonino Samuele, Burdisso Enrico.
 */
public class MenuViewController implements Initializable {

    public class TaskMenuReceiver implements EventReceiver {

        @Override
        public void update(EventSource source, Object arg) {
            if (source instanceof Shift) {
                HashMap<Event, Recipe> op;
                op = (HashMap<Event, Recipe>) arg;

                Event ev = op.keySet().iterator().next();
                Recipe argRecipe = op.get(ev);
                switch (ev) {
                    case DELETEASSIGNEMENT:
                        updateRemoveAssigment(argRecipe);
                        break;
                    case READYRECIPE:
                        updateReadyRecipe(argRecipe);
                        break;
                    case NOTREADYRECIPE:
                        updateNotReadyRecipe(argRecipe);
                        break;
                }
            }
        }
    }

    /**
     * Istanza statica del controller e puntatore al controller della business logic.
     */
    private static MenuViewController instance;
    private TaskMenuController ctrl;
    private GridPane gridPane;

    /**
     * Componenti dell'interfaccia grafica.
     */
    @FXML
    public AnchorPane menuMainPane;
    @FXML
    public Label selectedShiftLabel;
    @FXML
    public Label menuTitleLabel;
    @FXML
    public AnchorPane sectionMenuPane;
    @FXML
    public Button exitAssignementBtn;
    @FXML
    public Button endAssignementBtn;
    @FXML
    public ImageView goBackArrow;


    /**
     * Contatore in aiuto alla creazione della grafica e booleano per gestione salvataggio.
     */
    private final int[] j = {0};
    private static boolean isSave = false;

    /**
     * Tabella per mappare ad ogni elemento grafico la ricetta ad esso associata.
     */
    private HashMap<Button, Recipe> btnRecipes = new HashMap<>();
    private HashMap<Recipe, Button> recipeButton = new HashMap<>();
    private HashMap<Recipe, Label> lblRecipe = new HashMap<>();
    private HashMap<Recipe, Button> btnAssignCook = new HashMap<>();
    private HashMap<Button, Recipe> assignCookButton = new HashMap<>();
    private HashMap<Recipe, Label> lblAtCookInShift = new HashMap<>();
    private HashMap<Recipe, Item> itemRecipe = new HashMap<>();

    /**
     * Metodo derivato dal pattern Singleton per ottenere l'istanza dell'attuale classe.
     *
     * @return un istanza di {@link MenuViewController}.
     */
    public static MenuViewController getInstance() {
        if (instance == null) {
            instance = new MenuViewController();
        }
        return instance;
    }

    /**
     * Ritorna alla schermata del menu' rimuovendo dal pannello principale il pannello centrale sostituendolo
     * con la schermata "shifts_chooser_view.fxml".
     * Se il metodo e' stato chiamato cliccando sul bottone di salvataggio viene mostrata una Snackbar tramite
     * lo {@link SnakbarController} che mostra l'esito dell'operazione.
     * Viene poi aggiornato il valore booleano.
     */
    public void goBack() {
        Controller.getInstance().getMainPane().getChildren().remove(menuMainPane);
        try {
            Controller.getInstance().getMainPane().setCenter(FXMLLoader.load(getClass().getResource("../../view/gui2/shifts_chooser_view.fxml")));
            if (isSave) {
                SnakbarController.getInstance().showSnakBar("Assegnamenti salvati con successo", (Pane) Controller.getInstance().getMainPane().getCenter());
                isSave = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        TaskMenuReceiver receiver = new TaskMenuReceiver();
        ctrl.getCurrentShift().addReceiver(receiver);
    }

    /**
     * Imposta i vincoli di colonna del gridpane assegnandone le percentuali a ciascuna.
     * Vengono inoltre definiti i divari tra le varie righe e colonne della stessa gridpane.
     *
     * @param gridPane GridPane alla quale apportare modifiche.
     */
    private void setColumConstraint(GridPane gridPane) {
        gridPane.setHgap(35);
        gridPane.setVgap(15);
        gridPane.setMaxWidth(Double.MAX_VALUE);
        gridPane.setPrefWidth(930);
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(35);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(20);
        ColumnConstraints col3 = new ColumnConstraints();
        col3.setPercentWidth(20);
        ColumnConstraints col4 = new ColumnConstraints();
        col4.setPercentWidth(25);
        gridPane.getColumnConstraints().addAll(col1, col2, col3, col4);
    }

    /**
     * Assegna uno stile ad una Label impostandone ampiezza, altezza, padding, font-family, altezza del testo e
     * allineamento.
     *
     * @param label Label alla quale apportare modifiche.
     */
    private void setLabelStyleBig(Label label) {
        label.setPrefWidth(380);
        label.setPrefHeight(100);
        label.setPadding(new Insets(5, 5, 5, 5));
        label.setMaxWidth(Double.MAX_VALUE);
        label.setStyle("-fx-font-family: 'Trebuchet MS'");
        label.setFont(new Font(18));
        label.setAlignment(Pos.CENTER_LEFT);
    }

    /**
     * Assegna uno stile ad una Label impostandone ampiezza, altezza, padding, font-family, altezza del testo passategli
     * e allineamento.
     *
     * @param label      Label alla quale apportare modifiche.
     * @param prefWidth  ampiezza da assegnare.
     * @param prefHeight altezza da assegnare.
     * @param fontSize   altezza del testo da assegnare.
     */
    private void setLabelStyleSmall(Label label, int prefWidth, int prefHeight, int fontSize) {
        label.setPrefWidth(prefWidth);
        label.setPrefHeight(prefHeight);
        label.setPadding(new Insets(2, 0, 2, 10));
        label.setFont(new Font(fontSize));
        label.setStyle("-fx-font-family: 'Trebuchet MS';");
        label.setAlignment(Pos.CENTER_LEFT);
    }

    /**
     * Assegna uno stile ad un bottone impostandone colore di sfondo e ampiezza.
     *
     * @param button     Button al quale assegnare uno stile.
     * @param background colore di sfondo espresso in notazione esadecimale.
     * @param prefWidth  ampiezza da impostare.
     */
    private void setButtonStyle(Button button, String background, int prefWidth) {
        button.setStyle("-fx-background-color: " + background + ";" +
                "-fx-font-family: 'Trebuchet MS';" +
                "-fx-text-fill: #FFFFFF;" +
                "-fx-font-size: 18;");
        button.setPrefWidth(prefWidth);
        button.setPrefHeight(50);
        button.setMaxHeight(50);
    }

    /**
     * Aggiunge al gridpane della scena:
     * i bottoni per assegnare una ricetta ad un cuoco o per segnarla pronta nel caso
     * in cui lo stato di quest'ultima sia <b>NON_ASSEGNATA</b>;
     * il bottone per eliminare una ricetta come pronta nel caso in cui la ricetta sia nello stato <b>READY</b>;
     * una Label per mostrare l'assegamento ed il bottone per rimuoverlo nel caso in cui la ricetta sia nello
     * stato <b>ASSIGNED</b>.
     *
     * @param gridPane          Gridpane al quale aggiungere gli elementi.
     * @param recipe            ricetta da mappare agli element grafici.
     * @param recipeStatusLabel stato della ricetta selezionata.
     * @param i                 contatore per indicare la posizione nel gridpane dell'elemento.
     * @param item              voce del menu' selezionata.
     */
    private void addButtonToGridPane(GridPane gridPane, Recipe recipe, Label recipeStatusLabel, int i, Item item) {
        Button assignButton = new Button("ASSEGNA A CUOCO");
        btnAssignCook.put(recipe, assignButton);
        assignCookButton.put(assignButton, recipe);
        Label statusCookShift = new Label();
        lblAtCookInShift.put(recipe, statusCookShift);
        lblRecipe.put(recipe, recipeStatusLabel);
        if (checkStatus(recipe).equalsIgnoreCase("PRONTA")) {
            Button delReadyButton = new Button("ELIMINA PRONTA");
            setButtonStyle(delReadyButton, "#F44336", 170);
            btnRecipes.put(delReadyButton, recipe);
            recipeButton.put(recipe, delReadyButton);
            delReadyButton.setOnAction(e -> setReadyRecipe(delReadyButton, true));
            setButtonStyle(assignButton, "#FFC107", 200);
            assignButton.setDisable(true);
            gridPane.add(delReadyButton, 2, i);
            gridPane.add(assignButton, 3, i);
        } else if (checkStatus(recipe).equalsIgnoreCase("ASSEGNATA")) {
            Button setReadyButton = new Button("SEGNA PRONTA");
            setButtonStyle(setReadyButton, "#4CAF50", 170);
            setButtonStyle(assignButton, "#FFC107", 200);
            btnRecipes.put(setReadyButton, recipe);
            recipeButton.put(recipe, setReadyButton);
            setReadyButton.setOnAction(e -> setReadyRecipe(setReadyButton, false));
            setReadyButton.setDisable(true);
            setReadyButton.setVisible(false);
            statusCookShift.setText(addToStatus(recipe));
            setLabelStyleSmall(statusCookShift, 160, 100, 12);
            assignButton.setText("RIMUOVI");
            setButtonStyle(assignButton, "#F44336", 200);
            assignButton.setOnAction(e -> removeAssignement(recipe));
            gridPane.add(setReadyButton, 2, i);
            gridPane.add(statusCookShift, 2, i);
            gridPane.add(assignButton, 3, i);
        } else {
            Button setReadyButton = new Button("SEGNA PRONTA");
            setButtonStyle(setReadyButton, "#4CAF50", 170);
            setButtonStyle(assignButton, "#FFC107", 200);
            btnRecipes.put(setReadyButton, recipe);
            recipeButton.put(recipe, setReadyButton);
            setReadyButton.setOnAction(e -> setReadyRecipe(setReadyButton, false));
            assignButton.setDisable(false);
            assignButton.setOnAction(e -> showAssignView(assignButton, item));
            gridPane.add(setReadyButton, 2, i);
            gridPane.add(assignButton, 3, i);
        }
    }

    /**
     * Rimuove un assegnamento effettuato precedentemente usando il controller della business logic.
     * Elimina la visualizzazione dell'assegnamento, modifica l'handler del bottone per assegnare ricette e riattiva il
     * bottone per segnare come pronta la ricetta.
     * Modifica i colori dello stato della ricetta.
     *
     * @param recipe ricetta selezionata.
     */
    private void removeAssignement(Recipe recipe) {
        ctrl.removeAssignement(recipe, ctrl.getCurrentShift().getAssignements(recipe).getCook(), ctrl.getCurrentShift().getAssignements(recipe).getShift());
    }

    /**
     * Mostra a video le voci del menu'.
     *
     * @param items    lista di voci del menu'.
     * @param gridPane GridPane nel quale visualizzare le varie voci.
     */
    private void showItems(List<Item> items, GridPane gridPane) {
        items.forEach(item -> {
            List<Recipe> recipes = new ArrayList<>();
            recipes.add(item.getDish());
            recipes.forEach(recipe -> {
                Label recipeLabel = new Label(recipe.getName());
                setLabelStyleSmall(recipeLabel, 370, 60, 16);
                Label recipeStatusLabel = new Label(checkStatus(recipe));
                setLabelStyleSmall(recipeStatusLabel, 140, 60, 16);
                setColorStatus(recipeStatusLabel, checkStatus(recipe));
                addButtonToGridPane(gridPane, recipe, recipeStatusLabel, j[0], item);
                itemRecipe.put(recipe, item);
                gridPane.add(recipeLabel, 0, j[0]);
                gridPane.add(recipeStatusLabel, 1, j[0]);
                j[0]++;
            });
        });
    }

    /**
     * Assegna un colore alla Label dello stato della ricetta.
     *
     * @param recipeStatusLabel Label mostrante lo stato.
     * @param status            stato della ricetta.
     */
    private void setColorStatus(Label recipeStatusLabel, String status) {
        if (status.equalsIgnoreCase("PRONTA") || status.equalsIgnoreCase("ASSEGNATA")) {
            recipeStatusLabel.setStyle("-fx-font-family: 'Trebuchet MS';" +
                    "-fx-text-fill: #4CAF50;");
        } else {
            recipeStatusLabel.setStyle("-fx-font-family: 'Trebuchet MS';" +
                    "-fx-text-fill: #F44336;");
        }
    }

    /**
     * Mostra il menu' selezionato utilizzando i vari metodi ausiliari per mostrare le voci, assegna alla GridPane che
     * contiene e mostra il  menu' i vincoli necessari.
     * Ottiene il menu' da mostrare attraverso il puntatore al menu' corrente del controller della business logic.
     */
    void showExpandedMenu() {
        selectedShiftLabel.setText("Turno " + ctrl.getCurrentShift().getId().substring(12));
        if (gridPane == null) {
            gridPane = new GridPane();
            setColumConstraint(gridPane);
            Menu menu = ctrl.consultMenu(ctrl.getCurrentShift());
            menuTitleLabel.setText(ctrl.getCurrentMenu().getTitle());
            menu.getSections().forEach(section -> {
                Label label = new Label(section.getName());
                setLabelStyleBig(label);
                gridPane.add(label, 0, j[0]);
                j[0]++;
                showItems(section.getItems(), gridPane);
            });
            AnchorPane.setTopAnchor(gridPane, 0.0);
            AnchorPane.setBottomAnchor(gridPane, 0.0);
            AnchorPane.setRightAnchor(gridPane, 0.0);
            AnchorPane.setLeftAnchor(gridPane, 0.0);
            sectionMenuPane.getChildren().add(gridPane);
        }
    }

    /**
     * Ottiene informazioni su un assegnamento dal controller della business logic.
     *
     * @param recipe ricetta dalla quale ottenere informazioni.
     * @return una String contenente le informazioni sull'assegnamento.
     */
    private String addToStatus(Recipe recipe) {
        Assignement assignement = ctrl.getCurrentShift().getAssignements(recipe);
        return "a: " + assignement.getCook().getName() + "\nturno " + assignement.getShift().getId();
    }

    /**
     * Verifica lo stato della ricetta e lo ritorna.
     *
     * @param re ricetta dalla quale verificare lo stato.
     * @return una String contenente lo stato della ricetta.
     */
    private String checkStatus(Recipe re) {
        return ctrl.getCurrentShift().getRecipeStates().containsKey(re) ?
                ((ctrl.getCurrentShift().getRecipeStates().get(re).toString().compareTo("READY") == 0) ?
                        "PRONTA" : "ASSEGNATA") : "NON ASSEGNATA";
    }

    /**
     * Segna una ricetta come <b>READY</b> aggiornando lo stato della ricetta selezionata all'interno della mappa del
     * turno corrente nel caso in cui il parametro ready sia false, segna come <b>NOT_ASSIGNED</b> nel caso contrario.
     * Aggiorna la grafica nei due casi modificando l'handler del bottone per segnare pronta la ricetta.
     * Se la ricetta viene indicata come <b>READY</b> allora verra' disabilitato il bottone per assegnare una ricetta,
     * disabilitato in caso contrario.
     *
     * @param setReady Button per segnare o eliminare pronta una ricetta.
     * @param ready    booleano indicante il nuovo stato della ricetta.
     */
    private void setReadyRecipe(Button setReady, boolean ready) {
        if (!ready) {
            ctrl.setReadyRecipe(btnRecipes.get(setReady));
            setReady.setOnAction(e -> setReadyRecipe(setReady, true));
        } else {
            ctrl.removeReadyRecipe(btnRecipes.get(setReady));
            setReady.setOnAction(e -> setReadyRecipe(setReady, false));
        }
    }

    private void updateReadyRecipe(Recipe recipe) {
        Label status = lblRecipe.get(recipe);
        Button setReady = recipeButton.get(recipe);
        Button assign = btnAssignCook.get(recipe);
        status.setText("PRONTA");
        setLabelStyleSmall(status, 140, 60, 16);
        setColorStatus(status, "PRONTA");
        setReady.setText("ELIMINA PRONTA");
        setButtonStyle(setReady, "#F44336", 170);
        assign.setDisable(true);
    }

    private void updateNotReadyRecipe(Recipe recipe) {
        Label status = lblRecipe.get(recipe);
        Button setReady = recipeButton.get(recipe);
        Button assign = btnAssignCook.get(recipe);
        status.setText("NON ASSEGNATA");
        setLabelStyleSmall(status, 140, 60, 16);
        setColorStatus(status, "NON ASSEGNATA");
        setReady.setText("SEGNA PRONTA");
        setButtonStyle(setReady, "#4CAF50", 170);
        assign.setDisable(false);
    }

    private void updateRemoveAssigment(Recipe recipe) {
        Label statusCookShift = lblAtCookInShift.get(recipe);
        Button assignButton = btnAssignCook.get(recipe);
        Button setReadyButton = recipeButton.get(recipe);
        Label recipeStatusLabel = lblRecipe.get(recipe);
        Item item = itemRecipe.get(recipe);
        statusCookShift.setVisible(false);
        statusCookShift.setDisable(true);
        assignButton.setOnAction(e -> showAssignView(assignButton, item));
        assignButton.setText("ASSEGNA A CUOCO");
        setButtonStyle(assignButton, "#FFC107", 200);
        setReadyButton.setVisible(true);
        setReadyButton.setDisable(false);
        recipeStatusLabel.setText(checkStatus(recipe));
        setLabelStyleSmall(recipeStatusLabel, 140, 60, 16);
        setColorStatus(recipeStatusLabel, checkStatus(recipe));
    }

    /**
     * Mostra la schermata di assegnazione ricetta.
     * Chiama il controller della schermata di assegnazione {@link AssignRecipeController} e richiede l'impostazione
     * dei vari componenti.
     *
     * @param assign Button chiave per ottenere la ricetta.
     * @param item   voce del menu' selezionata.
     */
    private void showAssignView(Button assign, Item item) {
        Controller.getInstance().getMainPane().getChildren().remove(2);
        try {
            Controller.getInstance().getMainPane().setCenter(FXMLLoader.load(getClass().getResource("../../view/gui2/assign_recipe_view.fxml")));
            AssignRecipeController.getInstance().setComponent(assignCookButton.get(assign), item);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Chiama il controller della business logic e ne indica la conclusione degli assegnamenti per il turno corrente.
     * Imposta a true il valore booleano per il salvataggio permettendo cosi' la visualizzazione della notifica.
     * Chiama il metodo goBack per ritornare alla schermata precedente.
     */
    public void endAssignement() {
        ctrl.endAssignement();
        isSave = true;
        goBack();
    }

    /**
     * Chiama il metodo goBack per ritornare alla schermata precedente.
     */
    public void exitAssignement() {
        goBack();
    }

}
