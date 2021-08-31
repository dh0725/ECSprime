package prime.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import prime.domain.BoardFile;
import prime.service.BoardService;

@Slf4j
@Component
public class FileUtils {
	
	@Autowired
	private BoardService service;
	
	/**
	 * 파일 업로드 메소드
	 * @param files
	 * @param uploadPath
	 * @return
	 * @throws Exception
	 */
	public List<BoardFile> uploadFiles(MultipartFile[] files, String uploadPath) throws Exception {
		ArrayList<BoardFile> fileList = new ArrayList<BoardFile>();
		
		// 업로드 경로에 파일을 복사
		for (MultipartFile file : files) {
			// 파일 존재여부 확인하기
			if (file.isEmpty()) {
				continue;
			}
			
			UUID uid = UUID.randomUUID(); // 파일명 중복을 방지하기 위해 고유 ID를 생성
			String originalFileName = file.getOriginalFilename(); // 실제 원본 파일 이름
			String uploadFileName = uid.toString() + "_" + originalFileName; // 원본 파일 이름에 UID를 붙힌 이름
			byte[] fileData = file.getBytes(); // upload 폴더에 업로드 할 파일을 복사함
			File target = new File(uploadPath, uploadFileName); // ~/upload/UID_원본이름.jpg 의 형태
			// fileData : 요청 된 파일(upload 폴더에 올라간 파일)
			// target : 업로드 할 파일 객체
			FileCopyUtils.copy(fileData, target);
			String uploadedPath = uploadPath + "/" + uploadFileName; // 업로드 된 파일 경로는 경로 + 파일
			
			BoardFile f = new BoardFile();
			f.setFullName(uploadedPath);
			f.setFileName(originalFileName);
			fileList.add(f);
		}
		
		return fileList;
	}
	
	/**
	 * 동기 요청에 대한 단일 파일 삭제
	 * @param fileNo
	 * @throws Exception
	 */
	public void deleteFileSync(Integer fileNo) throws Exception {
		BoardFile file = service.readFile(fileNo);
		
		String fullName = file.getFullName();
		File deleteFile = new File(fullName);
		
		// 실제로 파일이 존재하는지 확인
		if (deleteFile.exists()) {
			// 파일 삭제
			if (deleteFile.delete()) {
				log.info("삭제한 파일 : " + fullName);
				log.info("파일 삭제 성공!");
				service.deleteFile(fileNo);
			} else {
				log.info("파일 삭제 실패!");
			} 
		} else {
			log.info("삭제(실패) : " + fullName);
			log.info("파일이 존재하지 않습니다.");
		}
	}
	
	/**
	 * 전체 파일 삭제 메소드
	 * @param fileList
	 * @throws Exception
	 */
	public void deleteFiles(List<BoardFile> fileList) throws Exception {
		// 해당 게시글의 첨부파일들 모두 삭제하기
		for (BoardFile file : fileList) {
			String fullName = file.getFullName();
			File deleteFile = new File(fullName);
			
			// 실제로 파일이 존재하는지 확인하기
			if (deleteFile.exists()) {
				// 파일 삭제
				if (deleteFile.delete()) {
					log.info("삭제한 파일 > " + fullName);
					log.info("파일 삭제 성공!");
				} else {
					log.info("파일 삭제 실패...");
				}
			} else {
				log.info("삭제(실패) > " + fullName);
				log.info("삭제할 파일이 존재하지 않습니다...");
			}
		}
	}

}
