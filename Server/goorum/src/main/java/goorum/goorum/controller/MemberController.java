package goorum.goorum.controller;

import goorum.goorum.domain.Member;
import goorum.goorum.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MemberController {

    @Autowired
    private MemberRepository memberRepository;

    @RequestMapping("/members")
    public List getMembers(){
        System.out.println("aaa");
        List<Member> list = memberRepository.findAll();
        return list;
    }
}