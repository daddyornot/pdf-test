package com.daddyornot.testpdf;

import com.daddyornot.testpdf.service.PdfService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.File;
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

        // request and response
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpServletResponse res = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        byte[] pdfContent = pdfService.generatePdf("example", data, req, res);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", "rapport.pdf");

        return new ResponseEntity<>(pdfContent, headers, HttpStatus.OK);
    }

    @GetMapping("/pdfi")
    public ResponseEntity<byte[]> generatePdfI() {
        // Données dynamiques
        Map<String, Object> data = new HashMap<>();
        data.put("title", "Rapport des Produits");
        data.put("items", List.of(
                Map.of("name", "Produit A", "value", 120),
                Map.of("name", "Produit B", "value", 80),
                Map.of("name", "Produit C", "value", 150)
        ));

        // Générer le PDF
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpServletResponse res = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        byte[] pdfContent = pdfService.generatePdfI("report", data, req, res);

        // Configurer les en-têtes HTTP
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", "rapport.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfContent);
    }
}
