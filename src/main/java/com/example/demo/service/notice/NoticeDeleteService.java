package com.example.demo.service.notice;

import com.example.demo.domain.entity.ApplyFile;
import com.example.demo.domain.entity.ApplyForm;
import com.example.demo.domain.entity.NoticeEntity;
import com.example.demo.domain.repository.ApplyResourceRepository;
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
    private final ApplyResourceRepository applyResourceRepository;
    private final NoticeStatusService noticeStatusService;

    public boolean removeNotice(Long noticeId) {
        Optional<NoticeEntity> findNotice = noticeRepository.findById(noticeId);
        if (findNotice.isEmpty()) return false;

        NoticeEntity notice = findNotice.get();


        String dtype = (String) noticeStatusService.findDtypeById(notice.getId());

        // 공지사항 삭제 전 해당 공지사항에 지원한 제출파일 삭제

        // FileNotice 타입일 때
        if (dtype.equals("file")) {
            List<ApplyFile> applyFiles = applyResourceRepository.findAllByNoticeEntity(notice);
            List<Long> ids = new ArrayList<>();
            for (ApplyFile x : applyFiles) {
                ids.add(x.getId());
            }
            applyResourceRepository.deleteAllByIdInQuery(ids);
            noticeRepository.delete(notice);
            return true;
        } else if (dtype.equals("form")) { // FormNotice 타입일 때

            List<ApplyForm> applyForms = applyResourceRepository.findAllByNoticeEntity(notice);
            List<Long> ids = new ArrayList<>();
            for (ApplyForm x : applyForms) {
                ids.add(x.getId());
            }
            applyResourceRepository.deleteAllByIdInQuery(ids);
            noticeRepository.delete(notice);
            return true;
        }

        return false;
    }
}
