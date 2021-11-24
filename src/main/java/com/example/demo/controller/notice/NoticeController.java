package com.example.demo.controller.notice;

import com.example.demo.dao.MemberDao;
import com.example.demo.domain.entity.*;
import com.example.demo.domain.repository.MemberApplyRepository;
import com.example.demo.domain.repository.MemberRepository;
import com.example.demo.domain.repository.NoticeRepository;
import com.example.demo.dto.FormNoticeDto;
import com.example.demo.dto.FileNoticeDto;
import com.example.demo.dto.NoticeInfoDto;
import com.example.demo.payload.ApiResponse;
import com.example.demo.service.MemberService;
import com.example.demo.service.NoticeService;
import com.example.demo.service.apply.ApplyFileResgisterService;
import com.example.demo.service.member.MemberStatusService;
import com.example.demo.service.notice.NoticeRegisterService;
import com.example.demo.service.notice.NoticeStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.LazyInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.*;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import javax.swing.text.html.Option;
import java.awt.print.Pageable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/notice")
public class NoticeController {

    private final NoticeStatusService noticeStatusService;
    private final NoticeRegisterService noticeRegisterService;
    private final MemberStatusService memberStatusService;
    private final MemberApplyRepository memberApplyRepository;
    private final ApplyFileResgisterService applyFileResgisterService;

    @ResponseBody
    @GetMapping("/{pageNum}")
    public Page<NoticeInfoDto> noticeList(@PathVariable int pageNum,
                                      @RequestParam(required = false) String searchWord,
                                      @RequestParam(required = true) String category) {
        if (category.equals("title")) { // 기본 상태 ( 기본이 제목으로 검색 )
           if (searchWord == null) { // 기본 상태에서 아무것도 검색하지 않았을 때
               return noticeStatusService.findAllByPagination(pageNum);
           } else { // 검색어가 있는 상태
               return noticeStatusService.findByTitle(pageNum, searchWord);
           }
        } else if (category.equals("author")) { // 작성자 검색
            if (searchWord == null) {
                return noticeStatusService.findAllByPagination(pageNum);
            } else {
                List<MemberDao> members = memberStatusService.findBySearchWordContaining(searchWord);
                if (members.isEmpty()) {
                    throw new IllegalStateException("존재하지 않는 작성자입니다.");
                }
                return noticeStatusService.findByAuthor(pageNum, members);
            }
        } else {
            throw new IllegalStateException("카테고리가 없습니다.");
        }
    }

    @ResponseBody
    @GetMapping("/detail/{noticeNum}")
    public NoticeType<? extends NoticeInfoDto> noticeDetails(@PathVariable Long noticeNum) {
        String dtype = (String) noticeStatusService.findDtypeById(noticeNum);
        log.debug("dtype = {}", dtype);
        if (dtype.equals("file")) { // FileNotice 타입일 때
            return noticeStatusService.getFileNotice(noticeNum);
        } else if (dtype.equals("form")) { // FormNotice 타입일 때
            log.debug("formNotice입니다.");
            return noticeStatusService.getFormNotice(noticeNum);
        } else {
            log.debug("올바르지 않은 공지사항입니다.");
            throw new IllegalStateException("해당 게시글이 존재하지 않습니다.");
        }
    }

    // 관리자가 첨부 파일 형식의 공지사항 작성할 때
    @Secured({"ROLE_USER","ROLE_ADMIN"})
    @ResponseBody
    @PostMapping("/file/board")
    public FileNoticeDto fileNoticeAdd(HttpServletRequest request, @RequestBody FileNoticeDto postInfo) {
        MemberDao user = memberStatusService.findMember(request).get();
        noticeRegisterService.makeFileNotice(user,postInfo);
        return postInfo;
    }

    // 관리자가 폼 형식의 공지사항 작성할 때
    @Secured({"ROLE_USER","ROLE_ADMIN"})
    @ResponseBody
    @PostMapping("/form/board")
    public FormNoticeDto formNoticeAdd(HttpServletRequest request, @RequestBody FormNoticeDto postInfo) {
        MemberDao user = memberStatusService.findMember(request).get();
        noticeRegisterService.makeFormNotice(user,postInfo);
        return postInfo;
    }

    //관리자가 작성한 게시물 목록 가져오기
    @GetMapping("/managers/{pageNum}")
    public Page<NoticeInfoDto> noticeManagerList(HttpServletRequest req, @PathVariable int pageNum) {

        return noticeStatusService.findAllByManager(req, pageNum);
    }
}