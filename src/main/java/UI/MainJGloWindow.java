package UI;

import API.GloAPIHandler;
import actions.CreateBoardAction;
import actions.DeleteBoardAction;
import com.intellij.icons.AllIcons;
import com.intellij.ide.IdeBundle;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionPopupMenu;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.ListPopupStep;
import com.intellij.openapi.ui.popup.PopupStep;
import com.intellij.openapi.ui.popup.util.BaseListPopupStep;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.PopupHandler;
import com.intellij.ui.AnActionButton;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.JBList;
import com.intellij.util.PlatformIcons;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import interfaces.ApiRequestHandler;
import javafx.scene.input.MouseButton;
import models.Glo.Board;
import models.Glo.Card;
import models.Glo.Column;
import callbacks.JGloCallback;
import models.Glo.User;
import models.JGloHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.List;

public class MainJGloWindow implements ApiRequestHandler {
    private GloAPIHandler apiHandler;
    private JGloJPanel mainPanel;
    private JBList boardList;
    private JTabbedPane columnTabbedPane;
    private JSplitPane splitPlane;

    private List<Board> boards;
    private Board currentBoard;
    private Column currentColumn;
    private Boolean firstSelect = false;

    // TODO: Split change - https://www.jetbrains.org/intellij/sdk/docs/user_interface_components/misc_swing_components.html
    public MainJGloWindow(ToolWindow toolWindow) {
        this.boards = new LinkedList<>();
        this.initializeComponents();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        Unirest.shutdown();
    }

    public void initializeComponents() {
        apiHandler = new GloAPIHandler();
        getBoards();
        boardList.addListSelectionListener(listSelectionEvent -> {
            if (listSelectionEvent.getValueIsAdjusting())
                return;

            Board selectedBoard = boards.get(boardList.getSelectedIndex());
            getColumns(selectedBoard.getId());
        });
    }

    private void getColumns(String boardId) {
        apiHandler.getBoardColumns(boardId, new JGloCallback() {
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

    }

    private void triggerColumnDialog() {
        CreateColumnDialog newDialog = new CreateColumnDialog(currentBoard.getId(), null, new JGloCallback() {
            @Override
            public void completed(HttpResponse response) {
                switch (response.getStatus()) {
                    case 200:
                    case 201:
                        getColumns(currentBoard.getId());
                        break;
                    case 400:
                        JGloHelper.showMessage("Bad request", "Error", Messages.getErrorIcon());
                        break;
                    case 500:
                        JGloHelper.showMessage("Server error", "Error", Messages.getErrorIcon());
                    default:
                        JGloHelper.showMessage("Unknow error", "Error", Messages.getErrorIcon());
                }
            }

            @Override
            public void cancelled() {
                super.cancelled();
                columnTabbedPane.setSelectedIndex(-1);
            }
        });
        newDialog.setVisible(true);
    }

    private void triggerCardDialog(Card card, int index) {
        AddEditCardDialog newDialog = new AddEditCardDialog(currentBoard, index - 1, card, new JGloCallback() {
            @Override
            public void completed(HttpResponse response) {
                if (successfullyResponse(response)){
                    loadCards(currentBoard.getId(), currentColumn, index);
                }
            }

            @Override
            public void cancelled() {
                super.cancelled();
                if (columnTabbedPane.getSelectedIndex() < 1)
                    columnTabbedPane.setSelectedIndex(-1);

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
        columnTabbedPane.setSelectedIndex(-1);

    }

    private void setTabListeners(String boardId) {
        columnTabbedPane.addChangeListener(changeEvent -> {
            int selectedIndex = columnTabbedPane.getSelectedIndex();

            if (this.firstSelect || selectedIndex < 0) {
                return;
            }

            if (selectedIndex == 0) {
                triggerColumnDialog();
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
        JPanel newPanel = customizeList(cards, new Dimension(child.getWidth(), child.getHeight()), index);

        child.removeAll();
        child.add(newPanel);
    }

    private <T> JPanel customizeList(List<T> items, Dimension size, int index) {
        JBList<T> cardJBList = new JBList<>();
        JGloHelper.initializeList(items, cardJBList);
        cardJBList.setBorder(null);
        ToolbarDecorator decorator = ToolbarDecorator.createDecorator(cardJBList);

        decorator.setAddAction(anActionButton -> triggerCardDialog(null, index));
        decorator.setEditAction(anActionButton -> triggerCardDialog((Card) cardJBList.getSelectedValue(), index));
        decorator.setRemoveAction(ActionButton -> deleteCard((Card) cardJBList.getSelectedValue(), index));

        AnActionButton moveAction = new ToolbarDecorator.ElementActionButton(IdeBundle.message("button.copy"), AllIcons.Actions.MoveTo2) {
            @Override
            public void actionPerformed(AnActionEvent e) {
                // TODO: Continue

                ListPopupStep step = new BaseListPopupStep("Move card to:", currentBoard.getColumns()) {
                    @Override
                    public PopupStep onChosen(Object selectedValue, boolean finalChoice) {
                        //super.onChosen(selectedValue, finalChoice);
                        if (finalChoice) {
                            Column selectedColumn = (Column) selectedValue;

                            apiHandler.moveCard(currentBoard.getId(), selectedColumn.getId(), (Card) cardJBList.getSelectedValue(), new JGloCallback() {
                                @Override
                                public void completed(HttpResponse response) {
                                    loadCards(currentBoard.getId(), currentColumn, index);
                                }
                            });
                        }
                        return super.onChosen(selectedValue, finalChoice);
                    }
                };

                JBPopupFactory.getInstance().createListPopup(step).showInBestPositionFor(e.getDataContext());
            }
        };
        decorator.addExtraAction(moveAction);

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
        mainPanel.setMainJGloWindow(this);

        boardList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {

                    PopupHandler popupHandler = new PopupHandler() {
                        @Override
                        public void invokePopup(Component comp, int x, int y) {

                            DefaultActionGroup actionPopupGroup = new DefaultActionGroup("JGloPopUpGroup", true);
                            actionPopupGroup.add(new CreateBoardAction());
                            ActionPopupMenu popupMenu;
//                            if (boardList.getSelectedIndex() != -1) {
//                                actionPopupGroup.add(new DeleteBoardAction());
//                            }

                            popupMenu = ActionManager.getInstance().createActionPopupMenu("POPUP", actionPopupGroup);

                            popupMenu.setTargetComponent(boardList);
                            JPopupMenu menu = popupMenu.getComponent();

                            menu.show(comp, x, y);
                        }
                    };
                    boardList.addMouseListener(popupHandler);

                }

                super.mouseClicked(e);
            }
        });

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
