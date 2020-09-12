package model.businesslogic;

import model.services.CookALotServiceProvider;
import model.utils.EventSource;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Picardi Claudia.
 */
public class Menu extends EventSource implements Cloneable {

    public enum Event {DATACHANGED, STRUCTURECHANGED, DELETED}

    private String id;
    private String title;
    private boolean complete;
    private boolean deleted;
    private boolean used;

    private ArrayList<Section> sections;

    public Menu(String title) {
        setTitle(title);
        this.complete = false;
        this.sections = new ArrayList<>();
        this.id = CookALotServiceProvider.getInstance().getDataManager().generateMenuId();
        this.used = false;
    }

    public Menu(String id, String title, boolean complete, boolean used, Section[] sects) {
        this.id = id;
        this.title = title;
        this.complete = complete;
        this.used = used;
        this.sections = new ArrayList<>();
        this.sections.addAll(Arrays.asList(sects));
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public boolean isUsed() {
        return used;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = this.used || (complete && this.canBeComplete());
    }

    public Section addSection(String name) {
        Section sect = new Section(name);
        this.sections.add(sect);
        return sect;
    }

    public boolean hasSection(Section s) {
        for (Section sect : sections) {
            if (sect == s) return true;
        }
        return false;
    }

    public void removeSection(Section sect) {
        boolean removed = this.sections.remove(sect);
        if (!removed) {
            throw new IllegalArgumentException("Section does not belong to Menu. Cannot remove.");
        }
    }

    public boolean canBeComplete() {
        if (this.sections.size() == 0) return false;
        for (Section sec : sections) {
            if (sec.getItemCount() == 0) return false;
        }
        return true;
    }

    public Menu clone() {
        Menu m;
        try {
            m = (Menu) super.clone();
            m.id = CookALotServiceProvider.getInstance().getDataManager().generateMenuId();
            m.used = false;
            m.sections = new ArrayList<>();
            for (Section s : sections) {
                m.sections.add(s.clone());
            }
            return m;

        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void delete() {
        this.deleted = true;
    }

    public int getSectionCount() {
        return this.sections.size();
    }

    public Section getSection(int n) {
        if (n < 0 || n >= sections.size()) {
            throw new IllegalArgumentException("Il menu " + title + " ha " + sections.size() + " sezioni. Valore non valido: " + n);
        }
        return this.sections.get(n);
    }

    public ArrayList<Section> getSections() {
        return sections;
    }

    public String toString() {
        return this.getTitle() + (complete ? (used ? " - (Completo, In uso)" : " - (Completo, Non in uso)") : " - (Incompleto)");
    }
}
