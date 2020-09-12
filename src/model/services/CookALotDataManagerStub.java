package model.services;

import model.businesslogic.*;
import model.utils.EventSource;

import javax.swing.*;
import java.sql.Time;
import java.time.LocalTime;
import java.util.*;

public class CookALotDataManagerStub implements CookALotDataManager {


    private long idGeneratorLast;
    private int idGeneratorCount;

    private DefaultListModel<Menu> theMenuList;
    private DefaultListModel<Recipe> theRecipeList;
    private List<Shift> theShiftList;
    private List<Cook> theCookList;

    private HashMap<String, Recipe> recipeTable;//id ricetta, ricetta
    private HashMap<String, Menu> menuTable;//id menù, menù
    private HashMap<String, Shift> shiftTable;//id turno, turno
    private HashMap<String, Cook> cookTable;//id cuoco, cuoco
    private HashMap<String, HashMap<String, Section>> sectionTable;//id menù, id sezione, sezione
    private HashMap<String, HashMap<String, Item>> itemTable;//id sezione, id item, item

    private final Recipe[] dummyRecipes = {
            //antipasti
            new Dish("Peperoni ripieni", this.generateRecipeId(), Time.valueOf(LocalTime.of(2, 20, 0))),
            new Dish("Vitello tonnato", this.generateRecipeId(), Time.valueOf(LocalTime.of(0, 20, 0))),
            new Dish("Panzanella", this.generateRecipeId(), Time.valueOf(LocalTime.of(0, 20, 0))),
            new Dish("Antipasto misto", this.generateRecipeId(), Time.valueOf(LocalTime.of(4, 20, 0))),
            //primi
            new Dish("Insalata di riso", this.generateRecipeId(), Time.valueOf(LocalTime.of(0, 20, 0))),
            new Dish("Spaghetti alla carbonara", this.generateRecipeId(), Time.valueOf(LocalTime.of(0, 20, 0))),
            new Dish("Pasta fredda", this.generateRecipeId(), Time.valueOf(LocalTime.of(0, 20, 0))),
            new Dish("Spaghetti alle vongole", this.generateRecipeId(), Time.valueOf(LocalTime.of(0, 20, 0))),
            //secondi
            new Dish("Polpette di carne", this.generateRecipeId(), Time.valueOf(LocalTime.of(0, 20, 0))),
            new Dish("Cosciotto di maiale", this.generateRecipeId(), Time.valueOf(LocalTime.of(0, 20, 0))),
            new Dish("Gateau di patate", this.generateRecipeId(), Time.valueOf(LocalTime.of(0, 20, 0))),
            new Dish("Parmigiana di melazane", this.generateRecipeId(), Time.valueOf(LocalTime.of(0, 20, 0))),
            //dolci
            new Dish("Tiramisù", this.generateRecipeId(), Time.valueOf(LocalTime.of(0, 20, 0))),
            new Dish("Torta fredda allo yogurt", this.generateRecipeId(), Time.valueOf(LocalTime.of(0, 20, 0))),
            new Dish("Crostata alla cnfettura di albicocche", this.generateRecipeId(), Time.valueOf(LocalTime.of(0, 20, 0))),
            new Dish("Crepes dolci", this.generateRecipeId(), Time.valueOf(LocalTime.of(0, 20, 0))),
    };

    private final Item[] items1 = {
            new Item(this.generateItemId(), "descrizione test 1", (Dish) dummyRecipes[0]),
            new Item(this.generateItemId(), "descrizione test 2", (Dish) dummyRecipes[1]),
            new Item(this.generateItemId(), "descrizione test 3", (Dish) dummyRecipes[2]),
            new Item(this.generateItemId(), "descrizione test 4", (Dish) dummyRecipes[3]),
    };
    private final Item[] items2 = {
            new Item(this.generateItemId(), "descrizione test 1", (Dish) dummyRecipes[4]),
            new Item(this.generateItemId(), "descrizione test 2", (Dish) dummyRecipes[5]),
            new Item(this.generateItemId(), "descrizione test 3", (Dish) dummyRecipes[6]),
            new Item(this.generateItemId(), "descrizione test 4", (Dish) dummyRecipes[7]),
    };
    private final Item[] items3 = {
            new Item(this.generateItemId(), "descrizione test 1", (Dish) dummyRecipes[8]),
            new Item(this.generateItemId(), "descrizione test 2", (Dish) dummyRecipes[9]),
            new Item(this.generateItemId(), "descrizione test 3", (Dish) dummyRecipes[10]),
            new Item(this.generateItemId(), "descrizione test 4", (Dish) dummyRecipes[11]),
    };
    private final Item[] items4 = {
            new Item(this.generateItemId(), "descrizione test 1", (Dish) dummyRecipes[12]),
            new Item(this.generateItemId(), "descrizione test 2", (Dish) dummyRecipes[13]),
            new Item(this.generateItemId(), "descrizione test 3", (Dish) dummyRecipes[14]),
            new Item(this.generateItemId(), "descrizione test 4", (Dish) dummyRecipes[15]),
    };

    private final Section[] sections = {
            new Section(this.generateSectionId(), "Antipasti", items1),
            new Section(this.generateSectionId(), "Primi piatti", items2),
            new Section(this.generateSectionId(), "Secondi piatti", items3),
            new Section(this.generateSectionId(), "Dolci", items4)
    };
    private HashMap<String, Section> sectionMap = new HashMap<>();

    public CookALotDataManagerStub() {
        idGeneratorLast = System.currentTimeMillis();
        idGeneratorCount = 0;
        menuTable = new HashMap<>();
        sectionTable = new HashMap<>();
        itemTable = new HashMap<>();
        recipeTable = new HashMap<>();
        shiftTable = new HashMap<>();
        cookTable = new HashMap<>();
        this.createFakeDBContent();
    }

    private void createFakeDBContent() {
        for (Recipe r : dummyRecipes) {
            this.recipeTable.put(r.getId(), r);
        }

        String secId1 = this.generateSectionId();
        String secId2 = this.generateSectionId();
        String secId3 = this.generateSectionId();
        String secId4 = this.generateSectionId();
        String menuId = this.generateMenuId();

        sectionMap.put(secId1, sections[0]);
        sectionMap.put(secId2, sections[1]);
        sectionMap.put(secId3, sections[2]);
        sectionMap.put(secId4, sections[3]);

        this.menuTable.put(menuId, new Menu(menuId, "Menù completo di test con titolo lungo", true, true, sections));

        HashMap<String, Item> itemHashMap1 = new HashMap<>();
        for (Item item : items1) {
            itemHashMap1.put(item.getId(), item);
        }
        this.itemTable.put(secId1, itemHashMap1);

        HashMap<String, Item> itemHashMap2 = new HashMap<>();
        for (Item item : items2) {
            itemHashMap2.put(item.getId(), item);
        }
        this.itemTable.put(secId2, itemHashMap2);

        HashMap<String, Item> itemHashMap3 = new HashMap<>();
        for (Item item : items3) {
            itemHashMap3.put(item.getId(), item);
        }
        this.itemTable.put(secId3, itemHashMap3);

        HashMap<String, Item> itemHashMap4 = new HashMap<>();
        for (Item item : items4) {
            itemHashMap4.put(item.getId(), item);
        }
        this.itemTable.put(secId4, itemHashMap4);

        HashMap<String, Section> sectionHashMap = new HashMap<>();
        for (Section sec : sections) {
            sectionHashMap.put(sec.getId(), sec);
        }
        this.sectionTable.put(menuId, sectionHashMap);

        String shiftId1 = this.generateShiftId();
        String shiftId2 = this.generateShiftId();
        String shiftId3 = this.generateShiftId();
        String shiftId4 = this.generateShiftId();
        String shiftId5 = this.generateShiftId();
        String shiftId6 = this.generateShiftId();

        Calendar calendar = Calendar.getInstance();

        calendar.set(2018, Calendar.JULY, 21, 7, 0, 0);
        Date start = calendar.getTime();
        calendar.set(2018, Calendar.JULY, 21, 9, 0, 0);
        Date end = calendar.getTime();
        this.shiftTable.put(shiftId1, new Shift(shiftId1, start, end, false, this.menuTable.get(menuId)));

        calendar.set(2018, Calendar.JULY, 21, 10, 0, 0);
        start = calendar.getTime();
        calendar.set(2018, Calendar.JULY, 21, 12, 0, 0);
        end = calendar.getTime();
        this.shiftTable.put(shiftId2, new Shift(shiftId2, start, end, false, this.menuTable.get(menuId)));

        calendar.set(2018, Calendar.JULY, 22, 13, 0, 0);
        start = calendar.getTime();
        calendar.set(2018, Calendar.JULY, 22, 16, 0, 0);
        end = calendar.getTime();
        this.shiftTable.put(shiftId3, new Shift(shiftId3, start, end, false, this.menuTable.get(menuId)));

        calendar.set(2018, Calendar.JULY, 21, 18, 0, 0);
        start = calendar.getTime();
        calendar.set(2018, Calendar.JULY, 21, 20, 0, 0);
        end = calendar.getTime();
        this.shiftTable.put(shiftId4, new Shift(shiftId4, start, end, false, this.menuTable.get(menuId)));

        List<Cook> cooks = new ArrayList<>();
        cooks.add(new Cook("Joe Bastianich", this.generateCookId()));
        cooks.add(new Cook("Carlo Cracco", this.generateCookId()));
        cooks.add(new Cook("Bruno Barbieri", this.generateCookId()));
        cooks.add(new Cook("Antonino Cannavacciuolo", this.generateCookId()));
        cooks.add(new Cook("Antonia Klugmann", this.generateCookId()));
        this.cookTable.put(this.generateCookId(), cooks.get(0));
        this.cookTable.put(this.generateCookId(), cooks.get(1));
        this.cookTable.put(this.generateCookId(), cooks.get(2));
        this.cookTable.put(this.generateCookId(), cooks.get(3));
        this.cookTable.put(this.generateCookId(), cooks.get(4));

        this.shiftTable.forEach((k, v) -> {
            v.setCooks(cooks);
            v.setLine(false);
        });

        calendar.set(2018, Calendar.JULY, 21, 21, 0, 0);
        start = calendar.getTime();
        calendar.set(2018, Calendar.JULY, 21, 23, 0, 0);
        end = calendar.getTime();
        this.shiftTable.put(shiftId5, new Shift(shiftId5, start, end, false, this.menuTable.get(menuId)));
        calendar.set(2018, Calendar.JULY, 21, 2, 0, 0);
        start = calendar.getTime();
        calendar.set(2018, Calendar.JULY, 21, 4, 0, 0);
        end = calendar.getTime();
        Shift inLine = new Shift(shiftId6, start, end, false, this.menuTable.get(menuId));
        inLine.setLine(true);
        inLine.setCooks(cooks);
        this.shiftTable.put(shiftId6, inLine);

    }

    public void reset() {
        // clear inner data structures
        this.clearMenus();
        this.clearRecipes();

        this.menuTable.clear();
        this.sectionTable.clear();
        this.itemTable.clear();
        this.recipeTable.clear();
        this.cookTable.clear();
        this.shiftTable.clear();

        this.createFakeDBContent();
    }

    private void loadMenus() {
        clearMenus();
        this.theMenuList = new DefaultListModel<>();
        for (Menu m : this.menuTable.values()) {
            this.theMenuList.addElement(m);
            m.addReceiver(this);
        }
    }

    private void clearMenus() {
        if (this.theMenuList == null) return;
        for (int i = 0; i < theMenuList.getSize(); i++) {
            theMenuList.get(i).removeReceiver(this);
        }
        theMenuList = null;
    }

    private void clearRecipes() {
        if (this.theRecipeList == null) return;
        theRecipeList = null;
    }

    private void loadRecipes() {
        this.theRecipeList = new DefaultListModel<>();
        for (Recipe r : this.recipeTable.values()) {
            this.theRecipeList.addElement(r);
        }
    }

    private void loadShifts() {
        this.theShiftList = new ArrayList<>();
        this.theShiftList.addAll(this.shiftTable.values());
    }

    private void clearShifts() {
        if (this.theShiftList == null) return;
        theShiftList = null;
    }

    private void loadCooks() {
        this.theCookList = new ArrayList<>();
        this.theCookList.addAll(this.cookTable.values());
    }

    private void clearCooks() {
        if (this.theCookList == null) return;
        theCookList = null;
    }

    private String generateId() {
        long curr = System.currentTimeMillis();
        if (curr != idGeneratorLast) {
            idGeneratorLast = curr;
            idGeneratorCount = 0;
        } else {
            idGeneratorCount++;
        }
        return Long.toHexString(idGeneratorLast) + '-' + idGeneratorCount;
    }

    @Override
    public String generateMenuId() {
        return this.generateId();
    }

    @Override
    public String generateSectionId() {
        return this.generateId();
    }

    @Override
    public String generateItemId() {
        return this.generateId();
    }

    @Override
    public String generateRecipeId() {
        return this.generateId();
    }

    @Override
    public String generateCookId() {
        return this.generateId();
    }

    @Override
    public void update(EventSource observable, Object o) {
        if (observable instanceof Menu) {
            this.menuUpdate((Menu) observable, (Menu.Event) o);
        }
    }

    private void menuUpdate(Menu m, Menu.Event ev) {
        switch (ev) {
            case DATACHANGED:
                // se i dati interni al menu o alle sue sezioni o voci sono cambiati
                // in questa implementazione non serve far nulla
                break;
            case STRUCTURECHANGED:
                // se è cambiata la struttura rimuoviamo i dati del menù e lo riscriviamo
                this.deleteData(m);
                this.writeData(m);
                break;
            case DELETED:
                // se il menù è stato eliminato ne rimuoviamo i dati
                this.deleteData(m);
                this.theMenuList.removeElement(m);
                break;
        }
    }


    @Override
    public void insertMenu(Menu m) {
        writeData(m);
        m.addReceiver(this);
        if (this.theMenuList == null)
            this.theMenuList = new DefaultListModel<>();
        this.theMenuList.addElement(m);
    }

    @Override
    public void insertCook(Cook c) {
        cookTable.put(c.getId(), c);
        if (this.theCookList == null)
            this.theCookList = new ArrayList<>();
        this.theCookList.add(c);
    }

    @Override
    public void insertShift(Shift s) {
        shiftTable.put(s.getId(), s);
        if (this.theShiftList == null)
            this.theShiftList = new ArrayList<>();
        this.theShiftList.add(s);
    }

    @Override
    public void insertRecipe(Recipe r) {
        recipeTable.put(r.getId(), r);
        if (this.theRecipeList == null)
            this.theRecipeList = new DefaultListModel<>();
        this.theRecipeList.addElement(r);
    }

    private void writeData(Menu m) {
        menuTable.put(m.getId(), m);
        HashMap<String, Section> sects = new HashMap<>();
        this.sectionTable.put(m.getId(), sects);

        for (int i = 0; i < m.getSectionCount(); i++) {
            Section sect = m.getSection(i);
            sects.put(sect.getId(), sect);

            HashMap<String, Item> its = new HashMap<>();
            this.itemTable.put(sect.getId(), its);

            for (int j = 0; j < sect.getItemCount(); j++) {
                Item it = sect.getItem(j);
                its.put(it.getId(), it);
            }
        }
    }


    private void deleteData(Menu m) {
        HashMap<String, Section> menuSections = this.sectionTable.remove(m.getId());
        if (menuSections != null) {
            for (Section sect : menuSections.values()) {
                this.itemTable.remove(sect.getId());
            }
        }
        this.menuTable.remove(m.getId());
    }

    public ListModel<Menu> getAllMenus() {
        if (this.theMenuList == null) this.loadMenus();
        return this.theMenuList;
    }


    public ListModel<Recipe> getAllRecipes() {
        if (this.theRecipeList == null) {
            this.loadRecipes();
        }
        return this.theRecipeList;
    }

    public List<Cook> getAllCooks() {
        if (this.theCookList == null) this.loadCooks();
        return this.theCookList;
    }

    @Override
    public List<Shift> getAllShifts(int day, int month, int year) {
        if (this.theShiftList == null) this.loadShifts();
        List<Shift> shifts = new ArrayList<>();
        this.theShiftList.forEach(s -> {
            Calendar cal1 = Calendar.getInstance();
            Calendar cal2 = Calendar.getInstance();
            cal1.set(year, month, day);
            cal2.setTime(s.getStartTime());
            boolean sameDay = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                    cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
            if (sameDay) {
                Shift fakeShift = this.fakeCloneShift(s);
                shifts.add(fakeShift);
            }
        });

        return shifts;
    }

    @Override
    public List<Shift> getAllShifts() {
        if (this.theShiftList == null) this.loadShifts();
        List<Shift> shifts = new ArrayList<>();
        this.theShiftList.forEach(s -> {
            Shift fakeShift = this.fakeCloneShift(s);
            shifts.add(fakeShift);
        });

        return shifts;
    }

    private Shift fakeCloneShift(Shift shiftToCopy) {
        List<Cook> fakeCookList = shiftToCopy.getCooks();
        List<Assignement> fakeAssigmentList = shiftToCopy.getAssignements();
        Menu fakeMenu = shiftToCopy.getInUseMenu();
        Date fakeStartTime = shiftToCopy.getStartTime();
        Date fakeEndTime = shiftToCopy.getEndTime();
        Boolean fakeInLine = shiftToCopy.getLine();
        Boolean fakeIsComplete = shiftToCopy.isComplete();
        String fakeId = shiftToCopy.getId();
        HashMap<Recipe, Shift.enumState> fakeRecipeStates = (HashMap<Recipe, Shift.enumState>) shiftToCopy.getRecipeStates().clone();
        List<Assignement> newFakeListAssignement = new ArrayList<>();

        fakeAssigmentList.forEach(a -> {
            String id = a.getId();
            Cook cook = a.getCook();
            Recipe recipe = a.getRecipe();
            Shift shift = a.getShift();
            Assignement newAssignement = new Assignement(id, recipe, cook, shift);
            newFakeListAssignement.add(newAssignement);
        });

        Shift fakeShift = new Shift(fakeId, new Date(fakeStartTime.getTime()), new Date(fakeEndTime.getTime()), fakeIsComplete,
                fakeMenu);
        fakeShift.setCooks(fakeCookList);
        fakeShift.setLine(fakeInLine);
        fakeShift.setAssignements(newFakeListAssignement);
        fakeShift.setRecipeStates(fakeRecipeStates);
        return fakeShift;
    }

    @Override
    public String generateAssignementId() {
        return this.generateId();
    }

    @Override
    public String generateShiftId() {
        return this.generateId();
    }

    @Override
    public void updateAssignements(Shift shift) {
        String key = shift.getId();
        Shift originalShift = this.shiftTable.get(key);
        originalShift.setRecipeStates(shift.getRecipeStates());
        originalShift.setAssignements(shift.getAssignements());

        this.loadShifts();
    }
}
