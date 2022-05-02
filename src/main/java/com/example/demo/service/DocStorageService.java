package com.example.demo.service;

import com.example.demo.model.Doc;
import com.example.demo.repository.DocRepository;
import fr.opensagres.poi.xwpf.converter.pdf.PdfConverter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;
import java.util.Optional;

@Service
public class DocStorageService {
	@Autowired
	private DocRepository docRepository;

	public Doc saveFile(MultipartFile file) {

		String docname = file.getOriginalFilename();
		try {
			Doc doc = new Doc(docname, file.getContentType(), file.getBytes());
			if (doc.getDocType().contains("application/vnd.openxmlformats-officedocument.wordprocessingml.document")){
				String item = wordToPdf("C:\\ferraz\\anotacoes\\estudos\\Senac\\" + doc.getDocName());
				doc.setDocType(item);
				System.out.println("doc.getDocName() = " + doc.getDocName());
				System.out.println("doc.getDocType() = " + doc.getDocType());

			}
			return docRepository.save(doc);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Optional<Doc> getFile(Integer fileId) {
		return docRepository.findById(fileId);
	}

	public List<Doc> getFiles() {
		return docRepository.findAll();
	}

	public String wordToPdf(String docname) {
		System.out.println("docname ============== " + docname);
			try {
//				InputStream docFile = new FileInputStream(new File("C:/Users/ferra/Downloads/senac.docx"));
				InputStream docFile = new FileInputStream(docname);
				XWPFDocument doc = new XWPFDocument(docFile);
				PdfOptions pdfOptions = PdfOptions.create();
				OutputStream out = new FileOutputStream(new File("C:/Users/ferra/Downloads/qqb.pdf"));
				PdfConverter.getInstance().convert(doc, out, pdfOptions);
				doc.close();
				out.close();
				System.out.println("Done");
				return out.toString();
			} catch (Exception e) {
				e.printStackTrace();
			}

		return "Error";
	}

}

