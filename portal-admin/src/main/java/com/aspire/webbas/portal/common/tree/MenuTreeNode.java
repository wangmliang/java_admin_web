package com.aspire.webbas.portal.common.tree;

public class MenuTreeNode extends TreeNode {
	private String menuKey;
	private String icon;
	private String url;

	public String getUrl() {
		return this.url;
	}

	public String getMenuKey() {
		return this.menuKey;
	}

	public void setMenuKey(String menuKey) {
		this.menuKey = menuKey;
	}

	public String getIcon() {
		return this.icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return "MenuTreeNode [menuKey=" + menuKey + ", icon=" + icon + ", url="
				+ url + "]";
	}
	
}