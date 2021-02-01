package goorum.goorum.util;

import goorum.goorum.domain.BoardList;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DateFormatConversion {
    public static void conversion(List<BoardList> boards) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(new Date());

        for (BoardList board : boards) {

            if (board.getDate().substring(0, 10).equals(today)) {
                board.setDate(board.getDate().substring(11, 16));
            } else {
                board.setDate(board.getDate().substring(0, 10));
            }
        }
    }
}

