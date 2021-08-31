package prime.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import prime.domain.Board;
import prime.domain.BoardDTO;
import prime.domain.BoardFile;
import prime.domain.Page;
import prime.service.BoardService;
import prime.utils.FileUtils;

@Slf4j												// log
@Controller											// @Controller
@RequestMapping("/board")
public class BoardController {
	
	@Autowired
	private BoardService service;
	@Autowired
	private FileUtils fileUtils;
	
	// 파일 업로드를 위한 경로
	@Value("${upload.path}")
	private String uploadPath;
	
	// 게시글 목록
	@GetMapping("/list")
	public String list(Model model, Board board, Page page) throws Exception {
		BoardDTO dto = service.list(page);
		
		model.addAttribute("list", dto.getBoardList());
		model.addAttribute("page", dto.getPage());
		
		return "board/list";	
	}
	
	// 게시글 등록 - 화면 
	@GetMapping("/register")
	public void registerForm(Model mopel, Board board) throws Exception { }
	// 게시글 등록 - 처리
	@PostMapping("/register")
	public String register(Model model, Board board) throws Exception {
		// 게시글 등록 요청
		service.create(board);
		
		// 파일 업로드 요청
		MultipartFile[] files = board.getFile();
		
		// 파일 정보 확인하기
		for (MultipartFile file : files) {
			log.info("filename - " + file.getOriginalFilename());
			log.info("contentType - " + file.getContentType());
			log.info("size - " + file.getSize());
		}
		
		List<BoardFile> fileList = fileUtils.uploadFiles(files, uploadPath);
		
		// DB에 파일 업로드 요청
		for (BoardFile file : fileList) {
			service.uploadFile(file);
		}
		
		model.addAttribute("msg", "게시글 등록이 완료되었습니다!");
		
		return "board/success";
	}
	
	// 게시글 조회 - 화면
	@GetMapping("/read/{boardNo}")
	public String read(Model model, @PathVariable("boardNo") Integer boardNo, Board board) throws Exception {
		// 게시글 조회 요청
		board = service.read(boardNo);
		model.addAttribute("board", board);
		
		// 파일 목록 조회 요청
		List<BoardFile> fileList = service.readFileList(boardNo);
		model.addAttribute("fileList", fileList);
		
		return "board/read";
	}
	
	// 게시글 수정 - 화면
	@GetMapping("/modify/{boardNo}")
	public String modifyForm(Model model, @PathVariable("boardNo") Integer boardNo, Board board) throws Exception {
		// 게시글 수정 요청
		board = service.read(boardNo);
		model.addAttribute("board", board);
		
		// 파일 목록 요청
		List<BoardFile> fileList = service.readFileList(boardNo);
		model.addAttribute("fileList", fileList);
		
		return "board/modify";
	}
	// 게시글 수정 - 처리
	@PostMapping("/modify/{boardNo}")
	public String modify(Model model, @PathVariable("boardNo") Integer boardNo, Board board, Integer[] deleteFileSync) throws Exception {
		// 체크한 파일 삭제하기 
		if (deleteFileSync != null) {
			for (int fileNo : deleteFileSync) {
				log.info("fileNo > " + fileNo);
				fileUtils.deleteFileSync(fileNo);
			}
		}
		
		// 게시글 수정 요청
		service.update(board);
		
		// 파일 업로드 요청
		MultipartFile[] files = board.getFile();
		
		// 파일 정보 확인하기
		for (MultipartFile file : files) {
			log.info("filename - " + file.getOriginalFilename());
			log.info("contentType - " + file.getContentType());
			log.info("size - " + file.getSize());
		}
		
		List<BoardFile> fileList = fileUtils.uploadFiles(files, uploadPath);
		
		// DB에 파일 업로드 요청
		for (BoardFile file : fileList) {
			service.uploadFile(file);
		}
		
		model.addAttribute("msg", "수정이 완료되었습니다.");
		
		return "board/success";
	}
	
	// 게시글 삭제
	@PostMapping("/remove/{boardNo}")
	public String remove(Model model, @PathVariable("boardNo") Integer boardNo) throws Exception {
		// 파일
		// 파일 게시판의 boardNo에 외래키를 지정 후 ON DELETE, ON UPDATE 옵션으로 CASCADE
		// 게시판의 게시글 삭제 시 파일 게시판의 파일들도 함께 삭제함
		List<BoardFile> fileList = service.readFileList(boardNo);
		fileUtils.deleteFiles(fileList);
		
		// 게시글 삭제 요청
		service.delete(boardNo);
		
		model.addAttribute("msg", "삭제가 완료되었습니다..");
		
		return "/board/success";
	}
	
	// 파일 목록 갱신
	@ResponseBody
	@GetMapping("/files/{boardNo}")
	public List<BoardFile> fileList(@PathVariable("boardNo") Integer boardNo) throws Exception {
		List<BoardFile> fileList = service.readFileList(boardNo);
		
		return fileList;
	}

}
