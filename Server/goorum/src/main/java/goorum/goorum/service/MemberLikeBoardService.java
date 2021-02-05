package goorum.goorum.service;

import goorum.goorum.domain.Member;
import goorum.goorum.domain.MemberLikeBoard;

import java.util.List;

public interface MemberLikeBoardService {
    boolean isLike(long boardId, long memberId);

    boolean like(long memberId, long boardId);

    void dislike(long memberId, long boardId);

    List<MemberLikeBoard> likeBoard(long memberId);
}

