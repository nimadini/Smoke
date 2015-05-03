package utils;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import jxl.CellView;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

/**
 * Produces the analysis report in .xls format (MS Excel)
 *
 * Created by Nima Dini | April 2015
 * Adapted from Vogella's tutorial: http://www.vogella.com/tutorials/JavaExcel/article.html
 */

public class ReportGenerator {
    private WritableCellFormat timesBoldUnderline;
    private WritableCellFormat times;
    private Analysis analysis;
    private String outputFile;

    public void setAnalysis(Analysis analysis) {
        this.analysis = analysis;
    }

    public String getOutputFilePath() {
        return this.outputFile;
    }

    public void write() throws IOException, WriteException {
        File file = new File(outputFile);
        WorkbookSettings wbSettings = new WorkbookSettings();

        wbSettings.setLocale(new Locale("en", "EN"));

        WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
        workbook.createSheet("Report", 0);
        WritableSheet excelSheet = workbook.getSheet(0);
        createLabel(excelSheet);

        workbook.write();
        workbook.close();
    }

    private void createLabel(WritableSheet sheet)
            throws WriteException {
        // Lets create a times font
        WritableFont times10pt = new WritableFont(WritableFont.TIMES, 11);
        // Define the cell format
        times = new WritableCellFormat(times10pt);
        // Lets automatically wrap the cells
        times.setWrap(true);

        // create create a bold font with unterlines
        WritableFont times10ptBoldUnderline = new WritableFont(WritableFont.TIMES, 11, WritableFont.BOLD, false,
                UnderlineStyle.SINGLE);
        timesBoldUnderline = new WritableCellFormat(times10ptBoldUnderline);
        // Lets automatically wrap the cells
        timesBoldUnderline.setWrap(true);

        CellView cv = new CellView();
        cv.setFormat(times);
        cv.setFormat(timesBoldUnderline);
        cv.setAutosize(true);

        for (int i = 0; i < analysis.getTestCases().length; i++) {
            addCaption(sheet, 0, i + 1, analysis.getTestCases()[i].getLongName());
        }

        for (int i = 0; i < analysis.getMatrix().getColumnDimension(); i++) {
            addLabel(sheet, i + 1, 0, "b" + i);
        }

        for (int i = 0; i < analysis.getMatrix().getRowDimension(); i++) {
            for (int j = 0; j < analysis.getMatrix().getColumnDimension(); j++) {
                addLabel(sheet, j+1, i+1, String.valueOf((int)(analysis.getMatrix().getEntry(i, j))));
            }
        }

        long origTime = analysis.getOriginalTotalExecTime();
        long smokeTime = analysis.getSmokeTotalExecTime();

        addLabel(sheet, 0, analysis.getMatrix().getRowDimension() + 2, "Original Execution Time");
        addLabel(sheet, 1, analysis.getMatrix().getRowDimension() + 2, String.valueOf(origTime));

        addLabel(sheet, 0, analysis.getMatrix().getRowDimension() + 3, "Smoke Execution Time");
        addLabel(sheet, 1, analysis.getMatrix().getRowDimension() + 3, String.valueOf(smokeTime));

        if (smokeTime != 0) {
            addLabel(sheet, 0, analysis.getMatrix().getRowDimension() + 4, "Speed up");
            addLabel(sheet, 1, analysis.getMatrix().getRowDimension() + 4, String.valueOf(1.0*origTime/smokeTime));
        }

        addLabel(sheet, 0, analysis.getMatrix().getRowDimension() + 6, "Keep test cases (" + analysis.getSubSet().size() + "/" + analysis.getTestCases().length + ")");
        int j = 0;
        for (TestCase tc : analysis.getSubSet()) {
            addLabel(sheet, j + 1, analysis.getMatrix().getRowDimension() + 6, tc.getLongName());
            j++;
        }

        addLabel(sheet, 0, analysis.getMatrix().getRowDimension() + 8, "Code Coverage");
        addLabel(sheet, 1, analysis.getMatrix().getRowDimension() + 8, String.valueOf(analysis.getCoverage()));
    }

    private void addCaption(WritableSheet sheet, int column, int row, String s)
            throws WriteException {
        Label label;
        label = new Label(column, row, s, timesBoldUnderline);
        sheet.addCell(label);
    }

    private void addLabel(WritableSheet sheet, int column, int row, String s)
            throws WriteException {
        Label label;
        label = new Label(column, row, s, times);
        sheet.addCell(label);
    }

    public void setOutputFile(String outputFile) {
        this.outputFile = outputFile;
    }

    public String getOutputFile() {
        return outputFile;
    }
}