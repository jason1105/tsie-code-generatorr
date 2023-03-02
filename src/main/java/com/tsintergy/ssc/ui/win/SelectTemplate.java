package com.tsintergy.ssc.ui.win;

import com.tsintergy.ssc.config.ConfigService;
import com.tsintergy.ssc.ui.win.tree.CheckBoxTreeCellRenderer;
import com.tsintergy.ssc.ui.win.tree.CheckBoxTreeNode;
import com.tsintergy.ssc.ui.win.tree.CheckBoxTreeNodeSelectionListener;
import com.tsintergy.ssc.util.PluginUtils;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.io.File;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 模板选择界面
 */
public class SelectTemplate implements IWindows {
    private final CheckBoxTreeNode root;
    /**
     * 模板属性结构数据
     */
    private JTree tree;
    /**
     * 面板：顶级内容面板
     */
    private JPanel content;

    private final ConfigService configService = PluginUtils.findConfigService();

    public SelectTemplate() {
        File extensionPluginPath = PluginUtils.getExtensionPluginDirFile(PluginUtils.TEMPLATE_DIR);
        File projectPluginPath = PluginUtils.getProjectPluginDirFile(PluginUtils.TEMPLATE_DIR);
        File projectWorkspacePluginPath = PluginUtils.getProjectWorkspacePluginDirFile(PluginUtils.TEMPLATE_DIR);

        root = new CheckBoxTreeNode("Code Template File");

        CheckBoxTreeNode extTemplate = new CheckBoxTreeNode("Scratches and Consoles - Extensions");
        getTreeData(extTemplate, extensionPluginPath.listFiles());

        CheckBoxTreeNode projectTemplate = new CheckBoxTreeNode("Current Project");
        getTreeData(projectTemplate, projectPluginPath.listFiles());

        CheckBoxTreeNode projectWorkspaceTemplate = new CheckBoxTreeNode("Project - IDE");
        getTreeData(projectWorkspaceTemplate, projectWorkspacePluginPath.listFiles());

        root.add(extTemplate);
        if (projectWorkspaceTemplate.getChildCount() != 0) {
            root.add(projectWorkspaceTemplate);
        }
        if (projectTemplate.getChildCount() != 0) {
            root.add(projectTemplate);
        }

        tree.addMouseListener(new CheckBoxTreeNodeSelectionListener());
        tree.setModel(new DefaultTreeModel(root));
        tree.setCellRenderer(new CheckBoxTreeCellRenderer());
        tree.setShowsRootHandles(true);

        expandDepthNode(new TreePath(root), true, 3);
        initSelected();
    }

    /**
     * Restore the state of selected from configService
     */
    private void initSelected() {
        iterateNodes(root);
    }

    private void iterateNodes(DefaultMutableTreeNode root) {

        // the key used for saving state in the set of configService
        final String key = Arrays.stream(root.getUserObjectPath()).map(Object::toString)
            .collect(Collectors.joining("."));

        final var isSelected = configService.getNodePaths().contains(key);

        /*
        setSelected() method will cause cascading update of the sub-nodes.
         */
        if (isSelected) {
            ((CheckBoxTreeNode)root).setSelected(true);
        }

        Enumeration children = root.children();
        while (children.hasMoreElements()) {
            DefaultMutableTreeNode childNode = (DefaultMutableTreeNode)children.nextElement();
            iterateNodes(childNode);
        }
    }

    /**
     * 展开树的所有节点的方法
     *
     * @param parent root
     * @param expand true false
     */
    private void expandDepthNode(TreePath parent, boolean expand, int depth) {
        if (depth-- <= 0) {
            return;
        }
        TreeNode node = (TreeNode)parent.getLastPathComponent();
        if (node.getChildCount() >= 0) {
            for (Enumeration e = node.children(); e.hasMoreElements(); ) {
                TreeNode n = (TreeNode)e.nextElement();
                TreePath path = parent.pathByAddingChild(n);
                expandDepthNode(path, expand, depth);
            }
        }
        if (expand) {
            tree.expandPath(parent);
        } else {
            tree.collapsePath(parent);
        }
    }

    private void getTreeData(DefaultMutableTreeNode treeNode, File[] files) {
        if (files == null) {
            return;
        }
        for (File file : files) {
            DefaultMutableTreeNode node;
            if (file.isDirectory()) {
                node = new CheckBoxTreeNode(file.getName());
                getTreeData(node, file.listFiles());
            } else {
                // 是一个模板文件
                node = new CheckBoxTreeNode(file);
            }
            treeNode.add(node);
        }
    }

    @Override
    public JPanel getContent() {
        return content;
    }

    public List<File> getAllSelectFile() {
        List<CheckBoxTreeNode> allSelectNodes = root.getAllSelectNodes();
        return allSelectNodes.stream().filter(item -> item.getUserObject() instanceof File)
            .map(item -> (File)item.getUserObject()).collect(Collectors.toList());
    }
}
