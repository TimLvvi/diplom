package ru.skypro.homework.mapper;

import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.entity.UserEntity;

@Component
public class UpdateUserMapper {
    public void updateEntity(UpdateUser dto, UserEntity entity) {
        if (dto == null) return;
        if (dto.getFirstName() != null) entity.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null) entity.setLastName(dto.getLastName());
        if (dto.getPhone() != null) entity.setPhone(dto.getPhone());
    }

    public UpdateUser toDto(UserEntity entity) {
        if (entity == null) return null;
        UpdateUser dto = new UpdateUser();
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setPhone(entity.getPhone());
        return dto;
    }
}
