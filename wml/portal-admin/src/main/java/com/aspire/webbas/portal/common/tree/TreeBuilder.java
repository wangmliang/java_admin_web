package com.aspire.webbas.portal.common.tree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TreeBuilder {
	private Map<String, TreeNode> treeNodes = new LinkedHashMap<String, TreeNode>();

	public void addNode(TreeNode node) {
		if (this.treeNodes.containsKey(node.getId())) {
			throw new RuntimeException("存在相同id[" + node.getId() + "]的树节点");
		}

		this.treeNodes.put(node.getId(), node);
	}

	public void markChecked(String id) {
		if (this.treeNodes.containsKey(id)) {
			TreeNode node = (TreeNode) this.treeNodes.get(id);
			if ((node instanceof CheckTreeNode)) {
				((CheckTreeNode) node).setChecked(true);

				expandNode(node);
			}
		}
	}

	private void createTree(TreeNode treeNode) {
		for (TreeNode node : this.treeNodes.values()) {
			if ((node.getParentId() != null)
					&& (!"".equals(node.getParentId()))
					&& (treeNode.getId().equals(node.getParentId()))) {
				treeNode.addChild(node);
			}
		}

		Set<TreeNode> childrens = treeNode.getChildren();
		if ((null != childrens) && (childrens.size() > 0)) {
			Iterator<TreeNode> it = childrens.iterator();
			while (it.hasNext())
				createTree((TreeNode) it.next());
		}
	}

	public List<TreeNode> buildTree() {
		List<TreeNode> rootList = new ArrayList<TreeNode>();

		for (TreeNode node : this.treeNodes.values()) {
			if ((node.getParentId() == null) || ("".equals(node.getParentId()))) {
				rootList.add(node);
				createTree(node);
			}
		}
		return rootList;
	}

	private void expandNode(TreeNode node) {
		if (this.treeNodes.containsKey(node.getParentId())) {
			expandNode((TreeNode) this.treeNodes.get(node.getParentId()));
		}

		node.setExpanded(true);
	}

	public List<TreeNode> buildTreeWithNoEmptyNode() {
		List<TreeNode> tempList = new ArrayList<TreeNode>();

		for (TreeNode node : this.treeNodes.values()) {
			if ((node.getParentId() != null)
					&& (!"".equals(node.getParentId()))
					&& (this.treeNodes.containsKey(node.getParentId()))) {
				((TreeNode) this.treeNodes.get(node.getParentId()))
						.addChild(node);
			}

			tempList.add(node);
		}

		for (TreeNode node : tempList) {
			if ((node.isLeaf()) && (!(node instanceof MenuTreeNode))) {
				removeNode(node);
			}
		}

		return buildTree();
	}

	private void removeNode(TreeNode node) {
		this.treeNodes.remove(node.getId());

		if (this.treeNodes.containsKey(node.getParentId())) {
			TreeNode parent = (TreeNode) this.treeNodes.get(node.getParentId());
			parent.removeChild(node);

			if (parent.isLeaf())
				removeNode(parent);
		}
	}
}