package com.example.demo.service;
import com.example.demo.model.DocDto;
import com.example.demo.repository.DocRepository;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DocStorageService {
    @Autowired
    private DocRepository docRepository;

    public static File createTempDirectory()
            throws IOException
    {
        final File temp;

        temp = File.createTempFile("temp", Long.toString(System.nanoTime()));

        if(!(temp.delete()))
        {
            throw new IOException("Could not delete temp file: " + temp.getAbsolutePath());
        }

        if(!(temp.mkdir()))
        {
            throw new IOException("Could not create temp directory: " + temp.getAbsolutePath());
        }

        return (temp);
    }

    public DocDto saveFile(MultipartFile file) {

        try {
            Path path = Files.createTempFile("",file.getOriginalFilename());
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            // create temporary folder

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        String docname = file.getOriginalFilename();
        try {
            DocDto doc = new DocDto(docname, file.getContentType(), file.getBytes());
            if (doc.getDocType().contains("application/vnd.openxmlformats-officedocument.wordprocessingml.document")) {
                String item = wordToPdf(docname);
            }
            return docRepository.save(doc);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Optional<DocDto> getFile(Integer fileId) {
        return docRepository.findById(fileId);
    }

    public List<DocDto> getFiles() {
        return docRepository.findAll();
    }

    public String wordToPdf(String docname) {
//        System.out.println("docname = " + docname);
//        try {
//            //InputStream docFile = new FileInputStream(new File("C:/Users/ferra/Downloads/senac.docx"));
//            InputStream docFile = new FileInputStream(docname);
//            XWPFDocument doc = new XWPFDocument(docFile);
//            PdfOptions pdfOptions = PdfOptions.create();
//            OutputStream out = new FileOutputStream(new File("C:/Users/ferra/Downloads/qqb.pdf"));
//            PdfConverter.getInstance().convert(doc, out, pdfOptions);
//
//            out.write();
//
//            doc.close();
//            out.close();
//            System.out.println("Done");
//            return out.toString();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        return "Error";
    }

}

