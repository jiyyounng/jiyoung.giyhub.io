package com.jachmi.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.jachmi.domain.RentAttachVO;

import lombok.extern.log4j.Log4j;

@Log4j
@Controller
public class UploadController {
		
	@GetMapping("/uploadForm")
	public String uploadForm() {
		log.info("/upload form");
		return "/uploadForm";
	}
	
	@GetMapping("/display")
	@ResponseBody
	public ResponseEntity<byte[]> getFile(String fileName){
		log.info("fileName : " + fileName);
		
		File file = new File("/Users/jiyoung/Desktop/upload/" + fileName);
		
		log.info("file: " + file);
		
		ResponseEntity<byte[]> result = null;
		
		try {
			HttpHeaders header = new HttpHeaders();
			
			header.add("Content-Type", Files.probeContentType(file.toPath()));
			result=new ResponseEntity<>(FileCopyUtils.copyToByteArray(file),header, HttpStatus.OK);
		}catch(IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@PostMapping("/uploadFormAction")
	public void uploadFormPost(MultipartFile[] uploadFile, Model model) {
		/*
		 * MultipartFileDML APTHEM
		 * String getName() 				-파라미터의 이름 <input> 태그의 이름
		 * String getOriginalFileName()		- 업로드 파일의 이름
		 * boolean isEmpty()				- 파일이 존재하지 않는 경우 true
		 * long getSize()					- 업로드 파일의 크기
		 * byte[] getBytes()				- byte[]로 파일 데이터 변환
		 * InputStream getInputStream()		- 파일 데이터와 연결된 반환
		 * transforTo(File file)			- 파일 저장
		 */
		String uploadFolder = "/Users/jiyoung/Desktop/upload";
		
		for(MultipartFile multipartFile : uploadFile) {
			log.info("----------------------");
			log.info("upload File Name : " + multipartFile.getOriginalFilename());
			log.info("upload File Size : " + multipartFile.getSize());
			
			File saveFile = new File(uploadFolder, multipartFile.getOriginalFilename());
			
			try {
				multipartFile.transferTo(saveFile);
			}catch(Exception e) {
				log.error(e.getMessage());
			}
		}
	}
	
	@GetMapping("/uploadAjax")
	public String uploadAjax() {
		log.info("/upload Ajax");
		return "/uploadAjax";
	}
	
	@PostMapping(value = "/uploadAjaxAction", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public ResponseEntity<List<RentAttachVO>> uploadAjaxPost(MultipartFile[] uploadFile) {
		log.info("update ajax post...........");
		
		List<RentAttachVO> list = new ArrayList<RentAttachVO>();
		
		String uploadFolder = "/Users/jiyoung/Desktop/upload";
		
		//make folder
		String uploadFolderPath = getFolder();
		File uploadPath = new File(uploadFolder, uploadFolderPath);
		log.info("uploadPath : " + uploadPath);
		
		if(uploadPath.exists() == false) {
			uploadPath.mkdirs();
		}
		
		for(MultipartFile multipartFile : uploadFile) {
			log.info("----------------------");
			log.info("upload File Name : " + multipartFile.getOriginalFilename());
			log.info("upload File Size : " + multipartFile.getSize());
	
			RentAttachVO attachVO = new RentAttachVO();
			
			String uploadFileName = multipartFile.getOriginalFilename();
			
			uploadFileName = uploadFileName.substring(uploadFileName.lastIndexOf("\\") + 1);			
			log.info("only file name : " + uploadFileName);
			
			attachVO.setFileName(uploadFileName);  //dto 파일명(원본) 저장
			
			UUID uuid = UUID.randomUUID();
			
			uploadFileName = uuid.toString() + "_" + uploadFileName;

			try {
				File saveFile = new File(uploadPath, uploadFileName);
				multipartFile.transferTo(saveFile);
				
				attachVO.setUuid(uuid.toString());
				attachVO.setUploadPath(uploadFolderPath);
				
				list.add(attachVO);

			}catch(Exception e) {
				log.error(e.getMessage());
			}
		} //end for
		return new ResponseEntity<List<RentAttachVO>>(list,HttpStatus.OK);
	}
	
	public String getFolder() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String str = sdf.format(date);
		return str.replace("-", File.separator);
	}
	
	@GetMapping(value = "/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@ResponseBody
	public ResponseEntity<Resource> downloadFile(String fileName){
		log.info("download file : " + fileName);
		Resource resource = new FileSystemResource("/Users/jiyoung/Desktop/upload/" + fileName);
		log.info("resource : " + resource);
		
		String resourceName = resource.getFilename();
		
		String resourceOriginalName = resourceName.substring(resourceName.indexOf("_")+1);
		
		HttpHeaders headers = new HttpHeaders();
		
		try {
			headers.add("Content-Disposition", "attachment; filename=" +
						new String(resourceOriginalName.getBytes("utf-8"),"ISO-8859-1"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ResponseEntity<Resource>(resource, headers, HttpStatus.OK);
	}
	
	@PostMapping("deleteFile")
	@ResponseBody
	public ResponseEntity<String> deleteFile(String fileName){
		log.info("deleteFile : " + fileName);
		
		File file = null;
		// URLDecoder.decode(fileName, "utf-8")
		try {
			file = new File("/Users/jiyoung/Desktop/upload/" + fileName);
			file.delete();
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<String>("deleted",HttpStatus.OK);
	}
}
