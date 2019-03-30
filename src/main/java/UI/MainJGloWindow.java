package UI;

import API.GloAPIHandler;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.JBList;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import models.Glo.Board;
import models.Glo.Card;
import models.Glo.Column;
import callbacks.JGloCallback;
import models.Glo.User;
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
    private Column currentColumn;
    private Boolean firstSelect = false;

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
            if (listSelectionEvent.getValueIsAdjusting())
                return;

            Board selectedBoard = boards.get(boardList.getSelectedIndex());

            apiHandler.getBoardColumns(selectedBoard.getId(), new JGloCallback() {
                @Override
                public void completed(HttpResponse response) {
                    JsonNode body = (JsonNode) response.getBody();

                    JSONObject responseJSON = body.getObject();
                    try {
                        currentBoard = JGloHelper.parseObjectJson(responseJSON, Board.class);

                        ApplicationManager.getApplication().invokeLater(() ->
                            populateTabs(currentBoard.getId(), currentBoard.getColumns())
                        );

                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            });


        });
    }

    private void triggerDialog(Card card) {
        int columnIndex = columnTabbedPane.getSelectedIndex() - 1;
        AddEditCardDialog newDialog = new AddEditCardDialog(currentBoard, columnIndex, card, new JGloCallback() {
            @Override
            public void completed(HttpResponse response) {
                if (columnTabbedPane.getSelectedIndex() > 0) {
                    Column column = currentBoard.getColumns().get(columnIndex);
                    loadCards(currentBoard.getId(), column, columnIndex);
                }
            }
        });
        newDialog.setVisible(true);
    }

    private void populateTabs(String boardId, List<Column> columns) {
        // updating the listener boardID
        if (columnTabbedPane.getChangeListeners().length > 0)
            columnTabbedPane.removeChangeListener(columnTabbedPane.getChangeListeners()[0]);

        columnTabbedPane.removeAll();

        columnTabbedPane.addTab("", AllIcons.ToolbarDecorator.AddIcon, new JPanel(new FlowLayout(FlowLayout.LEFT)), "New column");

        for (Column c : columns) {
            columnTabbedPane.addTab(c.getName(), new JPanel(new FlowLayout(FlowLayout.LEFT)));

        }


        setTabListeners(boardId);
        this.firstSelect = false;

    }

    private void setTabListeners(String boardId) {
        columnTabbedPane.addChangeListener(changeEvent -> {
            int selectedIndex = columnTabbedPane.getSelectedIndex();

            if (this.firstSelect || selectedIndex < 0) {
                return;
            }


            if (selectedIndex == 0) {
                triggerDialog(null);
                return;
            }

            currentColumn = currentBoard.getColumns().get(selectedIndex - 1);
            Column selectedColumn = currentBoard.getColumns().get(selectedIndex - 1);
            loadCards(boardId, selectedColumn, selectedIndex);
        });
    }

    private void loadCards(String boardId, Column column, int index) {
        apiHandler.getBoardCardsByColumn(boardId, column.getId(), new JGloCallback() {
            @Override
            public void completed(HttpResponse response) {
                JsonNode body = (JsonNode) response.getBody();
                try {
                    List<Card> cards = JGloHelper.parseJsonArray(body.getArray(), Card.class);
                    column.setCards(cards);

                    // Filling card assignees
                    for (Card card : cards) {
                        for (User assignee : card.getAssignees()) {
                            User member = currentBoard.getMember(assignee.getId());
                            assignee.setUsername(member.getUsername());
                            assignee.setRole(member.getRole());
                        }
                    }
                    ApplicationManager.getApplication().invokeLater(() ->
                            populateTabContent(cards, index)
                    );
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        });
    }

    private void populateTabContent(List<Card> cards, int index) {
        JPanel child = (JPanel) columnTabbedPane.getComponentAt(index);
        if (index != 0) {
            JPanel newPanel = customizeList(cards, new Dimension(child.getWidth(), child.getHeight()), index);

            child.removeAll();
            child.add(newPanel);
        }
    }

    private <T> JPanel customizeList(List<T> items, Dimension size, int index) {
        JBList<T> cardJBList = new JBList<>();
        JGloHelper.initializeList(items, cardJBList);
        cardJBList.setBorder(null);
        ToolbarDecorator decorator = ToolbarDecorator.createDecorator(cardJBList);

        decorator.setAddAction(anActionButton -> triggerDialog(null));
        decorator.setEditAction(anActionButton -> triggerDialog((Card) cardJBList.getSelectedValue()));
        decorator.setRemoveAction(ActionButton -> deleteCard((Card) cardJBList.getSelectedValue(), index));

        decorator.setAsUsualTopToolbar();
        decorator.setMinimumSize(size);
        decorator.setPreferredSize(size);

        return decorator.createPanel();
    }

    private void deleteCard(Card cardToDelete, int index) {
        apiHandler.deleteCard(currentBoard.getId(), cardToDelete, new JGloCallback() {
            @Override
            public void completed(HttpResponse response) {
                loadCards(currentBoard.getId(), currentColumn, index);
                JGloHelper.showMessage("Card deleted with success", "Info", Messages.getInformationIcon());
            }
        });
    }

    public JPanel getContent() {
        return mainPanel;
    }

    public void getBoards() {
        apiHandler.getBoards(new JGloCallback() {
            @Override
            public void completed(HttpResponse response) {
                JsonNode body = (JsonNode) response.getBody();
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
