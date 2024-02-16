package com.agileboot.domain.module.bom.db;

import com.agileboot.domain.system.notice.db.SysNoticeEntity;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 通知公告表 Mapper 接口
 * </p>
 *
 * @author valarchie
 * @since 2022-06-16
 */
public interface BomMapper extends BaseMapper<BomEntity> {

    /**
     * 根据条件分页查询角色关联的用户列表
     *
     * @param page         分页对象
     * @param queryWrapper 条件选择器
     * @return 分页处理后的公告列表
     */
    @Select("SELECT b.* "
        + "FROM inventory_bom b "
        + "LEFT JOIN sys_user u ON b.creator_id = u.user_id"
        + " ${ew.customSqlSegment}"
        + "WHERE b.deleted!=1")
    Page<BomEntity> getBomList(Page<BomEntity> page,
        @Param(Constants.WRAPPER) Wrapper<BomEntity> queryWrapper);

}
