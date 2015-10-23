package utility;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtils {

    private static XSSFSheet excelWSheet;
    private static XSSFWorkbook excelWBook;
    private static XSSFCell cell;
    private static XSSFRow row;

    //TODO: Error needs investigating (related to the getAllDataFromSheet method)
    //private static Iterator<XSSFRow> rowIterator = excelWSheet.iterator();
    //private static Iterator<cell> cellIterator = row.cellIterator();

    /**
     * Sets the file path and opens the excel file
     * @param Path the path to the excel sheet
     * @param SheetName the name of the worksheet to use
     * @throws Exception
     */
    public static void setExcelFile(String Path, String SheetName) throws Exception {
        try {
            FileInputStream excelFile = new FileInputStream(Path);          //Open Excel file
            //Access the required test data sheet
            excelWBook = new XSSFWorkbook(excelFile);
            excelWSheet = excelWBook.getSheet(SheetName);
        } catch (Exception e) {
            throw (e);
        }
    }
    //Read the test data from the Excel cell
    public static String getCellData(int rowNum, int colNum) throws Exception {

        try {
            cell = excelWSheet.getRow(rowNum).getCell(colNum);
            String cellData = cell.getStringCellValue();
            return cellData;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Writes to a cell in an excel file
     * @param result the text to write
     * @param rowNum the row number to write to
     * @param colNum the column number to write to
     * @throws Exception
     */
    public static void SetCellData(String result, int rowNum, int colNum) throws Exception {
        try {
            row = excelWSheet.getRow(rowNum);
            cell = row.getCell(colNum, row.RETURN_BLANK_AS_NULL);
            if (cell == null) {
                cell = row.createCell(colNum);
                cell.setCellValue(result);
            }
            else {
                cell.setCellValue(result);
            }

        //constant Test Data path and Test Data filename
            FileOutputStream fileOut = new FileOutputStream(Constant.path_TestData + Constant.file_testData);
            excelWBook.write(fileOut);
            fileOut.flush();
            fileOut.close();
        } catch (Exception e) {
            throw(e);
        }
    }
}
