package UI;

import com.intellij.icons.AllIcons;
import com.intellij.ui.ColoredTreeCellRenderer;
import com.intellij.ui.JBColor;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.tree.TreeModel;
import java.awt.*;
import java.util.Objects;

public class JGloTreeCellRenderer extends ColoredTreeCellRenderer {

    public JGloTreeCellRenderer() {
        this.setIcon(AllIcons.Idea_logo_welcome);
    }

    @Override
    public void customizeCellRenderer(@NotNull JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        TreeModel model = tree.getModel();
        model.getChildCount(model.getRoot());
        append(value.toString());
        setBackground(new JBColor(new Color(33, 49, 63), new Color(33, 49, 63)));
        setOpaque(true);
        setIcon(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("kraken.png"))));
    }


}
