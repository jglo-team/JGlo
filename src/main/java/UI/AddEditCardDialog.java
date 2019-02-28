package UI;

import API.GloAPIHandler;
import models.Glo.Card;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AddEditCardDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonCreate;
    private JButton buttonCancel;
    private JTextField newCardNameTextField;
    private JTextArea cardDescriptionTextArea;

    public AddEditCardDialog(Card card) {
        setContentPane(contentPane);
        setModal(true);
        setPreferredSize(new Dimension(400,400));
        setMinimumSize(new Dimension(300, 250));
        setTitle("Add new card");

        // TODO: Handle card description
        if (card != null) {
            newCardNameTextField.setText(card.getName());
        }


        Toolkit toolkit = Toolkit.getDefaultToolkit();
        final Dimension screenSize = toolkit.getScreenSize();
        final int x = (screenSize.width - this.getWidth()) / 2;
        final int y = (screenSize.height - this.getHeight()) / 2;
        setLocation(x, y);

        getRootPane().setDefaultButton(buttonCreate);

        buttonCreate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCreate();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onCreate() {
        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
