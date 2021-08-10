package com.example.demo.controller;

import com.example.demo.dao.MemberDao;
import com.example.demo.domain.entity.NoticeEntity;
import com.example.demo.domain.repository.NoticeRepository;
import com.example.demo.dto.NoticeDto;
import com.example.demo.dto.NoticeInfoDto;
import com.example.demo.service.MemberService;
import com.example.demo.service.NoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/notice")
public class NoticeController {

    private final NoticeService noticeService;
    private final NoticeRepository noticeRepository;

    private final MemberService memberService;

    @ResponseBody
    @GetMapping("/{pageNum}")
    public Page<NoticeInfoDto> notice(@PathVariable int pageNum) {
        Pageable page = PageRequest.of(pageNum, 10, Sort.by("uploadDay").descending());

        Page<NoticeEntity> noticeEntityPages = noticeRepository.findAll(page);
        List<NoticeInfoDto> noticeInfoDtoList = noticeEntityPages.stream().map(nep -> new NoticeInfoDto(nep)).collect((toList()));

        return new PageImpl<>(noticeInfoDtoList,page,noticeInfoDtoList.size());
    }

    @ResponseBody
    @GetMapping("/detail/{noticeNum}")
    public NoticeEntity detail(@PathVariable Long noticeNum) {
        NoticeEntity findNotice = noticeRepository.getById(noticeNum);
        if (findNotice == null) {
            // 존재하지 않는 숫자가 넘어왔을 때
            throw new IllegalStateException("해당 게시글이 존재하지 않습니다.");
        }
        return findNotice;
    }

    @Secured({"ROLE_USER","ROLE_ADMIN"})
    @ResponseBody
    @PostMapping("/file/board")
    public NoticeDto postBoard(HttpServletRequest request, @RequestBody NoticeDto postInfo) {

        MemberDao user = memberService.GetCurrentUserInfo(request).get();

        noticeService.newPostBoard(user,postInfo);
        return postInfo;
    }
}