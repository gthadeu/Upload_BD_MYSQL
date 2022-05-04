package com.example.demo.service;

import com.example.demo.model.DocDto;
import com.example.demo.repository.DocRepository;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

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
            String[] list = {"C:\\Users\\Guilherme\\Downloads\\pdf\\1.pdf","C:\\Users\\Guilherme\\Downloads\\pdf\\2.pdf"};
            mergePdf(list);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        String docname = file.getOriginalFilename();
        try {
            DocDto doc = new DocDto(docname, file.getContentType(), file.getBytes());
            String b64 = Base64.getEncoder().encodeToString(doc.getData());
            if (doc.getDocType().contains("application/vnd.openxmlformats-officedocument.wordprocessingml.document")) {
                String item = wordToPdf(docname);
            }
            System.out.println("BASE64 =======>" + b64);
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

    public String wordToPdf(String docname) throws IOException {
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

    public void mergePdf(String[] allFiles) throws IOException {
        PDFMergerUtility ut = new PDFMergerUtility();
        for (int i = 0; i < allFiles.length; i++) {
            ut.addSource(allFiles[i]);
//            Base64.getEncoder().encodeToString(allFiles[i].getBytes());
            System.out.println("allfiles[" + i + "] = " + allFiles[i]);
        }
        ut.setDestinationFileName("C:\\Users\\Guilherme\\Downloads\\pdf\\merge.pdf");
    }
    
}

