package UI;

import API.GloAPIHandler;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.JBList;
import com.mashape.unirest.http.Headers;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import models.Glo.Board;
import models.Glo.Card;
import models.Glo.Column;
import models.JGloCallback;
import models.JGloHelper;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class MainJGloWindow {
    private GloAPIHandler apiHandler;
    private JPanel mainPanel;
    private JBList boardList;
    private JTabbedPane columnTabbedPane;
    private JSplitPane splitPlane;

    private List<Board> boards;
    private Board currentBoard;

    // TODO: Split change - https://www.jetbrains.org/intellij/sdk/docs/user_interface_components/misc_swing_components.html
    public MainJGloWindow(ToolWindow toolWindow) {
        apiHandler = new GloAPIHandler();
        this.boards = new LinkedList<>();
        this.initializeComponents();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        Unirest.shutdown();
    }

    private void initializeComponents() {
        getBoards();

        boardList.addListSelectionListener(listSelectionEvent -> {
            Board selectedBoard = boards.get(listSelectionEvent.getLastIndex());

            apiHandler.getBoardColumns(selectedBoard.getId(), new JGloCallback() {
                @Override
                public void completed(HttpResponse response) {
                    JsonNode body = (JsonNode) response.getBody();

                    JSONObject responseJSON = body.getObject();
                    // TODO: Continue here
                    try {
                        List<Column> columns = JGloHelper.parseJsonArray(responseJSON.getJSONArray("columns"), Column.class);
                        selectedBoard.setColumns(columns);
                        currentBoard = selectedBoard;

                        populateTabs(selectedBoard.getId(), columns);

                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            });
        });
    }

    private void triggerDialog(Card card) {
        AddEditCardDialog newDialog = new AddEditCardDialog(currentBoard, columnTabbedPane.getSelectedIndex(),card, new JGloCallback() {
            @Override
            public void completed(HttpResponse response) {
                int h = response.getStatus();
                Column column =  currentBoard.getColumns().get(columnTabbedPane.getSelectedIndex());
                loadCards(currentBoard.getId(), column, columnTabbedPane.getSelectedIndex());
            }
        });
        newDialog.setVisible(true);
    }

    private void populateTabs(String boardId, List<Column> columns) {
        columnTabbedPane.removeAll();

        columnTabbedPane.addTab("", AllIcons.ToolbarDecorator.AddIcon,new JPanel(new FlowLayout(FlowLayout.LEFT)));

        for (Column c: columns) {
            columnTabbedPane.addTab(c.getName(), new JPanel(new FlowLayout(FlowLayout.LEFT)));
        }
        columnTabbedPane.addChangeListener(changeEvent -> {
            int selectedIndex = columnTabbedPane.getSelectedIndex();
            if (selectedIndex == 0) {
                triggerDialog(null);
                return;
            }
            selectedIndex--;
            Column selectedColumn = currentBoard.getColumns().get(selectedIndex);
            loadCards(boardId, selectedColumn, selectedIndex);
        });

        columnTabbedPane.setSelectedIndex(1);
    }

    private void loadCards(String boardId, Column column, int index) {
        apiHandler.getBoardCardsByColumn(boardId, column.getId(), new JGloCallback() {
            @Override
            public void completed(HttpResponse response) {
                JsonNode body = (JsonNode) response.getBody();
                try {
                    List<Card> cards = JGloHelper.parseJsonArray(body.getArray(), Card.class);
                    column.setCards(cards);

                    populateTabContent(cards, index);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        });
    }

    private void populateTabContent(List<Card> cards, int index) {
        JPanel child = (JPanel) columnTabbedPane.getComponentAt(index);

        JPanel newPanel = costumizeList(new JBList(), cards, new Dimension(child.getWidth(), child.getHeight()));

        child.removeAll();
        child.add(newPanel);
    }

    private <T> JPanel costumizeList(JBList list, List<T> items, Dimension size) {
        JBList<T> cardJBList = new JBList<>();
        JGloHelper.initializeList(items, cardJBList);
        cardJBList.setBorder(null);
        ToolbarDecorator decorator = ToolbarDecorator.createDecorator(cardJBList);

        decorator.setAddAction(anActionButton -> triggerDialog(null));
        decorator.setEditAction(anActionButton -> triggerDialog((Card) cardJBList.getSelectedValue()));

        decorator.setAsUsualTopToolbar();
        decorator.setMinimumSize(size);
        decorator.setPreferredSize(size);

        return decorator.createPanel();
    }

    public JPanel getContent() {
        return mainPanel;
    }

    public void getBoards() {
        apiHandler.getBoards(new JGloCallback() {
            @Override
            public void completed(HttpResponse response) {
                JsonNode body =  (JsonNode) response.getBody();
                JSONArray columnsArray = body.getArray();

                try {
                    boards = JGloHelper.parseJsonArray(columnsArray, Board.class);
                    JGloHelper.initializeList(boards, boardList);
                } catch (Exception e) {
                }
            }
        });
    }
}
