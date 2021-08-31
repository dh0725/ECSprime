package prime.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import prime.domain.Board;
import prime.domain.BoardDTO;
import prime.domain.BoardFile;
import prime.domain.Page;
import prime.mapper.BoardMapper;

@Service
public class BoardServiceImpl implements BoardService {
	
	@Autowired
	private BoardMapper mapper;
	@Autowired
	BoardDTO boardDTO;

	// 게시글 목록
	@Override
	public List<Board> list() throws Exception {

		return mapper.list();
	}
	// [페이징] 게시글 목록
	@Override
	public BoardDTO list(Page page) throws Exception {
		String keyword = page.getKeyword();
		Integer totalCount = page.getTotalCount();
		Integer rowsPerPage = page.getRowsPerPage();
		Integer pageCount = page.getPageCount();
		Integer pageNum = page.getPageNum();
		/*
		totalCount = totalCount == 0 ? service.totalCount(keyword) : totalCount;
		rowsPerPage = rowsPerPage == 0 ? Page.ROWS_PER_PAGE : rowsPerPage;
		pageNum = pageCount == 0 ? Page.PAGE_COUNT : pageCount;
		pageNum = pageNum == 0 ? Page.PAGE_NUM; : pageNum;
		*/
		
		// 검색어
		keyword = keyword == null || keyword.trim().equals("") ? "" : keyword;
		// 조회된 전체 게시글 수
		if (totalCount == 0) {
			totalCount = mapper.totalCount(keyword);
		}
		// 페이지 당 게시글 수
		if (rowsPerPage == 0) {
			rowsPerPage = Page.ROWS_PER_PAGE;
		}
		// 노출되는 페이지 수
		if (pageCount == 0) {
			pageCount = Page.PAGE_COUNT;
		}
		// 해당(현재) 페이지 번호
		if (pageNum == 0) {
			pageNum = Page.PAGE_NUM;
		}
		
		page = new Page(pageNum, rowsPerPage, pageCount, totalCount, keyword);
		List<Board> list = mapper.listWithPage(page);
		
		boardDTO.setPage(page);
		boardDTO.setBoardList(list);
		
		return boardDTO;
	}

	// 게시글 등록
	@Override
	public void create(Board board) throws Exception {
		mapper.create(board);
	}
	// 파일 업로드
	@Override
	public void uploadFile(BoardFile file) throws Exception {
		mapper.uploadFile(file);
	}
	
	// 게시글 조회
	@Override
	public Board read(Integer boardNo) throws Exception {

		return mapper.read(boardNo);
	}
	// 파일 목록 조회
	@Override
	public List<BoardFile> readFileList(Integer boardNo) throws Exception {

		return mapper.readFileList(boardNo);
	}
	// 파일 조회
	@Override
	public BoardFile readFile(Integer fileNo) throws Exception {

		return mapper.readFile(fileNo);
	}
	
	// 게시글 수정
	@Override
	public void update(Board board) throws Exception {
		mapper.update(board);
	}
	
	// 게시글 삭제
	@Override
	public void delete(Integer boardNo) throws Exception {
		mapper.delete(boardNo);
	}
	// 파일 삭제
	@Override
	public void deleteFile(Integer fileNo) throws Exception {
		mapper.deleteFile(fileNo);
	}

}
