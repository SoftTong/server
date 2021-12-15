package com.example.demo.domain.entity;

import lombok.Getter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@ToString
public class NoticeLike {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long userId;

  private Long noticeId;

  protected NoticeLike() {}

  public NoticeLike(Long userId, Long noticeId) {
    this.id = null;
    this.userId = userId;
    this.noticeId = noticeId;
  }
}
