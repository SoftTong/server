package com.example.demo.domain.entity;

import com.example.demo.dao.MemberDao;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "member_apply")
public class MemberApply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;
    private Long noticeId;
    private Long applyId;

    private String dtype;

    protected MemberApply(){

    }

    @Override
    public String toString() {
        return "MemberApply{" +
                "id=" + id +
                ", memberId=" + memberId +
                ", noticeId=" + noticeId +
                ", applyId=" + applyId +
                ", dtype='" + dtype + '\'' +
                '}';
    }

    public MemberApply(Long memberId, Long noticeId, Long applyId, String dtype) {
        this.memberId = memberId;
        this.noticeId = noticeId;
        this.applyId = applyId;
        this.dtype = dtype;
    }
}
