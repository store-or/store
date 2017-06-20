package com.core.util.poi;

import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Excel统一POI处理类（针对2003以前和2007以后两种格式的兼容处理）
 * Created by hyxing on 2015/3/21.
 */

public abstract class PoiExcelHelper {

    protected Logger logger = LoggerFactory.getLogger(getClass());
    //不连续值的分割符
    public static final String SEPARATOR = ",";
    //两个连续值的连接符
    public static final String CONNECTOR = "-";

    public static final int HSSF = 0;
    public static final int XSSF = 1;
    public static final String HSSF_EXTENSION = "xls";
    public static final String XSSF_EXTENSION = "xlsx";

    public abstract List<List<String>> readExcel(InputStream is, int sheetIndex, String rows, String columns) throws IOException;

    public abstract List<List<String>> readExcel(InputStream is, int sheetIndex, String rows, int[] cols) throws IOException;

    public abstract Workbook getWorkbookWithHeader(Map<String,List<List<String>>> data);

    public static PoiExcelHelper getHelper(int type){
        PoiExcelHelper helper = null;
        if(HSSF == type){
            helper = PoiExcelHSSFHelper.getInstance();

        }else if(XSSF == type){
            helper = PoiExcelXSSFHelper.getInstance();
        }
        return helper;
    }

    public static PoiExcelHelper getHelper(String extension){
        PoiExcelHelper helper = null;
        if(StringUtils.equals(extension, HSSF_EXTENSION)){
            helper = PoiExcelHSSFHelper.getInstance();

        }else if(StringUtils.equals(extension, XSSF_EXTENSION)){
            helper = PoiExcelXSSFHelper.getInstance();
        }
        return helper;
    }

    public List<List<String>> readExcel(File file, int sheetIndex) throws IOException {
        return readExcel(new FileInputStream(file), sheetIndex, "1-", "1-");
    }


    public List<List<String>> readExcel(InputStream is, int sheetIndex) throws IOException {
        return readExcel(is, sheetIndex, "1-", "1-");
    }

    public List<List<String>> readExcel(File file, int sheetIndex, String rows) throws IOException {
        return readExcel(new FileInputStream(file), sheetIndex, rows, "1-");
    }

    public List<List<String>> readExcel(InputStream is, int sheetIndex, String rows) throws IOException {
        return readExcel(is, sheetIndex, rows, "1-");
    }

    public List<List<String>> readExcel(File file, int sheetIndex, String[] columns) throws IOException {
        return readExcel(file, sheetIndex, "1-", columns);
    }

    public List<List<String>> readExcel(InputStream is, int sheetIndex, String[] columns) throws IOException {
        return readExcel(is, sheetIndex, "1-", columns);
    }

    public List<List<String>> readExcel(File file, int sheetIndex, String rows, String[] columns) throws IOException {
        int[] cols = getColumnNumber(columns);

        return readExcel(new FileInputStream(file), sheetIndex, rows, cols);
    }

    public List<List<String>> readExcel(InputStream is, int sheetIndex, String rows, String[] columns) throws IOException {
        int[] cols = getColumnNumber(columns);

        return readExcel(is, sheetIndex, rows, cols);
    }

    /** 读取Excel文件内容 */
    protected List<List<String>> readExcel(Sheet sheet, String rows, int[] cols) {
        List<List<String>> dataList = Lists.newArrayList();
        // 处理行信息，并逐行列块读取数据
        String[] rowList = rows.split(SEPARATOR);
        for (String rowStr : rowList) {
            if (rowStr.contains(CONNECTOR)) {
                String[] rowArr = rowStr.trim().split(CONNECTOR);
                int start = Integer.parseInt(rowArr[0]) - 1;
                int end;
                if (rowArr.length == 1) {
                    end = sheet.getLastRowNum();
                } else {
                    end = Integer.parseInt(rowArr[1].trim()) - 1;
                }
                dataList.addAll(getRowsValue(sheet, start, end, cols));
            } else {
                List<String> row = getRowValue(sheet, Integer.parseInt(rowStr) - 1, cols);
                if(row != null) {
                    dataList.add(row);
                }
            }
        }
        return dataList;
    }

    /** 获取连续行、列数据 */
    protected List<List<String>> getRowsValue(Sheet sheet, int startRow, int endRow,int startCol, int endCol) {
        if (endRow < startRow || endCol < startCol) {
            return null;
        }

        List<List<String>> data = Lists.newArrayList();
        for (int i = startRow; i <= endRow; i++) {
            List<String> row = getRowValue(sheet, i, startCol, endCol);
            if(row != null) {
                data.add(row);
            }
        }
        return data;
    }

    /** 获取连续行、不连续列数据 */
    private List<List<String>> getRowsValue(Sheet sheet, int startRow, int endRow, int[] cols) {
        if (endRow < startRow) {
            return null;
        }

        List<List<String>> data = Lists.newArrayList();
        for (int i = startRow; i <= endRow; i++) {
            List<String> rowData = getRowValue(sheet, i, cols);
            if(rowData != null) {
                data.add(rowData);
            }
        }
        return data;
    }

    /** 获取行连续列数据 */
    private List<String> getRowValue(Sheet sheet, int rowIndex, int startCol, int endCol) {
        if(endCol < startCol) {
            return null;
        }
        Row row = sheet.getRow(rowIndex);
        return getRowValue(row,startCol,endCol);
    }

    /** 获取行不连续列数据 */
    private List<String> getRowValue(Sheet sheet, int rowIndex, int[] cols) {
        Row row = sheet.getRow(rowIndex);
        return getRowValue(row,0,cols.length-1);
    }

    private List<String> getRowValue(Row row,int startCol, int endCol){
        boolean isRowBlank = true;
        List<String> rowData = Lists.newArrayList();

        for (int i=startCol; i <= endCol; i++) {
            String val = getCellValue(row, i);
            if(StringUtils.isNotBlank(val)){
                isRowBlank = false;
            }
            rowData.add(val);
        }
        if(isRowBlank){
            return null;
        }
        return rowData;
    }

    /**
     * 获取单元格内容
     *
     * @param row
     * @param column
     *            a excel column string like 'A', 'C' or "AA".
     * @return
     */
    protected String getCellValue(Row row, String column) {
        return getCellValue(row,getColumnNumber(column));
    }

    /**
     * 获取单元格内容
     *
     * @param row
     * @param col
     *            a excel column index from 0 to 65535
     * @return
     */
    private String getCellValue(Row row, int col) {
        if (row == null) {
            return "";
        }
        Cell cell = row.getCell(col);
        return getCellValue(cell);
    }

    /**
     * 获取单元格内容
     *
     * @param cell
     * @return
     */
    private String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }

        String value = "";
        try {
            // This step is used to prevent Integer string being output with
            // '.0'.
            if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                cell.setCellType(Cell.CELL_TYPE_STRING);
                value = cell.getStringCellValue();
                Float.parseFloat(value);
                value = value.replaceAll("\\.0$", "");
                value = value.replaceAll("\\.0+$", "");
            }else if(cell.getCellType() == Cell.CELL_TYPE_BOOLEAN){
                value = cell.getBooleanCellValue()+"";
            }else if(cell.getCellType() == Cell.CELL_TYPE_STRING){
                value = cell.getStringCellValue();
            }else {
                value = StringUtils.trim(cell.toString());
            }
            return value;
        } catch (NumberFormatException ex) {
            logger.error(ex.getMessage(),ex);
            throw ex;
        }
    }

    /**
     * Change excel column letter to integer number
     *
     * @param columns
     *            column letter of excel file, like A,B,AA,AB
     * @return
     */
    private int[] getColumnNumber(String[] columns) {
        int[] cols = new int[columns.length];
        for(int i=0; i<columns.length; i++) {
            cols[i] = getColumnNumber(columns[i]);
        }
        return cols;
    }

    /**
     * Change excel column letter to integer number
     *
     * @param column
     *            column letter of excel file, like A,B,AA,AB
     * @return
     */
    private int getColumnNumber(String column) {
        int length = column.length();
        short result = 0;
        for (int i = 0; i < length; i++) {
            char letter = column.toUpperCase().charAt(i);
            int value = letter - 'A' + 1;
            result += value * Math.pow(26, length - i - 1);
        }
        return result - 1;
    }

    /**
     * Change excel column string to integer number array
     *
     * @param sheet
     *            excel sheet
     * @param columns
     *            column letter of excel file, like A,B,AA,AB
     * @return
     */
    protected int[] getColumnNumber(Sheet sheet, String columns) {
        // 拆分后的列为动态，采用List暂存
        List<Integer> result = Lists.newArrayList();
        String[] colList = columns.split(SEPARATOR);
        for(String colStr : colList){
            if(colStr.contains(CONNECTOR)){
                String[] colArr = colStr.trim().split(CONNECTOR);
                int start = Integer.parseInt(colArr[0]) - 1;
                int end;
                if(colArr.length == 1){
                    end = sheet.getRow(sheet.getFirstRowNum()).getLastCellNum() - 1;
                }else{
                    end = Integer.parseInt(colArr[1].trim()) - 1;
                }
                for(int i=start; i<=end; i++) {
                    result.add(i);
                }
            }else{
                result.add(Integer.parseInt(colStr) - 1);
            }
        }

        // 将List转换为数组
        int len = result.size();
        int[] cols = new int[len];
        for(int i = 0; i<len; i++) {
            cols[i] = result.get(i).intValue();
        }

        return cols;
    }

    public void export(OutputStream out,List<List<String>> data){
        Map<String,List<List<String>>> map = new HashMap<String, List<List<String>>>();
        map.put("sheet1" , data);
        export(out , map);
    }

    public void export(OutputStream out,Map<String,List<List<String>>> data){
        try {
            Workbook workbook = getWorkbookWithHeader(data);
            if(workbook == null){
                return;
            }
            workbook.write(out);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }finally {
            try {
                out.close();
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }
}

