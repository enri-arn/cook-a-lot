import model.businesslogic.Recipe;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Time;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class RecipeTest {

    private static Recipe instance;


    @BeforeEach
    void setUp() {
        instance = new Recipe("ricetta test 1", new Time(1000));
    }

    @AfterEach
    void tearDown() {
        instance = null;
    }

    @Test
    void getName() {
        String name = instance.getName();
        assertNotNull(instance);
        assertEquals("ricetta test 1", name);
    }

    @Test
    void setName() {
        String name = "ricetta test 2";
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
        assertEquals(activityTime.getTime(), instance.getActivityTime().getTime());
    }

    @Test
    void getId() {
        String id = instance.getId();
        assertNotNull(id);
    }

    @Test
    void getAllRecipes() {
        List<Recipe> recipes = instance.getAllRecipes();
        assertNotNull(instance);
    }

}