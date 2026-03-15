package ru.skypro.homework.mapper;

import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.entity.AdEntity;

@Component
public class CreateOrUpdateAdMapper {
    public void updateEntity(CreateOrUpdateAd dto, AdEntity entity) {
        if (dto == null) return;
        if (dto.getTitle() != null) entity.setTitle(dto.getTitle());
        if (dto.getPrice() != null) entity.setPrice(dto.getPrice());
        if (dto.getDescription() != null) entity.setDescription(dto.getDescription());
    }
}
