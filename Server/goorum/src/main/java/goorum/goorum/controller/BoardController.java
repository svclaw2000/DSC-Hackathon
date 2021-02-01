package goorum.goorum.controller;

import goorum.goorum.domain.BoardList;
import goorum.goorum.repository.BoardRepository;
import goorum.goorum.repository.BoardListRepository;
import goorum.goorum.util.DateFormatConversion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;


@RestController
public class BoardController {

    @Autowired
    private BoardListRepository boardlistRepository;
    @Autowired
    private BoardRepository boardRepository;


    @GetMapping("/")
    public ModelAndView hello(){
        ModelAndView mav = new ModelAndView();
        List<BoardList> boards = boardlistRepository.findAll();

        DateFormatConversion.conversion(boards);

        mav.setViewName("board");
        mav.addObject("boards", boards);
        return mav;
    }
}
