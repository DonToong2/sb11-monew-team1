package com.sprint.mission.monew.domain.article.entity;

import com.sprint.mission.monew.common.entity.BaseSoftDeletableEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter
@Entity
@Table(name = "articles")
public class Article extends BaseSoftDeletableEntity {
}
