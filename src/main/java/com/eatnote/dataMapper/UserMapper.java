package com.eatnote.dataMapper;


import com.eatnote.dataobject.UserDTO;
import com.eatnote.entity.User;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public abstract  class UserMapper {

//    @Mapping(target = "descriptionFiles", ignore = true)
    public abstract UserDTO entityToDTO(User user);

    public abstract User dtoToEntity(UserDTO userDTO);
}
