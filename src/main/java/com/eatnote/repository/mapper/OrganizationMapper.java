package com.eatnote.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.eatnote.entity.Organization;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrganizationMapper extends BaseMapper<Organization> {
}
