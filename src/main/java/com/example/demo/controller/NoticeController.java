package com.example.demo.controller;

import com.example.demo.domain.entity.NoticeEntity;
import com.example.demo.domain.repository.NoticeRepository;
import com.example.demo.dto.NoticeDto;
import com.example.demo.service.MemberService;
import com.example.demo.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/notice")
public class NoticeController {

    private final NoticeService noticeService;
    private final NoticeRepository noticeRepository;

    @ResponseBody
    @GetMapping("/{pageNum}")
    public Page<NoticeEntity> notice(@PathVariable int pageNum) {
        Pageable page = PageRequest.of(pageNum, 10, Sort.by("uploadDay").descending());
        return noticeRepository.findAll(page);
    }

    @ResponseBody
    @GetMapping("/newpost/board")
    public NoticeDto postBoard(@RequestBody NoticeDto postInfo) {
        noticeService.newPostBoard(postInfo);
        return postInfo;
    }
}
