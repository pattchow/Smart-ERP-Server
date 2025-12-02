package com.eatnote.controller;

import com.eatnote.common.Result;
import com.eatnote.dataobject.OrgDTO;
import com.eatnote.service.OrgManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class OrgManagementController {
    @Autowired
    private OrgManagementService orgManagementService;

    @GetMapping("/organizations")
    public Result<List<OrgDTO>> listOrgs(@RequestParam(required = false) String search) {
        return Result.success(orgManagementService.listOrgs(search));
    }

    @GetMapping("/organizations/{id}")
    public Result<OrgDTO> getOrg(@PathVariable String id) {
        return Result.success(orgManagementService.getOrg(id));
    }

    @PutMapping("/organizations/{id}")
    public Result<Void> updateOrg(@PathVariable String id, @RequestBody OrgDTO orgDTO) {
        orgDTO.setId(id);
        orgManagementService.updateOrg(orgDTO);
        return Result.success();
    }

    @DeleteMapping("/organizations/{id}")
    public Result<Void> deleteOrg(@PathVariable String id) {
        orgManagementService.deleteOrg(id);

        return Result.success();
    }

    @PostMapping("/organizations")
    public Result<Void> createOrg(@RequestBody OrgDTO orgDTO) {
        orgManagementService.createOrg(orgDTO);
        return Result.success();
    }

}
