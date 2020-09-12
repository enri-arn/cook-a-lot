package model.businesslogic;

import model.services.CookALotServiceProvider;

/**
 * @author Arnaudo Enrico, Bonino Samuele, Burdisso Enrico
 */
public class Cook {

    /**
     * Nome del cuoco.
     */
    private String name;

    /**
     * Identificatore del cuoco.
     */
    private String id;

    /**
     * Costruttore.
     *
     * @param name nome del cuoco.
     */
    public Cook(String name) {
        this.name = name;
        this.id = CookALotServiceProvider.getInstance().getDataManager().generateCookId();
    }

    /**
     * Costruttore per i test.
     *
     * @param name nome del cuoco.
     * @param id identificatore del cuoco.
     */
    public Cook(String name, String id) {
        this.name = name;
        this.id = id;
    }

    /**
     * Ottiene il nome del cuoco.
     *
     * @return il nome del cuoco.
     */
    public String getName() {
        return name;
    }

    /**
     * Imposta il nome del cuoco.
     *
     * @param name nome del cuoco da impostare.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Ottiene l'identificatore del cuoco
     *
     * @return l'identificatore del cuoco.
     */
    public String getId() {
        return id;
    }

    /**
     * Imposta l'identificatore del cuoco.
     *
     * @param id identificatore da impostare.
     */
    public void setId(String id) {
        this.id = id;
    }
}
