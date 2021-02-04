package goorum.goorum.controller;

import goorum.goorum.domain.Board;
import goorum.goorum.domain.Boardlist;
import goorum.goorum.domain.Member;
import goorum.goorum.domain.Mypage;
import goorum.goorum.service.BoardService;
import goorum.goorum.service.MemberService;
import goorum.goorum.service.MypageService;
import goorum.goorum.service.ReplyService;
import goorum.goorum.util.Conversion;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static goorum.goorum.util.Constants.*;
import static java.util.Objects.isNull;

@Slf4j
@Controller
@RequestMapping("/mypage")
public class MypageController {
    private final String DEFAULT_PAGE = "1";
    private final int DEFAULT_LIST_SIZE = 10;
    private final String DEFAULT_TYPE = "board";

    @Autowired
    private BoardService boardService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private ReplyService replyService;

    @Autowired
    private MypageService mypageService;

    @GetMapping
    public void showMypage(@RequestParam(defaultValue = DEFAULT_TYPE, required = false) String type,
                                   @RequestParam(defaultValue = DEFAULT_PAGE, required = false) Integer page,
                                   @RequestParam(value = "id", required = false) Long memberId,
                                   HttpServletRequest request,
                                   HttpServletResponse response) throws IOException {
        ModelAndView mav = new ModelAndView();

        if (isNull(memberId)) {
            Member loginMember = (Member) request.getSession().getAttribute("loginMember");
            if (isNull(loginMember)) {
                mav.setViewName("redirect:/board");
                //return mav;
            }
            memberId = loginMember.getMemberId();
        }

        Mypage mypage = mypageService.getMypageInfo(memberId);
        if (isNull(mypage)) {
            //return ErrorPage.show();
        }

        // 내가쓴 게시글, 댓글 불러오기
        List<Boardlist> boards =  boardService.getListByMemberId(memberId, page - 1, DEFAULT_LIST_SIZE, mav);
        replyService.getListByMemberId(memberId, page - 1, DEFAULT_LIST_SIZE, mav);

        int startPage = Conversion.calcStartPage(page);
        mav.addObject("mypage", mypage);
        mav.addObject("type", type);
        mav.addObject("curPage", page);
        mav.addObject("startPage", startPage);
        mav.setViewName("my_page");

        String nickname = mypage.getNickname();
        Integer boardNum = mypage.getBoardNum();
        Integer replyNum = mypage.getReplyNum();

        JSONObject res = new JSONObject();

        res.put("nick", nickname);
        res.put("boardNum", boardNum); // 게시글 수
        res.put("replyNum", replyNum); // 답변 수

        JSONArray boardArray = new JSONArray();

        for(int i=0; i<boards.size(); i++){
            JSONObject data= new JSONObject();
            data.put("boardId",boards.get(i).getBoardId());
            data.put("category",boards.get(i).getCategory());
            data.put("sector",boards.get(i).getSector());
            data.put("company",boards.get(i).getCompany());
            data.put("title",boards.get(i).getTitle());
            data.put("content",boards.get(i).getContent());
            data.put("writerId",boards.get(i).getWriterId());
            data.put("writerNickname",boards.get(i).getWriterNickname());
            data.put("date",boards.get(i).getDate());
            data.put("likes",boards.get(i).getLikes());
            data.put("replies",boards.get(i).getReplies());
            boardArray.add(i,data);
        }
        res.put("board",boardArray);

        response.setContentType("application/json; charset=utf-8");
        response.getWriter().print(res);

        return;
    }

    @GetMapping("/show-profile")
    public ModelAndView showProfileImage(@RequestParam(value = "id") Long memberId) {
        ModelAndView mav = new ModelAndView();
        Mypage mypage = mypageService.getMypageInfo(memberId);
        mav.addObject("mypage", mypage);
        mav.setViewName("show_profile");

        return mav;
    }

    @GetMapping("/set-default-profile")
    public void setDefaultProfileImage(@RequestParam int id,
                                       HttpServletRequest request,
                                       HttpServletResponse response) throws IOException {
        Member loginMember = (Member) request.getSession().getAttribute("loginMember");
        JSONObject res = new JSONObject();
        if (isNull(loginMember) || loginMember.getMemberId() != id) {
            res.put("result", INVALID_APPROACH);
        }
        else {
            if (memberService.setProfilePhoto(loginMember.getMemberId(), null)) {
                res.put("result", SUCCESS);
            } else {
                res.put("result", FAIL);
            }
        }
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().print(res);
    }

}
