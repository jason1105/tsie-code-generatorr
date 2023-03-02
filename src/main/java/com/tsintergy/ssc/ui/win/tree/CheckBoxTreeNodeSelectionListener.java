package com.tsintergy.ssc.ui.win.tree;

import com.tsintergy.ssc.config.ConfigService;
import com.tsintergy.ssc.util.PluginUtils;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * 复选框树形节点鼠标点击监听事件
 */
public class CheckBoxTreeNodeSelectionListener extends MouseAdapter {

    /**
     * Persistence object
     */
    ConfigService configService = PluginUtils.findConfigService();

    @Override
    public void mouseClicked(MouseEvent event) {
        JTree tree = (JTree)event.getSource();
        int row = tree.getRowForLocation(event.getX(), event.getY());
        TreePath path = tree.getPathForRow(row);
        if (path != null) {
            CheckBoxTreeNode node = (CheckBoxTreeNode)path.getLastPathComponent();
            if (node != null) {
                boolean isSelected = !node.isSelected();
                node.setSelected(isSelected);
                ((CheckBoxTreeCellRenderer)tree.getCellRenderer()).updateUI();
                ((DefaultTreeModel)tree.getModel()).nodeStructureChanged(node);

                // Persist select status
                updateConfigService(node);
            }
        }
    }

    /**
     * Persist state of selected
     *
     * @param node
     */
    private void updateConfigService(CheckBoxTreeNode node) {
        final String key = Arrays.stream(node.getUserObjectPath()).map(Object::toString)
            .collect(Collectors.joining("."));
        if (node.isSelected()) {
            configService.getNodePaths().add(key);
        } else {
            configService.getNodePaths().remove(key);
        }
    }
}
