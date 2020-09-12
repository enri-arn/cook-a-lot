package model.services;

import model.businesslogic.Cook;
import model.businesslogic.Menu;
import model.businesslogic.Recipe;
import model.businesslogic.Shift;
import model.utils.EventReceiver;

import javax.swing.*;
import java.util.List;

public interface CookALotDataManager extends EventReceiver {
    public String generateMenuId();

    public String generateSectionId();

    public String generateItemId();

    public String generateRecipeId();

    public String generateCookId();

    public void insertMenu(Menu m);

    public void insertCook(Cook c);

    public void insertShift(Shift s);

    public void insertRecipe(Recipe r);

    public ListModel<Menu> getAllMenus();

    public ListModel<Recipe> getAllRecipes();

    public List<Cook> getAllCooks();

    public List<Shift> getAllShifts(int day, int month, int year);

    public List<Shift> getAllShifts();

    public String generateAssignementId();

    public String generateShiftId();

    public void updateAssignements(Shift shift);
}
