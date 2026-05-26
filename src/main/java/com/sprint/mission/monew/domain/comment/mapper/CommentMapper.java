package com.sprint.mission.monew.domain.comment.mapper;

import com.sprint.mission.monew.domain.comment.dto.response.CommentResponse;
import com.sprint.mission.monew.domain.comment.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(target = "articleId", expression = "java(comment.getArticle().getId())")
    @Mapping(target = "userId", expression = "java(comment.getUser().getId())")
    @Mapping(target = "userNickname", expression = "java(comment.getUser().getNickname())")
    @Mapping(target = "likedByMe", source = "likedByMe")
    CommentResponse toDto(Comment comment, boolean likedByMe);
}
