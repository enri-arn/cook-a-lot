package controller.gui_controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Arnaudo Enrico, Bonino Samuele, Burdisso Enrico.
 */
public class SnakbarController implements Initializable {

    /**
     * Istanza statica del controller.
     */
    private static SnakbarController instance;

    /**
     * Componenti dell'interfaccia grafica.
     */
    @FXML
    public Label defaultLabelText;
    @FXML
    public AnchorPane snakbarPane;
    private Node snakbar = null;

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
    }

    /**
     * Metodo derivato dal pattern Singleton per ottenere l'istanza dell'attuale classe.
     *
     * @return un istanza di {@link SnakbarController}.
     */
    public static SnakbarController getInstance() {
        if (instance == null) {
            instance = new SnakbarController();
        }
        return instance;
    }

    /**
     * Imposta la componente della snackbar.
     *
     * @param text testo da inserire nella snackbar.
     */
    private void setComponent(String text) {
        defaultLabelText.setText(text);
    }

    /**
     * Aggiunge la snakbar al pannello passategli come parametro.
     * Un Thread si occupera' di rimuovera dopo 3 secondi.
     *
     * @param text testo da inserire nella snackbar.
     * @param pane pannello nel quale mostrare la snackbar.
     */
    void showSnakBar(String text, Pane pane) {
        Thread thread = new Thread(() -> {
            try {
                Platform.runLater(() -> show(text, pane));
                Thread.sleep(3000);
                Platform.runLater(() -> delete(pane));
            } catch (InterruptedException exc) {
                throw new Error("Unexpected interruption");
            }
        });
        thread.start();
    }

    /**
     * Mostra a video la snakbar.
     *
     * @param text testo della notifica.
     * @param pane pannello dove attaccare la notifica.
     */
    private void show(String text, Pane pane) {
        try {
            snakbar = FXMLLoader.load(getClass().getResource("../../view/gui2/snackbar.fxml"));
            AnchorPane.setBottomAnchor(snakbar, 15.0);
            AnchorPane.setRightAnchor(snakbar, 15.0);
            pane.getChildren().add(snakbar);
            SnakbarController.getInstance().setComponent(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Elimina la snackbar.
     *
     * @param pane pannello dov'e' contenuta.
     */
    private void delete(Pane pane) {
        Platform.runLater(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            pane.getChildren().remove(snakbar);
        });
    }
}
