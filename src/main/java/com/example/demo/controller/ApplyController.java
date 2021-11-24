package main.java.com.example.demo.controller;

import com.example.demo.dao.MemberDao;
import com.example.demo.domain.entity.ApplyFileNoticeEntity;
import com.example.demo.domain.entity.MemberApply;
import com.example.demo.domain.entity.NoticeEntity;
import com.example.demo.dto.ApplyDto;
import com.example.demo.dto.FileApplyDto;
import com.example.demo.dto.NoticeInfoDto;
import com.example.demo.payload.ApiResponse;
import com.example.demo.service.MemberService;
import com.example.demo.service.apply.ApplyFileDeleteService;
import com.example.demo.service.apply.ApplyFileStatusService;
import com.example.demo.service.member.MemberStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/apply")
public class ApplyController {

    private final MemberStatusService memberStatusService;
    private final ApplyFileStatusService applyFileStatusService;
    private final ApplyFileDeleteService applyFileDeleteService;

    //사용자가 지원한 지원서들 가져오기
    @ResponseBody
    @GetMapping("/{pageNum}")
    public Page<ApplyDto> applyList(HttpServletRequest request, @PathVariable int pageNum) {
        return applyFileStatusService.findApply(request, pageNum);
    }

    //사용자가 지원한 지원 파일 정보
    @ResponseBody
    @GetMapping("/file/{applyId}")
    public FileApplyDto applyFileDetails(HttpServletRequest request, @PathVariable Long applyId) {
        return applyFileStatusService.findapplyFile(request, applyId);
    }

    //사용자가 지원한 지원 파일 삭제
    @ResponseBody
    @DeleteMapping("/file/{applyId}")
    public ApiResponse applicationFileRemove(HttpServletRequest request, @PathVariable Long applyId) {
        return applyFileDeleteService.removeApplyFile(request, applyId);
    }

    //관리자가 작성한 게시물 목록 가져오기
    @GetMapping("/{pageNum}")
    public Page<NoticeInfoDto> getManagerPage(HttpServletRequest req, @PathVariable int pageNum) {

        Pageable page = PageRequest.of(pageNum, 10, Sort.by("uploadDay").descending());
        MemberDao currentUser = memberStatusService.GetCurrentUserInfo(req).get();

        Page<NoticeEntity> noticeEntityPages = noticeStatusService.findAllByMemberDao(currentUser,page);

        List<NoticeInfoDto> noticeInfoDtoList = noticeEntityPages.stream().map(nep -> new NoticeInfoDto(nep)).collect((toList()));

        return new PageImpl<>(noticeInfoDtoList, page, noticeEntityPages.getTotalElements());
    }

    //관리자가 작성한 게시물의 지원한 지원서 정보들
    @GetMapping("/{noticeId}/{pageNum}")
    public PageImpl<Object> getNotice(@PathVariable Long noticeId, @PathVariable int pageNum) {
        Pageable page = PageRequest.of(pageNum, 10, Sort.by("member_id").ascending());

        String dtype = (String) noticeStatusService.findDtypeById(noticeId);

        if (dtype.equals("file")) {
            Page<ApplyFileNoticeEntity> applyPages = applyFileStatusService.findMemberById(noticeId,page);
            List<FileApplyDto> fileApplyDtoList = applyPages.stream().map(a-> new FileApplyDto(a) ).collect((toList()));
            return new PageImpl(fileApplyDtoList, page, applyPages.getTotalElements());
        } else if (dtype.equals("form")) {
            throw new IllegalStateException("폼 형식은 아직 구현되지 않았습니다.");
        } else {
            throw new IllegalStateException("올바르지 않은 공지사항입니다.");
        }

    }

    //이거 일단 보류
    @GetMapping()//현재 사용자 정보 받아오기
    public List<NoticeEntity> getManager(HttpServletRequest request){

        log.info("GetManager");
        MemberDao currentUser = memberStatusService.GetCurrentUserInfo(request).get();

        List<NoticeEntity> noticeEntitys = noticeStatusService.findByMember(currentUser);

        return noticeEntitys;
    }
}
