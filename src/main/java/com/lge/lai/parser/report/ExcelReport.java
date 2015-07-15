package com.lge.lai.parser.report;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.google.common.annotations.VisibleForTesting;
import com.lge.lai.common.data.Feature;
import com.lge.lai.parser.constants.ManifestAttr;

public class ExcelReport implements Report {
    private String name;
    private Map<String, Integer> categories = new HashMap<String, Integer>();

    private HSSFWorkbook workbook;
    private HSSFSheet sheet;

    private final static String[] ACTION_NAME_COL = new String[] { "Package", "Class",
            "Declaration type", "Line number", "Action name", "Resolved" };
    private final static String[] PASSING_INTENT_METHOD_COL = new String[] { "Package", "Class",
            "Line number", "Method name", "Resvoled" };
    private final static String[] MANIFEST_PROVIDER_COL = new String[] { "Type", "Component name",
            "Authorities", "Read permission", "Write permission" };
    private final static String[] MANIFEST_COMMON_COL = new String[] { "Action", "Type",
            "Component name", "Category", "Data (Scheme)", "Data (Host)", "Data (Port)",
            "Data (Path)", "Data (Pattern)", "Data (Prefix)", "Data (Mime)" };

    public ExcelReport(String name) {
        this.name = name;
        initialize(name);
    }

    private void initialize(String name) {
        String filename = name + ".xls";
        if (new File(filename).exists()) {
            try {
                InputStream is = new FileInputStream(filename);
                workbook = new HSSFWorkbook(is);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        workbook = new HSSFWorkbook();
    }

    @Override
    public void addCategory(String category) {
        categories.put(category, 0);
        sheet = workbook.getSheet(category);
        if (sheet == null) {
            sheet = workbook.createSheet(category);
            createColumnTitleCell(sheet, category);
        }
    }

    private void createColumnTitleCell(HSSFSheet sheet, String category) {
        HSSFRow row = sheet.createRow(0);
        String[] columnType = new String[] {};
        if (category.equalsIgnoreCase(ManifestAttr.PROVIDER)) {
            columnType = MANIFEST_PROVIDER_COL;
        } else {
            columnType = MANIFEST_COMMON_COL;
        }

        for (int i = 0; i < columnType.length; i++) {
            row.createCell(i).setCellValue(columnType[i]);
            row.getCell(i).setCellStyle(getCellStyle());
        }
    }

    private HSSFCellStyle getCellStyle() {
        HSSFCellStyle cs = workbook.createCellStyle();
        cs.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        cs.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
        return cs;
    }

    @Override
    public void setData(String category, Object obj) throws Exception {
        HSSFRow row = createRow(category);
        String[] columnType = new String[] {};
        Feature feature = (Feature)obj;
        if (category.equalsIgnoreCase(ManifestAttr.PROVIDER)) {
            row.createCell(0).setCellValue(feature.type);
            row.createCell(1).setCellValue(feature.className);
            row.createCell(2).setCellValue(feature.authorities);
            row.createCell(3).setCellValue(feature.readPermission);
            row.createCell(4).setCellValue(feature.writePermission);
            columnType = MANIFEST_PROVIDER_COL;
        } else {
            row.createCell(0).setCellValue(feature.actionName);
            row.createCell(1).setCellValue(feature.type);
            row.createCell(2).setCellValue(feature.className);
            row.createCell(3).setCellValue(feature.getCategories());
            row.createCell(4).setCellValue(feature.getSchemes());
            row.createCell(5).setCellValue(feature.getHosts());
            row.createCell(6).setCellValue(feature.getPorts());
            row.createCell(7).setCellValue(feature.getPaths());
            row.createCell(8).setCellValue(feature.getPathPatterns());
            row.createCell(9).setCellValue(feature.getPathPrefixes());
            row.createCell(10).setCellValue(feature.getMimeTypes());
            columnType = MANIFEST_COMMON_COL;
        }

        for (int i = 0; i < columnType.length; i++) {
            row.getCell(i).setCellStyle(getCellStyle());
            sheet.autoSizeColumn(i);
        }
        write();
    }

    private HSSFRow createRow(String category) {
        if (!sheet.getSheetName().equals(category)) {
            sheet = workbook.getSheet(category);
        }

        int index = categories.get(category);
        categories.put(category, index + 1);
        return sheet.createRow(index + 1);
    }

    private void write() throws Exception {
        OutputStream os = new FileOutputStream(name + ".xls");
        workbook.write(os);
    }

    @VisibleForTesting
    public String getCurrentSheetName() {
        return sheet.getSheetName();
    }

    @VisibleForTesting
    public int getColumnIndex(String category) {
        return categories.get(category);
    }
}
