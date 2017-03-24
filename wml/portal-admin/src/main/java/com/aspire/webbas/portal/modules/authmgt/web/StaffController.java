package com.aspire.webbas.portal.modules.authmgt.web;

import com.aspire.webbas.core.pagination.mybatis.pager.Page;
import com.aspire.webbas.core.web.BaseController;
import com.aspire.webbas.portal.common.config.StaffExtendPropertyConfig;
import com.aspire.webbas.portal.common.config.StaffField;
import com.aspire.webbas.portal.common.entity.City;
import com.aspire.webbas.portal.common.entity.Department;
import com.aspire.webbas.portal.common.entity.Role;
import com.aspire.webbas.portal.common.entity.Staff;
import com.aspire.webbas.portal.common.service.CityService;
import com.aspire.webbas.portal.common.service.DepartmentService;
import com.aspire.webbas.portal.common.service.RoleService;
import com.aspire.webbas.portal.common.service.StaffService;
import com.aspire.webbas.portal.common.util.RSAUtil;
import com.aspire.webbas.portal.common.util.StaffUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Ա��Controller
 * <pre>
 * <b>Title��</b>StaffController.java<br/>
 * <b>@author��</b>WML<br/>
 * <b>@date��</b>2016��11��8�� - ����8:55:43<br/>  
 * <b>@version v1.0</b></br/>
 * <b>Copyright (c) 2016 ASPire Tech.</b>   
 * </pre>
 */
@Controller
@RequestMapping({ "/staff" })
public class StaffController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(StaffController.class);

	@Autowired
	@Qualifier("staffService")
	private StaffService staffService;

	@Autowired
	@Qualifier("departmentService")
	private DepartmentService departmentService;

	@Autowired
	@Qualifier("roleService")
	private RoleService roleService;

	@Autowired
	@Qualifier("cityService")
	private CityService cityService;

	/**
	 * ���Ա����Ϣ
	 * @param staff
	 * @return
	 * @throws Exception
	 * @author WML
	 * 2016��11��8�� - ����8:55:56
	 */
	@RequestMapping(value = { "/createStaff.ajax" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseBody
	public Map<String, ? extends Object> createStaff(Staff staff)
			throws Exception {
		try {
			if (isNotEmpty(staff.getStaffId())) {
				staff.setLastUpdateDate(new Date());
				this.staffService.updateStaff(staff);
			} else {
				if (staff.getStatus() == null) {
					staff.setStatus(Staff.Status.INITIAL);
				}
				if (staff.getCreateUser() == null) {
					if (StaffUtil.getLoginStaff() == null) {
						staff.setCreateUser("nouser");
					} else {
						staff.setCreateUser(StaffUtil.getLoginStaff().getLoginName());
					}
				}
				this.staffService.createStaff(staff);
			}
		} catch (Exception e) {
			logger.error("�����û���Ϣ����", e);
			return fail(e.getMessage());
		}
		return success("�����û��ɹ���");
	}

	/**
	 * ����id��ѯԱ����Ϣ
	 * @param staffId
	 * @return
	 * @author WML
	 * 2016��11��8�� - ����8:56:14
	 */
	@RequestMapping(value = { "/findStaff.ajax" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseBody
	public Map<String, ? extends Object> findStaff(Long staffId) {
		try {
			if (isEmpty(staffId)) {
				throw new Exception("�û�Id��������Ϊ��");
			}
			Staff staff = this.staffService.findStaff(staffId);
			if (staff == null) {
				throw new Exception(new StringBuilder().append("û���ҵ��û�[id=").append(staffId).append("]").toString());
			}
			staff.setOthers(packStaffOthers(staff));
			return success(staff);
		} catch (Exception e) {
			logger.error(new StringBuilder().append("����staffId=").append(staffId).append(",��ѯ�û���Ϣ����").toString(), e);
			return fail(e.getMessage());
		}
	}

	/**
	 * ��ѯԱ����Դ
	 * @return
	 * @author WML
	 * 2016��11��8�� - ����8:56:45
	 */
	@RequestMapping({ "/findStaffExtendProperties.ajax" })
	@ResponseBody
	public Map<String, ? extends Object> findStaffExtendProperties() {
		try {
			List<StaffField> staffFields = new ArrayList<StaffField>();
			if (StaffExtendPropertyConfig.getInstance().isStaffExtendPropertyOn()) {
				staffFields = StaffExtendPropertyConfig.getInstance().getStaffFileds();
			}
			return success(staffFields);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return fail(e.getMessage());
		}
	}

	/**
	 * ɾ���û�
	 * @param staffId
	 * @return
	 * @author WML
	 * 2016��11��8�� - ����8:58:08
	 */
	@RequestMapping(value = { "/deleteStaff.ajax" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseBody
	public Map<String, ? extends Object> deleteStaff(Long staffId) {
		try {
			if (isEmpty(staffId)) {
				throw new Exception("staffId����Ϊ��");
			}
			Long[] staffs = { staffId };
			this.staffService.deleteStaffs(staffs);
		} catch (Exception e) {
			logger.error(new StringBuilder().append("����staffId=").append(staffId).append(",ɾ���û���Ϣ����").toString(), e);
			return fail(e.getMessage());
		}
		return success("ɾ���û��ɹ���");
	}

	/**
	 * Ա���б�
	 * @param page
	 * @return
	 * @author WML
	 * 2016��11��8�� - ����9:01:26
	 */
	@RequestMapping({ "/listStaff.ajax" })
	@ResponseBody
	public Map<String, ? extends Object> listStaff(Page<Staff> page) {
		try {
			page.getParams().put("departmentIds", transferList2QueryStr(this.departmentService.findMyDepartmentAndChildrenDeptIds()));
			return page(this.staffService.listStaff(page));
		} catch (Exception e) {
			logger.error("��ѯ�û���Ϣ����", e);
			return fail(e.getMessage());
		}
	}

	/**
	 * ������id����תΪ��","�ŵ��ַ���
	 * @param deptIds
	 * @return
	 * @author WML
	 * 2016��11��8�� - ����9:01:39
	 */
	private String transferList2QueryStr(List<String> deptIds) {
		StringBuilder sb = new StringBuilder();
		for (String id : deptIds) {
			if (!sb.toString().isEmpty()) {
				sb.append(",");
			}
			sb.append(id);
		}
		return sb.toString();
	}

	/**
	 * ����Ա����Ϣչʾ��Ӧ����-����-��ɫ��Ϣ
	 * @param staff
	 * @return
	 * @throws Exception
	 * @author WML
	 * 2016��11��8�� - ����9:01:44
	 */
	private Map<String, Object> packStaffOthers(Staff staff) throws Exception {
		Map<String, Object> others = new HashMap<String, Object>();
		if (isNotEmpty(staff.getDepartmentId())) {
			List<Department> list = this.departmentService.listPathFromRootToCurrentDepartmentId(staff.getDepartmentId());
			if ((null != list) && (list.size() > 0)) {
				StringBuilder sb = new StringBuilder();
				for (int i = list.size() - 1; i >= 0; i--)
					if (((Department) list.get(i)).getDepartmentId().longValue() != -999L) {
						if (!sb.toString().isEmpty()) {
							sb.append("/");
						}
						sb.append(((Department) list.get(i)).getDepartmentName());
					}
				others.put("departmentName", sb.toString());
			}
		}
		if (null != staff.getCityId()) {
			City city = this.cityService.get(staff.getCityId());
			if (null != city) {
				others.put("city", city);
			}
		}
		List<Role> roles = this.roleService.listStaffRoles(staff.getStaffId());
		others.put("roles", roles);
		return others;
	}

	/**
	 * �����û�
	 * @param operation
	 * @param staffId
	 * @return
	 * @author WML
	 * 2016��11��8�� - ����9:05:44
	 */
	@RequestMapping(value = { "/lockStaff.ajax" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseBody
	public Map<String, ? extends Object> lockStaff(
			@RequestParam String operation, @RequestParam Long staffId) {
		try {
			if ((isEmpty(operation)) || (isEmpty(staffId))) {
				throw new Exception("����Ϊ��");
			}
			if ("lock".equalsIgnoreCase(operation)) {
				this.staffService.lockStaff(staffId);
				return success("�����û��ɹ���");
			}
			this.staffService.unlockStaff(staffId);
			return success("�����û��ɹ���");
		} catch (Exception e) {
			logger.error("����/�����û�����", e);
			return fail(e.getMessage());
		}
	}

	/**
	 * �����û���ɫ
	 * @param operation
	 * @param staffId
	 * @param roleId
	 * @return
	 * @author WML
	 * 2016��11��8�� - ����9:05:54
	 */
	@RequestMapping(value = { "/updateStaffRole.ajax" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseBody
	public Map<String, ? extends Object> updateStaffRole(
			@RequestParam String operation, @RequestParam Long staffId,
			@RequestParam Long roleId) {
		try {
			if ((isEmpty(operation)) || (isEmpty(staffId)) || (isEmpty(roleId))) {
				throw new Exception("����Ϊ��");
			}
			if ("add".equalsIgnoreCase(operation)) {
				this.staffService.insertStaffRole(staffId, roleId);
				return success("��ӽ�ɫ�ɹ���");
			}
			Role role = this.roleService.findRole(roleId);
			Staff staff = StaffUtil.getLoginStaff();
			if ((staffId.equals(staff.getStaffId())) && ("1001".equals(role.getRoleKey()))) {
				throw new Exception("��������Ա����ɾ���Լ�ӵ�е�ϵͳ�����ɫ");
			}
			this.staffService.deleteStaffRole(staffId, roleId);
			return success("ɾ����ɫ�ɹ���");
		} catch (Exception e) {
			logger.error("ɾ����ɫ����", e);
			return fail(e.getMessage());
		}
	}

	/**
	 * �û���ɫ�б�
	 * @param staffId
	 * @return
	 * @author WML
	 * 2016��11��8�� - ����9:06:13
	 */
	@RequestMapping({ "/listStaffRoles.ajax" })
	@ResponseBody
	public Map<String, ? extends Object> listStaffRoles(Long staffId) {
		try {
			if (staffId == null) {
				throw new Exception("�û�idΪ��");
			}
			return page(this.staffService.listStaffRoles(staffId));
		} catch (Exception e) {
			logger.error("����:", e);
			return fail(e.getMessage());
		}
	}

	/**
	 * ����¼���Ƿ����
	 * @param loginName
	 * @return
	 * @author WML
	 * 2016��11��8�� - ����9:06:39
	 */
	@RequestMapping({ "/checkStaffLoginName.ajax" })
	@ResponseBody
	public String checkStaffLoginName(String loginName) {
		try {
			if (isEmpty(loginName)) {
				return "����Ϊ��";
			}
			Staff staff = this.staffService.findStaffByLoginName(loginName);
			if (staff != null)
				return "�û����Ѵ���";
		} catch (Exception e) {
			logger.error(new StringBuilder().append("����û���[").append(loginName).append("]�Ƿ���ڳ���").toString(), e);
			return "����ϵͳ�쳣";
		}
		return "true";
	}

	/**
	 * ����ֻ������Ƿ����
	 * @param mobile
	 * @return
	 * @author WML
	 * 2016��11��8�� - ����9:06:57
	 */
	@RequestMapping({ "/checkStaffMobile.ajax" })
	@ResponseBody
	public String checkStaffMobile(String mobile) {
		try {
			if (isEmpty(mobile)) {
				return "����Ϊ��";
			}
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("mobile", mobile);
			Staff staff = this.staffService.findStaffByMap(params);
			if (staff != null)
				return "�ֻ������Ѵ���";
		} catch (Exception e) {
			logger.error(new StringBuilder().append("����ֻ�����[").append(mobile).append("]�Ƿ���ڳ���").toString(), e);
			return "����ϵͳ�쳣";
		}
		return "true";
	}

	/**
	 * ����û��������Ƿ����
	 * @param email
	 * @return
	 * @author WML
	 * 2016��11��8�� - ����9:07:12
	 */
	@RequestMapping({ "/checkStaffEmail.ajax" })
	@ResponseBody
	public String checkStaffEmail(String email) {
		try {
			if (isEmpty(email)) {
				return "����Ϊ��";
			}
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("email", email);
			Staff staff = this.staffService.findStaffByMap(params);
			if (staff != null)
				return "�����Ѵ���";
		} catch (Exception e) {
			logger.error(new StringBuilder().append("�������[").append(email).append("]�Ƿ���ڳ���").toString(), e);
			return "����ϵͳ�쳣";
		}
		return "true";
	}

	/**
	 * ��ѯ��¼�û���Ϣ
	 * @return
	 * @throws Exception
	 * @author WML
	 * 2016��11��8�� - ����9:07:25
	 */
	@RequestMapping({ "/findLoginStaff.ajax" })
	@ResponseBody
	public Map<String, ? extends Object> findLoginStaff() throws Exception {
		try {
			Staff staff = StaffUtil.getLoginStaff();
			if (staff == null) {
				throw new Exception("�û�û�е�¼");
			}
			Staff staff2 = this.staffService.findStaffByLoginName(staff.getLoginName());
			staff2.setOthers(packStaffOthers(staff2));
			return success(staff2);
		} catch (Exception e) {
			logger.error("��ȡ��¼�û���Ϣ����=>", e);
			return fail(e.getMessage());
		}
	}

	/**
	 * �޸�����
	 * @param oldPassword
	 * @param newPassword
	 * @return
	 * @throws Exception
	 * @author WML
	 * 2016��11��8�� - ����9:07:42
	 */
	@RequestMapping({ "/changePwd.ajax" })
	@ResponseBody
	public Map<String, ? extends Object> changePwd(String oldPassword, String newPassword) throws Exception {
		try {
			Staff staff = StaffUtil.getLoginStaff();
			if (staff == null) {
				throw new Exception("�û�û�е�¼");
			}
			if ((isEmpty(oldPassword)) || (isEmpty(newPassword))) {
				throw new IllegalArgumentException("�������������û�����ã�");
			}
			String oldPasswordDecrypt = RSAUtil.decryptString(oldPassword);
			String newPasswordDecrypt = RSAUtil.decryptString(newPassword);
			this.staffService.changePassword(staff.getLoginName(), oldPasswordDecrypt, newPasswordDecrypt);

			return success("�޸ĳɹ�");
		} catch (Exception e) {
			logger.error("��ȡ��¼�û���Ϣ����=>", e);
			return fail(e.getMessage());
		}
	}

	/**
	 * ��������
	 * @param loginName
	 * @param password
	 * @return
	 * @throws Exception
	 * @author WML
	 * 2016��11��8�� - ����9:08:01
	 */
	@RequestMapping({ "/resetPwd.ajax" })
	@ResponseBody
	public Map<String, ? extends Object> resetPwd(String loginName,
			String password) throws Exception {
		try {
			if (isEmpty(password)) {
				throw new IllegalArgumentException("������û�����ã�");
			}
			String passwordDecrypt = RSAUtil.decryptString(password);
			this.staffService.resetPassword(loginName, passwordDecrypt);
			return success("���óɹ�");
		} catch (Exception e) {
			logger.error("��ȡ��¼�û���Ϣ����=>", e);
			return fail(e.getMessage());
		}
	}

	/**
	 * �����û���Ϣ
	 * @param staff
	 * @return
	 * @throws Exception
	 * @author WML
	 * 2016��11��8�� - ����9:08:11
	 */
	@RequestMapping(value = { "/updateStaff.ajax" }, method = {RequestMethod.POST })
	@ResponseBody
	public Map<String, ? extends Object> updateStaff(Staff staff)
			throws Exception {
		try {
			if (isEmpty(staff.getLoginName())) {
				throw new Exception("��������");
			}
			Staff staff2 = this.staffService.findStaffByLoginName(staff.getLoginName());
			staff2.setRealName(staff.getRealName());
			staff2.setSex(staff.getSex());
			staff2.setMobile(staff.getMobile());
			staff2.setTelephone(staff.getTelephone());
			staff2.setEmail(staff.getEmail());
			staff2.setExtendProperties(staff.getExtendProperties());
			this.staffService.updateStaff(staff2);
			StaffUtil.updateLoginStaff(staff2);
			return success("�޸ĳɹ�");
		} catch (Exception e) {
			logger.error("��ȡ��¼�û���Ϣ����=>", e);
			return fail(e.getMessage());
		}
	}

	/**
	 * �����֯��Ϣ
	 * @param departmentId
	 * @param staffIds
	 * @return
	 * @throws Exception
	 * @author WML
	 * 2016��11��8�� - ����9:08:39
	 */
	@RequestMapping(value = { "/updateStaffDepartment.ajax" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseBody
	public Map<String, ? extends Object> updateStaffDepartment(
			Long departmentId, String staffIds) throws Exception {
		try {
			if (isEmpty(departmentId)) {
				throw new Exception("��������");
			}
			this.staffService.updateStaffDepartment(departmentId, staffIds);
			return success("��ӳɹ�");
		} catch (Exception e) {
			logger.error("����û�����֯����=>", e);
			return fail(e.getMessage());
		}
	}

	/**
	 * ���ݲ��ź��û�id��ѯ��ɫ�б�
	 * @param departmentId
	 * @param staffIds
	 * @return
	 * @throws Exception
	 * @author WML
	 * 2016��11��8�� - ����9:08:56
	 */
	@RequestMapping(value = { "/listRoleByStaffIds.ajax" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseBody
	public Map<String, ? extends Object> listRoleByStaffIds(Long departmentId, String staffIds) throws Exception {
		try {
			return success(this.staffService.listRoleByStaffIds(departmentId, staffIds));
		} catch (Exception e) {
			logger.error("����û�����֯����=>", e);
			return fail(e.getMessage());
		}
	}

	/**
	 * �����û���ɫ��Ϣ
	 * @param departmentId
	 * @param staffIds
	 * @param staffIdRoles
	 * @return
	 * @throws Exception
	 * @author WML
	 * 2016��11��8�� - ����9:09:47
	 */
	@RequestMapping(value = { "/updateStaffRolesDepartment.ajax" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseBody
	public Map<String, ? extends Object> updateStaffRolesDepartment(Long departmentId, String staffIds, String staffIdRoles)
			throws Exception {
		try {
			if ((isEmpty(departmentId)) || (isEmpty(staffIds))) {
				throw new Exception("��������");
			}
			this.staffService.updateStaffRolesDepartment(departmentId, staffIds, staffIdRoles);
			return success("�޸ĳɹ�");
		} catch (Exception e) {
			logger.error("����û�����֯����=>", e);
			return fail(e.getMessage());
		}
	}
}