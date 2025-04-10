package com.daddyornot.testpdf.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.util.Map;

@Service
public class PdfService {

    @Autowired
    private TemplateEngine templateEngine;

    /**
     * Génère un PDF à partir d'un template HTML Thymeleaf.
     *
     * @param templateName Nom du fichier HTML (sans extension) dans le dossier templates.
     * @param data         Les données à injecter dans le template.
     * @return Un tableau de bytes représentant le contenu du PDF.
     */
    public byte[] generatePdf(String templateName, Map<String, Object> data) {
        // Générer le contenu HTML avec Thymeleaf
        Context context = new Context();
        context.setVariables(data);
        String htmlContent = templateEngine.process(templateName, context);

        // Convertir le HTML en PDF avec Flying Saucer
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(htmlContent);
            renderer.layout();
            renderer.createPDF(outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la génération du PDF", e);
        }
    }
}


