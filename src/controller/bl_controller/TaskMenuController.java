package controller.bl_controller;

import model.businesslogic.*;
import model.businesslogic.exception.*;
import model.services.CookALotServiceProvider;

import java.util.HashMap;

/**
 * @author Arnaudo Enrico, Bonino Samuele, Burdisso Enrico
 */
public class TaskMenuController {

    /**
     * Istanze statiche del suddetto controller e del ServiceProvider.
     */
    private static TaskMenuController instance;
    private static CookALotServiceProvider cookDM;

    /**
     * Puntatori al menu' e turno corrente.
     */
    private Menu currentMenu;
    private Shift currentShift;

    /**
     * Metodo derivato dal pattern Singleton per ottenere l'istanza dell'attuale classe.
     *
     * @return un istanza di {@link TaskMenuController}.
     */
    public static TaskMenuController getInstance() {
        if (instance == null) {
            instance = new TaskMenuController();
        }
        return instance;
    }

    /**
     * Metodo derivato dal pattern Singleton per ottenere l'istanza del ServiceProvider.
     *
     * @return un istanza di CookALotServiceProvider.
     */
    private static CookALotServiceProvider getCookDM() {
        if (cookDM == null) {
            cookDM = CookALotServiceProvider.getInstance();
        }
        return cookDM;
    }

    /**
     * Costruttore del controller.
     */
    private TaskMenuController() {
    }

    /**
     * Ritorna il menu' corrente.
     *
     * @return il menu' corrente.
     */
    public Menu getCurrentMenu() {
        return currentMenu;
    }

    /**
     * Imposta il menù corrente.
     *
     * @param currentMenu menu' da impostare come corrente.
     */
    public void setCurrentMenu(Menu currentMenu) {
        this.currentMenu = currentMenu;
    }

    /**
     * Ritorna il turno corrente.
     *
     * @return il turno corrente.
     */
    public Shift getCurrentShift() {
        return currentShift;
    }

    /**
     * Imposta il turno corrente.
     *
     * @param currentShift turno da impostare corrente.
     */
    public void setCurrentShift(Shift currentShift) {
        this.currentShift = currentShift;
    }

    /**
     * Assegna una ricetta ad un cuoco in un turno.
     * Se il turno e' nullo allora lancera' una Eccezione del tipo {@link NullPointerCurrentShiftException}; se il cuoco
     * non e' nel turno allora lancera' un'eccezione del tipo {@link CookNotInShiftException}; se invece il tempo di
     * attivita' del cuoco superera' quello del turno verra' lanciata una eccezione del tipo
     * {@link ShiftOverTimeException}.
     * All'attuale turno verrà aggiunto un assegnamento all'interno della sua lista di assegnamenti e lo stato della
     * ricetta verra' modificato da <b>NOT_ASSIGNED</b> ad <b>ASSIGNED</b>.
     *
     * @param recipe ricetta da assegnare.
     * @param cook cuoco a cui assegnare ricetta.
     * @param shift turno in cui assegnare ricetta e cuoco.
     */
    public void assign(Recipe recipe, Cook cook, Shift shift) {
        if (currentShift == null) {
            throw new NullPointerCurrentShiftException();
        }
        if (!shift.isInShift(cook)) {
            throw new CookNotInShiftException();
        }
        if (currentShift.overTime(shift, cook, recipe.getActivityTimeLong())) {
            throw new ShiftOverTimeException();
        }

        currentShift.addAssignement(cook, recipe, shift);
        currentShift.changeStateRecipe(recipe, Shift.enumState.ASSIGNED);
        HashMap<Shift.Event, Recipe> arg = new HashMap<>();
        arg.put(Shift.Event.COOKASSIGNED, recipe);
        currentShift.notifyAllReceivers(arg);
    }

    /**
     * Ottiene il menu' del turno corrente e lo imposta come menu' corrente.
     * Il menu' corrente viene impostato come non completo.
     *
     * @param shift turno da cui ottenere il menu'.
     * @return il menu' corrente.
     */
    public Menu consultMenu(Shift shift) {
        setCurrentShift(shift);
        Menu menu = this.currentShift.getInUseMenu();
        menu.setComplete(false);
        setCurrentMenu(menu);
        return this.currentMenu;
    }

    /**
     * Rimuove un assegnamento in un turno.
     * Se il turno selezionato e' nullo allora lancera' un eccezione del tipo {@link NullPointerCurrentShiftException};
     * se il turno corrente non contiene l'assegnamento lancera' un eccezione del tipo
     * {@link model.businesslogic.exception.AssignementNotInShiftException}.
     * Se nel turno e' presente l'assegnamento, questo verra' rimosso dalla lista di assegnamenti del turno e lo stato
     * della ricetta passera' da <b>ASSIGNED</b> a <b>NOT_ASSIGNED</b>.
     *
     * @param recipe ricetta dell'assegnamento.
     * @param cook cuoco dell'assegnamento.
     * @param shift turno in cui e' presente l'assegnamento.
     */
    public void removeAssignement(Recipe recipe, Cook cook, Shift shift) {
        if (currentShift == null) {
            throw new NullPointerCurrentShiftException();
        }
        if (!this.currentShift.checkAssignements(cook, recipe, shift)) {
            throw new AssignementNotInShiftException();
        }
        currentShift.removeAssignement(cook, recipe, shift);
        currentShift.removeStateRecipe(recipe);
        HashMap<Shift.Event, Recipe> arg = new HashMap<>();
        arg.put(Shift.Event.DELETEASSIGNEMENT, recipe);
        currentShift.notifyAllReceivers(arg);
    }

    /**
     * Conclude l'assegnamento dei compiti.
     * Se il turno corrente e' nullo allora verra' lanciata una eccezione del tipo {@link NullPointerCurrentShiftException}.
     * Il turno corrente verra' impostato come completo e verra' consegnato al DataManager
     * {@link model.services.CookALotDataManagerStub} che provvedera' al mantenimento della persistenza.
     */
    public void endAssignement() {
        if (currentShift == null) {
            throw new NullPointerCurrentShiftException();
        }
        currentShift.setComplete(true);
        getCookDM().getDataManager().updateAssignements(currentShift);
    }

    /**
     * Imposta come pronta una ricetta.
     * Se il turno corrente e' nullo allora verra' lanciata una eccezione del tipo {@link NullPointerCurrentShiftException}.
     * Viene aggiornata la mappa del turno assegnando alla ricetta selezionata lo stato <b>READY</b>.
     *
     * @param recipe ricetta segnata come pronta.
     */
    public void setReadyRecipe(Recipe recipe) {
        if (currentShift == null) {
            throw new UseCaseLogicException("Il currentShift e' null.");
        }
        currentShift.changeStateRecipe(recipe, Shift.enumState.READY);
        HashMap<Shift.Event, Recipe> arg = new HashMap<>();
        arg.put(Shift.Event.READYRECIPE, recipe);
        currentShift.notifyAllReceivers(arg);
    }

    /**
     * Rimuove una ricetta precedentemente assegnata come pronta.
     * Se il turno corrente e' nullo allora verra' lanciata una eccezione del tipo {@link NullPointerCurrentShiftException}.
     * Viene aggiornata la mappa del turno alla quale verra' rimossa la ricetta selezionata.
     *
     * @param recipe ricetta da rimuovere.
     */
    public void removeReadyRecipe(Recipe recipe) {
        if (currentShift == null) {
            throw new UseCaseLogicException("Il currentShift e' null.");
        }
        currentShift.removeStateRecipe(recipe);
        HashMap<Shift.Event, Recipe> arg = new HashMap<>();
        arg.put(Shift.Event.NOTREADYRECIPE, recipe);
        currentShift.notifyAllReceivers(arg);
    }

    /**
     * Imposta a null i due puntatori al menu' corrente e d al turno corrente.
     */
    public void reset() {
        currentShift = null;
        currentMenu = null;
    }
}
