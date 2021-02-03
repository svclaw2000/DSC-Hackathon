package goorum.goorum.controller;

import goorum.goorum.domain.*;

import goorum.goorum.service.BoardService;
import goorum.goorum.service.CategoryService;
import goorum.goorum.service.MemberLikeBoardService;
import goorum.goorum.service.ReplyService;
import goorum.goorum.util.Conversion;
import goorum.goorum.util.CurrentArticle;
import goorum.goorum.util.ErrorPage;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static java.util.Objects.isNull;

import static goorum.goorum.util.Constants.*;
import static java.util.Objects.isNull;

@Slf4j
@Controller
@RequestMapping("/board")
public class BoardController {
    private final String DEFAULT_CATEGORY = "전체보기";
    private final String DEFAULT_PAGE = "1";
    private final String DEFAULT_LIST_SIZE = "10";
    private final String ON = "ON";
    private final int MAIN_PAGE= 1;

    @Autowired
    private BoardService boardService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ReplyService replyService;

    @Autowired
    private MemberLikeBoardService memberLikeBoardService;

    @GetMapping
    public ModelAndView showBoard(
            @RequestParam(defaultValue = DEFAULT_CATEGORY, required = false) String category,
            @RequestParam(defaultValue = DEFAULT_PAGE, required = false) Integer page,
            @RequestParam(defaultValue = DEFAULT_LIST_SIZE, required = false) Integer size) {
        ModelAndView mav = new ModelAndView();
        Page<Boardlist> boardlistPage = boardService.getList(category, page - 1, size);
        List<Boardlist> boards = boardlistPage.getContent();

        boardService.convertArticleFormat(boards);
        List<Category> categories = categoryService.getList();
        int totalPage = boardlistPage.getTotalPages();
        int startPage = Conversion.calcStartPage(page);

        if ( category.equals(DEFAULT_CATEGORY) &&
                page == MAIN_PAGE ) {
            List<Boardlist> notices = boardService.getNotices();
            List<Boardlist> topLikes = boardService.getTopLikes();
            boardService.convertArticleFormat(notices);
            boardService.convertArticleFormat(topLikes);
            mav.addObject("notices", notices);
            mav.addObject("topLikes", topLikes);
        }

        mav.setViewName("board");
        mav.addObject("boards", boards);
        mav.addObject("categories", categories);
        mav.addObject("selectCategory", category);
        mav.addObject("selectSize", size);
        mav.addObject("curPage", page);
        mav.addObject("totalPage", totalPage);
        mav.addObject("startPage", startPage);
        return mav;
    }

    @GetMapping("/write")
    public ModelAndView showWriteForm(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        Member loginMember = (Member) request.getSession().getAttribute("loginMember");

        if (isNull(loginMember)) {
            mav.setViewName("redirect:/board");
            return mav;
        }
        mav.setViewName("write_form");
        mav.addObject("categories", categoryService.getList());
        return mav;
    }

    @PostMapping("/write")
    public void write(@RequestParam String title,
                      @RequestParam String content,
                      @RequestParam int category,
                      @RequestParam String sector,
                      @RequestParam String company,
                      HttpServletRequest request,
                      HttpServletResponse response) throws IOException {
        JSONObject res = new JSONObject();
        Member loginMember = (Member) request.getSession().getAttribute("loginMember");

        if (isNull(loginMember)) {
            res.put(RESULT, INVALID_APPROACH);
        }
        else if (boardService.write(loginMember.getMemberId(), title, content, category, sector, company)) {
            res.put(RESULT, SUCCESS);
        } else {
            res.put(RESULT, FAIL);
        }

        response.setContentType("application/json; charset=utf-8");
        response.getWriter().print(res);
    }

    @GetMapping("/{idx}")
    public void showArticle(@PathVariable("idx") int boardId,
                                    HttpServletRequest request,
                                    HttpServletResponse response) throws IOException {
        JSONObject res = new JSONObject();

        Member member = (Member)request.getSession().getAttribute("loginMember");
        ModelAndView mav = new ModelAndView();
        if (!isNull(member)) {
            boolean isLike = memberLikeBoardService.isLike(boardId, member.getMemberId());
            mav.addObject("isLike", isLike);
            res.put(isLike, isLike);
        }

        Boardlist article = boardService.getPostByIdForViewArticle(boardId);
        CurrentArticle currentArticle = boardService.getPrevAndNextArticle(boardId);
        List<Replylist> replies = replyService.getRepliesByBoardId(boardId);

        mav.setViewName("view_article");
        mav.addObject("article", article);
        mav.addObject("replies", replies);
        mav.addObject("current", currentArticle);

        res.put("article",article);
        res.put("replies",replies);
        res.put("current",currentArticle);

        response.setContentType("application/json; charset=utf-8");
        response.getWriter().print(res);

        return;
    }

    @PostMapping("/write/reply")
    public void writeReply(@RequestParam int boardId,
                                   @RequestParam int parent,
                                   @RequestParam String content,
                                   HttpServletRequest request,
                                   HttpServletResponse response) throws IOException {
        JSONObject res = new JSONObject();
        Member member = (Member)request.getSession().getAttribute("loginMember");
        boolean result = replyService.writeReply(boardId, parent, content, member);

        if (!result) {
            res.put(RESULT, INVALID_APPROACH);
            return;
        }
        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:/board/"+boardId);

        res.put(RESULT, SUCCESS);
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().print(res);
        return;
    }

    @PostMapping("/delete/reply")
    public void deleteReply(@RequestParam int replyId,
                                    @RequestParam int parent,
                                    @RequestParam int boardId,
                                    HttpServletRequest request,
                                    HttpServletResponse response) throws IOException {

        JSONObject res = new JSONObject();
        Member member = (Member)request.getSession().getAttribute("loginMember");
        boolean result = replyService.deleteReply(replyId, parent, member);

        if (!result) {
            res.put(RESULT, INVALID_APPROACH);
            return;
        }
        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:/board/"+boardId);

        res.put(RESULT, SUCCESS);
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().print(res);
        return;
    }

    @GetMapping("/modify/{idx}")
    public void showModifyForm(@PathVariable("idx") long boardId,
                                       HttpServletRequest request,
                                       HttpServletResponse response) throws IOException {
        JSONObject res = new JSONObject();

        Boardlist article = boardService.getPostById(boardId);
        Member loginMember = (Member) request.getSession().getAttribute("loginMember");

        if ( isNull(article) ||
                isNull(loginMember) ||
                (loginMember.getMemberId() != article.getWriterId()) ) {
            res.put(RESULT, FAIL);
            return;
        }

        ModelAndView mav = new ModelAndView();
        mav.setViewName("modify_form");
        mav.addObject("article", article);
        mav.addObject("categories", categoryService.getList());

        res.put("article",article);
        res.put("categories", categoryService.getList());

        response.setContentType("application/json; charset=utf-8");
        response.getWriter().print(res);

        return;
    }

    @PostMapping("/modify")
    public void modify(@RequestParam long boardId,
                       @RequestParam String title,
                       @RequestParam String content,
                       @RequestParam int category,
                       @RequestParam String sector,
                       @RequestParam String company,
                       HttpServletRequest request,
                       HttpServletResponse response) throws IOException {
        Board article = boardService.getBoardById(boardId);
        Member loginMember = (Member) request.getSession().getAttribute("loginMember");
        JSONObject res = new JSONObject();

        if ( isNull(article) ||
                isNull(loginMember) ||
                (loginMember.getMemberId() != article.getWriter()) ) {
            res.put(RESULT, INVALID_APPROACH);
        }
        else if (boardService.modify(article, title, content, category, sector, company)) {
            res.put(RESULT, SUCCESS);
        }
        else {
            res.put(RESULT, FAIL);
        }

        response.setContentType("application/json; charset=utf-8");
        response.getWriter().print(res);
    }

    @PostMapping("/like")
    public void likeOnOff(@RequestParam long boardId,
                          @RequestParam String flag,
                          HttpServletRequest request,
                          HttpServletResponse response) throws IOException {
        JSONObject res = new JSONObject();
        Member loginMember = (Member) request.getSession().getAttribute("loginMember");

        if (isNull(loginMember)) {
            res.put(RESULT, INVALID_APPROACH);
        }
        else if (ON.equals(flag)) { // like on
            if (memberLikeBoardService.like(loginMember.getMemberId(), boardId)) {
                res.put(RESULT, SUCCESS);
            } else {
                res.put(RESULT, FAIL);
            }
        }
        else {  // like off
            memberLikeBoardService.dislike(loginMember.getMemberId(), boardId);
            res.put(RESULT, SUCCESS);
        }

        response.setContentType("application/json; charset=utf-8");
        response.getWriter().print(res);
    }

    @PostMapping("/delete")
    public void deleteArticle(@RequestParam long boardId,
                                      HttpServletRequest request,
                                      HttpServletResponse response) throws IOException {
        JSONObject res = new JSONObject();
        Board article = boardService.getBoardById(boardId);
        Member loginMember = (Member) request.getSession().getAttribute("loginMember");

        if ( isNull(article) ||
                isNull(loginMember) ||
                (loginMember.getMemberId() != article.getWriter() &&
                        loginMember.getMemberId() != ADMIN_ID )) {
            res.put(RESULT,INVALID_APPROACH);
            return;
        }
        boardService.deleteArticle(boardId);
        res.put(RESULT,SUCCESS);

        response.setContentType("application/json; charset=utf-8");
        response.getWriter().print(res);

        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:/board");
        return;
    }
}