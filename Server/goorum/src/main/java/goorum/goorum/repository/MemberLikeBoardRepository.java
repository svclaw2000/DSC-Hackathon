package goorum.goorum.repository;

import goorum.goorum.domain.MemberLikeBoard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberLikeBoardRepository extends JpaRepository<MemberLikeBoard, Long> {
    Optional<MemberLikeBoard> findByBoardIdAndMemberId(long boardId, long memberId);
    List<MemberLikeBoard> findByMemberId(long memberId);

}
