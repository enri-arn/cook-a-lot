import controller.bl_controller.TaskMenuController;
import model.businesslogic.*;
import model.businesslogic.exception.UseCaseLogicException;
import model.services.CookALotDataManagerStub;
import model.services.CookALotServiceProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Time;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class TaskMenuControllerTest {

    private static TaskMenuController ctrl;
    private static CookALotDataManagerStub cookDM;
    private static Cook cookNotAssigned;
    private static Cook cookAssigned;
    private static Shift shift1;
    private static Shift shift2;
    private static Recipe recipe;
    private static Recipe recipe2;

    @BeforeEach
    void setUp() {
        cookDM = (CookALotDataManagerStub) CookALotServiceProvider.getInstance().getDataManager();
        ctrl = TaskMenuController.getInstance();

        cookNotAssigned = new Cook("prova", cookDM.generateCookId());
        cookDM.insertCook(cookNotAssigned);
        cookAssigned = new Cook("prova", cookDM.generateCookId());
        cookDM.insertCook(cookAssigned);

        Section[] ss = new Section[1];
        Section s2 = new Section("prova");
        s2.addItem(new Dish("provaPiatto", new Time(2000)), "provaItem");
        ss[0] = s2;
        Menu menu1 = new Menu(cookDM.generateMenuId(), "menuCompleteInUse", true, true, ss);
        cookDM.insertMenu(menu1);

        Section[] sss = new Section[1];
        Section s3 = new Section("prova");
        s3.addItem(new Dish("provaPiatto", new Time(2000)), "provaItem");
        sss[0] = s3;
        menu1 = new Menu(cookDM.generateMenuId(), "menuCompleteInUse", true, true, sss);
        cookDM.insertMenu(menu1);

        Calendar calendar = Calendar.getInstance();

        calendar.set(2018, Calendar.FEBRUARY, 1, 0, 15, 0);
        Date start = calendar.getTime();
        calendar.set(2018, Calendar.FEBRUARY, 1, 0, 20, 0);
        Date end = calendar.getTime();
        shift1 = new Shift(start, end, false, menu1);

        calendar.set(2018, Calendar.FEBRUARY, 1, 2, 0, 0);
        start = calendar.getTime();
        calendar.set(2018, Calendar.FEBRUARY, 1, 2, 20, 0);
        end = calendar.getTime();
        shift2 = new Shift(start, end, false, null);
        shift1.addCook(cookAssigned);
        shift2.addCook(cookAssigned);
        cookDM.insertShift(shift1);
        cookDM.insertShift(shift2);

        recipe = new Recipe("prova", Time.valueOf(LocalTime.of(2, 0, 0)));
        cookDM.insertRecipe(recipe);
        recipe2 = new Recipe("prova", Time.valueOf(LocalTime.of(0, 0, 5)));
        cookDM.insertRecipe(recipe2);
    }

    @AfterEach
    void tearDown() {
        cookDM.reset();
        ctrl.reset();
    }

    @Test
    void getCurrentMenu() {
        Menu menu = cookDM.getAllMenus().getElementAt(0);
        ctrl.setCurrentMenu(menu);
        assertNotNull(ctrl.getCurrentMenu());
    }

    @Test
    void setCurrentMenu() {
        Menu menu = cookDM.getAllMenus().getElementAt(0);
        ctrl.setCurrentMenu(menu);
        assertNotNull(ctrl.getCurrentMenu());
        assertEquals(menu, ctrl.getCurrentMenu());
    }

    @Test
    void getCurrentShift() {
        Shift shift = cookDM.getAllShifts().get(0);
        ctrl.setCurrentShift(shift);
        assertNotNull(ctrl.getCurrentShift());
    }

    @Test
    void setCurrentShift() {
        Shift shift = cookDM.getAllShifts().get(0);
        ctrl.setCurrentShift(shift);
        assertNotNull(ctrl.getCurrentShift());
        assertEquals(shift, ctrl.getCurrentShift());
    }

    @Test
    void assign() {
        //PRE-CONDIZIONI
        // E' in corso la consultazione del Turno t.

        //fail #1: non e' in corso la consultazione del Turno t.
        assertThrows(UseCaseLogicException.class, () -> ctrl.assign(recipe, cookNotAssigned, shift1));

        ctrl.reset();

        //fail #2: cuoco non e' assegnato a turno.
        ctrl.setCurrentShift(shift1);
        assertThrows(UseCaseLogicException.class, () -> ctrl.assign(recipe, cookNotAssigned, shift1));

        ctrl.reset();

        //fail #3: tempo di attivita' di cuoco supera tempo di turno.
        ctrl.setCurrentShift(shift1);
        assertThrows(UseCaseLogicException.class, () -> ctrl.assign(recipe, cookAssigned, shift1));

        //POST-CONDIZIONI
        // Se il Cuoco c e' in tra Turno t1 e c non e' sovraccarico, allora e' stata creata un'istanza di
        // Assegnazione a alla quale e' stato assegnato c, attribuita r, riferita a t1.
        ctrl.setCurrentShift(shift2);
        int assignCount = ctrl.getCurrentShift().getAssignementsCount();

        ctrl.assign(recipe2, cookAssigned, shift2);
        assertEquals(assignCount + 1, ctrl.getCurrentShift().getAssignementsCount());
        assertTrue(ctrl.getCurrentShift().checkAssignements(cookAssigned, recipe2, shift2));
    }

    @Test
    void consultMenu() {
        //PRE-CONDIZIONI
        // --

        //POST-CONDIZIONI
        // --

        Shift selectedShift = shift1;
        ctrl.consultMenu(selectedShift);
        assertEquals(selectedShift, ctrl.getCurrentShift());
    }

    @Test
    void removeAssignemt() {
        //PRE-CONDIZIONI: E’ in corso la consultazione del Turno t. Esiste un’istanza di assegnazione. associata a cuoco
        // per mezzo dell’associazione assegnato a; esiste un’associazione tra turno e assegnazione (riferita a); esiste
        // un’associazione tra ricetta e assegnazione (attribuita a).


        //fail #1: non e' in corso la consultazione del Turno t.
        assertThrows(UseCaseLogicException.class, () -> ctrl.removeAssignement(recipe, cookNotAssigned, shift1));
        ctrl.reset();

        //fail #2: l'assegnamento del cuoco a una ricetta in nel turno indicato non esiste.
        ctrl.setCurrentShift(shift1);
        assertThrows(UseCaseLogicException.class, () -> ctrl.removeAssignement(recipe, cookNotAssigned, shift1));
        ctrl.reset();

        //POST-CONDIZIONI: E’ stata eliminata Assegnazione assegnazione riferita a t. E’ stata eliminata l’associazione
        //riferita a fra l’assegnazione eliminata e t  e sono state eliminate le associazioni:
        //assegnato a  fra cuoco e assegnazione.
        //attribuita a  fra ricetta e assegnazione.
        ctrl.setCurrentShift(shift1);
        ctrl.consultMenu(shift1);

        ctrl.assign(recipe2, cookAssigned, shift2);
        int assignCount = ctrl.getCurrentShift().getAssignementsCount();

        ctrl.removeAssignement(recipe2, cookAssigned, shift2);

        assertEquals(assignCount - 1, ctrl.getCurrentShift().getAssignementsCount());
        assertFalse(ctrl.getCurrentShift().checkAssignements(cookAssigned, recipe2, shift2));
        assertNull(ctrl.getCurrentShift().getRecipeStates().get(recipe2));
    }

    @Test
    void endAssignement() {
        //PRE-CONDIZIONI
        // E' in corso la consultazione del Turno t.

        //fail #1: non e' in corso la consultazione del Turno t.
        assertThrows(UseCaseLogicException.class, () -> ctrl.removeAssignement(recipe, cookNotAssigned, shift1));
        ctrl.reset();

        //POST-CONDIZIONI
        // In t e' impostato t.completo = si'

        ctrl.setCurrentShift(shift1);
        ctrl.getCurrentShift().setComplete(true);
        assertTrue(ctrl.getCurrentShift().isComplete());
    }

    @Test
    void setReadyRecipe() {
        //PRE-CONDIZIONI
        // E' in corso la consultazione del Turno t

        //fail #1: non e' in corso la consultazione del Turno t.
        assertThrows(UseCaseLogicException.class, () -> ctrl.removeAssignement(recipe, cookNotAssigned, shift1));
        ctrl.reset();

        //POST-CONDIZIONI
        // E' stata creata l'associazione e' pronta in tra Ricetta r e Turno t
        ctrl.setCurrentShift(shift1);
        ctrl.setReadyRecipe(recipe);
        assertEquals(Shift.enumState.READY, ctrl.getCurrentShift().getRecipeStates().get(recipe));
    }

    @Test
    void removeReadyRecipe() {
        //PRE-CONDIZIONI
        // E' in corso la consultazione del Turno t

        //fail #1: non e' in corso la consultazione del Turno t.
        assertThrows(UseCaseLogicException.class, () -> ctrl.removeAssignement(recipe, cookNotAssigned, shift1));
        ctrl.reset();

        //POST-CONDIZIONI
        ctrl.setCurrentShift(shift1);
        ctrl.setReadyRecipe(recipe);
        assertEquals(Shift.enumState.READY, ctrl.getCurrentShift().getRecipeStates().get(recipe));
        ctrl.removeReadyRecipe(recipe);
        assertNull(ctrl.getCurrentShift().getRecipeStates().get(recipe));
    }

    @Test
    void showMenu() {
    }
}