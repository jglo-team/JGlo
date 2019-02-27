package UI;

import API.GloAPIHandler;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.treeStructure.Tree;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import models.Glo.Board;
import models.Glo.Card;
import models.Glo.Column;
import models.JGloCallback;
import models.JGloHelper;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.util.LinkedList;
import java.util.List;

public class MainJGloWindow {
    private GloAPIHandler apiHandler;
    private JPanel panel1;
    private Tree boardTree;
    private JButton button1;
    private Tree columnCardTree;

    private List<Board> boards;

    public MainJGloWindow(ToolWindow toolWindow) {
        apiHandler = new GloAPIHandler();
        this.boardTree.setModel(null);
        this.columnCardTree.setModel(null);



        this.boards = new LinkedList<>();
        getBoards();

        boardTree.addTreeSelectionListener(treeSelectionEvent -> {

            DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                    boardTree.getLastSelectedPathComponent();

            /* if nothing is selected */
            if (node == null || node.getUserObject() == "Glo Boards") return;

            /* retrieve the node that was selected */
            Board selectedBoard = (Board) node.getUserObject();

            populateColumnsTree(selectedBoard.getId());
        });
    }

    private void populateColumnsTree(String boardId) {
        apiHandler.getBoardColumns(boardId, new JGloCallback() {
            @Override
            public void completed(HttpResponse response) {
                JsonNode body = (JsonNode) response.getBody();

                JSONObject responseJSON = body.getObject();
                // TODO: Continue here
                try {
                    List<Column> columns = JGloHelper.parseJsonArray(responseJSON.getJSONArray("columns"), Column.class);
                    initializeTree("Columns", columns, columnCardTree);


                    columnCardTree.addTreeSelectionListener(treeSelectionEvent -> {
                        DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                                columnCardTree.getLastSelectedPathComponent();

                        /* if nothing is selected */
                        if (node == null || node.getUserObject() == "Columns" || !(node.getUserObject() instanceof Column)) return;


                        Column selectedColumn = (Column) node.getUserObject();
                        appendCardsToTree(boardId, selectedColumn.getId(), node);
                    });

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        });
    }

    private void appendCardsToTree(String boardId, String columnId, DefaultMutableTreeNode invokerNode) {
        apiHandler.getBoardCardsByColumn(boardId, columnId, new JGloCallback() {
            @Override
            public void completed(HttpResponse response) {
                JsonNode body = (JsonNode) response.getBody();
                try {
                    List<Card> cards = JGloHelper.parseJsonArray(body.getArray(), Card.class);

                    invokerNode.removeAllChildren();

                    for (Card card : cards) {
                        appendToNode(invokerNode, card);
                    }

                    columnCardTree.updateUI();
                    columnCardTree.setSelectionPath(new TreePath(invokerNode.getPath()));
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        });
    }

    public void appendToNode(DefaultMutableTreeNode parentNode, Object newNode) {
        parentNode.add(new DefaultMutableTreeNode(newNode));
    }

    public JPanel getContent() {
        return panel1;
    }

    public void getBoards() {
        apiHandler.getBoards(new JGloCallback() {
            @Override
            public void completed(HttpResponse response) {
                JsonNode body =  (JsonNode) response.getBody();
                JSONArray columnsArray = body.getArray();

                try {
                    boards = JGloHelper.parseJsonArray(columnsArray, Board.class);
                    initializeTree("JGlo Boards", boards, boardTree);
                } catch (Exception e) {
                }
            }
        });
    }



    public <T> void initializeTree(String topName, List<T> items, Tree tree) {
        DefaultMutableTreeNode treeTop = new DefaultMutableTreeNode(topName);

        DefaultTreeModel treeModel = new DefaultTreeModel(treeTop);
        for (T b : items) {
            treeTop.add( new DefaultMutableTreeNode(b));
        }

        tree.setModel(treeModel);
    }
}
