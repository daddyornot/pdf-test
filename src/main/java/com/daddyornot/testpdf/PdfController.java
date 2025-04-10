package com.daddyornot.testpdf;

import com.daddyornot.testpdf.service.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class PdfController {

    @Autowired
    private PdfService pdfService;

    /**
     * Endpoint pour générer et télécharger un PDF.
     */
    @GetMapping("/pdf")
    public ResponseEntity<byte[]> generatePdf() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        Map<String, Object> data = new HashMap<>();
        data.put("title", "Rapport PDF");
        data.put("items", List.of(
                Map.of("name", "Produit A", "value", 1),
                Map.of("name", "Produit B", "value", 80),
                Map.of("name", "Produit C", "value", 150)
        ));
        data.put("currentTime", LocalDateTime.now().format(formatter)); // Heure actuelle



        byte[] pdfContent = pdfService.generatePdf("example", data);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", "rapport.pdf");

        return new ResponseEntity<>(pdfContent, headers, HttpStatus.OK);
    }
}
