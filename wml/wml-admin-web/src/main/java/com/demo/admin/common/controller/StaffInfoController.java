package com.demo.admin.common.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.aspire.webbas.core.pagination.mybatis.pager.Page;
import com.aspire.webbas.portal.common.entity.Staff;
import com.demo.admin.common.service.SecStaffService;
import com.demo.common.entity.SecStaff;

/**
 * 员工信息管理Controller
 * <pre>
 * <b>Title：</b>StaffInfoController.java<br/>
 * <b>@author： </b>WML<br/>
 * <b>@date：</b>2016年10月31日 上午10:26:58<br/>  
 * <b>Copyright (c) 2016 ASPire Tech.</b>   
 * </pre>
 */
@Controller
@RequestMapping("/staff")
public class StaffInfoController extends BaseActionController {
    
    /** 员工管理Service  */
    @Autowired
    private SecStaffService secStaffService;
    
    /**
     * 列表
     * @param page
     * @return
     * @author WML
     * 2016年11月8日 - 下午2:28:25
     */
    @RequestMapping(value = "/query.ajax")
    @ResponseBody
    public Map<String, Object> pageQuery(Page<SecStaff> page) {
        Page<SecStaff> list = secStaffService.pageQuery(page);
        return super.page(list);
    }
    
    /**
     * 根据登录名查询用户信息
     * @return
     * @author WML
     * 2016年11月8日 - 下午2:28:22
     */
    @RequestMapping(value = "/selectStaffInfoByName.ajax", method = RequestMethod.POST)
    public Map<String, Object> selectStaffInfoByName(String staffName, HttpServletRequest request) {
    	return super.success(null);
    }
    
    /**
     * 添加用户信息
     * @param staff
     * @param request
     * @return
     * @author WML
     * 2016年11月8日 - 下午2:40:32
     */
    @RequestMapping(value = "/add.ajax", method = RequestMethod.POST)
    public Map<String, Object> add(Staff staff, HttpServletRequest request) {
    	
    	return super.success(null);
    }
    
    /**
     * 根据id删除用户
     * @param staffId
     * @return
     * @author WML
     * 2016年11月8日 - 下午2:29:01
     */
    @RequestMapping(value = "/delete.ajax")
    @ResponseBody
    public Map<String, Object> delete(Long staffId, HttpServletRequest request) {
        secStaffService.deleteByPrimaryKey(staffId);
        return super.success("删除成功");
    }
    
    /**
     * 导出数据
     * @author WML
     * 2016年11月8日 - 下午2:38:38
     */
    @RequestMapping(value = "/staffExport.ajax", method = RequestMethod.POST)
    public void staffExport() {
    	
    }
}
