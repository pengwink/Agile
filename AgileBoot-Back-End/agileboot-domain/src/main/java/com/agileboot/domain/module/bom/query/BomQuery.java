package com.agileboot.domain.module.bom.query;

import com.agileboot.common.core.page.AbstractPageQuery;
import com.agileboot.domain.module.bom.db.BomEntity;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import cn.hutool.core.util.StrUtil;
import lombok.Data;

/**
 * @author jinani7
 */
@Data
public class BomQuery extends AbstractPageQuery<BomEntity> {

    private String bomName;
    private String creatorName;

    @Override
    public QueryWrapper<BomEntity> addQueryCondition() {
        QueryWrapper<BomEntity> queryWrapper = new QueryWrapper<BomEntity>()
                .like(StrUtil.isNotEmpty(bomName),"bom_name", bomName)
                .like(StrUtil.isNotEmpty(creatorName),"u.user_name", creatorName);
        this.setTimeRangeColumn("create_time");
        return queryWrapper;
    }
}
