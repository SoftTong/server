package com.example.demo.service.apply;

import com.example.demo.controller.ApiResult;
import com.example.demo.dao.MemberDao;
import com.example.demo.domain.entity.*;
import com.example.demo.domain.repository.ApplyResourceRepository;
import com.example.demo.service.member.MemberStatusService;
import com.example.demo.service.notice.NoticeStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ApplyFileResgisterService {

    private final MemberStatusService memberStatusService;
    private final NoticeStatusService noticeStatusService;
    private final ApplyResourceRepository applyResourceRepository;

    public boolean addApplyFileSave(String filePath, MemberDao memberDao, NoticeEntity noticeEntity, String fileName){

        applyResourceRepository.save(new ApplyFile(filePath, fileName, memberDao, (FileNotice)noticeEntity));

        return true;
    }

    //사용자가 파일을 제출할때
    @Secured({"ROLE_USER","ROLE_ADMIN"})
    @ResponseBody
    public ApiResult<?> addApplyFile(HttpServletRequest request, MultipartFile multipartFile, Long noticeId) {

        MemberDao currentMember = memberStatusService.findMember(request).get();
        NoticeEntity noticeEntity = noticeStatusService.findById(noticeId);

        Optional<ApplyResource> apply = applyResourceRepository.findByNoticeWithMember(noticeEntity,currentMember);

        if (apply.isPresent()){
            return ApiResult.ERROR(new IllegalStateException("이미 신청한 게시물입니다."), HttpStatus.BAD_REQUEST);
        }

        //File클래스를 통해 파일과 디렉터리를 다룬다 -> File인스턴스는 파일일 수 도 있고 디렉터리 일 수 도 있다다
        //MultipartFile을 받아와서 그 FileInputStream을 얻고 빈 targetFile에 스트림을 복사
        UUID uid = UUID.randomUUID();
        String extension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
        File targetFile = new File("src/main/resources/menufiles/" + uid.toString() + "." + extension);
        try {
            InputStream fileStream = multipartFile.getInputStream();
            FileUtils.copyInputStreamToFile(fileStream, targetFile);
            addApplyFileSave(uid.toString() + "." + extension, currentMember, noticeEntity, multipartFile.getOriginalFilename());

            return ApiResult.OK(uid.toString());
        } catch (IOException e) {
            FileUtils.deleteQuietly(targetFile); //지움
            e.printStackTrace();
        }

        return ApiResult.ERROR(new IllegalStateException("서버 오류입니다."), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
