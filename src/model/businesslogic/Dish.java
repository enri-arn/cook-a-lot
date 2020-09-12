package model.businesslogic;

import java.sql.Time;

/**
 * @author Arnaudo Enrico, Bonino Samuele, Burdisso Enrico
 */
public class Dish extends Recipe {

    /**
     * Costruttore.
     *
     * @param name nome del piatto.
     * @param activityTime tempo di attivita' della ricetta.
     */
    public Dish(String name, Time activityTime) {
        super(name, activityTime);
    }


    /**
     * Costruttore.
     *
     * @param name nome del piatto.
     * @param id identificatore del piatto.
     * @param activityTime tempo di attivita' della ricetta.
     */
    public Dish(String name, String id, Time activityTime) {
        super(name, id, activityTime);
    }

    /**
     * Verifica che l'oggetto sia istanza di piatto.
     *
     * @return valore booleano <b>true</b>.
     */
    public boolean isDish() {
        return true;
    }
}
