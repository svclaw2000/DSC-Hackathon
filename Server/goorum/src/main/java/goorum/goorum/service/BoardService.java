package goorum.goorum.service;

import goorum.goorum.domain.Board;
import goorum.goorum.domain.Boardlist;
import goorum.goorum.util.CurrentArticle;
import org.springframework.data.domain.Page;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

public interface BoardService {
    Page<Boardlist> getList(String category, int page, int size);

    boolean write(Long memberId, String title, String content, long category, String sector, String company);

    Boardlist getPostByIdForViewArticle(long boardId);

    Boardlist getPostById(long boardId);

    CurrentArticle getPrevAndNextArticle(long boardId);

    Board getBoardById(long boardId);

    Boardlist getBoardListById(long boardId);

    boolean modify(Board article, String title, String content, long category, String sector, String company);

    void deleteArticle(long boardId);

    List<Boardlist> getListByMemberId(long memberId, int page, int size, ModelAndView mav);

    List<Boardlist> getNotices();

    List<Boardlist> getTopLikes();

    public void convertArticleFormat(List<Boardlist> articles);
}
