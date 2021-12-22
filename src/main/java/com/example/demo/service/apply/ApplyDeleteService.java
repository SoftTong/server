package com.example.demo.service.apply;

import com.example.demo.dao.MemberDao;
import com.example.demo.domain.entity.ApplyResource;
import com.example.demo.domain.repository.ApplyResourceRepository;
import com.example.demo.service.member.MemberStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class ApplyDeleteService {

    private final MemberStatusService memberStatusService;
    private final ApplyResourceRepository applyResourceRepository;

    //사용자가 지원한 지원 파일 삭제
    public Boolean removeApply(HttpServletRequest request, @PathVariable Long applyId) {

        MemberDao currentMember = memberStatusService.findMember(request).get();
        Optional<ApplyResource> apply = applyResourceRepository.findById(applyId);

        if (apply.isPresent()) {
            if (currentMember.getId().equals(apply.get().getMemberDao().getId())) {
                applyResourceRepository.deleteById(applyId);
                return true;
            } else {
                log.info("wrong");
                return false;
            }
        }
        return false;
    }

}
