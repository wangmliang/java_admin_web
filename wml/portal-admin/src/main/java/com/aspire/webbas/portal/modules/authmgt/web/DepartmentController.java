package com.aspire.webbas.portal.modules.authmgt.web;

import com.aspire.webbas.core.web.BaseController;
import com.aspire.webbas.portal.common.entity.Department;
import com.aspire.webbas.portal.common.entity.Role;
import com.aspire.webbas.portal.common.entity.Staff;
import com.aspire.webbas.portal.common.service.DepartmentService;
import com.aspire.webbas.portal.common.service.RoleService;
import com.aspire.webbas.portal.common.service.StaffService;
import com.aspire.webbas.portal.common.util.StaffUtil;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * ����Controller
 * <pre>
 * <b>Title��</b>DepartmentController.java<br/>
 * <b>@author��</b>WML<br/>
 * <b>@date��</b>2016��11��7�� - ����5:55:03<br/>  
 * <b>@version v1.0</b></br/>
 * <b>Copyright (c) 2016 ASPire Tech.</b>   
 * </pre>
 */
@Controller
@RequestMapping({ "/department" })
public class DepartmentController extends BaseController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentController.class);

	@Autowired
	@Qualifier("departmentService")
	private DepartmentService departmentService;

	@Autowired
	@Qualifier("roleService")
	private RoleService roleService;

	@Autowired
	@Qualifier("staffService")
	private StaffService staffService;

	/**
	 * ���ݲ���id��ѯ������Ϣ
	 * @param departmentId	����id
	 * @return
	 * @author WML
	 * 2016��11��7�� - ����5:55:21
	 */
	@RequestMapping({ "/findDepartment.ajax" })
	@ResponseBody
	public Map<String, ? extends Object> findRoles(Long departmentId) {
		try {
			if (departmentId == null) {
				throw new Exception("��ѯ����ID����Ϊ��");
			}
			Department d = this.departmentService.findDepartment(departmentId);
			if (d == null) {
				throw new Exception("�Ҳ�����Ӧ�Ĳ���[id=" + departmentId + "]");
			}
			return success(d);
		} catch (Exception e) {
			LOGGER.error("����[id=" + departmentId + "]��ѯ��ɫ����", e);
			return fail(e.getMessage());
		}
	}

	/**
	 * ���²�����Ϣ
	 * @param department
	 * @return
	 * @author WML
	 * 2016��11��7�� - ����5:55:46
	 */
	@RequestMapping(value = { "/updateDepartment.ajax" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseBody
	public Map<String, ? extends Object> updateDepartment(Department department) {
		try {
			String text = "";
			if (department == null) {
				throw new Exception("�ն������");
			}
			if (isEmpty(department.getDepartmentId())) {
				department.setAddSub("1");
				if (StaffUtil.getLoginStaff() == null)
					department.setCreateUser("system");
				else {
					department.setCreateUser(StaffUtil.getLoginStaff().getLoginName());
				}
				this.departmentService.insertDepartment(department);
				text = "�������ųɹ�";
			} else {
				this.departmentService.updateDepartment(department);
				text = "�޸Ĳ��ųɹ�";
			}
			LOGGER.debug(text);
			return success(department);
		} catch (Exception e) {
			LOGGER.error("�޸Ĳ�����Ϣ����", e);
			return fail(e.getMessage());
		}
	}

	/**
	 * ɾ��������Ϣ
	 * @param departmentId
	 * @return
	 * @author WML
	 * 2016��11��7�� - ����5:56:05
	 */
	@RequestMapping({ "/delDepartment.ajax" })
	@ResponseBody
	public Map<String, ? extends Object> delDepartment(Long departmentId) {
		try {
			if (departmentId == null) {
				throw new Exception("����ID����Ϊ��");
			}
			Staff staff = StaffUtil.getLoginStaff();
			if ((null != staff) && (staff.getDepartmentId() == departmentId)) {
				throw new Exception("��û��Ȩ��ɾ����ǰ��֯!");
			}
			this.departmentService.deleteDepartment(departmentId);
			return success("ɾ�����ųɹ�");
		} catch (Exception e) {
			LOGGER.error("����[departmentId]=" + departmentId + ",ɾ�����ų���", e);
			return fail(e.getMessage());
		}
	}

	/**
	 * ��ȡ������Ϣ
	 * @return
	 * @author WML
	 * 2016��11��7�� - ����5:56:15
	 */
	@RequestMapping({ "/listDepartmentTree.ajax" })
	@ResponseBody
	public Map<String, ? extends Object> listDepartmentTree() {
		try {
			return success(this.departmentService.buildDepartmentTree());
		} catch (Exception e) {
			LOGGER.error("��ȡ��֯�б����", e);
			return fail(e.getMessage());
		}
	}

	/**
	 * ���ݲ���id��ѯ���Ŷ�Ӧ�Ľ�ɫ��Ϣ
	 * @param departmentId
	 * @return
	 * @author WML
	 * 2016��11��7�� - ����5:56:50
	 */
	@RequestMapping({ "/findDepartmentInfo.ajax" })
	@ResponseBody
	public Map<String, ? extends Object> findDepartmentInfo(Long departmentId) {
		try {
			if (departmentId == null) {
				throw new Exception("��ѯ����ID����Ϊ��");
			}
			Department d = this.departmentService.findDepartment(departmentId);
			if (d == null) {
				throw new Exception("�Ҳ�����Ӧ�Ĳ���[id=" + departmentId + "]");
			}
			List<Role> roles = this.roleService.listDepartmentRoles(departmentId);
			d.addOtherField("roles", roles);
			List<Staff> staffs = this.staffService.listDepartmentStaffs(departmentId);
			d.addOtherField("staffs", staffs);
			return success(d);
		} catch (Exception e) {
			LOGGER.error("����[departmentId]=" + departmentId + ",��ѯ���ų���", e);
			return fail(e.getMessage());
		}
	}

	/**
	 * ���ݲ���id��ѯ��Ӧ�Ľ�ɫ����
	 * @param departmentId
	 * @return
	 * @author WML
	 * 2016��11��7�� - ����5:57:15
	 */
	@RequestMapping({ "/listDepartmentRoles.ajax" })
	@ResponseBody
	public Map<String, ? extends Object> listDepartmentRoles(Long departmentId) {
		try {
			return page(this.departmentService
					.listDepartmentRoles(departmentId));
		} catch (Exception e) {
			LOGGER.error("����[departmentId]=" + departmentId + ",��ѯ��ɫ����", e);
			return fail(e.getMessage());
		}
	}

	/**
	 * ��ӻ�ɾ����ɫ��Ϣ
	 * @param operation
	 * @param departmentId
	 * @param roleId
	 * @return
	 * @author WML
	 * 2016��11��7�� - ����5:58:08
	 */
	@RequestMapping(value = { "/updateDepartmentRole.ajax" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseBody
	public Map<String, ? extends Object> updateStaffRole(
			@RequestParam String operation, @RequestParam Long departmentId,
			@RequestParam Long roleId) {
		try {
			if ((isEmpty(operation)) || (isEmpty(departmentId))
					|| (isEmpty(roleId))) {
				throw new Exception("����Ϊ��");
			}
			if ("add".equalsIgnoreCase(operation)) {
				this.departmentService.insertDepartmentRole(departmentId, roleId);
				return success("��ӽ�ɫ�ɹ���");
			}
			this.departmentService.deleteDepartmentRoleByRoleIdAndDeptId(departmentId, roleId);
			return success("ɾ����ɫ�ɹ���");
		} catch (Exception e) {
			LOGGER.error("����[][departmentId]=" + departmentId + ",��ѯ��ɫ����", e);
			return fail(e.getMessage());
		}
	}

	/**
	 * ���²����û���Ϣ
	 * @param departmentId
	 * @param staffIds
	 * @param oraginalIds
	 * @return
	 * @author WML
	 * 2016��11��7�� - ����5:59:38
	 */
	@RequestMapping(value = { "/updateStaffDepartment.ajax" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseBody
	public Map<String, ? extends Object> updateStaffDepartment(
			@RequestParam Long departmentId, @RequestParam String staffIds,
			@RequestParam String oraginalIds) {
		try {
			if (isEmpty(departmentId)) {
				throw new Exception("����Id����Ϊ��");
			}
			if (isEmpty(staffIds)) {
				staffIds = "''";
			}
			if (isEmpty(oraginalIds)) {
				oraginalIds = "''";
			}
			this.staffService.updateStaffDepartment(null, wrapIds(oraginalIds));
			this.staffService.updateStaffDepartment(departmentId, wrapIds(staffIds));
			return success("���²����û��ɹ�");
		} catch (Exception e) {
			LOGGER.error("����[staffIds]=" + staffIds + ",[oraginalIds]="
					+ oraginalIds + ",[departmentId]=" + departmentId
					+ ",���²����û�����", e);
			return fail(e.getMessage());
		}
	}

	/**
	 * ��ȡid
	 * @param ids
	 * @return
	 * @author WML
	 * 2016��11��7�� - ����5:58:46
	 */
	private String wrapIds(String ids) {
		if (ids.indexOf(",") > 0) {
			return "'" + ids.replace(",", "','") + "'";
		}
		return ids;
	}
}