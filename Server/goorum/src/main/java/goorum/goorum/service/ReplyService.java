package goorum.goorum.service;

import goorum.goorum.domain.Member;
import goorum.goorum.domain.Replylist;
import org.springframework.data.domain.Page;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

public interface ReplyService {
    List<Replylist> getRepliesByBoardId(long boardId);

    boolean writeReply(long boardId, long parent, String content, Member member);

    boolean deleteReply(long replyId, long parent, Member member);

    int getListByMemberId(long memberId, int page, int size, ModelAndView mav);
}
