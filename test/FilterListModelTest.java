import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.utils.FilterListModel;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import static org.junit.jupiter.api.Assertions.*;

class FilterListModelTest {

    private FilterListModel<String> testSubject;
    private DefaultListModel<String> baseList;
    private boolean invoked;

    @BeforeEach
    void setUp() {
        baseList = new DefaultListModel<>();
        baseList.addElement("Abracadabra");
        baseList.addElement("1Boogie");
        baseList.addElement("Putipù");
        baseList.addElement("2Pincopanco");
        baseList.addElement("Wlamamma");
        baseList.addElement("7Nani");
        testSubject = new FilterListModel<>(baseList, s -> Character.isDigit(s.charAt(0)));
        invoked = false;
    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void base() {
        assertEquals(3, testSubject.getSize());
        assertEquals("1Boogie", testSubject.getElementAt(0));
        assertEquals("2Pincopanco", testSubject.getElementAt(1));
        assertEquals("7Nani", testSubject.getElementAt(2));
    }


    @Test
    void addNotFiltered() {
        testSubject.addListDataListener(new ListDataListener() {
            @Override
            public void intervalAdded(ListDataEvent e) {
                invoked = true;
                fail("intervalAdded should not be called here");
            }

            @Override
            public void intervalRemoved(ListDataEvent e) {

            }

            @Override
            public void contentsChanged(ListDataEvent e) {

            }
        });
        baseList.addElement("Ciccia");
        assertEquals(false, invoked);
    }

    @Test
    void addFiltered() {
        testSubject.addListDataListener(new ListDataListener() {
            @Override
            public void intervalAdded(ListDataEvent e) {
                invoked = true;
                assertEquals(ListDataEvent.INTERVAL_ADDED, e.getType());
                assertEquals(3, e.getIndex0());
                assertEquals(3, e.getIndex1());
            }

            @Override
            public void intervalRemoved(ListDataEvent e) {

            }

            @Override
            public void contentsChanged(ListDataEvent e) {

            }
        });
        baseList.addElement("3Ciccia");
        assertEquals(true, invoked);
    }

    @Test
    void addIndexNotFiltered() {
        testSubject.addListDataListener(new ListDataListener() {
            @Override
            public void intervalAdded(ListDataEvent e) {
                invoked = true;
                fail("intervalAdded should not be called here");
            }

            @Override
            public void intervalRemoved(ListDataEvent e) {

            }

            @Override
            public void contentsChanged(ListDataEvent e) {

            }
        });
        baseList.add(2, "Ciccia");
        assertEquals(false, invoked);
    }

    @Test
    void addIndexFiltered() {
        testSubject.addListDataListener(new ListDataListener() {
            @Override
            public void intervalAdded(ListDataEvent e) {
                invoked = true;
                assertEquals(ListDataEvent.INTERVAL_ADDED, e.getType());
                assertEquals(1, e.getIndex0());
                assertEquals(1, e.getIndex1());
            }

            @Override
            public void intervalRemoved(ListDataEvent e) {

            }

            @Override
            public void contentsChanged(ListDataEvent e) {

            }
        });
        baseList.add(2, "3Ciccia");
        assertEquals(true, invoked);
    }

    @Test
    void insertElementNotFiltered() {
        testSubject.addListDataListener(new ListDataListener() {
            @Override
            public void intervalAdded(ListDataEvent e) {
                invoked = true;
                fail("intervalAdded should not be called here");
            }

            @Override
            public void intervalRemoved(ListDataEvent e) {

            }

            @Override
            public void contentsChanged(ListDataEvent e) {

            }
        });
        baseList.insertElementAt("Ciccia", 2);
        assertEquals(false, invoked);
    }

    @Test
    void insertElementFiltered() {
        testSubject.addListDataListener(new ListDataListener() {
            @Override
            public void intervalAdded(ListDataEvent e) {
                invoked = true;
                assertEquals(ListDataEvent.INTERVAL_ADDED, e.getType());
                assertEquals(1, e.getIndex0());
                assertEquals(1, e.getIndex1());
            }

            @Override
            public void intervalRemoved(ListDataEvent e) {

            }

            @Override
            public void contentsChanged(ListDataEvent e) {

            }
        });
        baseList.insertElementAt("3Ciccia", 2);
        assertEquals(true, invoked);
    }

    @Test
    void removeNotFiltered1() {
        testSubject.addListDataListener(new ListDataListener() {
            @Override
            public void intervalAdded(ListDataEvent e) {
            }

            @Override
            public void intervalRemoved(ListDataEvent e) {
                invoked = true;
                fail("intervalRemoved should not be called here");
            }

            @Override
            public void contentsChanged(ListDataEvent e) {

            }
        });
        baseList.remove(0);
        assertEquals(false, invoked);
    }

    @Test
    void removeNotFiltered2() {
        testSubject.addListDataListener(new ListDataListener() {
            @Override
            public void intervalAdded(ListDataEvent e) {
            }

            @Override
            public void intervalRemoved(ListDataEvent e) {
                invoked = true;
                fail("intervalRemoved should not be called here");
            }

            @Override
            public void contentsChanged(ListDataEvent e) {

            }
        });
        baseList.remove(2);
        assertEquals(false, invoked);
    }

    @Test
    void removeNotFiltered3() {
        testSubject.addListDataListener(new ListDataListener() {
            @Override
            public void intervalAdded(ListDataEvent e) {
            }

            @Override
            public void intervalRemoved(ListDataEvent e) {
                invoked = true;
                fail("intervalRemoved should not be called here");
            }

            @Override
            public void contentsChanged(ListDataEvent e) {

            }
        });
        baseList.remove(4);
        assertEquals(false, invoked);
    }

    @Test
    void removeFiltered1() {
        testSubject.addListDataListener(new ListDataListener() {
            @Override
            public void intervalAdded(ListDataEvent e) {
            }

            @Override
            public void intervalRemoved(ListDataEvent e) {
                invoked = true;
                assertEquals(ListDataEvent.INTERVAL_REMOVED, e.getType());
                assertEquals(0, e.getIndex0());
                assertEquals(0, e.getIndex1());
            }

            @Override
            public void contentsChanged(ListDataEvent e) {

            }
        });
        baseList.remove(1);
        assertEquals(true, invoked);
    }

    @Test
    void removeFiltered2() {
        testSubject.addListDataListener(new ListDataListener() {
            @Override
            public void intervalAdded(ListDataEvent e) {
            }

            @Override
            public void intervalRemoved(ListDataEvent e) {
                invoked = true;
                assertEquals(ListDataEvent.INTERVAL_REMOVED, e.getType());
                assertEquals(1, e.getIndex0());
                assertEquals(1, e.getIndex1());
            }

            @Override
            public void contentsChanged(ListDataEvent e) {

            }
        });
        baseList.remove(3);
        assertEquals(true, invoked);
    }

    @Test
    void removeFiltered3() {
        testSubject.addListDataListener(new ListDataListener() {
            @Override
            public void intervalAdded(ListDataEvent e) {
            }

            @Override
            public void intervalRemoved(ListDataEvent e) {
                invoked = true;
                assertEquals(ListDataEvent.INTERVAL_REMOVED, e.getType());
                assertEquals(2, e.getIndex0());
                assertEquals(2, e.getIndex1());
            }

            @Override
            public void contentsChanged(ListDataEvent e) {

            }
        });
        baseList.remove(5);
        assertEquals(true, invoked);
    }

    @Test
    void removeElAtNotFiltered1() {
        testSubject.addListDataListener(new ListDataListener() {
            @Override
            public void intervalAdded(ListDataEvent e) {
            }

            @Override
            public void intervalRemoved(ListDataEvent e) {
                invoked = true;
                fail("intervalRemoved should not be called here");
            }

            @Override
            public void contentsChanged(ListDataEvent e) {

            }
        });
        baseList.removeElementAt(0);
        assertEquals(false, invoked);
    }

    @Test
    void removeElAtNotFiltered2() {
        testSubject.addListDataListener(new ListDataListener() {
            @Override
            public void intervalAdded(ListDataEvent e) {
            }

            @Override
            public void intervalRemoved(ListDataEvent e) {
                invoked = true;
                fail("intervalRemoved should not be called here");
            }

            @Override
            public void contentsChanged(ListDataEvent e) {

            }
        });
        baseList.removeElementAt(2);
        assertEquals(false, invoked);
    }

    @Test
    void removeElAtNotFiltered3() {
        testSubject.addListDataListener(new ListDataListener() {
            @Override
            public void intervalAdded(ListDataEvent e) {
            }

            @Override
            public void intervalRemoved(ListDataEvent e) {
                invoked = true;
                fail("intervalRemoved should not be called here");
            }

            @Override
            public void contentsChanged(ListDataEvent e) {

            }
        });
        baseList.removeElementAt(4);
        assertEquals(false, invoked);
    }

    @Test
    void removeElAtFiltered1() {
        testSubject.addListDataListener(new ListDataListener() {
            @Override
            public void intervalAdded(ListDataEvent e) {
            }

            @Override
            public void intervalRemoved(ListDataEvent e) {
                invoked = true;
                assertEquals(ListDataEvent.INTERVAL_REMOVED, e.getType());
                assertEquals(0, e.getIndex0());
                assertEquals(0, e.getIndex1());
            }

            @Override
            public void contentsChanged(ListDataEvent e) {

            }
        });
        baseList.removeElementAt(1);
        assertEquals(true, invoked);
    }

    @Test
    void removeElAtFiltered2() {
        testSubject.addListDataListener(new ListDataListener() {
            @Override
            public void intervalAdded(ListDataEvent e) {
            }

            @Override
            public void intervalRemoved(ListDataEvent e) {
                invoked = true;
                assertEquals(ListDataEvent.INTERVAL_REMOVED, e.getType());
                assertEquals(1, e.getIndex0());
                assertEquals(1, e.getIndex1());
            }

            @Override
            public void contentsChanged(ListDataEvent e) {

            }
        });
        baseList.removeElementAt(3);
        assertEquals(true, invoked);
    }

    @Test
    void removeElAtFiltered3() {
        testSubject.addListDataListener(new ListDataListener() {
            @Override
            public void intervalAdded(ListDataEvent e) {
            }

            @Override
            public void intervalRemoved(ListDataEvent e) {
                invoked = true;
                assertEquals(ListDataEvent.INTERVAL_REMOVED, e.getType());
                assertEquals(2, e.getIndex0());
                assertEquals(2, e.getIndex1());
            }

            @Override
            public void contentsChanged(ListDataEvent e) {

            }
        });
        baseList.removeElementAt(5);
        assertEquals(true, invoked);
    }

    @Test
    void removeElementNotFiltered1() {
        testSubject.addListDataListener(new ListDataListener() {
            @Override
            public void intervalAdded(ListDataEvent e) {
            }

            @Override
            public void intervalRemoved(ListDataEvent e) {
                invoked = true;
                fail("intervalRemoved should not be called here");
            }

            @Override
            public void contentsChanged(ListDataEvent e) {

            }
        });
        baseList.removeElement(baseList.get(0));
        assertEquals(false, invoked);
    }

    @Test
    void removeElementNotFiltered2() {
        testSubject.addListDataListener(new ListDataListener() {
            @Override
            public void intervalAdded(ListDataEvent e) {
            }

            @Override
            public void intervalRemoved(ListDataEvent e) {
                invoked = true;
                fail("intervalRemoved should not be called here");
            }

            @Override
            public void contentsChanged(ListDataEvent e) {

            }
        });
        baseList.removeElement(baseList.get(2));
        assertEquals(false, invoked);
    }

    @Test
    void removeElementNotFiltered3() {
        testSubject.addListDataListener(new ListDataListener() {
            @Override
            public void intervalAdded(ListDataEvent e) {
            }

            @Override
            public void intervalRemoved(ListDataEvent e) {
                invoked = true;
                fail("intervalRemoved should not be called here");
            }

            @Override
            public void contentsChanged(ListDataEvent e) {

            }
        });
        baseList.removeElement(baseList.get(4));
        assertEquals(false, invoked);
    }

    @Test
    void removeElementFiltered1() {
        testSubject.addListDataListener(new ListDataListener() {
            @Override
            public void intervalAdded(ListDataEvent e) {
            }

            @Override
            public void intervalRemoved(ListDataEvent e) {
                invoked = true;
                assertEquals(ListDataEvent.INTERVAL_REMOVED, e.getType());
                assertEquals(0, e.getIndex0());
                assertEquals(0, e.getIndex1());
            }

            @Override
            public void contentsChanged(ListDataEvent e) {

            }
        });
        baseList.removeElement(baseList.get(1));
        assertEquals(true, invoked);
    }

    @Test
    void removeElementFiltered2() {
        testSubject.addListDataListener(new ListDataListener() {
            @Override
            public void intervalAdded(ListDataEvent e) {
            }

            @Override
            public void intervalRemoved(ListDataEvent e) {
                invoked = true;
                assertEquals(ListDataEvent.INTERVAL_REMOVED, e.getType());
                assertEquals(1, e.getIndex0());
                assertEquals(1, e.getIndex1());
            }

            @Override
            public void contentsChanged(ListDataEvent e) {

            }
        });
        baseList.removeElement(baseList.get(3));
        assertEquals(true, invoked);
    }


    @Test
    void removeRangeNotFiltered1() {
        baseList.remove(1);
        testSubject.addListDataListener(new ListDataListener() {
            @Override
            public void intervalAdded(ListDataEvent e) {
            }

            @Override
            public void intervalRemoved(ListDataEvent e) {
                invoked = true;
                fail("intervalRemoved should not be called here");
            }

            @Override
            public void contentsChanged(ListDataEvent e) {

            }
        });
        baseList.removeRange(0,1);
        assertEquals(false, invoked);
    }

    @Test
    void removeRangeNotFiltered2() {
        baseList.remove(3);
        testSubject.addListDataListener(new ListDataListener() {
            @Override
            public void intervalAdded(ListDataEvent e) {
            }

            @Override
            public void intervalRemoved(ListDataEvent e) {
                invoked = true;
                fail("intervalRemoved should not be called here");
            }

            @Override
            public void contentsChanged(ListDataEvent e) {

            }
        });
        baseList.removeRange(2,3);
        assertEquals(false, invoked);
    }

    @Test
    void removeRangeNotFiltered3() {
        baseList.remove(1);
        baseList.remove(2);
        testSubject.addListDataListener(new ListDataListener() {
            @Override
            public void intervalAdded(ListDataEvent e) {
            }

            @Override
            public void intervalRemoved(ListDataEvent e) {
                invoked = true;
                fail("intervalRemoved should not be called here");
            }

            @Override
            public void contentsChanged(ListDataEvent e) {

            }
        });
        baseList.removeRange(0,2);
        assertEquals(false, invoked);
    }

    @Test
    void removeRangeFiltered1() {
        testSubject.addListDataListener(new ListDataListener() {
            @Override
            public void intervalAdded(ListDataEvent e) {
            }

            @Override
            public void intervalRemoved(ListDataEvent e) {
                invoked = true;
                assertEquals(ListDataEvent.INTERVAL_REMOVED, e.getType());
                assertEquals(0, e.getIndex0());
                assertEquals(0, e.getIndex1());            }

            @Override
            public void contentsChanged(ListDataEvent e) {

            }
        });
        baseList.removeRange(0,2);
        assertEquals(true, invoked);
    }

    @Test
    void removeRangeFiltered2() {
        testSubject.addListDataListener(new ListDataListener() {
            @Override
            public void intervalAdded(ListDataEvent e) {
            }

            @Override
            public void intervalRemoved(ListDataEvent e) {
                invoked = true;
                assertEquals(ListDataEvent.INTERVAL_REMOVED, e.getType());
                assertEquals(0, e.getIndex0());
                assertEquals(1, e.getIndex1());            }

            @Override
            public void contentsChanged(ListDataEvent e) {

            }
        });
        baseList.removeRange(0,3);
        assertEquals(true, invoked);
    }

    @Test
    void removeRangeFiltered3() {
        testSubject.addListDataListener(new ListDataListener() {
            @Override
            public void intervalAdded(ListDataEvent e) {
            }

            @Override
            public void intervalRemoved(ListDataEvent e) {
                invoked = true;
                assertEquals(ListDataEvent.INTERVAL_REMOVED, e.getType());
                assertEquals(1, e.getIndex0());
                assertEquals(2, e.getIndex1());            }

            @Override
            public void contentsChanged(ListDataEvent e) {

            }
        });
        baseList.removeRange(2,5);
        assertEquals(true, invoked);
    }

    @Test
    void removeAllNotFiltered() {
        baseList.remove(1);
        baseList.remove(2);
        baseList.remove(3);
        testSubject.addListDataListener(new ListDataListener() {
            @Override
            public void intervalAdded(ListDataEvent e) {
            }

            @Override
            public void intervalRemoved(ListDataEvent e) {
                invoked = true;
                fail("intervalRemoved should not be called here");
          }

            @Override
            public void contentsChanged(ListDataEvent e) {

            }
        });
        baseList.removeAllElements();
        assertEquals(false, invoked);
    }

    @Test
    void removeAllFiltered() {
        testSubject.addListDataListener(new ListDataListener() {
            @Override
            public void intervalAdded(ListDataEvent e) {
            }

            @Override
            public void intervalRemoved(ListDataEvent e) {
                invoked = true;
                assertEquals(ListDataEvent.INTERVAL_REMOVED, e.getType());
                assertEquals(0, e.getIndex0());
                assertEquals(2, e.getIndex1());            }

            @Override
            public void contentsChanged(ListDataEvent e) {

            }
        });
        baseList.removeAllElements();
        assertEquals(true, invoked);
    }

    @Test
    void setNotFiltered() {
        testSubject.addListDataListener(new ListDataListener() {
            @Override
            public void intervalAdded(ListDataEvent e) {

            }

            @Override
            public void intervalRemoved(ListDataEvent e) {

            }

            @Override
            public void contentsChanged(ListDataEvent e) {
                invoked = true;
                fail("contentsChanged should not be called here");
            }
        });
        baseList.set(2, "Ciccia");
        assertEquals(false, invoked);
    }

    @Test
    void setFiltered1() {
        testSubject.addListDataListener(new ListDataListener() {
            @Override
            public void intervalAdded(ListDataEvent e) {

            }

            @Override
            public void intervalRemoved(ListDataEvent e) {

            }

            @Override
            public void contentsChanged(ListDataEvent e) {
                invoked = true;
                assertEquals(ListDataEvent.CONTENTS_CHANGED, e.getType());
                assertEquals(1, e.getIndex0());
                assertEquals(1, e.getIndex1());
            }
        });
        baseList.set(2, "3Ciccia");
        assertEquals(true, invoked);
    }

    @Test
    void setFiltered2() {
        testSubject.addListDataListener(new ListDataListener() {
            @Override
            public void intervalAdded(ListDataEvent e) {

            }

            @Override
            public void intervalRemoved(ListDataEvent e) {

            }

            @Override
            public void contentsChanged(ListDataEvent e) {
                invoked = true;
                assertEquals(ListDataEvent.CONTENTS_CHANGED, e.getType());
                assertEquals(1, e.getIndex0());
                assertEquals(1, e.getIndex1());
            }
        });
        baseList.set(3, "3Ciccia");
        assertEquals(true, invoked);
    }

    @Test
    void setFiltered3() {
        testSubject.addListDataListener(new ListDataListener() {
            @Override
            public void intervalAdded(ListDataEvent e) {

            }

            @Override
            public void intervalRemoved(ListDataEvent e) {

            }

            @Override
            public void contentsChanged(ListDataEvent e) {
                invoked = true;
                assertEquals(ListDataEvent.CONTENTS_CHANGED, e.getType());
                assertEquals(1, e.getIndex0());
                assertEquals(1, e.getIndex1());
            }
        });
        baseList.set(3, "Ciccia");
        assertEquals(true, invoked);
    }

    @Test
    void setElementNotFiltered() {
        testSubject.addListDataListener(new ListDataListener() {
            @Override
            public void intervalAdded(ListDataEvent e) {

            }

            @Override
            public void intervalRemoved(ListDataEvent e) {

            }

            @Override
            public void contentsChanged(ListDataEvent e) {
                invoked = true;
                fail("contentsChanged should not be called here");
            }
        });
        baseList.setElementAt("Ciccia", 2);
        assertEquals(false, invoked);
    }

    @Test
    void setElementFiltered1() {
        testSubject.addListDataListener(new ListDataListener() {
            @Override
            public void intervalAdded(ListDataEvent e) {

            }

            @Override
            public void intervalRemoved(ListDataEvent e) {

            }

            @Override
            public void contentsChanged(ListDataEvent e) {
                invoked = true;
                assertEquals(ListDataEvent.CONTENTS_CHANGED, e.getType());
                assertEquals(1, e.getIndex0());
                assertEquals(1, e.getIndex1());
            }
        });
        baseList.setElementAt("3Ciccia",2);
        assertEquals(true, invoked);
    }

    @Test
    void setElementFiltered2() {
        testSubject.addListDataListener(new ListDataListener() {
            @Override
            public void intervalAdded(ListDataEvent e) {

            }

            @Override
            public void intervalRemoved(ListDataEvent e) {

            }

            @Override
            public void contentsChanged(ListDataEvent e) {
                invoked = true;
                assertEquals(ListDataEvent.CONTENTS_CHANGED, e.getType());
                assertEquals(1, e.getIndex0());
                assertEquals(1, e.getIndex1());
            }
        });
        baseList.setElementAt("3Ciccia",3);
        assertEquals(true, invoked);
    }

    @Test
    void setElementFiltered3() {
        testSubject.addListDataListener(new ListDataListener() {
            @Override
            public void intervalAdded(ListDataEvent e) {

            }

            @Override
            public void intervalRemoved(ListDataEvent e) {

            }

            @Override
            public void contentsChanged(ListDataEvent e) {
                invoked = true;
                assertEquals(ListDataEvent.CONTENTS_CHANGED, e.getType());
                assertEquals(1, e.getIndex0());
                assertEquals(1, e.getIndex1());
            }
        });
        baseList.setElementAt("Ciccia", 3);
        assertEquals(true, invoked);
    }
}