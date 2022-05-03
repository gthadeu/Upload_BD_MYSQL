package com.example.demo.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import com.example.demo.model.DocDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;
import com.example.demo.service.DocStorageService;

@Controller
public class DocController {

	@Autowired 
	private DocStorageService docStorageService;
	
	@GetMapping("/")
	public String get(Model model) {
		List<DocDto> docs = docStorageService.getFiles();
		model.addAttribute("docs", docs);
		return "doc";
	}
	
	@PostMapping("/uploadFiles")
	public String uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {

		long sumBytes = Arrays.stream(files).mapToLong(o -> o.getSize()).sum();
		final int FILE_MAX_SIZE_MB = 5 * 1024 * 1024;
		final List<String> FILE_EXTENSIONS = Arrays.asList(".pdf", ".png", ".jpg", ".xls", ".xlsx");

		if(sumBytes > FILE_MAX_SIZE_MB)
		{
			return "ERRO";
		}

//		if(Arrays.stream(files).anyMatch(c-> FILE_EXTENSIONS.stream().noneMatch(f -> !c.getName().contains(f) )))
//		{
//			return "ERRO";
//		}

		for (MultipartFile file: files)
		{
			System.out.println("file = " + file.getName());
			System.out.println("file.getOriginalFilename() = " + file.getOriginalFilename());
			//if(file.getContentType()== "") {
			docStorageService.saveFile(file);
			//}


		}
		return "redirect:/";
	}
	@GetMapping("/downloadFile/{fileId}")
	public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable Integer fileId){
		DocDto doc = docStorageService.getFile(fileId).get();
		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType(doc.getDocType()))
				.header(HttpHeaders.CONTENT_DISPOSITION,"attachment:filename=\""+doc.getDocName()+"\"")
				.body(new ByteArrayResource(doc.getData()));
	}
	
}
