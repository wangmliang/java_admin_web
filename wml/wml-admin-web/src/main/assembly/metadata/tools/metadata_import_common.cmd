rem 导入菜单
java -Djava.ext.dirs="lib" -Ddbtype=%1 -cp . com.aspire.sims.rtplt.framework.portal.util2.MenuMetadataImporter ../menu/metadata_menu_common_admin_all.xml


rem 导入权限
java -Djava.ext.dirs="lib" -Ddbtype=%1 -cp . com.aspire.sims.rtplt.component.security.metadata2.AuthMetadataImporter import ../auth/metadata_auth_common_admin_all.xml

pause