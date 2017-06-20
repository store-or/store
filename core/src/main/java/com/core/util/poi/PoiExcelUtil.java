package com.core.util.poi;

import com.google.common.collect.Lists;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.WorkbookUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangmj on 2015/9/17.
 */
public class PoiExcelUtil {
    private static Logger logger = LoggerFactory.getLogger(PoiExcelUtil.class);
    public static final String HSSF_EXTENSION = "xls";
    public static final String XSSF_EXTENSION = "xlsx";

    /**
     * @param workbook      Excel工作薄
     * @param sheetName     sheet工作表名
     * @param datas         内容(含表头内容，第一行为表头)
     */
    private static void export(Workbook workbook ,String sheetName, List<List<String>> datas){
        //创建工PoiExcelUtil作
        Sheet sheet = workbook.createSheet(WorkbookUtil.createSafeSheetName(sheetName));
        //设置默认列宽度10个字节
        sheet.setDefaultColumnWidth(10);
        //设置Excel表头字体
        Font headerFont = workbook.createFont();
        headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        //设置单元格样式(用于表头)
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);

        //Excel表头数据
        List<String> headerData = datas.get(0);
        Row headerRow = sheet.createRow(0);
        for (int i=0;i<headerData.size();i++){
            Cell cell = headerRow.createCell(i);
            String value = headerData.get(i);
            cell.setCellValue(value);
            cell.setCellStyle(headerCellStyle);
            //自适应宽度
        }
        //Excel内容
        for (int i=1;i<datas.size();i++){
            List<String> data = datas.get(i);
            Row row = sheet.createRow(i);
            for (int j=0;j<data.size();j++){
                Cell cell = row.createCell(j,Cell.CELL_TYPE_STRING);
                cell.setCellValue(data.get(j));
            }
        }
    }

    /**
     * 导出Excel(2003-2007,xls)
     * @param file      导出到文件file(xls文件)
     * @param datas     Map<sheet名称,Excel内容>
     */
    public static void exportExcel(File file , Map<String,List<List<String>>> datas){
        try {
            OutputStream out = new FileOutputStream(file);
            exportExcel(out,datas);
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 导出Excel(2003-2007,xls)
     * @param out       输出流
     * @param datas     Map<sheet名称,Excel内容>
     */
    public static void exportExcel(OutputStream out ,Map<String,List<List<String>>> datas){
        try {
            if (MapUtils.isEmpty(datas)) {
                return;
            }
            Workbook workbook = new HSSFWorkbook();
            for (Map.Entry<String,List<List<String>>> entry : datas.entrySet()){
                String sheetName = entry.getKey();
                List<List<String>> data = entry.getValue();
                export(workbook, sheetName, data);
            }
            workbook.write(out);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    /**
     * Http请求中导出Excel(2003-2007,xls)到客户端
     * @param request       http导出Excel请求
     * @param response      http导出Excel响应
     * @param fileName      导出的文件名
     * @param datas         Map<sheet名称,Excel内容>
     */
    public static void exportExcel(HttpServletRequest request ,
           HttpServletResponse response,String fileName ,Map<String,List<List<String>>> datas){
        try {
            String userAgent = request.getHeader("USER-AGENT");
            if (StringUtils.contains(userAgent, "MSIE") || StringUtils.contains(userAgent,"Trident")){
                //IE浏览器
                fileName = URLEncoder.encode(fileName, "UTF8");
            } else if (StringUtils.contains(userAgent, "Mozilla")) {
                //google,火狐浏览器
                fileName = new String(fileName.getBytes(), "ISO8859-1");
            } else {
                fileName = URLEncoder.encode(fileName, "UTF8");
            }
            fileName = fileName + "." + HSSF_EXTENSION;

            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            //定义输出类型
            response.setContentType("APPLICATION/msexcel");
            exportExcel(response.getOutputStream(), datas);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * @param workbook      Excel工作薄
     * @param sheetName     sheet工作表名
     * @param datas         内容(第一行为表1标题，第二行为表1表头，随后表1内容，之后表2标题，表2表头，表2内容...)
     *                      条件：标题行只有1列，表头必须大于1列
     */
    private static void exportMerge(Workbook workbook ,String sheetName, List<List<String>> datas){
        //创建工作
        Sheet sheet = workbook.createSheet(WorkbookUtil.createSafeSheetName(sheetName));
        //设置默认列宽度10个字节
        sheet.setDefaultColumnWidth(10);

        //设置合并表的标题字体
        Font titleFont = workbook.createFont();
        titleFont.setColor(Font.COLOR_RED);
        titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        CellStyle titleCellStyle = workbook.createCellStyle();
        titleCellStyle.setFont(titleFont);

        //设置Excel表头字体
        Font headerFont = workbook.createFont();
        headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        //设置单元格样式(用于表头)
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);

        //设置内容
        int columnSize = 0;
        boolean isHeader = false;
        int size = datas.size();
        for (int i=0;i< size;i++){
            List<String> lineData = datas.get(i);
            if (!isHeader && lineData.size()==1){
                String title = lineData.get(0);
                Row titleRow = sheet.createRow(i);
                Cell titleCell = titleRow.createCell(0);
                titleCell.setCellValue(title);
                titleCell.setCellStyle(titleCellStyle);
                isHeader = true;
                continue;
            }
            if (isHeader){
                columnSize = lineData.size();
                Row headerRow = sheet.createRow(i);
                for (int j=0;j<columnSize;j++){
                    Cell cell = headerRow.createCell(j);
                    cell.setCellValue(lineData.get(j));
                    cell.setCellStyle(headerCellStyle);
                }
                isHeader = false;
                continue;
            }
            Row row = sheet.createRow(i);
            for (int j=0;j<columnSize;j++){
                Cell cell = row.createCell(j,Cell.CELL_TYPE_STRING);
                cell.setCellValue(lineData.get(j));
            }
        }
    }

    public static void exportMergeExcel(OutputStream out ,Map<String,List<List<String>>> datas){
        try {
            if (MapUtils.isEmpty(datas)) {
                return;
            }
            Workbook workbook = new HSSFWorkbook();
            for (Map.Entry<String,List<List<String>>> entry : datas.entrySet()){
                String sheetName = entry.getKey();
                List<List<String>> data = entry.getValue();
                exportMerge(workbook, sheetName, data);
            }
            workbook.write(out);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    /**
     * Http请求中导出Excel(2003-2007,xls)到客户端,导出多个table表(含标题1列，表头、内容多列)结果到同一sheet中
     * @param request       http导出Excel请求
     * @param response      http导出Excel响应
     * @param fileName      导出的文件名
     * @param datas         Map<sheet名称,Excel内容>
     */
    public static void exportMergeExcel(HttpServletRequest request ,
                                   HttpServletResponse response,String fileName ,Map<String,List<List<String>>> datas){
        try {
            String userAgent = request.getHeader("USER-AGENT");
            if (StringUtils.contains(userAgent, "MSIE") || StringUtils.contains(userAgent,"Trident")){
                //IE浏览器
                fileName = URLEncoder.encode(fileName, "UTF8");
            } else if (StringUtils.contains(userAgent, "Mozilla")) {
                //google,火狐浏览器
                fileName = new String(fileName.getBytes(), "ISO8859-1");
            } else {
                fileName = URLEncoder.encode(fileName, "UTF8");
            }
            fileName = fileName + "." + HSSF_EXTENSION;

            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            //定义输出类型
            response.setContentType("APPLICATION/msexcel");
            exportMergeExcel(response.getOutputStream(), datas);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }


    /**
     * 读取工作表sheet的数据
     * @param sheet         Excel工作表
     * @param datas         用于收集工作表数据(每行数据data(表头除外)后增加列rowIndex，用于指示数据所在行)
     * @param readHeader    是否读取表头行数据(true:读取，false:不读取)
     */
    private static void read(Sheet sheet , List<List<String>> datas , boolean readHeader){
        int firstRow = sheet.getFirstRowNum();  //firstRow为header表头行,第0行开始
        int lastRow = sheet.getLastRowNum();    //lastRow有数据，lastRow+1无数据
        if (sheet.getRow(firstRow) == null) {
            return;
        }
        int columnSize = sheet.getRow(firstRow).getLastCellNum();      //取得表头列长度，用于单元格列循环获取数据
        if (!readHeader){
            firstRow = firstRow+1;
            if (firstRow>lastRow)  {
                return;
            }
        }
        for (int i=firstRow ; i<=lastRow ; i++){
            Row row = sheet.getRow(i);
            if (row==null)  {
                continue;
            }
            //lastCell列无数据，lastCell-1列有数据
            List<String> data = Lists.newArrayList();   //收集每行数据
            for (int j=0; j<columnSize ;j++){        //列索引从0开始
                Cell cell = row.getCell(j,Row.RETURN_BLANK_AS_NULL);
                if (cell==null){
                    data.add("");
                }else {
                    dealCellType(data, cell);
                }
            }
            if(readHeader){
                if (i==firstRow){
                    datas.add(data);
                    continue;
                }
            }
            data.add(String.valueOf(i+1));
            datas.add(data);
        }
    }

    private static void dealCellType(List<String> data, Cell cell) {
        switch (cell.getCellType()){
            case Cell.CELL_TYPE_STRING:
                data.add(cell.getRichStringCellValue().getString());
                break;
            case Cell.CELL_TYPE_BOOLEAN:
                data.add(Boolean.valueOf(cell.getBooleanCellValue()).toString());
                break;
            case Cell.CELL_TYPE_NUMERIC:
                cell.setCellType(Cell.CELL_TYPE_STRING);
                String value = cell.getStringCellValue();
                Float.parseFloat(value);
                value = value.replaceAll("\\.0$", "");
                value = value.replaceAll("\\.0+$", "");
                data.add(value);
                break;
            case Cell.CELL_TYPE_FORMULA:
                data.add(cell.getCellFormula());
                break;
            default:
                data.add(StringUtils.trim(cell.toString()));
                break;
        }
    }

    /**
     * 读取Excel(xls,xlsx)
     * @param in            输入流
     * @param readHeader    是否读取表头行数据(true:读取，false:不读取)
     * @return              Map<sheet名称,Excel内容>
     */
    public static Map<String,List<List<String>>> readExcel(InputStream in , boolean readHeader){
        Map<String,List<List<String>>> datas = new HashMap<String, List<List<String>>>();
        try {
            Workbook workbook = WorkbookFactory.create(in);
            if (workbook==null) {
                return null;
            }
            int sheetNum = workbook.getNumberOfSheets();
            if (sheetNum <= 0)  {
                return null;
            }
            for (int i=0;i<sheetNum;i++){       //循环读取每个工作表
                Sheet sheet = workbook.getSheetAt(i);
                List<List<String>> data = Lists.newArrayList();
                read(sheet,data,readHeader);
                datas.put(sheet.getSheetName(),data);
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } catch (InvalidFormatException e) {
            logger.error(e.getMessage(), e);
        }
        return datas;
    }
}
