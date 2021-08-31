package prime.domain;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class BoardDTO {
	
	private Board board;						// 게시판 DTO
	private Page page;							// 페이지 DTO
	private List<Board> boardList;				// 게시판 리스트 DTO
	private List<BoardFile> fileList;			// 파일 DTO

}
