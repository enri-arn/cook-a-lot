package model.businesslogic;

import model.services.CookALotServiceProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Picardi Claudia
 */
public class Section implements Cloneable {

    private String id;
    private String name;
    private ArrayList<Item> items;

    public Section(String name) {
        this.setName(name);
        items = new ArrayList<>();
        this.id = CookALotServiceProvider.getInstance().getDataManager().generateSectionId();
    }

    public Section(String id, String name, Item[] its) {
        this.id = id;
        this.name = name;
        this.items = new ArrayList<>();
        Collections.addAll(items, its);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addItem(Dish dish, String description) {
        Item it = new Item(description, dish);
        this.items.add(it);
    }

    public void removeItem(Item it) {
        this.items.remove(it);
    }

    public void removeItem(String itemDesc) {
        Item toRemove = null;
        for (Item it : items) {
            if (it.getDescription().equals(itemDesc)) {
                toRemove = it;
                break;
            }
        }

        if (toRemove != null) {
            items.remove(toRemove);
        }
    }

    public Section clone() {
        Section sec;
        try {
            sec = (Section) super.clone();
            sec.id = CookALotServiceProvider.getInstance().getDataManager().generateSectionId();
            sec.items = new ArrayList<>();
            for (Item it : items) {
                sec.items.add(it.clone());
            }
            return sec;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean hasItem(Item it) {
        for (Item i : items) {
            if (i == it) return true;
        }
        return false;
    }

    public boolean hasItem(String itemName) {
        for (Item it : items) {
            if (it.getDescription().equals(itemName)) return true;
        }
        return false;
    }

    public int getItemCount() {
        return this.items.size();
    }

    public Item getItem(int n) {
        if (n < 0 || n >= items.size()) {
            throw new IllegalArgumentException("La sezione " + name + " ha " + items.size() + " voci. Valore non valido: " + n);
        }
        return this.items.get(n);
    }

    public List<Item> getItems() {
        return this.items;
    }

    public String toString() {
        return this.getName();
    }

}
