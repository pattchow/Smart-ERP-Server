package com.eatnote.repository;

import com.eatnote.dataobject.OrgDTO;
import com.eatnote.entity.Organization;

import java.util.List;

public interface OrgRepository {
    void save(OrgDTO org);

    void update(OrgDTO org);

    OrgDTO findById(String id);

    List<Organization> findByName(String name);

    List<Organization> findByParentId(String parentId);

    boolean deleteById(String id);
}
