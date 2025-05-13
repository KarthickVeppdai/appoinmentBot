package com.ChatBot.ChatBot.rag_service;

import com.ChatBot.ChatBot.chat_configuration.PgVector;
import com.ChatBot.ChatBot.miniIO_util.PdfUploaderService;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.parser.apache.pdfbox.ApachePdfBoxDocumentParser;
import dev.langchain4j.data.document.parser.apache.tika.ApacheTikaDocumentParser;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@RestController
@RequestMapping("/load")
public class LoadsDocuments {




    @Autowired
    public EmbeddingStoreIngestor embeddingStoreIngestor;

    @Autowired
    public PdfUploaderService pdfUploaderService;

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody String userRequest) throws Exception {
        System.out.println("Inside ");

        ApacheTikaDocumentParser parser = new ApacheTikaDocumentParser();
        ApachePdfBoxDocumentParser parser1 = new ApachePdfBoxDocumentParser();
        InputStream inputStream =pdfUploaderService.getPdfFromMinio("whatsapp-media", "hdetails.pdf");

        Document s=parser1.parse(inputStream);

        embeddingStoreIngestor.ingest(s);
        System.out.println("Loaded Document");
        return ResponseEntity.ok("200");

    }
}
