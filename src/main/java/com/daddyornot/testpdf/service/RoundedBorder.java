package com.daddyornot.testpdf.service;

import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPCellEvent;
import com.lowagie.text.pdf.PdfPTable;

import java.awt.*;

class RoundedBorder implements PdfPCellEvent {
    @Override
    public void cellLayout(PdfPCell cell, Rectangle rect, PdfContentByte[] canvas) {
        PdfContentByte cb = canvas[PdfPTable.LINECANVAS];
        cb.setLineWidth(1f);
        cb.setColorStroke(Color.LIGHT_GRAY);
        float r = 2f;
        cb.roundRectangle(rect.getLeft() + 1, rect.getBottom() + 1,
                rect.getWidth() - 2, rect.getHeight() - 2, r);
        cb.stroke();
    }
}
