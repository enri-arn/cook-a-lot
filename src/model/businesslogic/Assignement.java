package model.businesslogic;

import model.services.CookALotServiceProvider;

/**
 * @author Arnaudo Enrico, Bonino Samuele, Burdisso Enrico
 */
public class Assignement {

    /**
     * Ricetta <b>recipe</b> assegnata a cuoco <b>cook</b> in turno <b>shift</b>.
     */
    private Recipe recipe;
    private Cook cook;
    private Shift shift;

    /**
     * Identificatore dell'assegnamento.
     */
    private String id;

    /**
     * Costruttore.
     *
     * @param recipe ricetta da assegnare.
     * @param cook cuoco a cui assegnare una ricetta.
     * @param shift turno dell'assegnamento.
     */
    public Assignement(Recipe recipe, Cook cook, Shift shift) {
        this.recipe = recipe;
        this.cook = cook;
        this.shift = shift;
        this.id = CookALotServiceProvider.getInstance().getDataManager().generateAssignementId();
    }

    /**
     * Costruttore per i test.
     *
     * @param id identificatore dell'assegnamento.
     * @param recipe ricetta da assegnare.
     * @param cook cuoco a cui  assegnare una ricetta.
     * @param shift turno dell'assegnamento.
     */
    public Assignement(String id, Recipe recipe, Cook cook, Shift shift) {
        this.recipe = recipe;
        this.cook = cook;
        this.shift = shift;
        this.id = id;
    }

    /**
     * Ottiene la ricetta dell'assegnamento.
     *
     * @return la ricetta dell'assegnamento.
     */
    public Recipe getRecipe() {
        return recipe;
    }

    /**
     * Imposta la ricetta dell'assegnamento.
     *
     * @param recipe ricetta da impostare.
     */
    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    /**
     * Ottiene il cuoco dell'assegnamento.
     *
     * @return il cuoco dell'assegnamento.
     */
    public Cook getCook() {
        return cook;
    }

    /**
     * Imposta il cuoco dell'assegnamento.
     *
     * @param cook cuoco da impostare
     */
    public void setCook(Cook cook) {
        this.cook = cook;
    }

    /**
     * Ottiene l'id dell'assegnamento.
     *
     * @return l'id dell'assegnamento.
     */
    public String getId() {
        return id;
    }

    /**
     * Ottiene il turno dell'assegnamento.
     *
     * @return il turno dell'assegnamento.
     */
    public Shift getShift() {
        return shift;
    }
}
