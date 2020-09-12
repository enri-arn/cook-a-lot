import model.businesslogic.Dish;
import model.businesslogic.Item;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Time;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ItemTest {

    private static Item instance;
    private static Dish dish;

    @BeforeEach
    void setUp() {
        dish = new Dish(
                "dish test 1",
                new Time(1000));
        instance = new Item(
                "item description 1",
                dish);
    }

    @AfterEach
    void tearDown() {
        dish = null;
        instance = null;
    }

    @Test
    void getId() {
        String id = instance.getId();
        assertNotNull(instance);
        assertNotNull(id);
    }

    @Test
    void getDescription() {
        String description = "item description 1";
        assertNotNull(instance);
        assertEquals(description, instance.getDescription());
    }

    @Test
    void getDish() {
        assertNotNull(instance);
        assertEquals(dish, instance.getDish());
    }

    @Test
    void setDescription() {
        String description = "item description 2";
        instance.setDescription(description);
        assertNotNull(instance);
        assertEquals(description, instance.getDescription());
    }

    @Test
    void setDish() {
        Dish dish = new Dish(
                "dish test 2",
                new Time(2000)
        );
        instance.setDish(dish);
        assertNotNull(instance);
        assertEquals(dish, instance.getDish());
    }
}