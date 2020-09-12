package controller.bl_controller;

import model.businesslogic.*;
import model.businesslogic.exception.UseCaseLogicException;
import model.services.CookALotServiceProvider;

public class MenuDefinitionController {
    private static MenuDefinitionController singleInstance = new MenuDefinitionController();
    public static MenuDefinitionController getInstance() {
        return MenuDefinitionController.singleInstance;
    }


    private Menu currentMenu;
    private Section currentSection;

    private MenuDefinitionController() {
    }

    public Menu createMenu(String title) {
        Menu m = new Menu(title);
        CookALotServiceProvider.getInstance().getDataManager().insertMenu(m);
        this.currentMenu = m;
        return this.currentMenu;
    }

    public Menu copyMenu(Menu m) {
        Menu copia = m.clone();
        copia.setComplete(false);
        copia.setTitle("Copia di " + copia.getTitle());
        CookALotServiceProvider.getInstance().getDataManager().insertMenu(copia);
        this.currentMenu = copia;
        return this.currentMenu;
    }

    public Menu workOnMenu(Menu m) {
        if (m.isUsed()) {
            throw new UseCaseLogicException("Non è possibile modificare un menù in uso.");
        }
        m.setComplete(false);
        this.currentMenu = m;
        m.notifyAllReceivers(Menu.Event.DATACHANGED);
        return this.currentMenu;
    }

    public void changeMenuTitle(String title) {
        if (this.currentMenu == null) {
            throw new UseCaseLogicException("Non è possibile modificare il titolo: non è stato indicato il menù su cui operare.");
        }
        if (this.currentMenu.isComplete()) {
            throw new UseCaseLogicException("Non è possibile modificare un menù contrassegnato come 'completo'.");
        }
        this.currentMenu.setTitle(title);
        this.currentMenu.notifyAllReceivers(Menu.Event.DATACHANGED);
    }

    public Section defineSection(String name) {
        if (this.currentMenu == null) {
            throw new UseCaseLogicException("Non è possibile definire una sezione: non è stato indicato il menù su cui operare.");
        }
        if (this.currentMenu.isComplete()) {
            throw new UseCaseLogicException("Non è possibile modificare un menù contrassegnato come 'completo'.");
        }
        // precondizioni verificate
        this.currentSection = this.currentMenu.addSection(name);
        this.currentMenu.notifyAllReceivers(Menu.Event.STRUCTURECHANGED);
        return this.currentSection;
    }

    public void workOnSection(Section sect) {
        if (this.currentMenu == null) {
            throw new UseCaseLogicException("Non è possibile scegliere una sezione: non è stato indicato il menù su cui operare.");
        }
        if (this.currentMenu.isComplete()) {
            throw new UseCaseLogicException("Non è possibile modificare un menù contrassegnato come 'completo'.");
        }
        if (!this.currentMenu.hasSection(sect)) {
            throw new UseCaseLogicException("Non è possibile scegliere una sezione che non appartiene al menù corrente.");
        }

        this.currentSection = sect;
    }

    public void deleteSection(Section sect) {
        if (this.currentMenu == null) {
            throw new UseCaseLogicException("Non è possibile eliminare la sezione: non è stato indicato il menù su cui operare.");
        }
        if (this.currentMenu.isComplete()) {
            throw new UseCaseLogicException("Non è possibile modificare un menù contrassegnato come 'completo'.");
        }
        if (!this.currentMenu.hasSection(sect)) {
            throw new UseCaseLogicException("Non è possibile eliminare una sezione che non appartiene al menù corrente.");
        }

        this.currentMenu.removeSection(sect);
        this.currentSection = null;
        this.currentMenu.notifyAllReceivers(Menu.Event.STRUCTURECHANGED);
    }

    public void changeSectionName(String name) {
        if (this.currentSection == null) {
            throw new UseCaseLogicException("Non è possibile modificare il nome della sezione: non è stata indicata la sezione su cui operare.");
        }
        if (this.currentMenu.isComplete()) {
            throw new UseCaseLogicException("Non è possibile modificare un menù contrassegnato come 'completo'.");
        }

        this.currentSection.setName(name);
        this.currentMenu.notifyAllReceivers(Menu.Event.DATACHANGED);
    }


    public Item insertItem(Dish dish, String description) {
        if (this.currentSection == null) {
            throw new UseCaseLogicException("Non è possibile inserire una voce: non è stata indicata la sezione su cui operare.");
        }
        if (this.currentMenu.isComplete()) {
            throw new UseCaseLogicException("Non è possibile modificare un menù contrassegnato come 'completo'.");
        }
        this.currentSection.addItem(dish, description);
        this.currentMenu.notifyAllReceivers(Menu.Event.STRUCTURECHANGED);
        return this.currentSection.getItem(this.currentSection.getItemCount()-1);

    }

    public Item insertItem(Dish dish) {
        return this.insertItem(dish, dish.getName());
    }

    public void deleteItem(String itemName) {
        if (this.currentSection == null) {
            throw new UseCaseLogicException("Non è possibile inserire una voce: non è stata indicata la sezione su cui operare.");
        }
        if (this.currentMenu.isComplete()) {
            throw new UseCaseLogicException("Non è possibile modificare un menù contrassegnato come 'completo'.");
        }
        if (!this.currentSection.hasItem(itemName)) {
            throw new UseCaseLogicException("Non è possibile eliminare una voce che non appartiene alla sezione corrente.");
        }

        this.currentSection.removeItem(itemName);
        this.currentMenu.notifyAllReceivers(Menu.Event.STRUCTURECHANGED);
    }

    public void deleteItem(Item it) {
        if (this.currentSection == null) {
            throw new UseCaseLogicException("Non è possibile inserire una voce: non è stata indicata la sezione su cui operare.");
        }
        if (this.currentMenu.isComplete()) {
            throw new UseCaseLogicException("Non è possibile modificare un menù contrassegnato come 'completo'.");
        }
        if (!this.currentSection.hasItem(it)) {
            throw new UseCaseLogicException("Non è possibile eliminare una voce che non appartiene alla sezione corrente.");
        }

        this.currentSection.removeItem(it);
        this.currentMenu.notifyAllReceivers(Menu.Event.STRUCTURECHANGED);
    }

    public void modifyItem(Item it, Dish newDish) {
        this.modifyItem(it, newDish, null);
    }

    public void modifyItem(Item it, String newDesc) {
        this.modifyItem(it, null, newDesc);
    }

    public void modifyItem(Item it, Dish newDish, String newDesc) {
        if (this.currentSection == null) {
            throw new UseCaseLogicException("Non è possibile modificare la voce: non è stata indicata la sezione su cui operare.");
        }
        if (this.currentMenu.isComplete()) {
            throw new UseCaseLogicException("Non è possibile modificare un menù contrassegnato come 'completo'.");
        }
        if (!this.currentSection.hasItem(it)) {
            throw new UseCaseLogicException("Non è possibile modificare una voce che non appartiene alla sezione corrente.");
        }

        if (newDesc != null) {
            it.setDescription(newDesc);
        }

        if (newDish != null) {
            it.setDish(newDish);
        }

        this.currentMenu.notifyAllReceivers(Menu.Event.DATACHANGED);
    }

    public boolean setMenuAsComplete() {
        if (this.currentMenu == null) {
            throw new UseCaseLogicException("Non è possibile impostare il menù come completo: non è stato indicato il menù su cui operare.");
        }
        if (this.currentMenu.isComplete()) {
            throw new UseCaseLogicException("Non è possibile impostare il menù come 'completo' perché lo è già.");
        }
        if (!this.currentMenu.canBeComplete()) {
            throw new UseCaseLogicException("Non è possibile impostare il menù come 'completo' perché non ha una struttura ben formata.");
        }

        this.currentMenu.setComplete(true);

        this.currentMenu.notifyAllReceivers(Menu.Event.DATACHANGED);

        return this.currentMenu.isComplete();
    }

    public void deleteMenu() {
        if (this.currentMenu == null) {
            throw new UseCaseLogicException("Non è possibile eliminare il menù: non è stato indicato il menù su cui operare.");
        }
        if (this.currentMenu.isComplete()) {
            throw new UseCaseLogicException("Non è possibile eliminare un menù contrassegnato come completo.");
        }
        this.currentMenu.delete();
        this.currentMenu.notifyAllReceivers(Menu.Event.DELETED);
        this.currentMenu = null;
    }

    public void reset() {
        currentMenu = null;
        currentSection = null;
    }

    public Menu getCurrentMenu() {
        return currentMenu;
    }

    public Section getCurrentSection() {
        return currentSection;
    }
}
