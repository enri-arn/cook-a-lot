package model.utils;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.util.ArrayList;

public class FilterListModel<E> extends AbstractListModel<E> implements ListDataListener {
    private ListModel<E> baseList;
    private Filter<? super E> filter;

    // filter memory - avoid recomputing when no changes are detected
    private ArrayList<E> filteredObjects;

    public FilterListModel(ListModel<E> baseList, Filter<? super E> filter) {
        this.baseList = baseList;
        this.baseList.addListDataListener(this);
        this.filter = filter;
        this.filteredObjects = new ArrayList<>();
        this.applyFilter();
    }

    private void applyFilter() {
        this.filteredObjects.clear();
        for (int i = 0; i < baseList.getSize(); i++) {
            E el = baseList.getElementAt(i);
            if (filter.filter(el)) this.filteredObjects.add(el);
        }
    }
    @Override
    public int getSize() {
        return filteredObjects.size();
    }

    @Override
    public E getElementAt(int index) {
        return filteredObjects.get(index);
    }

    @Override
    public void intervalAdded(ListDataEvent e) {
        ArrayList<E> old = new ArrayList<>();
        old.addAll(filteredObjects);
        applyFilter();
        int[] indexes = new int[2];
        for (indexes[0] = 0; indexes[0] < filteredObjects.size(); indexes[0]++) {
            if (indexes[0] >= old.size() || filteredObjects.get(indexes[0]) != old.get(indexes[0])) {
                break;
            }
        }
        if (indexes[0]<filteredObjects.size()){
            indexes[1] = indexes[0] + (filteredObjects.size()-old.size()) -1;
            this.fireIntervalAdded(this, indexes[0], indexes[1]);
        }
    }

    @Override
    public void intervalRemoved(ListDataEvent e) {
        ArrayList<E> old = new ArrayList<>();
        old.addAll(filteredObjects);
        applyFilter();
        int[] indexes = new int[2];
        for (indexes[0] = 0; indexes[0] < old.size(); indexes[0]++) {
            if (indexes[0] >= filteredObjects.size() || filteredObjects.get(indexes[0]) != old.get(indexes[0])) {
                break;
            }
        }
        if (indexes[0]<old.size()){
            indexes[1] = indexes[0] + (old.size()-filteredObjects.size()) -1;
            this.fireIntervalRemoved(this, indexes[0], indexes[1]);
        }
    }

    @Override
    public void contentsChanged(ListDataEvent e) {
        ArrayList<E> old = new ArrayList<>();
        old.addAll(filteredObjects);
        applyFilter();
        int[] indexes = new int[2];
        for (indexes[0] = 0; indexes[0] < old.size() && indexes[0] < filteredObjects.size(); indexes[0]++) {
            if (filteredObjects.get(indexes[0]) != old.get(indexes[0])) {
                break;
            }
        }
        if ((filteredObjects.size() != old.size()) || (indexes[0] < filteredObjects.size())){
            int diff = filteredObjects.size()-old.size();
            for (indexes[1] = filteredObjects.size()-1; indexes[1] >= indexes[0]; indexes[1]--) {
                if (filteredObjects.get(indexes[1]) != old.get(indexes[1]-diff)) {
                    break;
                }
            }
            if (indexes[1] < indexes[0]) {
                indexes[1] = indexes[0];
            }
            this.fireContentsChanged(this, indexes[0], indexes[1]);
        }
    }
}
