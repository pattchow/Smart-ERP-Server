package com.eatnote.dataobject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode
public class UserDTO {
    private String id;
    private String username;
    private String fullName;
    private String email;
}
