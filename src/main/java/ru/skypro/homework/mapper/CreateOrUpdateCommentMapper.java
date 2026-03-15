package ru.skypro.homework.mapper;

import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.entity.CommentEntity;

@Component
public class CreateOrUpdateCommentMapper {
    public void updateEntity(CreateOrUpdateComment dto, CommentEntity entity) {
        if (dto == null || dto.getText() == null) return;
        entity.setText(dto.getText());
    }
}
