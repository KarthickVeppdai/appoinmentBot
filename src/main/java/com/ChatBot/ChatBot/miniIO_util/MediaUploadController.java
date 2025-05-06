package com.ChatBot.ChatBot.miniIO_util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/upload")
public class MediaUploadController {

    @Autowired
    public PdfUploaderService pdfUploaderService;

    @PostMapping(path = "pdf")
    public ResponseEntity<String> uploadPdf(@RequestParam String url, @RequestParam String bucket, @RequestParam String name) {
        try {
            pdfUploaderService.uploadPdfFromUrl(url, bucket, name);
            return ResponseEntity.ok("200");
        } catch (Exception e) {
            System.out.println("Inside Upload Exception"+e);
            return ResponseEntity.ok("200");
        }
    }

}
