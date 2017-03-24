package com.aspire.webbas.portal.common.tree;

public class CheckTreeNode extends TreeNode {
	private boolean checked = false;

	public CheckTreeNode() {
	}

	public CheckTreeNode(String id, String parentId, String text) {
		super(id, parentId, text);
	}

	public boolean isChecked() {
		return this.checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}
}