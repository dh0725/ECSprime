package prime.domain;

import lombok.Data;

@Data
public class Page {
	
	/* 필수 정보 기본값 상수화 */
	// 해당 페이지 번호
	public static final int PAGE_NUM = 1;
	// 페이지 당 게시글 수
	public static final int ROWS_PER_PAGE = 10;
	// 노출되는 페이지 개수
	public static final int PAGE_COUNT = 10;
	
	/* 필수 정보 */
	private int pageNum;						// 해당 페이지 번호
	private int rowsPerPage;					// 페이지 당 게시글 수
	private int pageCount = PAGE_COUNT;			// 노출되는 페이지 수
	private int totalCount;						// 전체 게시글 수
	
	/* 수식 정보 */
	private int startPage;						// 시작 페이지 번호
	private int endPage;						// 끝 페이지 번호
	private int firstPage;						// 첫번째 페이지 번호
	private int lastPage;						// 마지막 페이지 번호
	private int prev;							// 이전 페이지 번호
	private int next;							// 다음 페이지 번호
	private int startRowIndex;					// 시작 데이터의 index
	private String keyword;						// 검색어
	
	/* 생성자 */
	public Page() {
		this(0, 0);
	}
	public Page(int pageNum, int rowsPerPage) {
		this.pageNum = pageNum;
		this.rowsPerPage = rowsPerPage;
		
		this.startPage = ( (pageNum - 1) / pageCount ) * pageCount + 1;
		this.endPage = ( ((pageNum - 1) / pageCount) + 1 ) * pageCount;
	}
	public Page(int pageNum, int rowsPerPage, int pageCount , int totalCount, String keyword) {
		this.pageNum = pageNum;
		this.rowsPerPage = rowsPerPage;
		this.pageCount = pageCount;
		this.totalCount = totalCount;
		this.keyword = keyword;
		
		/* ----- 수식 ----- */
		// 시작
		this.startPage = ( (pageNum - 1) / pageCount) * pageCount + 1;
		// 끝
		this.endPage = ( (pageNum - 1) / pageCount + 1) * pageCount;
		
		// 첫
		this.firstPage = 1;
		// 마지막
		if (totalCount % rowsPerPage == 0) {
			this.lastPage = (totalCount / rowsPerPage);
		} else {
			this.lastPage = (totalCount / rowsPerPage) + 1;
		}
		
		// 이전
		this.prev = pageNum - 1;
		this.next = pageNum + 1;
		
		// 끝 > 마지막 ==> 보정
		if (this.endPage > this.lastPage) {
			this.endPage = this.lastPage;
		}
		
		this.startRowIndex = (pageNum - 1) * rowsPerPage;
	}

}
