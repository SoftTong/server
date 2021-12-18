package com.example.demo.controller.notice;

import com.example.demo.controller.ApiResult;
import com.example.demo.dao.MemberDao;
import com.example.demo.dto.FormNoticeDto;
import com.example.demo.dto.FileNoticeDto;
import com.example.demo.dto.NoticeInfoDto;
import com.example.demo.service.member.MemberStatusService;
import com.example.demo.service.notice.NoticeDeleteService;
import com.example.demo.service.notice.NoticeRegisterService;
import com.example.demo.service.notice.NoticeStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/notice")
public class NoticeController {

    private final NoticeStatusService noticeStatusService;
    private final NoticeRegisterService noticeRegisterService;
    private final MemberStatusService memberStatusService;
    private final NoticeDeleteService noticeDeleteService;

    @ResponseBody
    @GetMapping("/{pageNum}")
    public ApiResult<?> noticeList(@PathVariable int pageNum,
                                   @RequestParam(required = false) String searchWord,
                                   @RequestParam(required = true) String category) {
        if (category.equals("title")) { // 기본 상태 ( 기본이 제목으로 검색 )
           if (searchWord == null) { // 기본 상태에서 아무것도 검색하지 않았을 때
               return ApiResult.OK(noticeStatusService.findAllByPagination(pageNum));
           } else { // 검색어가 있는 상태
               return ApiResult.OK(noticeStatusService.findByTitle(pageNum, searchWord));
           }
        } else if (category.equals("author")) { // 작성자 검색
            if (searchWord == null) {
                return ApiResult.OK(noticeStatusService.findAllByPagination(pageNum));
            } else {
                List<MemberDao> members = memberStatusService.findBySearchWordContaining(searchWord);
                if (members.isEmpty()) {
                    return ApiResult.ERROR(new IllegalStateException("존재하지 않는 작성자입니다."), HttpStatus.BAD_REQUEST);
                }
                return ApiResult.OK(noticeStatusService.findByAuthor(pageNum, members));
            }
        } else {
            return ApiResult.ERROR(new IllegalStateException("카테고리가 없습니다."), HttpStatus.BAD_REQUEST);
        }
    }

    @ResponseBody
    @GetMapping("/detail/{noticeNum}")
    public ApiResult<?> noticeDetails(@PathVariable Long noticeNum) {
        String dtype = (String) noticeStatusService.findDtypeById(noticeNum);
        log.debug("dtype = {}", dtype);
        if (dtype.equals("file")) { // FileNotice 타입일 때
            return ApiResult.OK(noticeStatusService.getFileNotice(noticeNum));
        } else if (dtype.equals("form")) { // FormNotice 타입일 때
            log.debug("formNotice입니다.");
            return ApiResult.OK(noticeStatusService.getFormNotice(noticeNum));
        } else {
            log.debug("올바르지 않은 공지사항입니다.");
            return ApiResult.ERROR(new IllegalStateException("해당 게시글이 존재하지 않습니다."), HttpStatus.BAD_REQUEST);
        }
    }

    // 관리자가 첨부 파일 형식의 공지사항 작성할 때
    @Secured({"ROLE_USER","ROLE_ADMIN"})
    @ResponseBody
    @PostMapping("/file/board")
    public ApiResult<?> fileNoticeAdd(HttpServletRequest request, @RequestBody FileNoticeDto postInfo) {
        MemberDao user = memberStatusService.findMember(request).get();
        noticeRegisterService.makeFileNotice(user,postInfo);
        System.out.println(ApiResult.OK(postInfo));
        log.info("{}",postInfo.toString());
        log.info("{}",postInfo);
        return ApiResult.OK(postInfo);
    }

    // 관리자가 폼 형식의 공지사항 작성할 때
    @Secured({"ROLE_USER","ROLE_ADMIN"})
    @ResponseBody
    @PostMapping("/form/board")
    public ApiResult<?> formNoticeAdd(HttpServletRequest request, @RequestBody FormNoticeDto postInfo) {
        MemberDao user = memberStatusService.findMember(request).get();
        noticeRegisterService.makeFormNotice(user,postInfo);
        return ApiResult.OK(postInfo);
    }

    //관리자가 작성한 게시물 목록 가져오기
    @ResponseBody
    @GetMapping("/managers/{pageNum}")
    public ApiResult<?> noticeManagerList(HttpServletRequest req, @PathVariable int pageNum) {
        return ApiResult.OK(noticeStatusService.findAllByManager(req, pageNum));
    }

    // 공지사항 삭제
    @ResponseBody
    @PostMapping("/remove/{noticeId}")
    public ApiResult<?> noticeRemove(@PathVariable Long noticeId) {
        return ApiResult.OK(noticeDeleteService.removeNotice(noticeId));
    }

    @ResponseBody
    @PostMapping("/like/{noticeId}")
    public ApiResult<?> noticeLike(HttpServletRequest request, @PathVariable Long noticeId){
        MemberDao user = memberStatusService.findMember(request).get();

        if(noticeStatusService.findByLike(user.getId(),noticeId).isPresent()){
            noticeStatusService.dislikeNotice(user.getId(),noticeId);

            return ApiResult.OK("User dislike a notice");
        }else{
            noticeStatusService.likeNotice(user.getId(),noticeId);

            return ApiResult.OK("User like a notice");
        }
    }

    @ResponseBody
    @GetMapping(value="/like")
    public ApiResult<?> memberNoticeLike(HttpServletRequest request){
        Long userId = memberStatusService.findMember(request).get().getId();

        List<NoticeInfoDto> noticeInfoDtoList = new ArrayList<>();

        for (Long noticeId : noticeStatusService.noticeUserLike(userId)) {
            noticeInfoDtoList.add(new NoticeInfoDto(noticeStatusService.findById(noticeId)));
        }

        return ApiResult.OK(noticeInfoDtoList);
    }
}