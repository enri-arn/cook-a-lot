package controller.gui_controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Arnaudo Enrico, Bonino Samuele, Burdisso Enrico.
 */
public class ShiftChooserEmptyController implements Initializable {

    /**
     * Componenti dell'interfaccia grafica.
     */
    @FXML
    public ImageView goBackArrow;

    /**
     * Istanza statica del controller.
     */
    private static ShiftChooserEmptyController instance;

    /**
     * Metodo derivato dal pattern Singleton per ottenere l'istanza dell'attuale classe.
     *
     * @return un istanza di {@link ShiftChooserController}.
     */
    public static ShiftChooserEmptyController getInstance() {
        if (instance == null){
            instance = new ShiftChooserEmptyController();
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
}
