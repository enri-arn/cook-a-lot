import model.businesslogic.Dish;
import model.businesslogic.Recipe;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Time;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DishTest {

    private static Recipe instance;

    @BeforeEach
    void setUp() {
        instance = new Dish("dish test 1", new Time(1000));
    }

    @AfterEach
    void tearDown() {
        instance = null;
    }

    @Test
    void getName() {
        String name = "dish test 1";
        assertNotNull(instance);
        assertEquals(name, instance.getName());
    }

    @Test
    void setName() {
        String name = "dish test 2";
        instance.setName(name);
        assertNotNull(instance);
        assertEquals(name, instance.getName());
    }

    @Test
    void getActivityTime() {
        Time activityTime = instance.getActivityTime();
        assertNotNull(instance);
        assertEquals(1000, activityTime.getTime());
    }

    @Test
    void setActivityTime() {
        Time activityTime = new Time(2000);
        instance.setActivityTime(activityTime);
        assertNotNull(instance);
        assertEquals(activityTime, instance.getActivityTime());
    }

    @Test
    void getId() {
        String id = instance.getId();
        assertNotNull(id);
    }

    @Test
    void getAllRecipes() {
        List<Recipe> recipes = instance.getAllRecipes();
        assertNotNull(recipes);
    }
}