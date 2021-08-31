package prime.domain;

import java.util.Date;

import lombok.Data;

@Data
public class BoardFile {
	
	private int fileNo;							// 파일 번호
	private int boardNo;						// 게시글 번호
	private String fileName;					// 파일 이름
	private String fullName;					// 파일 경로
	private Date regDate;						// 파일 등록일자

}
