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
import com.google.common.collect.HashMultimap;
import com.lge.lai.common.data.Feature;
import com.lge.lai.parser.constants.Manifest;
import com.lge.lai.parser.data.ActionData;
import com.lge.lai.parser.data.IntentMethodData;
import com.lge.lai.parser.manifest.ManifestParser;
import com.lge.lai.parser.source.ActionNameVisitor;
import com.lge.lai.parser.source.IntentMethodVisitor;

public class ExcelReport implements Report {
    private String name;
    private Map<String, Integer> categories = new HashMap<String, Integer>();

    private HSSFWorkbook workbook;
    private HSSFSheet sheet;
    private static HSSFCellStyle cs;

    private final static String[] MANIFEST_PROVIDER_COL = new String[] { "Type", "Component name",
            "Authorities", "Read permission", "Write permission" };
    private final static String[] MANIFEST_COMMON_COL = new String[] { "Action", "Type",
            "Component name", "Category", "Data (Scheme)", "Data (Host)", "Data (Port)",
            "Data (Path)", "Data (Pattern)", "Data (Prefix)", "Data (Mime)" };
    private final static String[] ACTION_LIST_COL = new String[] { "Package", "Class",
            "Declaration type", "Line number", "Action name", "Resolved" };
    private final static String[] INTENT_HANDLING_METHOD_COL = new String[] { "Package", "Class",
            "Line number", "Method name", "Resvoled" };

    private final static Map<String, String[]> CATEGORY_COL_TABLE = new HashMap<>();
    static {
        CATEGORY_COL_TABLE.put(ManifestParser.CATEGORY_PREFIX + Manifest.ACTIVITY,
                MANIFEST_COMMON_COL);
        CATEGORY_COL_TABLE.put(ManifestParser.CATEGORY_PREFIX + Manifest.ACTIVITY_ALIAS,
                MANIFEST_COMMON_COL);
        CATEGORY_COL_TABLE.put(ManifestParser.CATEGORY_PREFIX + Manifest.RECEIVER,
                MANIFEST_COMMON_COL);
        CATEGORY_COL_TABLE.put(ManifestParser.CATEGORY_PREFIX + Manifest.SERVICE,
                MANIFEST_COMMON_COL);
        CATEGORY_COL_TABLE.put(ManifestParser.CATEGORY_PREFIX + Manifest.PROVIDER,
                MANIFEST_PROVIDER_COL);
        CATEGORY_COL_TABLE.put(ActionNameVisitor.CATEGORY, ACTION_LIST_COL);
        CATEGORY_COL_TABLE.put(IntentMethodVisitor.CATEGORY, INTENT_HANDLING_METHOD_COL);
    }

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
        cs = getCellStyle();
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
        String[] columnType = CATEGORY_COL_TABLE.get(category);
        for (int i = 0; i < columnType.length; i++) {
            row.createCell(i).setCellValue(columnType[i]);
            row.getCell(i).setCellStyle(cs);
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
        String[] columnType = CATEGORY_COL_TABLE.get(category);
        if (category.startsWith(ManifestParser.CATEGORY)) {
            Feature feature = (Feature)obj;
            if (category.contains(Manifest.PROVIDER)) {
                setProviderDataToCell(row, feature, columnType);
            } else {
                setASBDataToCell(row, feature, columnType);
            }
        } else {
            if (category.equalsIgnoreCase(ActionNameVisitor.CATEGORY)) {
                ActionData action = (ActionData)obj;
                setActionDataToCell(row, action, columnType);
            } else if (category.equalsIgnoreCase(IntentMethodVisitor.CATEGORY)) {
                IntentMethodData method = (IntentMethodData)obj;
                setIntentMethodDataToCell(row, method, columnType);
            }
        }
    }

    private void setProviderDataToCell(HSSFRow row, Feature feature, String[] columnType) {
        row.createCell(0).setCellValue(feature.type);
        row.createCell(1).setCellValue(feature.className);
        row.createCell(2).setCellValue(feature.authorities);
        row.createCell(3).setCellValue(feature.readPermission);
        row.createCell(4).setCellValue(feature.writePermission);

        for (int i = 0; i < columnType.length; i++) {
            row.getCell(i).setCellStyle(cs);
            sheet.autoSizeColumn(i);
        }
        write();
    }

    private void setASBDataToCell(HSSFRow row, Feature feature, String[] columnType) {
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

        for (int i = 0; i < columnType.length; i++) {
            row.getCell(i).setCellStyle(cs);
            sheet.autoSizeColumn(i);
        }
        write();
    }

    private void setActionDataToCell(HSSFRow row, ActionData action, String[] columnType) {
        row.createCell(0).setCellValue(action.packageName);
        row.createCell(1).setCellValue(action.className);
        row.createCell(2).setCellValue(action.declarationType);
        row.createCell(3).setCellValue(action.lineNumber);
        row.createCell(4).setCellValue(action.actionName);
        row.createCell(5).setCellValue(action.isResolved);

        for (int i = 0; i < columnType.length; i++) {
            row.getCell(i).setCellStyle(cs);
            sheet.autoSizeColumn(i);
        }
        write();
    }

    private void setIntentMethodDataToCell(HSSFRow row, IntentMethodData method, String[] columnType) {
        row.createCell(0).setCellValue(method.packageName);
        row.createCell(1).setCellValue(method.className);
        row.createCell(2).setCellValue(method.lineNumber);
        row.createCell(3).setCellValue(method.methodName);
        row.createCell(4).setCellValue(method.isResolved);

        for (int i = 0; i < columnType.length; i++) {
            row.getCell(i).setCellStyle(cs);
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

    private void write() {
        try {
            OutputStream os = new FileOutputStream(name + ".xls");
            workbook.write(os);
        } catch (Exception e) {

        }
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
