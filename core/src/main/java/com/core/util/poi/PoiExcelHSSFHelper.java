package com.core.util.poi;

import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Excel 读取（97-2003格式）
 * Created by hyxing on 2015/3/21.
 */

public class PoiExcelHSSFHelper extends PoiExcelHelper {
    private static PoiExcelHSSFHelper instance = new PoiExcelHSSFHelper();

    private PoiExcelHSSFHelper(){}

    public static PoiExcelHSSFHelper getInstance(){
        return instance;
    }


    @Override
    public List<List<String>> readExcel(InputStream is, int sheetIndex, String rows, String columns) throws IOException {
        List<List<String>> dataList = Lists.newArrayList();
        try {
            HSSFWorkbook workbook = new HSSFWorkbook(is);
            HSSFSheet sheet = workbook.getSheetAt(sheetIndex);

            dataList = readExcel(sheet, rows, getColumnNumber(sheet, columns));
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
        return dataList;
    }

    @Override
    public List<List<String>> readExcel(InputStream is, int sheetIndex, String rows, int[] cols) throws IOException {
        List<List<String>> dataList = Lists.newArrayList();
        try {
            HSSFWorkbook workbook = new HSSFWorkbook(is);
            HSSFSheet sheet = workbook.getSheetAt(sheetIndex);

            dataList = readExcel(sheet, rows, cols);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
        return dataList;
    }

    @Override
    public Workbook getWorkbookWithHeader(Map<String,List<List<String>>> data){
        if(MapUtils.isEmpty(data)){
            return null;
        }

        HSSFWorkbook workbook = new HSSFWorkbook();

        //设置表头字体
        HSSFFont headerFont = workbook.createFont();
        headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        HSSFCellStyle cellStyle= workbook.createCellStyle();
        cellStyle.setFont(headerFont);
        List<String> header = null ;
        HSSFRow headerRow = null ;
        HSSFSheet sheet = null ;
        List<List<String>> rows = null ;
        for(Map.Entry<String,List<List<String>>> entry : data.entrySet()){
            sheet = workbook.createSheet(entry.getKey());
            headerRow = sheet.createRow((short)0);
            rows = entry.getValue();
            if(CollectionUtils.isEmpty(rows)){
                continue;
            }
            header = rows.get(0);
            if(CollectionUtils.isNotEmpty(header)){
                int headSize = header.size();
                for(int i=0;i<headSize;i++){
                    HSSFCell cell = headerRow.createCell(i);
                    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue((String)header.get(i));
                }
            }

            int rowSize = rows.size();
            for (int i=1; i<rowSize; i++){
                HSSFRow row = sheet.createRow(i);
                List<String> rowData = rows.get(i);
                int rowDataSize = rowData.size();
                for (int j=0;j<rowDataSize;j++){
                    HSSFCell cell = row.createCell(j);
                    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
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
