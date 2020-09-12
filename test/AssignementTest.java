import model.businesslogic.Assignement;
import model.businesslogic.Cook;
import model.businesslogic.Recipe;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Time;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AssignementTest {


    private static Recipe recipe = new Recipe("ricetta test 1", new Time(1000));
    private static Cook cook = new Cook("test cook", "test_id");
    private static Assignement instance;

    @BeforeEach
    void setUp() {
        instance = new Assignement(recipe, cook, null);
    }

    @AfterEach
    void tearDown() {
        instance = null;
    }

    @Test
    void getRecipe() {
        Recipe recipe = instance.getRecipe();
        assert recipe != null;
        assertEquals("ricetta test 1", instance.getRecipe().getName());
        assertEquals(1000, instance.getRecipe().getActivityTime().getTime());
    }

    @Test
    void setRecipe() {
        instance.setRecipe(new Recipe("ricetta test 2", new Time(2000)));
        assert recipe != null;
        assertEquals("ricetta test 2", instance.getRecipe().getName());
        assertEquals(2000, instance.getRecipe().getActivityTime().getTime());
    }

    @Test
    void getCook() {
        Cook cook = instance.getCook();
        assert cook != null;
    }

    @Test
    void setCook() {
        Cook cook = new Cook("test cook 2", "id");
        instance.setCook(cook);
        assertEquals(cook, instance.getCook());
    }

    @Test
    void getId() {
        String id = instance.getId();
        assert id != null;
    }
}