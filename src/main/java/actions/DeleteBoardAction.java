package actions;

import API.GloAPIHandler;
import callbacks.JGloCallback;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.mashape.unirest.http.HttpResponse;
import interfaces.ApiRequestHandler;
import models.JGloHelper;
import org.jetbrains.annotations.NotNull;

public class DeleteBoardAction extends AnAction implements ApiRequestHandler {
    private String boardId;

    public DeleteBoardAction(String boardId) {
        super();
        this.boardId = boardId;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        GloAPIHandler handler = new GloAPIHandler();
        handler.deleteBoard(boardId, new JGloCallback() {
            @Override
            public void completed(HttpResponse response) {
                if (successfullyResponse(response)){
                    Project project = e.getProject();
                    if (project != null){
                        ApplicationManager.getApplication().invokeLater(() ->
                            JGloHelper.getMainWindow(e.getProject()).getBoards()
                        );
                    }

                }
            }
        });
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        e.getPresentation().setText("Delete");
        e.getPresentation().setDescription("Delete the selected board");
        e.getPresentation().setIcon(AllIcons.Actions.Cancel);

        super.update(e);
    }
}
