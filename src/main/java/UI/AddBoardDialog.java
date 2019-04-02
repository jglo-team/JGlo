package UI;

import API.GloAPIHandler;
import callbacks.JGloCallback;
import com.intellij.openapi.ui.Messages;
import models.Glo.Board;
import models.JGloHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AddBoardDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textFieldBoardName;

    private JGloCallback callback;

    public AddBoardDialog(JGloCallback callback) {
        this.callback = callback;
        setTitle("New board");
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        setPreferredSize(new Dimension(400, 150));
        setMinimumSize(new Dimension(300, 150));

        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        setToSize();

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void setToSize() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        final Dimension screenSize = toolkit.getScreenSize();
        final int x = (screenSize.width - this.getWidth()) / 2;
        final int y = (screenSize.height - this.getHeight()) / 2;
        setLocation(x, y);
    }

    private void onOK() {

        String boardName = textFieldBoardName.getText().trim();

        if (boardName.isEmpty()) {
            JGloHelper.showMessage("A name should be specified", "Error", Messages.getErrorIcon());
            return;
        }

        GloAPIHandler gloAPIHandler = new GloAPIHandler();


        Board board = new Board(boardName);
        gloAPIHandler.createBoard(board, callback);
        dispose();
    }

    private void onCancel() {
        callback.cancelled();
        dispose();
    }

}
