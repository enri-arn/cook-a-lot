package model.businesslogic;

import model.services.CookALotServiceProvider;
import model.utils.EventSource;

import java.util.*;

/**
 * @author Arnaudo Enrico, Bonino Samuele, Burdisso Enrico
 */
public class Shift extends EventSource {

    /**
     * booleano per indicare se un turno e' completo o meno, booleano per indicare se un turno e' di lineo o di servizio,
     * data di inizio turno e di fine.
     */
    private boolean complete;
    private Boolean line;
    private Date startTime;
    private Date endTime;

    /**
     * Identificatore del turno.
     */
    private String id;

    /**
     * Lista di assegnamenti del turno.
     */
    private List<Assignement> assignements;
    private List<Cook> cooks;

    /**
     * Stato delle ricette in un turno.
     */
    public enum enumState {
        ASSIGNED,
        READY
    }

    /**
     * Eventi generati dall'utente.
     */
    public enum Event {
        COOKASSIGNED,
        DELETEASSIGNEMENT,
        READYRECIPE,
        NOTREADYRECIPE
    }

    /**
     * Mappa di ricette e loro stato.
     * Se non presenti sono non assegnate.
     */
    private HashMap<Recipe, enumState> recipeStates;

    /**
     * Puntatore al menu' corrente.
     */
    private Menu inUseMenu;

    /**
     * Costruttore.
     *
     * @param startTime data di inizio turno.
     * @param endTime   data di fine turno.
     * @param complete  booleano che indica la completezza del turno.
     * @param inUseMenu puntatore al menu' del turno.
     */
    public Shift(Date startTime, Date endTime, boolean complete, Menu inUseMenu) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.complete = complete;
        this.line = false;
        this.inUseMenu = inUseMenu;
        this.assignements = new ArrayList<>();
        this.cooks = new ArrayList<>();
        this.recipeStates = new HashMap<>();
        this.id = CookALotServiceProvider.getInstance().getDataManager().generateShiftId();
    }

    /**
     * Costruttore per i test.
     *
     * @param id        identificatore del turno.
     * @param startTime data di inizio turno.
     * @param endTime   data di fine turno.
     * @param complete  booleano che indica la completezza del turno.
     * @param inUseMenu puntatore al menu' del turno.
     */
    public Shift(String id, Date startTime, Date endTime, boolean complete, Menu inUseMenu) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.complete = complete;
        this.line = false;
        this.inUseMenu = inUseMenu;
        this.assignements = new ArrayList<>();
        this.cooks = new ArrayList<>();
        this.recipeStates = new HashMap<>();
        this.id = id;
    }

    /**
     * Imposta il turno come di linea o di servizio.
     *
     * @param line booleano per indicare il tipo di turno.
     */
    public void setLine(Boolean line) {
        this.line = line;
    }

    /**
     * Ottiene informazioni sul tipo di turno.
     *
     * @return un valore booleano per indicare il tipo di un turno.
     */
    public Boolean getLine() {
        return this.line;
    }

    /**
     * Ottiene l'identificatore del turno.
     *
     * @return l'identificatore del turno.
     */
    public String getId() {
        return this.id;
    }

    /**
     * Ottiene la data d'inizio del turno.
     *
     * @return la data d'inizio del turno.
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * Imposta la data d'inizio del turno.
     *
     * @param startTime la data d'inizio del turno.
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * Ottiene la data di fine del turno.
     *
     * @return la data di fine turno.
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * Imposta la data di fine turno.
     *
     * @param endTime data da impostare.
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /**
     * Verifica la completezza del turno.
     *
     * @return un valore booleano che ne indica la completezza.
     */
    public boolean isComplete() {
        return complete;
    }

    /**
     * Imposta la completezza del turno.
     *
     * @param complete valore booleano da impostare.
     */
    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    /**
     * Ottiene il puntatore al menu' in uso.
     *
     * @return il menu' in uso.
     */
    public Menu getInUseMenu() {
        return inUseMenu;
    }

    /**
     * Imposta la lista di assegnamenti.
     *
     * @param assignements lista da impostare.
     */
    public void setAssignements(List<Assignement> assignements) {
        this.assignements = assignements;
    }

    /**
     * Imposta la mappa di ricette e stato ricette.
     *
     * @param recipeStates mappa da impostare.
     */
    public void setRecipeStates(HashMap<Recipe, enumState> recipeStates) {
        this.recipeStates = recipeStates;
    }

    /**
     * Aggiunge un assegnamento al turno.
     *
     * @param cook   cuoco dell'assegnamento.
     * @param recipe ricetta assegnata.
     * @param shift  turno dell'assegnamento.
     */
    public void addAssignement(Cook cook, Recipe recipe, Shift shift) {
        Assignement assignement = new Assignement(recipe, cook, shift);
        assignements.add(assignement);

    }

    /**
     * Rimuove un assegnamento.
     *
     * @param cook   cuoco dell'assegnamento.
     * @param recipe ricetta assegnata.
     * @param shift  turno dell'assegnamento.
     */
    public void removeAssignement(Cook cook, Recipe recipe, Shift shift) {
        for (Assignement assignement : assignements) {
            if (assignement.getCook().getId().equals(cook.getId()) && assignement.getRecipe().getId().equals(recipe.getId()) && assignement.getShift().getId().equals(shift.getId())) {
                assignements.remove(assignement);
                return;
            }
        }
    }

    /**
     * Cambia lo stato di una ricetta.
     *
     * @param recipe ricetta per cui e' modificato lo stato.
     * @param state  nuovo stato della ricetta.
     */
    public void changeStateRecipe(Recipe recipe, enumState state) {
        recipeStates.put(recipe, state);
    }

    /**
     * Rimuove lo stato di una ricetta.
     *
     * @param recipe ricetta il cui  stato e' modificato.
     */
    public void removeStateRecipe(Recipe recipe) {
        recipeStates.remove(recipe);
    }

    /**
     * Imposta una ricetta come pronta.
     *
     * @param recipe ricetta da impostare.
     */
    public void setReadyRecipe(Recipe recipe) {
        recipeStates.put(recipe, enumState.READY);
    }

    /**
     * Verifica la presenza di un assegnamento.
     *
     * @param cook   cuoco dell'assegnamento.
     * @param recipe ricetta assegnata.
     * @param shift  turno dell'assegnamento.
     * @return un valore booleano che ne indica la presenza o meno.
     */
    public boolean checkAssignements(Cook cook, Recipe recipe, Shift shift) {
        for (Assignement assignement : assignements) {
            if (assignement.getCook().getId().equals(cook.getId()) && assignement.getRecipe().getId().equals(recipe.getId()) && assignement.getShift().getId().equals(shift.getId())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Ottiene l'assegnamento di una ricetta.
     *
     * @param recipe ricetta dell'assegnamento.
     * @return l'assegnamento della ricetta.
     */
    public Assignement getAssignements(Recipe recipe) {
        for (Assignement assignement : assignements) {
            if (assignement.getRecipe().getId().equals(recipe.getId())) {
                return assignement;
            }
        }
        return null;
    }

    /**
     * Ottiene il numero di assegnamenti in un turno.
     *
     * @return un interno indicante il numero di assegnamenti.
     */
    public int getAssignementsCount() {
        return assignements.size();
    }

    /**
     * Ottiene la lista di assegnamenti.
     *
     * @return la lista di assegnamenti.
     */
    public List<Assignement> getAssignements() {
        return assignements;
    }

    /**
     * Ottiene la lista di cuochi del turno.
     *
     * @return la lista di cuochi.
     */
    public List<Cook> getCooks() {
        return cooks;
    }

    /**
     * Imposta la lista di cuochi del turno.
     *
     * @param cooks lista da impostare.
     */
    public void setCooks(List<Cook> cooks) {
        this.cooks = cooks;
    }

    /**
     * Aggiunge un cuoco alla lista di cuochi del turno.
     *
     * @param cook cuoco da aggiungere.
     */
    public void addCook(Cook cook) {
        this.cooks.add(cook);
    }

    /**
     * Ottiene la mappa indicante per ogni ricetta il suo stato.
     *
     * @return la mappa indicante per ogni ricetta il suo stato.
     */
    public HashMap<Recipe, enumState> getRecipeStates() {
        return recipeStates;
    }

    /**
     * Verifica la presenza di un cuoco in un turno.
     *
     * @param cook cuoco selezionato.
     * @return un valore booleano che ne indichera' la presenza o meno.
     */
    public boolean isInShift(Cook cook) {
        for (Cook c : cooks) {
            if (c.getId().equals(cook.getId())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Ricava la differenza tra due date
     *
     * @param date1 la data più vecchia
     * @param date2 la data più nuova
     * @return la differenza tra le due date.
     */
    private static long getDateDiff(Date date1, Date date2) {
        return date2.getTime() - date1.getTime();
    }

    /**
     * Ricava la durata in minuti del turno corrente.
     *
     * @return un long rappresentante la durata del turno.
     */
    public long getShiftTime() {
        return getDateDiff(startTime, endTime);
    }

    /**
     * Verifica se in cuoco e' sovraccarico andando a sommare tutti gli assegnamenti ove presente il cuoco.
     *
     * @param shift        turno selezionato.
     * @param cook         cuoco selezionato.
     * @param activityTime tempo di attivita' della ricetta.
     * @return un valore booleano che indichera' se il cuoco selezionato e' sovraccarico.
     */
    public Boolean overTime(Shift shift, Cook cook, Long activityTime) {
        Long shiftTime = shift.getShiftTime();
        final Long[] cookTime = {activityTime};
        this.assignements.forEach(a -> {
            if (a.getCook().getId().equals(cook.getId()) && a.getShift().getId().equals(shift.getId())) {
                cookTime[0] += a.getRecipe().getActivityTimeLong();
            }
        });
        return cookTime[0] > shiftTime;
    }

}
