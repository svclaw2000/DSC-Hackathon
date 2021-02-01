package goorum.goorum.repository;

import goorum.goorum.domain.BoardList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardListRepository extends JpaRepository<BoardList, Long>{
}
