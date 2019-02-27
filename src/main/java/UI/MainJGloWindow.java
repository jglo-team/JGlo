package UI;

import API.GloAPIHandler;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.packageDependencies.ui.TreeModel;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import models.Board;
import models.JGloCallback;
import models.JGloHelper;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import java.util.LinkedList;
import java.util.List;

public class MainJGloWindow {
    private GloAPIHandler apiHandler;
    private JPanel panel1;
    private JTree tree1;
    private JButton button1;

    private List<Board> boards;

    public MainJGloWindow(ToolWindow toolWindow) {
        apiHandler = new GloAPIHandler();
        this.tree1.setModel(null);
        this.boards = new LinkedList<>();
        getBoards();

        tree1.addTreeSelectionListener(treeSelectionEvent -> {

            DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                    tree1.getLastSelectedPathComponent();

            /* if nothing is selected */
            if (node == null) return;

            /* retrieve the node that was selected */
            Board selectedBoard = (Board) node.getUserObject();


            Messages.showMessageDialog(selectedBoard.toString(), "Tree", Messages.getInformationIcon());
        });
    }

    public JPanel getContent() {
        return panel1;
    }

    public void getBoards() {
        apiHandler.getBoards(new JGloCallback() {
            @Override
            public void completed(HttpResponse response) {
                JsonNode body =  (JsonNode) response.getBody();
                try {
                    boards = JGloHelper.parseJsonArray(body, Board.class);
                    initializeTree(boards);
                } catch (Exception e) {
                }
            }
        });
    }



    public void initializeTree(List<Board> boards) {
        DefaultMutableTreeNode treeTop = new DefaultMutableTreeNode("Glo Boards");

        DefaultTreeModel treeModel = new DefaultTreeModel(treeTop);
        for (Board b : boards) {
            treeTop.add( new DefaultMutableTreeNode(b));
        }

        this.tree1.setModel(treeModel);
    }
}
