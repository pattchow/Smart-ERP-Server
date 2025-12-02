package com.eatnote.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.eatnote.dataobject.OrgDTO;
import com.eatnote.entity.Organization;
import com.eatnote.repository.OrgRepository;
import com.eatnote.repository.mapper.OrganizationMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrgRepositoryImpl extends ServiceImpl<OrganizationMapper, Organization> implements OrgRepository {
    @Override
    public void save(OrgDTO org) {
        Organization organization = new Organization();
        organization.setName(org.getName());
        organization.setParentId(org.getParentId());
        organization.setId(org.getId());
        organization.setCreatedTime(LocalDateTime.now());
        organization.setUpdatedTime(LocalDateTime.now());
        super.saveOrUpdate(organization);
    }

    @Override
    public void update(OrgDTO org) {
        Organization organization = new Organization();
        organization.setName(org.getName());
        organization.setParentId(org.getParentId());
        organization.setId(org.getId());
        organization.setUpdatedTime(LocalDateTime.now());
        baseMapper.updateById(organization);
    }

    @Override
    public OrgDTO findById(String id) {
        Organization organization = baseMapper.selectById(id);
        OrgDTO orgDTO = new OrgDTO(organization.getId(), organization.getName(), organization.getParentId(), null);
        return orgDTO;
    }

    @Override
    public List<Organization> findByName(String name) {
        return List.of();
    }

    @Override
    public List<Organization> findByParentId(String parentId) {
        LambdaQueryWrapper<Organization> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Organization::getParentId, parentId);
        return list(wrapper);
    }

    @Override
    public boolean deleteById(String id) {
        return removeById(id);
    }
}
