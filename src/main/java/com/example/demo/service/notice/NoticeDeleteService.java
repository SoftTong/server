package com.example.demo.service.notice;

import com.example.demo.domain.entity.ApplyFileNoticeEntity;
import com.example.demo.domain.entity.NoticeEntity;
import com.example.demo.domain.repository.ApplyFileRepository;
import com.example.demo.domain.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class NoticeDeleteService {

    private final NoticeRepository noticeRepository;
    private final ApplyFileRepository applyFileRepository;

    public boolean removeNotice(Long noticeId) {
        Optional<NoticeEntity> findNotice = noticeRepository.findById(noticeId);
        if (findNotice.isEmpty()) return false;

        NoticeEntity notice = findNotice.get();
        List<ApplyFileNoticeEntity> afns = applyFileRepository.findAllByNoticeEntity(notice);
        List<Long> ids = new ArrayList<>();
        for (ApplyFileNoticeEntity x : afns) {
            ids.add(x.getId());
        }
        applyFileRepository.deleteAllByIdInQuery(ids); // 공지사항 삭제 전 해당 공지사항에 지원한 제출파일 삭제

        noticeRepository.delete(notice);
        return true;
    }
}
