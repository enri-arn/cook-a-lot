import controller.bl_controller.MenuDefinitionController;
import model.businesslogic.*;
import model.businesslogic.exception.UseCaseLogicException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.services.CookALotDataManagerStub;
import model.services.CookALotServiceProvider;

import javax.swing.*;

import java.sql.Time;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class MenuDefinitionControllerTest {

    static MenuDefinitionController ctrl;
    static CookALotDataManagerStub dm;
    static ListModel<Recipe> allRecipes;
    static ArrayList<Dish> dishRecipes;

    static Menu menuCompleteNotInUse;
    static Menu menuCompleteInUse;
    static Menu menuNotComplete;

    ListModel<Menu> allMenus;

    @BeforeAll
    static void globalSetup() {
        ctrl = MenuDefinitionController.getInstance();
        dm = (CookALotDataManagerStub)CookALotServiceProvider.getInstance().getDataManager();
        allRecipes = dm.getAllRecipes();
        dishRecipes = new ArrayList<>();
        for (int i= 0; i < allRecipes.getSize(); i++) {
            Recipe r = allRecipes.getElementAt(i);
            if (r.isDish()) {
                dishRecipes.add((Dish)r);
            }
        }


        Section[] s = new Section[1];
        Section s1 = new Section("prova");
        s1.addItem(new Dish("provaPiatto", new Time(2000)), "provaItem");
        s[0] = s1;
        menuCompleteNotInUse = new Menu(dm.generateMenuId(), "menuCompleteNotInUse", true, false, s);
        dm.insertMenu(menuCompleteNotInUse);

        Section[] ss = new Section[1];
        Section s2 = new Section("prova");
        s2.addItem(new Dish("provaPiatto", new Time(2000)), "provaItem");
        ss[0] = s2;
        menuCompleteInUse = new Menu(dm.generateMenuId(), "menuCompleteInUse", true, true, ss);
        dm.insertMenu(menuCompleteInUse);

        Section[] sss = new Section[1];
        Section s3 = new Section("prova");
        sss[0] = s3;
        menuNotComplete = new Menu(dm.generateMenuId(), "menuCompleteNotInUse", false, false, sss);
        dm.insertMenu(menuNotComplete);
    }

    @BeforeEach
    void setUp() {

        allMenus = dm.getAllMenus();
    }

    @AfterEach
    void tearDown() {
        ctrl.reset();
        dm.reset();
    }


    @Test
    void createMenu() {
        // Pre-condizioni: ---

        Menu m = ctrl.createMenu("Cena di compleanno");
        // Post-condizioni: È stato creato un Menù m con
        // m.titolo = titolo e m.completo = falso.

        assertEquals("Cena di compleanno", m.getTitle());
        assertEquals(false, m.isComplete());
    }

    @Test
    void copyMenu() {
    }

    @Test
    void workOnMenu() {
        // Pre-condizioni: Il Menù m non è in uso in alcun Turno di Servizio
        assertThrows(UseCaseLogicException.class, ()->ctrl.workOnMenu(menuCompleteInUse));
        ctrl.reset();

        // Post-condizioni: m.completo = falso
        Menu m = ctrl.workOnMenu(menuNotComplete);
        assertSame(m, menuNotComplete);
        assertEquals(false, m.isComplete());
    }

    @Test
    void changeMenuTitle() {
    }

    @Test
    void defineSection() {
        // Pre-condizioni: È in corso la definizione
        // di un Menù m e m.completo = falso

        // fail #1: non è in corso la def di un Menù
        assertThrows(UseCaseLogicException.class,
                ()->ctrl.defineSection("Antipasti"));
        ctrl.reset();

        // fail #2: è in corso la def di un Menù
        // che però è completo
        ctrl.workOnMenu(menuCompleteNotInUse);
        ctrl.setMenuAsComplete();
        assertThrows(UseCaseLogicException.class,
                ()->ctrl.defineSection("Antipasti"));
        ctrl.reset();


        // Post-condizioni: È stata creata una Sezione sez con sez.nome = nome e
        // il Menù m è stato associato (ha) con sez.
        Menu m = menuNotComplete;
        ctrl.workOnMenu(m);
        int oldSecNum = m.getSectionCount();
        ctrl.defineSection("Antipasti");
        Section sect = m.getSection(m.getSectionCount()-1);
        assertEquals(oldSecNum+1, m.getSectionCount());
        assertEquals(sect.getName(), "Antipasti");
        assertEquals(sect.getItemCount(), 0);
    }

    @Test
    void workOnSection() {
    }

    @Test
    void deleteSection() {
    }

    @Test
    void changeSectionName() {
    }

    @Test
    void insertItem() {
        // Pre-condizioni: È in corso la definizione di una Sezione sez
        // appartenente a  un Menù m e m.completo = falso

        // fail #1: non è in corso la definizione di una sezione

        final Dish d = dishRecipes.get(0);
        assertThrows(UseCaseLogicException.class,
                ()->ctrl.insertItem(d));
        ctrl.reset();

        Menu m = menuCompleteNotInUse;//allMenus.getElementAt(0);
        ctrl.workOnMenu(m);
        assertThrows(UseCaseLogicException.class,
                ()->ctrl.insertItem(d));
        ctrl.reset();

        // fail #2: definizione sezione in corso ma m completo
        // QUESTO CASO NON E' RIPRODUCIBILE PERCHE' LO STATO CORRISPONDENTE
        // DEL CONTROLLER E' IRRAGGIUNGIBILE

        // Post-condizioni: È stata creata una Voce v corrispondente
        // al Piatto p con v.descrizione = nome se specificato,
        // v.descrizione = p.nome altrimenti.
        ctrl.workOnMenu(m);
        Section s = m.getSection(0);
        ctrl.workOnSection(s);
        int oldItemNum = s.getItemCount();
        ctrl.insertItem(d);
        assertEquals(oldItemNum+1, s.getItemCount());
        Item it = s.getItem(s.getItemCount()-1);
        assertSame(d, it.getDish());
        assertEquals(d.getName(), it.getDescription());

        ctrl.reset();

        ctrl.workOnMenu(m);
        s = m.getSection(0);
        ctrl.workOnSection(s);
        oldItemNum = s.getItemCount();
        ctrl.insertItem(d, "Bla bla");
        assertEquals(oldItemNum+1, s.getItemCount());
        it = s.getItem(s.getItemCount()-1);
        assertSame(d, it.getDish());
        assertEquals("Bla bla", it.getDescription());
    }

    @Test
    void deleteItem() {
    }

    @Test
    void deleteItem1() {
    }

    @Test
    void modifyItem() {
    }

    @Test
    void modifyItem1() {
    }

    @Test
    void modifyItem2() {
    }

    @Test
    void setMenuAsComplete() {
        // Pre-condizioni: È in corso la definizione di un Menù m e m.completo = falso;
        // esiste almeno una Sezione appartenente a m e tutte le Sezioni appartenenti a m contengono almeno una Voce.

        // fail #1: non è in corso la definizione di un menu
        assertThrows(UseCaseLogicException.class,
                ()->ctrl.setMenuAsComplete());
        ctrl.reset();

        // fail #2: è in corso la definizione di un Menù completo
        ctrl.workOnMenu(menuCompleteNotInUse);
        ctrl.setMenuAsComplete();
        assertThrows(UseCaseLogicException.class,
                ()->ctrl.setMenuAsComplete());
        ctrl.reset();

        // fail #3: il Menù non ha sezioni
        ctrl.createMenu("Vuoto");
        assertThrows(UseCaseLogicException.class,
                ()->ctrl.setMenuAsComplete());
        ctrl.reset();

        // fail #4: il Menù ha una sezione vuota
        ctrl.createMenu("Quasi vuoto");
        ctrl.defineSection("Sezione vuota");
        assertThrows(UseCaseLogicException.class,
                ()->ctrl.setMenuAsComplete());
        ctrl.reset();

        // Post-condizioni: m.completo diventa vero
        Menu m = ctrl.createMenu("Non vuoto");
        ctrl.defineSection("Non vuota");
        ctrl.insertItem(dishRecipes.get(0));
        ctrl.setMenuAsComplete();
        assertEquals(true, m.isComplete());
    }

    @Test
    void deleteMenu() {
    }
}