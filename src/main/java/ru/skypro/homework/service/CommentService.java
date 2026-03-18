package ru.skypro.homework.service;

import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.CommentEntity;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.mapper.CreateOrUpdateCommentMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;

import java.time.Instant;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final AdRepository adRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;
    private final CreateOrUpdateCommentMapper createOrUpdateCommentMapper;

    public CommentService(CommentRepository commentRepository, AdRepository adRepository, UserRepository userRepository, CommentMapper commentMapper, CreateOrUpdateCommentMapper createOrUpdateCommentMapper) {
        this.commentRepository = commentRepository;
        this.adRepository = adRepository;
        this.userRepository = userRepository;
        this.commentMapper = commentMapper;
        this.createOrUpdateCommentMapper = createOrUpdateCommentMapper;
    }

    //Получение комментариев объявления
    public Comments getComments(Integer adId) {
        return commentMapper.toCommentsDto(commentRepository.findByAdPk(adId));
    }

    //Добавление комментария к объявлению
    public Comment addComment(Integer adId, CreateOrUpdateComment createComment, String userEmail) {
        AdEntity ad = adRepository.findById(adId)
                .orElseThrow(() -> new RuntimeException("Объявление не найдено"));
        UserEntity author = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        CommentEntity entity = new CommentEntity();
        entity.setAd(ad);
        entity.setAuthor(author);
        createOrUpdateCommentMapper.updateEntity(createComment, entity);
        entity.setCreatedAt(Instant.now().toEpochMilli());
        return commentMapper.toDto(commentRepository.save(entity));
    }

    //Удаление комментариями
    public void deleteComment(Integer adId, Integer commentId, String userEmail) {
        CommentEntity comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Комментарий не найден"));
        if (!comment.getAd().getPk().equals(adId)) {
            throw new RuntimeException("Комментарий не относится к этому объявлению");
        }
        checkPermission(comment, userEmail);
        commentRepository.delete(comment);
    }

    //Обновление комментария
    public Comment updateComment(Integer adId, Integer commentId, CreateOrUpdateComment updateComment, String userEmail) {
        CommentEntity comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Комментарий не найден"));
        if (!comment.getAd().getPk().equals(adId)) {
            throw new RuntimeException("Комментарий не относится к этому объявлению");
        }
        checkPermission(comment, userEmail);
        createOrUpdateCommentMapper.updateEntity(updateComment, comment);
        return commentMapper.toDto(commentRepository.save(comment));
    }

    //Проверки прав доступа
    private void checkPermission(CommentEntity comment, String userEmail) {
        UserEntity user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        if (user.getRole().name().equals("ADMIN")) return;
        if (!comment.getAuthor().getId().equals(user.getId())) {
            throw new RuntimeException("Нет прав на редактирование этого комментария");
        }
    }
}
