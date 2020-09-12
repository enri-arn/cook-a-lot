package view.gui;

import javax.swing.*;
import java.awt.event.*;

public class ShortTextDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField menuTitleField;
    private JLabel requestLabel;
    private int value;

    public ShortTextDialog(String request) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        requestLabel.setText(request);

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
    }

    private void onOK() {
        // add your code here
        this.value = JOptionPane.OK_OPTION;
        setVisible(false);
    }

    private void onCancel() {
        // add your code here if necessary
        this.value = JOptionPane.CANCEL_OPTION;
        setVisible(false);
    }

    public int getValue() {
        return value;
    }

    public void setInitialText(String text) {
        this.menuTitleField.setText(text);
    }

    public String getText() {
        return this.menuTitleField.getText();
    }
}
