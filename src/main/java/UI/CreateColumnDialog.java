package UI;

import callbacks.JGloCallback;
import models.Glo.Column;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CreateColumnDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField newColumnNameTextField;

    public CreateColumnDialog(Column column, JGloCallback callback) {
        setContentPane(contentPane);
        setModal(true);
        setPreferredSize(new Dimension(300,120));
        setMinimumSize(new Dimension(300, 120));
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel(callback));

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel(callback);
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(callback), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        setToSize();
        setTitle("Add new column");

    }

    private void setToSize() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        final Dimension screenSize = toolkit.getScreenSize();
        final int x = (screenSize.width - this.getWidth()) / 2;
        final int y = (screenSize.height - this.getHeight()) / 2;
        setLocation(x, y);
    }

    private void onOK() {
        // add your code here
        dispose();
    }

    private void onCancel(JGloCallback callback) {
        // add your code here if necessary
        callback.cancelled();
        dispose();
    }
}
