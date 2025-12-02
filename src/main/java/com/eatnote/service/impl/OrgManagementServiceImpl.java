package com.eatnote.service.impl;

import com.eatnote.dataobject.OrgDTO;
import com.eatnote.entity.Organization;
import com.eatnote.exception.NotFoundException;
import com.eatnote.repository.OrgRepository;
import com.eatnote.service.OrgManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrgManagementServiceImpl implements OrgManagementService {

    @Autowired
    private OrgRepository orgRepository;

    @Override
    public OrgDTO createOrg(OrgDTO orgDTO) {
        String id = UUID.randomUUID().toString();
        orgDTO.setId(id);
        orgDTO.setParentId(orgDTO.getParentId()==null?"-1": orgDTO.getParentId());
        orgRepository.save(orgDTO);
        return orgDTO;
    }

    @Override
    public void updateOrg(OrgDTO orgDTO) {
        OrgDTO org = orgRepository.findById(orgDTO.getId());
        if (org == null) {
            throw new NotFoundException();
        }
        orgDTO.setParentId(orgDTO.getParentId()==null?"-1": orgDTO.getParentId());
        orgRepository.update(orgDTO);
    }

    /**
     * 默认展示org tree list
     *
     * @param search
     * @return
     */
    @Override
    public List<OrgDTO> listOrgs(String search) {
        List<OrgDTO> list = listChildren("-1");
        return list;
    }

    private List<OrgDTO> listChildren(String id) {
        List<Organization> list = orgRepository.findByParentId(id);
        List<OrgDTO> result = new ArrayList<>();
        for (Organization organization : list) {
            if (organization == null) continue;
            OrgDTO orgDTO = new OrgDTO();
            orgDTO.setName(organization.getName());
            orgDTO.setParentId(organization.getParentId());
            orgDTO.setId(organization.getId());
            orgDTO.setOrgDTOs(listChildren(orgDTO.getId()));
            result.add(orgDTO);
        }
        return result;
    }

    @Override
    public OrgDTO getOrg(String id) {
        return orgRepository.findById(id);
    }

    @Override
    public void deleteOrg(String id) {
        orgRepository.deleteById(id);
    }

//    private Map<String, OrgDTO> getOrgDB() {
//        if (CollectionUtils.isEmpty(orgDB)) {
//            OrgDTO mainOrg = new OrgDTO("1", "tech",null,null);
//            OrgDTO subOrg2 = new OrgDTO("2", "tech 1", mainOrg.getId(),null);
//            orgDB.put(mainOrg.getId(), mainOrg);
//            orgDB.put(subOrg2.getId(), subOrg2);
//            childrenOrgsDB.put(subOrg2.getParentId(),List.of(subOrg2));
//        }
//        return orgDB;
//    }
}
