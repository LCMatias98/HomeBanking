package com.mindhub.homebanking.utils;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class TransactionsPDFExporter {

    private List<Transaction> transactions;
    private Account account;

    private Client client;

    public TransactionsPDFExporter(List<Transaction> transactions, Account account, Client client) {
        this.transactions = transactions;
        this.account = account;
        this.client = client;
    }

    private void writeTableHeader(PdfPTable table) {
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

    private void writeTableData(PdfPTable table) {
        transactions.forEach(transaction -> {
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

        Paragraph p = new Paragraph("List of Transactions", font);
        p.setAlignment(Paragraph.ALIGN_CENTER);

        document.add(p);

        PdfPTable transactionTable = new PdfPTable(6);
        transactionTable.setWidthPercentage(100f);
        transactionTable.setWidths(new float[]{1.5f, 3.5f, 3.0f, 3.0f, 3.0f, 3.0f});
        transactionTable.setSpacingBefore(10);

        writeTableHeader(transactionTable);
        writeTableData(transactionTable);

        document.add(transactionTable);

        // TITULO ACCOUNT
        Paragraph p2 = new Paragraph("Details Of Account", font);
        p2.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(p2);

        PdfPTable accountTable = new PdfPTable(6);
        accountTable.setWidthPercentage(100f);
        accountTable.setWidths(new float[]{1.5f, 3.5f, 3.0f, 3.0f, 3.0f, 3.0f});
        accountTable.setSpacingBefore(10);

        // HEADER TABLA ACCOUNT
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(BaseColor.GREEN);
        cell.setPadding(1);
        cell.setPhrase(new Phrase("ID"));
        accountTable.addCell(cell);

        cell.setPhrase(new Phrase("Balance"));
        accountTable.addCell(cell);

        cell.setPhrase(new Phrase("Creation Date"));
        accountTable.addCell(cell);

        cell.setPhrase(new Phrase("Hidden"));
        accountTable.addCell(cell);

        cell.setPhrase(new Phrase("Number of Account"));
        accountTable.addCell(cell);

        cell.setPhrase(new Phrase("ID-Client"));
        accountTable.addCell(cell);

        // CELDAS ACCOUNT
        accountTable.addCell(String.valueOf(account.getId()));
        accountTable.addCell(String.valueOf(account.getBalance()));
        accountTable.addCell(String.valueOf(account.getCreationDate()));
        accountTable.addCell(String.valueOf(account.getHidden()));
        accountTable.addCell(account.getNumber());
        accountTable.addCell(String.valueOf(account.getClient().getId()));

        document.add(accountTable);



        // TITULO CLIENT
        Paragraph p3 = new Paragraph("Details Of Client", font);
        p3.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(p3);

        PdfPTable clientTable = new PdfPTable(3);
        clientTable.setWidthPercentage(100f);
        clientTable.setWidths(new float[]{1.5f, 3.5f, 3.0f});
        clientTable.setSpacingBefore(10);

        // HEADER TABLA ACCOUNT
        PdfPCell cellClient = new PdfPCell();
        cellClient.setBackgroundColor(BaseColor.GREEN);
        cellClient.setPadding(1);
        cellClient.setPhrase(new Phrase("Name"));
        clientTable.addCell(cellClient);

        cellClient.setPhrase(new Phrase("Last Name"));
        clientTable.addCell(cellClient);

        cellClient.setPhrase(new Phrase("Email"));
        clientTable.addCell(cellClient);

        // CELDAS ACCOUNT
        clientTable.addCell(String.valueOf(client.getFirstName()));
        clientTable.addCell(String.valueOf(client.getLastName()));
        clientTable.addCell(String.valueOf(client.getEmail()));


        document.add(clientTable);



        document.close();
    }
}
