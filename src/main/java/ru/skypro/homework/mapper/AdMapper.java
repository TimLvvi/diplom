package ru.skypro.homework.mapper;

import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.entity.AdEntity;


import java.util.List;
import java.util.stream.Collectors;

@Component
public class AdMapper {
    public Ad toAdDto(AdEntity entity) {
        if (entity == null) return null;
        Ad dto = new Ad();
        dto.setAuthor(entity.getAuthor().getId());
        dto.setImage(entity.getImage());
        dto.setPk(entity.getPk());
        dto.setPrice(entity.getPrice());
        dto.setTitle(entity.getTitle());
        return dto;
    }

    public ExtendedAd toExtendedAdDto(AdEntity entity) {
        if (entity == null) return null;
        ExtendedAd dto = new ExtendedAd();
        dto.setPk(entity.getPk());
        dto.setAuthorFirstName(entity.getAuthor().getFirstName());
        dto.setAuthorLastName(entity.getAuthor().getLastName());
        dto.setDescription(entity.getDescription());
        dto.setEmail(entity.getAuthor().getEmail());
        dto.setImage(entity.getImage());
        dto.setPhone(entity.getAuthor().getPhone());
        dto.setPrice(entity.getPrice());
        dto.setTitle(entity.getTitle());
        return dto;
    }

    public Ads toAdsDto(List<AdEntity> entities) {
        Ads ads = new Ads();
        ads.setCount(entities.size());
        ads.setResults(entities.stream().map(this::toAdDto).collect(Collectors.toList()));
        return ads;
    }
}
