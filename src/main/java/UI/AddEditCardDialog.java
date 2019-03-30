package UI;

import API.GloAPIHandler;
import models.Glo.Board;
import models.Glo.Card;
import callbacks.JGloCallback;
import models.Glo.Description;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class AddEditCardDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonCreateEdit;
    private JButton buttonCancel;
    private JTextField newCardNameTextField;
    private JTextArea cardDescriptionTextArea;

    public AddEditCardDialog(Board targetBoard, int columnIndex, Card card, JGloCallback callback) {
        setContentPane(contentPane);
        setModal(true);
        setPreferredSize(new Dimension(400,400));
        setMinimumSize(new Dimension(300, 250));

        setTitle("Add new card");

        // TODO: Handle card description
        if (card != null) {
            buttonCreateEdit.setText("Edit");
            newCardNameTextField.setText(card.getName());
            cardDescriptionTextArea.setText(card.getDescription().getText());
            buttonCreateEdit.addActionListener(e -> {
                card.setName(newCardNameTextField.getText());
                card.getDescription().setText(cardDescriptionTextArea.getText());

                onEdit(targetBoard.getId(), card, callback);
            });
        } else {
            buttonCreateEdit.addActionListener(e -> onCreate(targetBoard.getId(), targetBoard.getColumns().get(columnIndex).getId(),callback));
        }
        getRootPane().setDefaultButton(buttonCreateEdit);

        setToSize();

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
    }

    private void setToSize() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        final Dimension screenSize = toolkit.getScreenSize();
        final int x = (screenSize.width - this.getWidth()) / 2;
        final int y = (screenSize.height - this.getHeight()) / 2;
        setLocation(x, y);
    }

    private void onCreate(String boardId, String columnId, JGloCallback callback) {
        GloAPIHandler handler = new GloAPIHandler();
        handler.createCard(boardId, columnId,new Card("", new Description(cardDescriptionTextArea.getText()), newCardNameTextField.getText()), callback);
        dispose();
    }

    private void onEdit(String boardId, Card card, JGloCallback callback) {
        // add your code here
        GloAPIHandler handler = new GloAPIHandler();
        handler.editCard(boardId, card, callback);
        dispose();
    }

    private void onCancel(JGloCallback callback) {
        dispose();

        callback.cancelled();
    }
}
