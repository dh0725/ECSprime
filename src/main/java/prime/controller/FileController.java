package prime.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import prime.domain.BoardFile;
import prime.service.BoardService;
import prime.utils.MediaUtils;

@Slf4j
@RestController
@RequestMapping("/files")
public class FileController {
	
	@Autowired
	private BoardService service;
	
	/**
	 * 단일 파일 다운로드
	 * @param request
	 * @param response
	 * @param fileNo
	 * @throws Exception
	 */
	@GetMapping("/download/{fileNo}")
	public void fileDownload(HttpServletRequest request, HttpServletResponse response, @PathVariable("fileNo") Integer fileNo) throws Exception {
		BoardFile boardFile = service.readFile(fileNo);
		
		String fullName = boardFile.getFullName();				// 파일 경로
		String fileName = boardFile.getFileName();				// 파일 이름
		
		// 다운로드 할 파일
		File file = new File(fullName);
		
		FileInputStream fileInputStream = null;
		ServletOutputStream servletOutputStream = null;
		
		try {
			String downName = null;
			String browser = request.getHeader("User-Agent");
			
			// 브라우저 별로 파일명 인코딩하기
			if (browser.contains("MSIE") || browser.contains("Trident") || browser.contains("Chrome")) {
				downName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
			} else {
				downName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
			}
			
			// response 헤더에 다운로드 파일명, 전송 인코딩등 세팅하기
			response.setHeader("Content-Disposition","attachment;filename=\"" + downName + "\"");
		    response.setContentType("text/html");
		    response.setHeader("Content-Transfer-Encoding", "binary;");
		    
		    fileInputStream = new FileInputStream(file);
		    servletOutputStream = response.getOutputStream();
		    
		    byte b[] = new byte[1024];
		    int data = 0;
		    
		    // 5KB = 1024B * 5 = 5120B
		    // 만약 5150B (== 5KB + 30B)
		    // 1024 => 1024 => 1024 => 1024 => 1024 => 30
		    while ((data = (fileInputStream.read(b, 0, b.length))) != -1) {
		    	servletOutputStream.write(b, 0 ,data);
		    }
		    servletOutputStream.flush(); // 파일 출력
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (servletOutputStream != null) {
				try {
					servletOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fileInputStream != null) {
				try {
					fileInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 전체 파일 압축 다운로드
	 * @param request
	 * @param response
	 * @param boardNo
	 * @throws Exception
	 */
	@GetMapping("/download/zip/{boardNo}")
	public void zipDonwload(HttpServletRequest request, HttpServletResponse response, @PathVariable("boardNo") Integer boardNo) throws Exception {
		// 글 제목
		String title = service.read(boardNo).getTitle();
		
		// 글 번호에 따른 모든 첨부파일 목록 조회
		List<BoardFile> fileList = service.readFileList(boardNo);
		
		// 다운로드 할 파일 명
		String zipFile = "temp.zip";
		String downloadFileName = title;
		
		// 브라우저 별로 파일명 인코딩하기
		String browser = request.getHeader("User-Agent");
		if (browser.contains("MSIE") || browser.contains("Trident") || browser.contains("Chrome")) {
			downloadFileName = URLEncoder.encode(downloadFileName, "UTF-8").replaceAll("\\+", "%20");
		} else {
			downloadFileName = new String(downloadFileName.getBytes("UTF-8"), "ISO-8859-1");
		}
		
		// 파일 압축하기
		try {
			FileOutputStream fout = new FileOutputStream(zipFile);
			ZipOutputStream zout = new ZipOutputStream(fout);
			
			for (int i = 0; i < fileList.size(); i++) {
				// 본래 파일명은 유지하고 경로를 제외한 파일압출을 위해 new File로
				// 압출 파일의 파일항목을 실제 파일명으로 추가하기
				ZipEntry zipEntry = new ZipEntry(new File(fileList.get(i).getFileName()).getName());
				zout.putNextEntry(zipEntry);
				
				// 해당 파일의 데이터(byte)를 추가하기
				FileInputStream fin = new FileInputStream(fileList.get(i).getFullName());
				byte[] buffer = new byte[1024];
				int length;
				
				while ((length = fin.read(buffer)) > 0) {
					zout.write(buffer, 0, length);
				}
				zout.closeEntry();
				fin.close();
			}
			zout.close();
			
			response.setContentType("application/zip");
			response.addHeader("Content-Disposition", "attachment; filename = " + downloadFileName + ".zip");
			
			FileInputStream fis = new FileInputStream(zipFile);
			BufferedInputStream bis = new BufferedInputStream(fis);
			ServletOutputStream so = response.getOutputStream();
			BufferedOutputStream bos = new BufferedOutputStream(so);
			
			byte[] data = new byte[2048];
			int input = 0;
			
			while ((input = bis.read(data)) != -1) {
				bos.write(data, 0, input);
				bos.flush();
			}
			
			if (fis != null) fis.close();
			if (bis != null) bis.close();
			if (so != null) so.close();
			if (bos != null) bos.close();
		} catch (Exception e) {
			
		}
	}
	
	/**
	 * Ajax 비동기 요청에 대한 단일 파일 삭제
	 * @param fileNo
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/delete/{fileNo}")
	public ResponseEntity<String> deleteFileAsync(@PathVariable("fileNo") Integer fileNo) throws Exception {
		BoardFile boardFile = service.readFile(fileNo);
		String fullName = boardFile.getFullName();
		
		File file = new File(fullName);
		
		// 실제로 파일이 존재하는지 확인
		if (file.exists()) {
			if (file.delete() ) {
				log.info("삭제할 파일 > " + fullName);
				log.info("파일 삭제 성공");
				service.deleteFile(fileNo);
			} else {
				log.info("파일 삭제 실패");
			}
		} else {
			log.info("파일 삭제 실패 > " + fullName);
			log.info("파일이 존재하지 않습니다.");
		}
		
		return new ResponseEntity<String>("success", HttpStatus.OK);
	}
	
	/**
	 * 이미지 썸네일 보여주기 메소드
	 * @param fullName
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/image")
	public ResponseEntity<byte[]> displayFile(String fullName) throws Exception {
		InputStream in = null;
		ResponseEntity<byte[]> entity = null;
		log.info("fileName > " + fullName);
		
		try {
			String formatName = fullName.substring(fullName.lastIndexOf(".") + 1); // 확장자 찾기 
			MediaType mType = MediaUtils.getMediaType(formatName);
			
			HttpHeaders headers = new HttpHeaders();
			in = new FileInputStream(fullName);
			
			// 이미지 타입인지 확인함
			if (mType != null) {
				headers.setContentType(mType);
			} else {
				fullName = fullName.substring(fullName.lastIndexOf("_") + 1); // UUID_강아지.png
				headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
				headers.add("Content-Disposition", "attachment; fullName=\"" + new String(fullName.getBytes("UTF-8"), "ISO-8859") + "\"");
			}
			entity = new ResponseEntity<byte[]>(IOUtils.toByteArray(in), headers, HttpStatus.CREATED);
			
		} catch (Exception e) {
			e.printStackTrace();
			entity = new ResponseEntity<byte[]>(HttpStatus.BAD_REQUEST);
		} finally {
			in.close();
		}
		
		return entity;
	}

}
