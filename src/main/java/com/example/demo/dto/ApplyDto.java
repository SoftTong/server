package com.example.demo.dto;

import com.example.demo.dao.MemberDao;
import com.example.demo.domain.entity.ApplyFileNoticeEntity;
import com.example.demo.domain.entity.MemberApply;
import com.example.demo.domain.entity.NoticeEntity;
import com.example.demo.domain.entity.StatusName;
import com.example.demo.domain.repository.ApplyFileRepository;
import com.example.demo.domain.repository.MemberApplyRepository;
import com.example.demo.domain.repository.MemberRepository;
import com.example.demo.domain.repository.NoticeRepository;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Optional;

@Getter
@Setter
public class ApplyDto {
    private String noticeTitle;
    private StatusName status;
    private Long memberId;
    private String memberName;
    private String userId;
    private Timestamp uploadDay;
    private String dtype;

    public ApplyDto(MemberApply memberApply, MemberRepository memberRepository, NoticeRepository noticeRepository, ApplyFileRepository applyFileRepository){

        MemberDao member = memberRepository.findById(memberApply.getMemberId()).get();
        NoticeEntity noticeEntity =  noticeRepository.findById(memberApply.getNoticeId()).get();
        
        if(memberApply.getDtype().equals("file")){

            System.out.println("file type");
            ApplyFileNoticeEntity applyFileNoticeEntity = applyFileRepository.findById(memberApply.getNoticeId()).get();

            this.noticeTitle = noticeEntity.getName();
            this.status = applyFileNoticeEntity.getStatus();
            this.memberId = member.getId();
            this.memberName = member.getName();
            this.userId = member.getUserId();
            this.uploadDay = applyFileNoticeEntity.getUploadDay();
            this.dtype = memberApply.getDtype();
        }

        else{
            System.out.println("form type");
        }
    }
}
