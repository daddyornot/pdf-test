package com.daddyornot.testpdf.service;

import com.itextpdf.html2pdf.HtmlConverter;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.ServletWebRequest;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.servlet.IServletWebExchange;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Service
public class PdfService {

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private ServletContext servletContext;

    /**
     * Génère un PDF à partir d'un template HTML Thymeleaf.
     *
     * @param templateName Nom du fichier HTML (sans extension) dans le dossier templates.
     * @param data         Les données à injecter dans le template.
     * @return Un tableau de bytes représentant le contenu du PDF.
     */
    public byte[] generatePdf(String templateName, Map<String, Object> data, HttpServletRequest request, HttpServletResponse response) {
        // Générer le contenu HTML avec Thymeleaf
//        Context context = new Context();
        // Créer IWebExchange
        JakartaServletWebApplication application = JakartaServletWebApplication.buildApplication(this.servletContext);
        IServletWebExchange webExchange = application.buildExchange(request, response);


        Map<String, Object> variables = new HashMap<>();
        variables.put("title", "Mon Rapport PDF");

        WebContext context = new WebContext(
                webExchange,    // IWebExchange
                Locale.FRENCH,  // Locale (ou request.getLocale())
                variables       // Map<String, Object>
        );
        context.setVariables(data);
        String htmlContent = templateEngine.process(templateName, context);

        // Convertir le HTML en PDF avec Flying Saucer
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(htmlContent);
            renderer.getSharedContext().setBaseURL("http://localhost:8080/");

            renderer.layout();
            renderer.createPDF(outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la génération du PDF", e);
        }
    }

    public byte[] generatePdfI(String templateName, Map<String, Object> data, HttpServletRequest request, HttpServletResponse response) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            // Générer le HTML avec Thymeleaf
            JakartaServletWebApplication application = JakartaServletWebApplication.buildApplication(this.servletContext);
            IServletWebExchange webExchange = application.buildExchange(request, response);


            Map<String, Object> variables = new HashMap<>();
            variables.put("title", "Mon Rapport PDF");

            WebContext context = new WebContext(
                    webExchange,    // IWebExchange
                    Locale.FRENCH,  // Locale (ou request.getLocale())
                    variables       // Map<String, Object>
            );
            context.setVariables(data);
            String htmlContent = templateEngine.process(templateName, context);

            // Convertir HTML en PDF
            HtmlConverter.convertToPdf(htmlContent, outputStream);

            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la génération du PDF", e);
        }
    }


}


