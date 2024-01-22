package majestatyczne.bestie.rewardsmanager.util.file_creator;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class XlsxFileCreator implements FileCreator {

    @Override
    public byte[] createFileWithTable(List<List<String>> inputRows, List<Integer> rowsToHighlight)
            throws IOException {
        if (inputRows.isEmpty() || inputRows.get(0).isEmpty()) {
            throw new IllegalArgumentException("cannot create file with empty table");
        }

        int rowSize = inputRows.size();
        int columnSize = inputRows.get(0).size();

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Sheet1");

            for (int rowIndex = 0; rowIndex < rowSize; rowIndex++) {
                Row row = sheet.createRow(rowIndex);

                for (int columnIndex = 0; columnIndex < columnSize; columnIndex++) {
                    Cell cell = row.createCell(columnIndex);
                    cell.setCellValue(inputRows.get(rowIndex).get(columnIndex));
                }
            }

            for (int rowIndex : rowsToHighlight) {
                setRowColor(sheet.getRow(rowIndex), workbook);
            }

            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                workbook.write(outputStream);
                return outputStream.toByteArray();
            }
        }
    }

    private void setRowColor(Row row, Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        for (Cell cell : row) {
            cell.setCellStyle(style);
        }
    }
}
