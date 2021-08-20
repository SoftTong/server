package com.example.demo.controller;

import com.example.demo.dao.MemberDao;
import com.example.demo.domain.entity.FileNotice;
import com.example.demo.domain.entity.FormNotice;
import com.example.demo.domain.entity.NoticeEntity;
import com.example.demo.domain.repository.NoticeRepository;
import com.example.demo.dto.FormNoticeDto;
import com.example.demo.dto.FileNoticeDto;
import com.example.demo.dto.NoticeInfoDto;
import com.example.demo.service.MemberService;
import com.example.demo.service.NoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.LazyInitializer;
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

        return new PageImpl<>(noticeInfoDtoList, page, noticeEntityPages.getTotalElements());
    }

    @ResponseBody
    @GetMapping("/detail/{noticeNum}")
    public Object detail(@PathVariable Long noticeNum) {
        String dtype = (String) noticeRepository.findDtypeById(noticeNum);
        log.debug("dtype = {}", dtype);

        if (dtype.equals("file")) { // FileNotice 타입일 때

            FileNotice fileNotice = getFileNotice(noticeNum);
            FileNoticeDto fileNoticeDto = new FileNoticeDto(fileNotice, Boolean.FALSE); // File 타입이므로 False를 같이 넘김
            log.debug("fileNotice입니다.");

            return fileNoticeDto;

        } else if (dtype.equals("form")) { // FormNotice 타입일 때

            FormNotice formNotice = getFormNotice(noticeNum);
            FormNoticeDto formNoticeDto = new FormNoticeDto(formNotice, Boolean.TRUE); // Form 타입이므로 True를 같이 넘김
            log.debug("formNotice입니다.");

            return formNoticeDto;
        } else {
            log.debug("올바르지 않은 공지사항입니다.");
            throw new IllegalStateException("해당 게시글이 존재하지 않습니다.");
        }
    }

    // Proxy 객체에서 실제 구현체 값을 가져오는 메서드
    private FormNotice getFormNotice(Long noticeNum) {
        HibernateProxy hibernateProxy = (HibernateProxy) noticeRepository.getById(noticeNum);
        LazyInitializer initializer = hibernateProxy.getHibernateLazyInitializer();
        FormNotice formNotice = (FormNotice) initializer.getImplementation();
        return formNotice;
    }

    // Proxy 객체에서 실제 구현체 값을 가져오는 메서드
    private FileNotice getFileNotice(Long noticeNum) {
        HibernateProxy hibernateProxy = (HibernateProxy) noticeRepository.getById(noticeNum);
        LazyInitializer initializer = hibernateProxy.getHibernateLazyInitializer();
        FileNotice fileNotice = (FileNotice) initializer.getImplementation();
        return fileNotice;
    }

    // 관리자가 첨부 파일 형식의 공지사항 작성할 때
    @Secured({"ROLE_USER","ROLE_ADMIN"})
    @ResponseBody
    @PostMapping("/file/board")
    public FileNoticeDto postFileBoard(HttpServletRequest request, @RequestBody FileNoticeDto postInfo) {

        MemberDao user = memberService.GetCurrentUserInfo(request).get();

        noticeService.makeFileNotice(user,postInfo);
        return postInfo;
    }

    // 관리자가 폼 형식의 공지사항 작성할 때
    @Secured({"ROLE_USER","ROLE_ADMIN"})
    @ResponseBody
    @PostMapping("/form/board")
    public FormNoticeDto postFormBoard(HttpServletRequest request, @RequestBody FormNoticeDto postInfo) {

        MemberDao user = memberService.GetCurrentUserInfo(request).get();

        noticeService.makeFormNotice(user,postInfo);
        return postInfo;
    }
}