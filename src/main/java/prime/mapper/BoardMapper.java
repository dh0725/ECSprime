package prime.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import prime.domain.Board;
import prime.domain.BoardFile;
import prime.domain.Page;

@Mapper
public interface BoardMapper {
	
	// 게시글 목록
	public List<Board> list() throws Exception;
	// [페이징] 게시글 목록
	public List<Board> listWithPage(Page page);
	// [페이징] 검색된 게시글 수 조회
	public Integer totalCount(String keyword) throws Exception;
	
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
