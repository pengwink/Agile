package com.agileboot.domain.module.bom.dto;

import com.agileboot.common.annotation.ExcelColumn;
import com.agileboot.domain.common.cache.CacheCenter;
import com.agileboot.domain.module.bom.db.BomEntity;
import com.agileboot.domain.system.notice.db.SysNoticeEntity;
import com.agileboot.domain.system.user.db.SysUserEntity;

import java.util.Date;

import cn.hutool.core.bean.BeanUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jinani7
 */
@Data
public class BomDTO {

    public BomDTO(BomEntity entity) {
        if (entity != null) {
            this.bomId = entity.getBomId();
            this.bomName = entity.getBomName();
            this.createTime = entity.getCreateTime();
            SysUserEntity cacheUser = CacheCenter.userCache.getObjectById(entity.getCreatorId());
            if (cacheUser != null) {
                this.creatorName = cacheUser.getUsername();
            }
        }
    }

    private Long bomId;

    private String bomName;

    private Date createTime;

    private String creatorName;
}
