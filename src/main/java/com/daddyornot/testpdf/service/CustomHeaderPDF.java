package com.daddyornot.testpdf.service;

import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.draw.LineSeparator;
import org.apache.commons.io.IOUtils;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CustomHeaderPDF {

    public static void main(String[] args) throws Exception {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("rapport_header.pdf"));
        document.open();

        // === HEADER PERSONNALISÉ ===
        PdfPTable headerTable = new PdfPTable(2);
        headerTable.setWidthPercentage(100);
        headerTable.setWidths(new float[]{1, 3}); // logo : texte

        // Logo à gauche
        InputStream is = CustomHeaderPDF.class.getClassLoader().getResourceAsStream("static/images/logo.jpg");
        if (is == null) throw new FileNotFoundException("Logo introuvable !");
        Image logo = Image.getInstance(IOUtils.toByteArray(is));
        logo.scaleToFit(80, 80);
        PdfPCell logoCell = new PdfPCell(logo);
        logoCell.setBorder(Rectangle.NO_BORDER);
        logoCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        logoCell.setVerticalAlignment(Element.ALIGN_TOP);

        // Texte à droite
        Font titleFont = new Font(Font.HELVETICA, 16, Font.BOLD, Color.BLACK);
        Font timeFont = new Font(Font.HELVETICA, 10, Font.NORMAL, Color.DARK_GRAY);
        Font dateFont = new Font(Font.HELVETICA, 10, Font.ITALIC, Color.GRAY);

        Paragraph text = new Paragraph();
        text.add(new Chunk("Titre du Document\n", titleFont));
        text.add(new Chunk("Heure : " + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "\n", timeFont));
        text.add(new Chunk("Date  : " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), dateFont));

        PdfPCell textCell = new PdfPCell(text);
        textCell.setBorder(Rectangle.NO_BORDER);
        textCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        textCell.setVerticalAlignment(Element.ALIGN_TOP);

        headerTable.addCell(logoCell);
        headerTable.addCell(textCell);

        document.add(headerTable);

        // Ligne de séparation
        LineSeparator separator = new LineSeparator(1f, 100, Color.GRAY, Element.ALIGN_CENTER, -2);
        document.add(separator);

        document.add(new Paragraph(" ")); // Saut de ligne

        // Simule une liste de données
        List<Etat> mesObjets = new ArrayList<>();
        mesObjets.add(new Etat("Item 1", "42"));

        // Tableau à gauche
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setSpacingBefore(5f);
        table.setSpacingAfter(5f);

        // Enlève les bordures et centre les cellules
        if (mesObjets.isEmpty()) {
            PdfPCell noData = new PdfPCell(new Phrase("Pas de données"));
            noData.setBorder(Rectangle.NO_BORDER);
            noData.setColspan(2);
            noData.setHorizontalAlignment(Element.ALIGN_CENTER);
            noData.setPadding(10f); // Padding autour de la cellule
            table.addCell(noData);
        } else {
            // Définir la largeur des colonnes (en pourcentage)
            table.setWidths(new float[]{1f, 1f}); // Colonnes avec largeur égale

            // Itération sur les objets Etat
            for (Etat etat : mesObjets) {
                // Cellule pour le nom
                PdfPCell cellNom = new PdfPCell(new Phrase(etat.getNom()));
//                cellNom.setBorder(Rectangle.BOX); // Bordure autour de la cellule
                cellNom.setBorder(Rectangle.NO_BORDER);
                cellNom.setPadding(10f); // Définir un padding
//                cellNom.setMinimumHeight(25f); // (Optionnel) définit une hauteur minimale
                table.addCell(cellNom);

                // Cellule pour la valeur
                PdfPCell cellValeur = new PdfPCell(new Phrase(etat.getValeur()));
                cellValeur.setBorder(Rectangle.NO_BORDER);
//                cellValeur.setBorder(Rectangle.BOX); // Bordure autour de la cellule
                cellValeur.setPadding(10f); // Définir un padding
//                cellValeur.setMinimumHeight(25f); // (Optionnel) définit une hauteur minimale
                table.addCell(cellValeur);
            }
        }

        // === Cadre avec coins arrondis (simulé par une cellule avec bordure et padding)
        PdfPCell cadreGauche = new PdfPCell();
        cadreGauche.addElement(table);
        cadreGauche.setBorder(Rectangle.BOX);
        cadreGauche.setBorderColor(Color.LIGHT_GRAY);
        cadreGauche.setPadding(10f);
        cadreGauche.setCellEvent(new RoundedBorder()); // pour coins arrondis, voir ci-dessous

        // === Bloc de texte à droite
        PdfPCell texteDroit = new PdfPCell();
        texteDroit.setBorder(Rectangle.BOX);
        texteDroit.setBorderColor(Color.LIGHT_GRAY);
        texteDroit.setPadding(10f);
        texteDroit.addElement(new Paragraph("Voici une description ou un bloc de texte à afficher à droite. Il peut contenir plusieurs lignes, du texte simple, ou même des sauts de ligne pour structurer."));

        texteDroit.setCellEvent(new RoundedBorder()); // coins arrondis aussi ici

        // Met les deux dans une table à 2 colonnes
        PdfPTable sectionTable = new PdfPTable(3);
        sectionTable.setWidthPercentage(100);
        sectionTable.setWidths(new float[]{1, 0.2f ,1});
        sectionTable.setSpacingBefore(20f);

        sectionTable.addCell(cadreGauche);
        sectionTable.addCell(""); // Cellule vide pour l'espacement
        sectionTable.addCell(texteDroit);

        document.add(sectionTable);

        document.close();
        System.out.println("PDF généré avec header personnalisé !");
    }
}
