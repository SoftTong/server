package com.example.demo.domain.entity;

import lombok.Getter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Getter
@Entity
@DiscriminatorValue("form")
public class ApplyFormNotice extends ApplyResource {
    private String content;
}
