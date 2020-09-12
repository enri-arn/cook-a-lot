import model.businesslogic.Dish;
import model.businesslogic.Item;
import model.businesslogic.Section;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.services.CookALotDataManager;
import model.services.CookALotServiceProvider;

import java.sql.Time;

import static org.junit.jupiter.api.Assertions.*;

class SectionTest {

    private Section sVuota;
    private Section sUnItem;
    private Section sMultiItem;

    private static Dish[] dishes;
    private static CookALotDataManager dmService;

    @BeforeAll
    static void globalSetUp() {
        dishes = new Dish[5];
        dishes[0] = new Dish("Pastasciutta", new Time(1000));
        dishes[1] = new Dish("Insalata di riso", new Time(1000));
        dishes[2] = new Dish("Arrosto", new Time(1000));
        dishes[3] = new Dish("Uovo fritto", new Time(1000));
        dishes[4] = new Dish("Banana split", new Time(1000));
        dmService = CookALotServiceProvider.getInstance().getDataManager();
    }

    @BeforeEach
    void setUp() {
        sVuota = new Section("Piatti principali");

        Item[] unItem = new Item[1];
        unItem[0] = new Item("Pastasciutta",
                SectionTest.dishes[0]);
        sUnItem = new Section(dmService.generateSectionId(),
                "Antipasti",
                unItem);

        Item[] multiItem = new Item[3];
        multiItem[0] = new Item("Insalata di riso",
                SectionTest.dishes[1]);
        multiItem[1] = new Item("Arrosto",
                SectionTest.dishes[2]);
        multiItem[0] = new Item("Uovo fritto",
                SectionTest.dishes[3]);
        sMultiItem = new Section(dmService.generateSectionId(),
                "Piatti tutti",
                multiItem);
    }

    @AfterEach
    void tearDown() {
        sVuota = null;
        sUnItem = null;
        sMultiItem = null;
    }

    @Test
    void addItem() {
        addItemSuSection(sVuota);
        addItemSuSection(sUnItem);
        addItemSuSection(sMultiItem);
    }

    private void addItemSuSection(Section sec) {
        int oldnum = sec.getItemCount();
        sec.addItem(SectionTest.dishes[4], "Banana split");
        assertEquals(oldnum+1, sec.getItemCount());
        Item nuovo = sec.getItem(sec.getItemCount()-1);
        assertSame(SectionTest.dishes[4], nuovo.getDish());
        assertEquals("Banana split", nuovo.getDescription());
    }

    @Test
    void removeItem() {
    }

    @Test
    void removeItem1() {
    }

    @Test
    void testClone() {
    }

    @Test
    void hasItem() {
    }

    @Test
    void hasItem1() {
    }
}