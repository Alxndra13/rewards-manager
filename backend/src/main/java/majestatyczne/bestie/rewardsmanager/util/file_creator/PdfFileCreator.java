package majestatyczne.bestie.rewardsmanager.util.file_creator;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfWriter;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFColor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.util.List;

public class PdfFileCreator implements FileCreator {

    private final XlsxFileCreator xlsxFileCreator = new XlsxFileCreator();

    @Override
    public byte[] createFileWithTable(List<List<String>> rows, List<Integer> rowsToHighlight) throws IOException {
        byte[] xlsxByteArray = xlsxFileCreator.createFileWithTable(rows, rowsToHighlight);

        return convertXlsxToPdf(xlsxByteArray);
    }

    private byte[] convertXlsxToPdf(byte[] xlsxByteArray) throws IOException {
        try (Workbook workbook = WorkbookFactory.create(new ByteArrayInputStream(xlsxByteArray));
             ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream()) {

            Document document = new Document();
            PdfWriter.getInstance(document, pdfOutputStream);
            document.open();

            Font font = FontFactory.getFont(FontFactory.HELVETICA, BaseFont.CP1250);

            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                Sheet sheet = workbook.getSheetAt(i);

                for (Row row : sheet) {
                    PdfPTable pdfPTable = new PdfPTable(row.getLastCellNum());

                    for (Cell cell : row) {
                        PdfPCell pdfCell = new PdfPCell(new Phrase(cell.toString(), font));
                        copyCellStyle(cell, pdfCell);
                        pdfPTable.addCell(pdfCell);
                    }

                    document.add(pdfPTable);
                }
            }

            document.close();

            return pdfOutputStream.toByteArray();

        } catch (DocumentException e) {
            throw new IOException("error converting XLSX to PDF", e);
        }
    }

    private void copyCellStyle(Cell sourceCell, PdfPCell targetCell) {
        CellStyle sourceStyle = sourceCell.getCellStyle();
        if (sourceStyle == null) {
            return;
        }

        XSSFColor color = (XSSFColor) sourceStyle.getFillForegroundColorColor();
        if (color == null) {
            return;
        }

        byte[] rgb = color.getRGB();
        if (rgb != null && rgb.length == 3) {
            BaseColor backgroundColor = new BaseColor(rgb[0] & 0xFF, rgb[1] & 0xFF,
                    rgb[2] & 0xFF);
            targetCell.setBackgroundColor(backgroundColor);
        }
    }
}
