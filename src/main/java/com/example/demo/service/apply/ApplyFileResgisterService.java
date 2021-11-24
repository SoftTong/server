package com.example.demo.service.apply;

import com.example.demo.dao.MemberDao;
import com.example.demo.domain.entity.ApplyFileNoticeEntity;
import com.example.demo.domain.entity.MemberApply;
import com.example.demo.domain.entity.NoticeEntity;
import com.example.demo.domain.entity.StatusName;
import com.example.demo.domain.repository.ApplyFileRepository;
import com.example.demo.domain.repository.MemberApplyRepository;
import com.example.demo.payload.ApiResponse;
import com.example.demo.service.member.MemberStatusService;
import com.example.demo.service.notice.NoticeStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ApplyFileResgisterService {

    private final ApplyFileRepository applyFileRepository;
    private final MemberApplyRepository memberApplyRepository;
    private final MemberStatusService memberStatusService;
    private final NoticeStatusService noticeStatusService;

    public ApplyFileNoticeEntity addApplyFileNotice(String filePath, MemberDao memberDao, NoticeEntity noticeEntity, String fileName){

        ApplyFileNoticeEntity applyFileNotice = new ApplyFileNoticeEntity(filePath, memberDao, noticeEntity, fileName);
        applyFileNotice.setStatus(StatusName.wait); //처음은 대기 상태로 저장
        ApplyFileNoticeEntity savedApplyFileNotice = applyFileRepository.save(applyFileNotice);
        MemberApply memberApply = new MemberApply(memberDao.getId(), noticeEntity.getId(), savedApplyFileNotice.getId(),"file");
        memberApplyRepository.save(memberApply);
        return applyFileNotice;
    }

    //사용자가 파일을 제출할때
    @Secured({"ROLE_USER","ROLE_ADMIN"})
    @ResponseBody
    public ApiResponse addApplyFile(HttpServletRequest request, MultipartFile multipartFile, Long noticeId) {

        MemberDao currentMember = memberStatusService.findMember(request).get();
        NoticeEntity noticeEntity = noticeStatusService.findById(noticeId);

        Optional<MemberApply> apply = memberApplyRepository.findByNoticeWithMember(noticeId,currentMember.getId());
        log.info("apply = {}", apply);
        log.info("apply.name = {}", apply.get().getNoticeId());
        if (apply.isPresent()){
            return new ApiResponse(false,"이미 신청한 게시물입니다.");
        }

        //File클래스를 통해 파일과 디렉터리를 다룬다 -> File인스턴스는 파일일 수 도 있고 디렉터리 일 수 도 있다다
        //MultipartFile을 받아와서 그 FileInputStream을 얻고 빈 targetFile에 스트림을 복사
        UUID uid = UUID.randomUUID();
        String extension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
        File targetFile = new File("src/main/resources/menufiles/" + uid.toString() + "." + extension);
        try {
            InputStream fileStream = multipartFile.getInputStream();
            FileUtils.copyInputStreamToFile(fileStream, targetFile);
            addApplyFileNotice(uid.toString() + "." + extension, currentMember, noticeEntity, multipartFile.getOriginalFilename());
            return new ApiResponse(true,"제출 완료했습니다.");
        } catch (IOException e) {
            FileUtils.deleteQuietly(targetFile); //지움
            e.printStackTrace();
        }

        return new ApiResponse(false,"서버 오류");
    }
}
