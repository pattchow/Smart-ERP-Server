package com.eatnote.service;

import com.eatnote.dataobject.OrgDTO;

import java.util.List;

public interface OrgManagementService {
    OrgDTO createOrg(OrgDTO orgDTO);

    void updateOrg(OrgDTO orgDTO);

    List<OrgDTO> listOrgs(String search);

    OrgDTO getOrg(String id);

    void deleteOrg(String id);
}
