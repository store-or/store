package com.core.util.poi;


import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.*;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
* Excel 读取2007格式
* Created by hyxing on 2015/3/21.
*/

public class PoiExcelXSSFHelper extends PoiExcelHelper {
    private static PoiExcelXSSFHelper instance = new PoiExcelXSSFHelper();

    private PoiExcelXSSFHelper(){}

    public static PoiExcelXSSFHelper getInstance(){
        return instance;
    }

    @Override
    public List<List<String>> readExcel(InputStream is, int sheetIndex, String rows, String columns) throws IOException {
        List<List<String>> dataList = Lists.newArrayList();
        XSSFWorkbook workbook = new XSSFWorkbook(is);
        XSSFSheet sheet = workbook.getSheetAt(sheetIndex);

        dataList = readExcel(sheet, rows, getColumnNumber(sheet, columns));
        return dataList;
    }

    @Override
    public List<List<String>> readExcel(InputStream is, int sheetIndex, String rows, int[] cols) throws IOException{
        List<List<String>> dataList = Lists.newArrayList();
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(is);
            XSSFSheet sheet = workbook.getSheetAt(sheetIndex);

            dataList = readExcel(sheet, rows, cols);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
        return dataList;
    }

    @Override
    public Workbook getWorkbookWithHeader(Map<String,List<List<String>>> data) {
        if(MapUtils.isEmpty(data)){
            return null;
        }

        XSSFWorkbook workbook = new XSSFWorkbook();


        //设置表头字体
        XSSFFont headerFont = workbook.createFont();
        headerFont.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        XSSFCellStyle cellStyle= workbook.createCellStyle();
        cellStyle.setFont(headerFont);
        XSSFSheet sheet = null ;
        XSSFRow headerRow = null ;
        List<List<String>> rows = null ;
        for(Map.Entry<String,List<List<String>>> entry : data.entrySet()){
            sheet = workbook.createSheet(entry.getKey());
            headerRow = sheet.createRow((short)0);
            rows = entry.getValue();
            if(CollectionUtils.isEmpty(rows)){
                continue;
            }
            List<String> header = rows.get(0);
            if(CollectionUtils.isNotEmpty(header)){
                int headSize = header.size();
                for(int i=0;i<headSize;i++){
                    XSSFCell cell = headerRow.createCell(i);
                    cell.setCellType(XSSFCell.CELL_TYPE_STRING);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue((String)header.get(i));
                }
            }

            int rowSize = rows.size();
            for (int i=1; i<rowSize; i++){
                XSSFRow row = sheet.createRow(i);
                List<String> rowData = rows.get(i);
                int columnSize = rowData.size();
                for (int j=0;j<columnSize;j++){
                    XSSFCell cell = row.createCell(j);
                    cell.setCellType(XSSFCell.CELL_TYPE_STRING);
                    Object o = rowData.get(j);
                    //写入到表中的数值型对象恢复为数值型，
                    if(o instanceof BigDecimal){
                        BigDecimal b=(BigDecimal)o;
                        cell.setCellValue(b.doubleValue());

                    }else if(o instanceof Integer){
                        Integer it =(Integer)o;
                        cell.setCellValue(it.intValue());

                    }else if(o instanceof Long){
                        Long l =(Long)o;
                        cell.setCellValue(l.intValue());

                    }else if(o instanceof Double){
                        Double d =(Double)o;
                        cell.setCellValue(d.doubleValue());

                    }else if(o instanceof Float){
                        Float f = (Float)o;
                        cell.setCellValue(f.floatValue());

                    }else{
                        cell.setCellValue(o+"");
                    }
                }
            }
        }
        return workbook;
    }
}