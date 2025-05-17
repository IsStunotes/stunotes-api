package com.example.service;

import com.example.model.Reminder;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReminderExportService {

    public ByteArrayInputStream generatePdf(Reminder reminder) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
            Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

            document.add(new Paragraph("Horario del Recordatorio", titleFont));
            document.add(new Paragraph(" ", bodyFont));

            document.add(new Paragraph("Actividad: " + reminder.getActivity().getTitle(), bodyFont));
            document.add(new Paragraph("Fecha y hora: " + reminder.getDateTime().toString(), bodyFont));
            document.add(new Paragraph("Categoría: " + reminder.getActivity().getCategory().getName(), bodyFont));

            document.close();

        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}
