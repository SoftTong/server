package com.example.demo.controller;

import com.example.demo.dao.MemberDao;
import com.example.demo.domain.entity.*;
import com.example.demo.dto.*;
import com.example.demo.payload.ApiResponse;
import com.example.demo.service.MemberService;
import com.example.demo.service.apply.*;
import com.example.demo.service.member.MemberStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
    private final ApplyFormRegisterService applyFormRegisterService;
    private final ApplyStatusService applyStatusService;

    //사용자가 지원한 지원서들 가져오기
    @ResponseBody
    @GetMapping("/{pageNum}")
    public ApiResult<Page<ApplyListDto>> applyList(HttpServletRequest request, @PathVariable int pageNum) {
        return ApiResult.OK(applyStatusService.findApply(request, pageNum));
    }

    //사용자가 지원한 지원 파일 정보
    @ResponseBody
    @GetMapping("/file/{applyId}")
    public ApiResult<FileApplyDto> applyFileDetails(HttpServletRequest request, @PathVariable Long applyId) {
        return ApiResult.OK(applyFileStatusService.findApplyFileByApplyId(request, applyId));
    }

    //사용자가 지원한 지원 파일 삭제
    @ResponseBody
    @DeleteMapping("/detail/{applyId}")
    public ApiResult<Boolean> applyFileRemove(HttpServletRequest request, @PathVariable Long applyId) {
        if (applyFileDeleteService.removeApply(request, applyId)){
            return ApiResult.OK(Boolean.TRUE);
        }
        else{
            throw new IllegalArgumentException("not founded");
        }
    }

    //관리자가 작성한 게시물의 지원한 지원서 정보들
    @ResponseBody
    @GetMapping("/{noticeId}/{pageNum}")
    public ApiResult<Page<?>> applyManagerList(@PathVariable Long noticeId, @PathVariable int pageNum) {
        return ApiResult.OK(applyStatusService.findApplyByNoticeId(noticeId, pageNum));
        //return ApiResult.OK(applyFileStatusService.findApplyFileByNoticeId(noticeId, pageNum));
    }

    //사용자가 파일을 제출할때
    @Secured({"ROLE_USER","ROLE_ADMIN"})
    @ResponseBody
    @PostMapping(value="/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResult<?> applyFileAdd(HttpServletRequest request, @RequestPart(name="file", required = false) MultipartFile multipartFile, @RequestParam("noticeId") Long noticeId) {
        return applyFileResgisterService.addApplyFile(request, multipartFile, noticeId);
    }

    //사용자가 폼을 제출할때
    @Secured({"ROLE_USER","ROLE_ADMIN"})
    @ResponseBody
    @PostMapping(value="/form/{QuestionId}")
    public ApiResult<?> applyFormAdd(HttpServletRequest request, @RequestBody FormAnswerDto formAnswerDto, @PathVariable Long QuestionId) {
        return applyFormRegisterService.addApplyForm(request, formAnswerDto, QuestionId);
    }

    //파일 다운로드
    @GetMapping("/download/file/{fileName:.+}")
    public ResponseEntity<InputStreamResource> applyFileDownload(@PathVariable String fileName) throws IOException {
        return applyFileStatusService.downloadApplyFile(fileName);
    }
}
