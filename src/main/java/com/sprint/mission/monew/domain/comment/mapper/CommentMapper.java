package com.sprint.mission.monew.domain.comment.mapper;

import com.sprint.mission.monew.domain.comment.dto.response.CommentResponse;
import com.sprint.mission.monew.domain.comment.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(target = "articleId", source = "comment.article.id")
    @Mapping(target = "userId", source = "comment.user.id")
    @Mapping(target = "userNickname", source = "comment.user.nickname")
    @Mapping(target = "likedByMe", source = "likedByMe")
    CommentResponse toDto(Comment comment, boolean likedByMe);
}
