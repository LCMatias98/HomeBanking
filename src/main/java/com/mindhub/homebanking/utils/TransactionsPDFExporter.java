package com.mindhub.homebanking.utils;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfFunction;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Transaction;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class TransactionsPDFExporter {

//    public LocalDateTime getLocalDateTimeStart;
    private List<Transaction> transactions;
    private Account account;
    public TransactionsPDFExporter(List<Transaction> transactions){
        this.transactions = transactions;
    }

    private void writeTableHeader(PdfPTable table){
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(BaseColor.GREEN);
        cell.setPadding(1);

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(BaseColor.BLACK);

        cell.setPhrase(new Phrase("ID"));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Amount"));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Date"));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Description"));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Hidden"));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Type"));
        table.addCell(cell);
    }

    private void writeTableData(PdfPTable table){
        transactions.forEach(transaction ->{
            table.addCell(String.valueOf(transaction.getId()));
            table.addCell(String.valueOf(transaction.getAmount()));
            table.addCell(String.valueOf(transaction.getDate()));
            table.addCell(transaction.getDescription());
            table.addCell(String.valueOf(transaction.getHidden()));
            table.addCell(String.valueOf(transaction.getType()));
        });
    }

    public void export(HttpServletResponse response) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(18);
        font.setColor(BaseColor.DARK_GRAY);

        Paragraph p = new Paragraph("List of Transactions",font);
        p.setAlignment(Paragraph.ALIGN_CENTER);

        document.add(p);

        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100f);
        table.setWidths(new float[]{1.5f, 3.5f, 3.0f, 3.0f, 3.0f, 3.0f});
        table.setSpacingBefore(10);

        writeTableHeader(table);
        writeTableData(table);

        document.add(table);

        document.close();
    }

}
