package com.agileboot.domain.module.bom.db;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import org.apache.ibatis.annotations.Param;

/**
 * @author jinani7
 * @ClassName BomService
 * @description: TODO
 * @date 2024年01月31日
 * @version: 1.0
 */
public interface BomService extends IService<BomEntity> {


    Page<BomEntity> getBomList(Page<BomEntity> page,
                                        @Param(Constants.WRAPPER) Wrapper<BomEntity> queryWrapper);

    boolean isBomNameDuplicated(Long bomId, String bomName);

    int deleteBomInfo(Long bomId);
}
