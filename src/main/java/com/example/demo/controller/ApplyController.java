package com.example.demo.controller;

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
import com.example.demo.service.apply.ApplyFileResgisterService;
import com.example.demo.service.apply.ApplyFileStatusService;
import com.example.demo.service.member.MemberStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.awt.print.Pageable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/apply")
public class ApplyController {

    private final ApplyFileStatusService applyFileStatusService;
    private final ApplyFileDeleteService applyFileDeleteService;
    private final ApplyFileResgisterService applyFileResgisterService;

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
        return applyFileStatusService.findApplyFileByApplyId(request, applyId);
    }

    //사용자가 지원한 지원 파일 삭제
    @ResponseBody
    @DeleteMapping("/file/{applyId}")
    public ApiResponse applyFileRemove(HttpServletRequest request, @PathVariable Long applyId) {
        return applyFileDeleteService.removeApplyFile(request, applyId);
    }

    //관리자가 작성한 게시물의 지원한 지원서 정보들
    @GetMapping("/{noticeId}/{pageNum}")
    public PageImpl<Object> applyManagerList(@PathVariable Long noticeId, @PathVariable int pageNum) {
        return applyFileStatusService.findApplyFileByNoticeId(noticeId, pageNum);
    }

    //사용자가 파일을 제출할때
    @Secured({"ROLE_USER","ROLE_ADMIN"})
    @ResponseBody
    @PostMapping(value="/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse applyFileAdd(HttpServletRequest request, @RequestPart(name="file", required = false) MultipartFile multipartFile, @RequestParam("noticeId") Long noticeId) {
        return applyFileResgisterService.addApplyFile(request, multipartFile, noticeId);
    }

    //파일 다운로드
    @GetMapping("/download/file/{fileName:.+}")
    public ResponseEntity<InputStreamResource> applyFileDownload(@PathVariable String fileName) throws IOException {
        return applyFileStatusService.downloadApplyFile(fileName);
    }
}
