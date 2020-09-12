package view.gui;

import model.businesslogic.Item;
import model.businesslogic.Menu;
import controller.bl_controller.MenuDefinitionController;
import model.businesslogic.Section;
import model.services.CookALotServiceProvider;
import model.utils.EventReceiver;
import model.utils.EventSource;

import javax.swing.*;
import java.awt.*;

public class MenuDefinitionSampleGUI {

    public class MenuEventReceiver implements EventReceiver {

        @Override
        public void update(EventSource o, Object arg) {
            if (o instanceof Menu) {
                Menu.Event ev = (Menu.Event) arg;
                switch (ev) {
                    case DATACHANGED:
                        updateMenuDetailsPanel();
                        break;
                    case STRUCTURECHANGED:
                        updateMenuDetailsPanel();
                        break;
                    case DELETED:
                        clearMenuDetailsPanel();
                        break;
                }
            }
        }
    }
    private static String CARD_MENUSELECTION = "menuSelection";
    private static String CARD_MENUDETAILS = "menuDetails";
    private JPanel mainPanel;
    private JPanel menuSelectionPanel;
    private JPanel menuDetailsPanel;
    private JList<Menu> menuList;
    private JScrollPane menuScroll;
    private JButton createMenuButton;
    private JButton copyMenuButton;
    private JButton editMenuButton;
    private JButton rinominaMenuButton;
    private JLabel menuTitle;
    private JList<Section> sectionsList;
    private DefaultListModel<Section> sectionsLM;
    private JButton aggiungiSezButton;
    private JButton eliminaSezButton;
    private JButton rinominaSezButton;
    private JList<Item> itemsList;
    private DefaultListModel<Item> itemsLM;
    private JButton aggiungiVoceButton;
    private JButton eliminaVoceButton;
    private JButton modificaVoceButton;
    private JButton eliminaMenuButton;
    private JButton completoButton;
    private JButton esciButton;
    private ShortTextDialog menuTitleDialog;
    private ShortTextDialog sectionNameDialog;
    private EditItemDialog editItemDialog;
    private Menu visualizedMenu;
    private MenuEventReceiver menuEventReceiver;


    public MenuDefinitionSampleGUI() {
        menuList.setModel(CookALotServiceProvider.getInstance().getDataManager().getAllMenus());
        menuList.setSelectedIndex(-1);
        copyMenuButton.setEnabled(false);
        editMenuButton.setEnabled(false);
        sectionsLM = new DefaultListModel<>();
        sectionsList.setModel(sectionsLM);
        itemsLM = new DefaultListModel<>();
        itemsList.setModel(itemsLM);
        menuTitleDialog = new ShortTextDialog("Titolo del menù:");
        sectionNameDialog = new ShortTextDialog("Nome della sezione:");
        menuEventReceiver = new MenuEventReceiver();
        editItemDialog = new EditItemDialog();

        menuList.addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;
            copyMenuButton.setEnabled(menuList.getSelectedIndex() >= 0);
            editMenuButton.setEnabled((menuList.getSelectedIndex() >= 0) && !menuList.getSelectedValue().isUsed());
        });
        createMenuButton.addActionListener(e -> {
            menuTitleDialog.setInitialText("");
            menuTitleDialog.pack();
            menuTitleDialog.setVisible(true);
            if (menuTitleDialog.getValue() == JOptionPane.OK_OPTION) {
                String title = menuTitleDialog.getText();
                Menu m = MenuDefinitionController.getInstance().createMenu(title);
                m.addReceiver(menuEventReceiver);
                setupMenuDetailsPanel(m);
                m.addReceiver(menuEventReceiver);
                ((CardLayout) mainPanel.getLayout()).show(mainPanel, MenuDefinitionSampleGUI.CARD_MENUDETAILS);

            }
        });
        copyMenuButton.addActionListener(e -> {
            Menu toCopy = menuList.getSelectedValue();
            Menu m = MenuDefinitionController.getInstance().copyMenu(toCopy);
            setupMenuDetailsPanel(m);
            m.addReceiver(menuEventReceiver);
            ((CardLayout) mainPanel.getLayout()).show(mainPanel, MenuDefinitionSampleGUI.CARD_MENUDETAILS);
        });
        editMenuButton.addActionListener(e -> {
            Menu m = MenuDefinitionController.getInstance().workOnMenu(menuList.getSelectedValue());
            setupMenuDetailsPanel(m);
            m.addReceiver(menuEventReceiver);
            ((CardLayout) mainPanel.getLayout()).show(mainPanel, MenuDefinitionSampleGUI.CARD_MENUDETAILS);
        });
        esciButton.addActionListener(e -> {
            MenuDefinitionController.getInstance().reset();
            visualizedMenu.removeReceiver(menuEventReceiver);
            ((CardLayout) mainPanel.getLayout()).show(mainPanel, MenuDefinitionSampleGUI.CARD_MENUSELECTION);
            clearMenuDetailsPanel();
        });
        rinominaMenuButton.addActionListener(e -> {
            menuTitleDialog.setInitialText(visualizedMenu.getTitle());
            menuTitleDialog.setVisible(true);
            if (menuTitleDialog.getValue() == JOptionPane.OK_OPTION) {
                String title = menuTitleDialog.getText();
                MenuDefinitionController.getInstance().changeMenuTitle(title);
            }
        });
        eliminaMenuButton.addActionListener(e -> {
            Menu beingDeleted = visualizedMenu;
            MenuDefinitionController.getInstance().deleteMenu();
            beingDeleted.removeReceiver(menuEventReceiver);
            ((CardLayout) mainPanel.getLayout()).show(mainPanel, MenuDefinitionSampleGUI.CARD_MENUSELECTION);
        });
        completoButton.addActionListener(e -> {
            boolean ok = MenuDefinitionController.getInstance().setMenuAsComplete();
            if (!ok) {
                JOptionPane.showMessageDialog(mainPanel, "Questo menu non può essere 'completo'.");
            } else {
                MenuDefinitionController.getInstance().reset();
                visualizedMenu.removeReceiver(menuEventReceiver);
                ((CardLayout) mainPanel.getLayout()).show(mainPanel, MenuDefinitionSampleGUI.CARD_MENUSELECTION);
                clearMenuDetailsPanel();
            }
        });

        sectionsList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Section selSec = sectionsList.getSelectedValue();
                this.itemsLM.removeAllElements();
                boolean selected = selSec != null;
                if (selected) {
                    MenuDefinitionController.getInstance().workOnSection(selSec);
                    for (int i = 0; i < selSec.getItemCount(); i++) {
                        itemsLM.addElement(selSec.getItem(i));
                    }
                }
                eliminaSezButton.setEnabled(selected);
                rinominaSezButton.setEnabled(selected);
                aggiungiVoceButton.setEnabled(selected);
            }
        });
        rinominaSezButton.addActionListener(e -> {
            sectionNameDialog.setInitialText(MenuDefinitionController.getInstance().getCurrentSection().getName());
            sectionNameDialog.pack();
            sectionNameDialog.setVisible(true);
            if (menuTitleDialog.getValue() == JOptionPane.OK_OPTION) {
                String name = sectionNameDialog.getText();
                MenuDefinitionController.getInstance().changeSectionName(name);
            }
        });
        aggiungiSezButton.addActionListener(e -> {
            sectionNameDialog.setInitialText("");
            sectionNameDialog.pack();
            sectionNameDialog.setVisible(true);
            if (menuTitleDialog.getValue() == JOptionPane.OK_OPTION) {
                String name = sectionNameDialog.getText();
                MenuDefinitionController.getInstance().defineSection(name);
                sectionsList.setSelectedValue(MenuDefinitionController.getInstance().getCurrentSection(), true);
            }
        });
        eliminaSezButton.addActionListener(e -> {
            int index = sectionsList.getSelectedIndex();
            Section sec = sectionsList.getSelectedValue();
            MenuDefinitionController.getInstance().deleteSection(sec);
            if (index < sectionsLM.getSize()) {
                sectionsList.setSelectedIndex(index);
            } else if (index > 0) {
                sectionsList.setSelectedIndex(index-1);
            }
        });
        itemsList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Item selIt = itemsList.getSelectedValue();
                boolean selected = selIt != null;
                eliminaVoceButton.setEnabled(selected);
                modificaVoceButton.setEnabled(selected);
            }
        });
        aggiungiVoceButton.addActionListener(e -> {
            editItemDialog.initialize(null);
            editItemDialog.pack();
            editItemDialog.setVisible(true);
            if (editItemDialog.getValue() == JOptionPane.OK_OPTION) {
                this.sectionsList.setSelectedValue(MenuDefinitionController.getInstance().getCurrentSection(), true);
                this.itemsList.setSelectedValue(editItemDialog.getItem(), true);
            }
        });

        eliminaVoceButton.addActionListener(e -> {
            int index = itemsList.getSelectedIndex();
            Item it = itemsList.getSelectedValue();
            MenuDefinitionController.getInstance().deleteItem(it);
            this.sectionsList.setSelectedValue(MenuDefinitionController.getInstance().getCurrentSection(), true);
            if (index < itemsLM.getSize()) {
                itemsList.setSelectedIndex(index);
            } else if (index > 0) {
                itemsList.setSelectedIndex(index-1);
            }
        });
        modificaVoceButton.addActionListener(e -> {
            editItemDialog.initialize(itemsList.getSelectedValue());
            editItemDialog.pack();
            editItemDialog.setVisible(true);
            if (editItemDialog.getValue() == JOptionPane.OK_OPTION) {
                this.sectionsList.setSelectedValue(MenuDefinitionController.getInstance().getCurrentSection(), true);
                this.itemsList.setSelectedValue(editItemDialog.getItem(), true);
            }
        });
    }

    public static void main(String[] args) {
        JFrame theFrame = new JFrame("MenuDefinitionSampleGUI");
        MenuDefinitionSampleGUI currentInstance = new MenuDefinitionSampleGUI();
        ((CardLayout) currentInstance.mainPanel.getLayout()).show(currentInstance.mainPanel, MenuDefinitionSampleGUI.CARD_MENUSELECTION);
        theFrame.setContentPane(currentInstance.mainPanel);
        theFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        theFrame.pack();
        theFrame.setVisible(true);
    }

    private void setupMenuDetailsPanel(Menu m) {
        this.visualizedMenu = m;
        menuTitle.setText(m.getTitle());
        for (int i = 0; i < m.getSectionCount(); i++) {
            Section s = m.getSection(i);
            sectionsLM.addElement(s);
        }
        sectionsList.setSelectedIndex(-1);
        aggiungiSezButton.setEnabled(true);
        eliminaSezButton.setEnabled(false);
        rinominaSezButton.setEnabled(false);
        aggiungiVoceButton.setEnabled(false);
        eliminaVoceButton.setEnabled(false);
        modificaVoceButton.setEnabled(false);
        rinominaMenuButton.setEnabled(true);
        eliminaMenuButton.setEnabled(true);
        completoButton.setEnabled(m.canBeComplete());
        esciButton.setEnabled(true);
    }

    private void clearMenuDetailsPanel() {
        this.visualizedMenu = null;
        menuTitle.setText("");
        sectionsLM.removeAllElements();
        itemsLM.removeAllElements();
        aggiungiSezButton.setEnabled(false);
        eliminaSezButton.setEnabled(false);
        rinominaSezButton.setEnabled(false);
        aggiungiVoceButton.setEnabled(false);
        eliminaVoceButton.setEnabled(false);
        modificaVoceButton.setEnabled(false);
        rinominaMenuButton.setEnabled(false);
        eliminaMenuButton.setEnabled(false);
        completoButton.setEnabled(false);
        esciButton.setEnabled(false);
    }

    private void updateMenuDetailsPanel() {
        sectionsLM.removeAllElements();
        itemsLM.removeAllElements();
        this.setupMenuDetailsPanel(visualizedMenu);
    }
}
