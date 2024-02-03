package com.agileboot.domain.module.bom.db;

import com.agileboot.domain.system.notice.db.SysNoticeEntity;
import com.agileboot.domain.system.post.db.SysPostEntity;
import com.agileboot.domain.system.user.db.SysUserMapper;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.stereotype.Service;

/**
 * <p>
 * 通知公告表 服务实现类
 * </p>
 *
 * @author valarchie
 * @since 2022-06-16
 */
@Service
public class BomServiceImpl extends ServiceImpl<BomMapper, BomEntity> implements BomService {

    private final BomInfoMapper bomInfoMapper;

    public BomServiceImpl(BomInfoMapper bomInfoMapper) {
        this.bomInfoMapper = bomInfoMapper;
    }

    @Override
    public Page<BomEntity> getBomList(Page<BomEntity> page, Wrapper<BomEntity> queryWrapper) {
        return this.baseMapper.getBomList(page, queryWrapper);
    }

    @Override
    public boolean isBomNameDuplicated(Long bomId, String bomName) {
        QueryWrapper<BomEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.ne(bomId != null, "bom_id", bomId)
                .eq("bom_name", bomName);
        return baseMapper.exists(queryWrapper);
    }

    @Override
    public int deleteBomInfo(Long bomId) {
        QueryWrapper<BomInfoEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("bom_id", bomId);
        return bomInfoMapper.delete(queryWrapper);
    }
}
