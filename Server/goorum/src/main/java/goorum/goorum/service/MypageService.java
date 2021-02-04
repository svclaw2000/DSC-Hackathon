package goorum.goorum.service;

import goorum.goorum.domain.Mypage;

public interface MypageService {
    Mypage getMypageInfo(long memberId);
}
