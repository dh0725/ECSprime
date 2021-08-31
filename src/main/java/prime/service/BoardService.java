package prime.service;

import java.util.List;

import prime.domain.Board;
import prime.domain.BoardDTO;
import prime.domain.BoardFile;
import prime.domain.Page;

public interface BoardService {
	
	// 게시글 목록
	public List<Board> list() throws Exception;
	// [페이징] 게시글 목록
	public BoardDTO list(Page page) throws Exception;
	
	// 게시글 등록
	public void create(Board board) throws Exception;
	// 파일 업로드
	public void uploadFile(BoardFile file) throws Exception;
	
	// 게시글 조회
	public Board read(Integer boardNo) throws Exception;
	// 파일 목록 조회
	public List<BoardFile> readFileList(Integer boardNo) throws Exception;
	// 파일 조회
	public BoardFile readFile(Integer fileNo) throws Exception;
	
	// 게시글 수정
	public void update(Board board) throws Exception;
	
	// 게시글 삭제
	public void delete(Integer boardNo) throws Exception;
	// 파일 삭제
	public void deleteFile(Integer fileNo) throws Exception;

}
