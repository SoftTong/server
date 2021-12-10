package com.example.demo.service.role;

import com.example.demo.dao.MemberDao;
import com.example.demo.dao.RoleDao;
import com.example.demo.dao.RoleName;
import com.example.demo.domain.repository.MemberRepository;
import com.example.demo.domain.repository.RoleRepository;
import com.example.demo.exception.AppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class RoleStatusService {
    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;

    public boolean addRole(Long memberId, String role) {
        Optional<MemberDao> findMember = memberRepository.findById(memberId);
        if (findMember.isEmpty()) return false;

        MemberDao member = findMember.get();

        role = "ROLE_"+role.toUpperCase();
        RoleDao roleDao = roleRepository.findByName(RoleName.valueOf(role)).orElseThrow(() -> new AppException("Admin Role not set"));
        member.addRoles(roleDao);
        log.info("여기까진 되나??");
        log.info("{}",member.getName());
        log.info("roel = {}", role);
        memberRepository.save(member);
        return true;
    }

    public boolean removeRole(Long memberId, String role) {
        Optional<MemberDao> findMember = memberRepository.findById(memberId);
        if (findMember.isEmpty()) return false;

        MemberDao member = findMember.get();

        role = "ROLE_"+role.toUpperCase();
        RoleDao roleDao = roleRepository.findByName(RoleName.valueOf(role)).orElseThrow(() -> new AppException("Admin Role not set"));
        member.removeRoles(roleDao);
        log.info("여기까진 되나??");
        log.info("{}",member.getName());
        log.info("roel = {}", role);
        memberRepository.save(member);
        return true;
    }
}
