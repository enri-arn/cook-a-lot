package controller.gui_controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Arnaudo Enrico, Bonino Samuele, Burdisso Enrico.
 */
public class Controller implements Initializable {

    /**
     * Componenti dell'interfaccia grafica.
     */
    @FXML
    public BorderPane mainPane;
    @FXML
    public Circle profileImage;
    @FXML
    public Label exitLabel;
    @FXML
    public ImageView exitImage;

    /**
     * Istanza statica del controller.
     */
    private static Controller instance;

    /**
     * Metodo derivato dal pattern Singleton per ottenere l'istanza dell'attuale classe.
     *
     * @return un istanza di {@link Controller}.
     */
    public static Controller getInstance() {
        if (instance == null) {
            instance = new Controller();
        }
        return instance;
    }

    /**
     * Ottiene il pannello principale dell'applicazione.
     *
     * @return il pannello principale dell'applicazione.
     */
    BorderPane getMainPane() {
        return mainPane;
    }

    /**
     * Metodo derivato dall'interfaccia {@link Initializable} la quale garantisce l'assegnazione dell'istanza statica del
     * controller all'attuale classe.
     * Carica nel pannello principale la scena iniziale "center_main_pane.fxml".
     * Utilizza un Thread per andare ad inserire all'interno del cerchio del pannello principale l'immagine dell'utente
     * autenticato.
     *
     * @param location  Il percorso utilizzato per risolvere i percorsi relativi per l'oggetto radice o null se la posizione
     *                  non e' nota.
     * @param resources Le risorse utilizzate per localizzare l'oggetto radice o null se l'oggetto radice non e'
     *                  stato localizzato.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instance = this;
        try {
            mainPane.setCenter(FXMLLoader.load(getClass().getResource("../../view/gui2/center_main_pane.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Platform.runLater(() -> profileImage.setFill(new ImagePattern(new Image("view/image/antonino-cannavacciuolo-256x256.png"))));
    }

    /**
     * Esce dall'applicazione andando a richiamare il metodo base System.exit con stato "0".
     */
    public void exitApplication() {
        System.exit(0);
    }
}
