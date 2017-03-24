package com.demo.admin.common.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aspire.webbas.core.pagination.mybatis.pager.Page;
import com.demo.common.dao.SecStaffMapper;
import com.demo.common.entity.SecStaff;

/**
 * 员工管理Service
 * <pre>
 * <b>Title：</b>SecStaffService.java<br/>
 * <b>@author： </b>WML<br/>
 * <b>@date：</b>2016年10月31日 下午3:43:52<br/>  
 * <b>Copyright (c) 2016 ASPire Tech.</b>   
 * </pre>
 */
@Service
public class SecStaffService {

    /** 成员Mapper  */
    @Autowired
    private SecStaffMapper secStaffMapper;
    
    /**
     * 数据列表
     * @param page
     * @return
     * @author WML
     * 2016年10月31日 - 下午3:45:48
     */
    public Page<SecStaff> pageQuery(Page<SecStaff> page) {
        List<SecStaff> list = secStaffMapper.pageQuery(page);
        page.setDatas(list);
        return page;
    }
    
    /**
     * 根据主键id查询数据
     * @param  staffId  主键id
     * @return SecStaff对象
     * @author WML
     * 2016-10-31 15:37:54  Created
     */
    public SecStaff selectByPrimaryKey(Long staffId) {
        return secStaffMapper.selectByPrimaryKey(staffId);
    }
    
    /**
     * 根据主键id删除数据
     * @param  staffId  主键id    
     * @return 影响行数
     * @author WML
     * 2016-10-31 15:37:54  Created
     */
    public int deleteByPrimaryKey(Long staffId) {
        return secStaffMapper.deleteByPrimaryKey(staffId);
    }
    
}
