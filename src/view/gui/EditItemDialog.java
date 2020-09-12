package view.gui;

import model.businesslogic.Dish;
import model.businesslogic.Item;
import controller.bl_controller.MenuDefinitionController;
import model.businesslogic.Recipe;
import model.services.CookALotServiceProvider;
import model.utils.FilterComboBoxModel;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class EditItemDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JCheckBox modDescCheckBox;
    private JTextField itemDescText;
    private JComboBox<Recipe> dishesCombo;

    private Item theItem;
    private int value;

    public EditItemDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);


        modDescCheckBox.addActionListener(e -> {
            if (modDescCheckBox.isSelected()) {
                itemDescText.setEnabled(true);
            } else {
                Dish currentDish = (Dish) dishesCombo.getSelectedItem();
                if (currentDish == null) {
                    itemDescText.setText("");
                } else {
                    itemDescText.setText(currentDish.getName());
                }
                itemDescText.setEnabled(false);
            }
        });

        dishesCombo.addActionListener(e -> {
            this.buttonOK.setEnabled(dishesCombo.getSelectedIndex() >= 0);
            if (!modDescCheckBox.isSelected() && dishesCombo.getSelectedItem() != null) {
                Dish d = (Dish)dishesCombo.getSelectedItem();
                itemDescText.setText(d.getName());
            }
        });

        itemDescText.setText("");
        modDescCheckBox.setSelected(false);


        dishesCombo.setModel(new FilterComboBoxModel<>(CookALotServiceProvider.getInstance().getDataManager().getAllRecipes(), recipe -> recipe.isDish()));
        dishesCombo.setSelectedIndex(-1);
        buttonOK.setEnabled(false);


    }

    private void onOK() {
        // add your code here
        value = JOptionPane.OK_OPTION;
        if (theItem != null) {
            MenuDefinitionController.getInstance().modifyItem(theItem, (Dish)dishesCombo.getSelectedItem(), itemDescText.getText());
        } else {
            theItem = MenuDefinitionController.getInstance().insertItem((Dish)dishesCombo.getSelectedItem(), itemDescText.getText());
        }
        setVisible(false);
    }

    public Item getItem() {
        return this.theItem;
    }

    public int getValue() {
        return value;
    }

    private void onCancel() {
        // add your code here if necessary
        value = JOptionPane.CANCEL_OPTION;
        setVisible(false);
    }


    public void initialize(Item item) {
        theItem = item;
        if (theItem == null) {
            this.dishesCombo.setSelectedIndex(-1);
            this.modDescCheckBox.setSelected(false);
            this.itemDescText.setText("");
        } else {
            this.itemDescText.setText(theItem.getDescription());
            this.dishesCombo.setSelectedItem(theItem.getDish());
            this.modDescCheckBox.setSelected(!theItem.getDescription().equals(theItem.getDish().getName()));
        }
    }
}
