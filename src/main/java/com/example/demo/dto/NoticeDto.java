package com.example.demo.dto;


import lombok.*;

import java.sql.Date;
import java.sql.Timestamp;


@Getter
@Setter
public class NoticeDto {
    private String name;
    private String swurl;
    private String tag1;
    private String tag2;
    private String tag3;
    private Timestamp uploadDay;
    private Date startDay;
    private Date destDay;
    private int viewCount;
}
