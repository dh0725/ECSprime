package prime.domain;

import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class Board {
	
	private int rowNo;							// 행 번호
	private int boardNo;						// 게시글 번호
	private String title;						// 게시글 제목
	private String content;						// 게시글 내용
	private String writer;						// 게시글 작성자
	private Date regDate;						// 게시글 등록일자
	
	private MultipartFile[] file; 				// 파일 정보

}
