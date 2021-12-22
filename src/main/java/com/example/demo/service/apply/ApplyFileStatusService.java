package com.example.demo.service.apply;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ApplyFileStatusService {

  //지원한 파일 다운로드
  public ResponseEntity<InputStreamResource> downloadApplyFile(String fileName) throws IOException {

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
