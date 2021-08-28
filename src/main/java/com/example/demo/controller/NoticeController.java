package com.example.demo.controller;

import com.example.demo.dao.MemberDao;
import com.example.demo.domain.entity.ApplyFileNoticeEntity;
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
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
    public Page<NoticeInfoDto> notice(@PathVariable int pageNum, @RequestParam(required = false) String searchWord) {
        if (searchWord == null) { // 검색어가 없는 기본 상태
            Pageable page = PageRequest.of(pageNum, 10, Sort.by("uploadDay").descending());

            Page<NoticeEntity> noticeEntityPages = noticeRepository.findAll(page);
            List<NoticeInfoDto> noticeInfoDtoList = noticeEntityPages.stream().map(nep -> new NoticeInfoDto(nep)).collect((toList()));

            return new PageImpl<>(noticeInfoDtoList, page, noticeEntityPages.getTotalElements());

        } else { // 검색어가 있는 상태, 검색 완료 후 해당 게시글들이 존재하지 않는 다면 content안에 아무것도 안담김
            // 그러면 프론트쪽에서 content가 비어있는 것을 토대로 검색결과가 없다는 메세지를 출력
            Pageable page = PageRequest.of(pageNum, 10, Sort.by("uploadDay").descending());

            Page<NoticeEntity> noticeEntityPages = noticeRepository.findByNameContaining(searchWord, page);
            List<NoticeInfoDto> noticeInfoDtoList = noticeEntityPages.stream().map(nep -> new NoticeInfoDto(nep)).collect((toList()));

            return new PageImpl<>(noticeInfoDtoList, page, noticeEntityPages.getTotalElements());
        }
    }

    @ResponseBody
    @GetMapping("/detail/{noticeNum}")
    public Object detail(@PathVariable Long noticeNum) {
        String dtype = (String) noticeRepository.findDtypeById(noticeNum);
        log.debug("dtype = {}", dtype);

        if (dtype.equals("file")) { // FileNotice 타입일 때

            FileNotice fileNotice = noticeService.getFileNotice(noticeNum);
            FileNoticeDto fileNoticeDto = new FileNoticeDto(fileNotice, Boolean.FALSE); // File 타입이므로 False를 같이 넘김
            log.debug("fileNotice입니다.");

            return fileNoticeDto;

        } else if (dtype.equals("form")) { // FormNotice 타입일 때

            FormNotice formNotice = noticeService.getFormNotice(noticeNum);
            FormNoticeDto formNoticeDto = new FormNoticeDto(formNotice, Boolean.TRUE); // Form 타입이므로 True를 같이 넘김
            log.debug("formNotice입니다.");

            return formNoticeDto;
        } else {
            log.debug("올바르지 않은 공지사항입니다.");
            throw new IllegalStateException("해당 게시글이 존재하지 않습니다.");
        }
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

    //사용자가 파일을 제출할때
    @Secured({"ROLE_USER","ROLE_ADMIN"})
    @ResponseBody
    @PostMapping(value="/file/apply", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Boolean applyFileBoard(HttpServletRequest request, @RequestPart(name="file", required = false) MultipartFile multipartFile, @RequestParam("noticeId") Long noticeId) {

        System.out.println(multipartFile.getOriginalFilename());
        System.out.println(noticeId);

        MemberDao currentMember = memberService.GetCurrentUserInfo(request).get();
        NoticeEntity noticeEntity = noticeRepository.findById(noticeId).get();

        //File클래스를 통해 파일과 디렉터리를 다룬다 -> File인스턴스는 파일일 수 도 있고 디렉터리 일 수 도 있다다
        //MultipartFile을 받아와서 그 FileInputStream을 얻고 빈 targetFile에 스트림을 복사
        UUID uid = UUID.randomUUID();
        String extension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
        File targetFile = new File("src/main/resources/menufiles/" + uid.toString() + "." + extension);
        try {
            InputStream fileStream = multipartFile.getInputStream();
            FileUtils.copyInputStreamToFile(fileStream, targetFile);
            noticeService.makeApplyFileNotice(uid.toString() + "." + extension, currentMember, noticeEntity, multipartFile.getOriginalFilename());
            return true;
        } catch (IOException e) {
            FileUtils.deleteQuietly(targetFile); //지움
            e.printStackTrace();
        }

        return false;
    }

    @RequestMapping("/download/file/{fileName:.+}")
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable String fileName) throws IOException {

        System.out.println(fileName);

        String path = "src/main/resources/menufiles/"+fileName;
        File file = new File(path);
        HttpHeaders header = new HttpHeaders();

        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+fileName);
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        return ResponseEntity.ok() .headers(header) .contentLength(file.length()) .contentType(MediaType.parseMediaType("application/octet-stream")) .body(resource);
    }
}