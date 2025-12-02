package com.eatnote.dataobject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class OrgDTO {
    private String id;
    private String name;
    private String parentId;
    private List<OrgDTO> orgDTOs;
}
