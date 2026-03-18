package ru.skypro.homework.service;



import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.mapper.CreateOrUpdateAdMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.UserRepository;


@Service
public class AdService {

    private final AdRepository adRepository;
    private final UserRepository userRepository;
    private final AdMapper adMapper;
    private final CreateOrUpdateAdMapper createOrUpdateAdMapper;

    public AdService(AdRepository adRepository, UserRepository userRepository, AdMapper adMapper, CreateOrUpdateAdMapper createOrUpdateAdMapper) {
        this.adRepository = adRepository;
        this.userRepository = userRepository;
        this.adMapper = adMapper;
        this.createOrUpdateAdMapper = createOrUpdateAdMapper;
    }

    //Получение всех объявлений
    public Ads getAllAds() {
        return adMapper.toAdsDto(adRepository.findAll());
    }

    //Получение информации об объявлении
    public ExtendedAd getAds(Integer id) {
        AdEntity entity = adRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Объявление не найдено"));
        return adMapper.toExtendedAdDto(entity);
    }

    //Получение объявлений авторизованного пользователя
    public Ads getAdsMe(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        return adMapper.toAdsDto(adRepository.findByAuthorId(user.getId()));
    }

    //Добавление объявления
    public Ad addAd(String email, CreateOrUpdateAd createAd, String imageUrl) {
        UserEntity author = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        AdEntity ad = new AdEntity();
        ad.setAuthor(author);
        ad.setTitle(createAd.getTitle());
        ad.setPrice(createAd.getPrice());
        ad.setDescription(createAd.getDescription());
        ad.setImage(imageUrl);

        return adMapper.toAdDto(adRepository.save(ad));
    }

    //Обновление информации об объявлении
    public Ad updateAds(Integer id, CreateOrUpdateAd updateAd, String userEmail) {
        AdEntity entity = adRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Объявление не найдено"));
        checkPermission(entity, userEmail);
        createOrUpdateAdMapper.updateEntity(updateAd, entity);
        return adMapper.toAdDto(adRepository.save(entity));
    }

    //Удаление объявления
    public void removeAd(Integer id, String userEmail) {
        AdEntity entity = adRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Объявление не найдено"));
        checkPermission(entity, userEmail);
        adRepository.delete(entity);
    }

    //Обновление картинки объявления
    public void updateAdImage(Integer id, String imageUrl, String email) {
        AdEntity ad = adRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Объявление не найдено"));
        checkPermission(ad, email);
        ad.setImage(imageUrl);
        adRepository.save(ad);
    }

    //Проверки прав доступа
    private void checkPermission(AdEntity ad, String userEmail) {
        UserEntity user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        if (user.getRole().name().equals("ADMIN")) return;
        if (!ad.getAuthor().getId().equals(user.getId())) {
            throw new RuntimeException("Нет прав на редактирование этого объявления");
        }
    }
}

