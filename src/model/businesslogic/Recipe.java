package model.businesslogic;

import model.services.CookALotServiceProvider;

import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Arnaudo Enrico, Bonino Samuele, Burdisso Enrico.
 */
public class Recipe {

    /**
     * Nome, tempo di attivita' e ricette ad essa associate.
     */
    private String name;
    private Time activityTime;
    private List<Recipe> recipes = new ArrayList<>();

    /**
     * Identificatore di ricetta.
     */
    private String id;

    /**
     * Costruttore.
     *
     * @param name nome della ricetta.
     * @param activityTime tempo di attivita' della ricetta.
     */
    public Recipe(String name, Time activityTime) {
        this.name = name;
        this.id = CookALotServiceProvider.getInstance().getDataManager().generateRecipeId();
        this.activityTime = activityTime;
    }

    /**
     * Costruttore per i test.
     *
     * @param name nome della ricetta.
     * @param id identificatore della ricetta.
     * @param activityTime tempo di attivita' della ricetta.
     */
    public Recipe(String name, String id, Time activityTime) {
        this.name = name;
        this.id = id;
        this.activityTime = activityTime;
    }

    /**
     * Ottiene la lista di ricette.
     *
     * @return la lista di ricette.
     */
    public List<Recipe> getAllRecipes() {
        return recipes;
    }

    /**
     * Ottiene il tempo di attivita' della ricetta.
     *
     * @return il tempo di attivita' della ricetta.
     */
    public Time getActivityTime() {
        return activityTime;
    }

    /**
     * Ottiene il tempo di attivita' della ricetta in un long.
     *
     * @return il tempo di attivita' della ricetta in un long.
     */
    public long getActivityTimeLong() {
        Time timeZero = Time.valueOf( LocalTime.of(0,0,0));
        return activityTime.getTime() - timeZero.getTime();
    }

    /**
     * Imposta il tempo di attivita' della ricetta.
     *
     * @param activityTime tempo di attivita' della ricetta.
     */
    public void setActivityTime(Time activityTime) {
        this.activityTime = activityTime;
    }

    /**
     * Ottiene il nome della ricetta.
     *
     * @return il  nome della ricetta.
     */
    public String getName() {
        return name;
    }

    /**
     * Imposta il nome della ricetta.
     *
     * @param name nome da impostare.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Indica che una ricetta non e' istanza di piatto.
     *
     * @return un valore booleano <b>false</b>.
     */
    public boolean isDish() {
        return false;
    }

    /**
     * Ottiene l'identificatore della ricetta.
     *
     * @return l'identificatore della ricetta.
     */
    public String getId() {
        return id;
    }

    /**
     * Ottiene una visualizzazione su stringa della ricetta mostrandone il nome.
     *
     * @return una stringa contenente il nome della ricetta.
     */
    public String toString() {
        return getName();
    }
}
