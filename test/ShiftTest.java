import model.businesslogic.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ShiftTest {

    private static Shift instance;
    private static Assignement assignement;
    private static Recipe recipe;
    private static Cook cook;

    //Considero solo fino ai secondi.
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @BeforeEach
    void setUp() {
        recipe = new Recipe(
                "recipe test 1",
                new Time(1000));
        cook = new Cook("cook_test", "id");
        assignement = new Assignement(
                recipe,
                cook, null);
        Calendar calendar = Calendar.getInstance();
        calendar.set(2018, Calendar.FEBRUARY, 1, 8, 0 );
        Date start = calendar.getTime();
        calendar.set(2018, Calendar.FEBRUARY, 1, 10, 0);
        Date end = calendar.getTime();
        instance = new Shift(
                start,
                end,
                false,
                new Menu("menù test 1"));
    }

    @AfterEach
    void tearDown() {
        instance = null;
    }

    @Test
    void getStartTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2018, Calendar.FEBRUARY, 1, 8, 0 );
        Date startTime = calendar.getTime();
        assertNotNull(instance);
        assertEquals(formatter.format(startTime), formatter.format(instance.getStartTime()));
    }

    @Test
    void setStartTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2018, Calendar.FEBRUARY, 1, 9, 0);
        Date startTime = calendar.getTime();
        instance.setStartTime(startTime);
        assertNotNull(instance);
        assertEquals(formatter.format(startTime), formatter.format(instance.getStartTime()));
    }

    @Test
    void getEndTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2018, Calendar.FEBRUARY, 1, 10, 0);
        Date endTime = calendar.getTime();
        assertNotNull(instance);
        assertEquals(formatter.format(endTime), formatter.format(instance.getEndTime()));
    }

    @Test
    void setEndTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2018, Calendar.FEBRUARY, 1, 10, 0);
        Date endTime = calendar.getTime();
        instance.setEndTime(endTime);
        assertNotNull(instance);
        assertEquals(formatter.format(endTime), formatter.format(instance.getEndTime()));
    }

    @Test
    void isComplete() {
        assertNotNull(instance);
        assertFalse(instance.isComplete());
    }

    @Test
    void setComplete() {
        instance.setComplete(true);
        assertNotNull(instance);
        assertTrue(instance.isComplete());
    }

    @Test
    void getInUseMenu() {
        String title = "menù test 1";
        Menu menu = instance.getInUseMenu();
        assertNotNull(instance);
        assertNotNull(menu);
        assertEquals(title, menu.getTitle());
    }

    @Test
    void addAssignement() {
        instance.addAssignement(cook, recipe, instance);
        List<Assignement> assignements = instance.getAssignements();
        assertNotNull(instance);
        assertNotNull(assignements);
        assertEquals(assignements.size(), 1);
        assertEquals(assignements.get(0).getCook(), assignement.getCook());
        assertEquals(assignements.get(0).getRecipe(), assignement.getRecipe());
    }

    @Test
    void getAssignementsTest(){
        List<Assignement> assignements = instance.getAssignements();
        assertNotNull(instance);
        assertNotNull(assignements);
    }

    @Test
    void getCooksTest(){
        List<Cook> cooks = instance.getCooks();
        assertNotNull(instance);
        assertNotNull(cooks);
    }

    @Test
    void getRecipeStateTest(){
        Map<Recipe, Shift.enumState> recipeenumStateMap = instance.getRecipeStates();
        assertNotNull(instance);
        assertNotNull(recipeenumStateMap);
    }

    @Test
    void isLine() {
        assertFalse(instance.getLine());
        instance.setLine(true);
        assertTrue(instance.getLine());
    }

    @Test
    void removeAssignement() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2019, Calendar.FEBRUARY, 1);
        Date start = calendar.getTime();
        calendar.set(2019, Calendar.FEBRUARY, 1);
        Date end = calendar.getTime();
        Shift tempShift = new Shift("prova", start, end,true,  new Menu("menù test 1"));
        instance.addAssignement(cook, recipe, tempShift);
        List<Assignement> assignements = instance.getAssignements();
        assertNotNull(instance);
        assertNotNull(assignements);
        assertEquals(1, assignements.size());
        assertNotNull(assignements.get(0));
        assertEquals(assignements.get(0).getCook(), assignement.getCook());
        assertEquals(assignements.get(0).getRecipe(), assignement.getRecipe());
        instance.removeAssignement(cook, recipe, tempShift);
        assertTrue(assignements.isEmpty());
    }

    @Test
    void changeStateRecipe() {
        instance.changeStateRecipe(recipe, Shift.enumState.READY);
        assertNotNull(instance);
        assertEquals(Shift.enumState.READY, instance.getRecipeStates().get(recipe));
        instance.changeStateRecipe(recipe, Shift.enumState.ASSIGNED);
        assertEquals(Shift.enumState.ASSIGNED, instance.getRecipeStates().get(recipe));
    }

    @Test
    void removeStateRecipe() {
        instance.removeStateRecipe(recipe);
        assertNotNull(instance);
        assertNull(instance.getRecipeStates().get(recipe));
    }

    @Test
    void setReadyRecipe() {
        instance.setReadyRecipe(recipe);
        assertNotNull(instance);
        assertEquals(Shift.enumState.READY, instance.getRecipeStates().get(recipe));
    }

    @Test
    void checkAssignements() {
        assert instance != null;
        instance.addAssignement(cook, recipe,instance);
        List<Assignement> assignements = instance.getAssignements();
        assertNotNull(instance);
        assertNotNull(assignements);
        assertEquals(1, assignements.size());
        assertNotNull(assignements.get(0));
        assertEquals(assignements.get(0).getCook(), assignement.getCook());
        assertEquals(assignements.get(0).getRecipe(), assignement.getRecipe());
    }

    @Test
    void isInShift() {
        List<Cook> cooks = new ArrayList<>();
        cooks.add(cook);
        instance.setCooks(cooks);
        assertNotNull(instance);
        assertNotNull(instance.getCooks());
        assertFalse(instance.getCooks().isEmpty());
        assertTrue(instance.isInShift(cook));

    }

    @Test
    void getShiftTime() {
        long minutes = 7200000;
        assertNotNull(instance);
        assertEquals(minutes, instance.getShiftTime());
    }

    @Test
    void overTime() {
        LocalTime time1 = LocalTime.of(1, 0, 0);
        LocalTime time2 = LocalTime.of(1, 30, 0);
        Recipe recipe1 = new Recipe("test", "id", Time.valueOf(time1));
        Recipe recipe2 = new Recipe("test", "id", Time.valueOf(time2));
        instance.addAssignement(cook, recipe1, instance);
        boolean overtime1 = instance.overTime(instance, cook, recipe1.getActivityTimeLong());
        assertFalse(overtime1);
        boolean overtime2 = instance.overTime(instance, cook, recipe2.getActivityTimeLong());
        assertTrue(overtime2);
    }
}