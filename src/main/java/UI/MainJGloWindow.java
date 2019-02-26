package UI;

import API.GloAPIHandler;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.wm.ToolWindow;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import models.Board;
import models.JGloCallback;
import models.JGloHelper;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import java.util.List;

public class MainJGloWindow {
    private GloAPIHandler apiHandler;
    private JPanel panel1;
    private JTree tree1;
    private JButton button1;



    public MainJGloWindow(ToolWindow toolWindow) {
        apiHandler = new GloAPIHandler();
        getBoards();
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
                    List<Board> result = JGloHelper.parseJsonArray(body, Board.class);
                    initializeTree(result);
                } catch (Exception e) {
                }
            }
        });
    }



    public void initializeTree(List<Board> boards) {

        DefaultMutableTreeNode treeTop = new DefaultMutableTreeNode("Glo Boards");

        for (Board b : boards) {
            treeTop.add( new DefaultMutableTreeNode(b.getName()));
        }
        this.tree1 = new JTree(treeTop);
        //this.tree1 = new com.intellij.ui.treeStructure.Tree(treeTop);
        this.panel1.updateUI();
    }
}
