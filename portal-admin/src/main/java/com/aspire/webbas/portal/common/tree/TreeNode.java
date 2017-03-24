package com.aspire.webbas.portal.common.tree;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TreeNode {
	private String id;
	private String parentId;
	private String text;
	private boolean expanded = false;

	private boolean leaf = true;

	private Set<TreeNode> children = new LinkedHashSet<TreeNode>();

	public TreeNode() {
	}

	public TreeNode(String id, String parentId, String text) {
		this.id = id;
		this.parentId = parentId;
		this.text = text;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParentId() {
		return this.parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public boolean isExpanded() {
		return this.expanded;
	}

	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}

	public boolean isLeaf() {
		return this.leaf;
	}

	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}

	public Set<TreeNode> getChildren() {
		return this.children;
	}

	public void setChildren(Set<TreeNode> children) {
		if (isLeaf()) {
			setLeaf(false);
		}
		this.children = children;
	}

	public void addChild(TreeNode node) {
		if (isLeaf()) {
			setLeaf(false);
		}
		this.children.add(node);
	}

	public void removeChild(TreeNode node) {
		this.children.remove(node);

		if (this.children.size() == 0)
			setLeaf(true);
	}

	public static TreeNode buildTree(List<TreeNode> list) {
		Map<String, TreeNode> map = new HashMap<String, TreeNode>();

		TreeNode top = null;
		for (TreeNode node : list) {
			map.put(node.getId(), node);
			if ((node.getParentId() == null) || ("".equals(node.getParentId()))) {
				top = node;
			}
		}

		for (TreeNode node : list) {
			if ((node.getParentId() != null)
					&& (!"".equals(node.getParentId()))) {
				TreeNode tmpNode = (TreeNode) map.get(node.getParentId());
				tmpNode.addChild(node);
			}
		}

		return top;
	}
}