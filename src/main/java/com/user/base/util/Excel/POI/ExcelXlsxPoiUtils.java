package com.user.base.util.Excel.POI;

import com.user.base.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author ：mmzs
 * @date ：Created in 2020/3/26 17:11
 * @description：导出Excel xlsx 2007以上的支持大数据量
 * @modified By：
 * @version: 1$
 */
@Slf4j
public class ExcelXlsxPoiUtils {

    private static final int DEFAULT_COLUMN_SIZE = 30;

    /**
     * 断言Excel文件写入之前的条件
     *
     * @param directory 目录
     * @param fileName  文件名
     * @return file
     * @throws IOException
     */
    private static File assertFile(String directory, String fileName) throws IOException {
        File tmpFile = new File(directory + File.separator + fileName + ".xlsx");
        if (tmpFile.exists()) {
            //如果文件存在
            if (tmpFile.isDirectory()) {
                throw new IOException("File '" + tmpFile + "' exists but is a directory");
            }
            if (!tmpFile.canWrite()) {
                throw new IOException("File '" + tmpFile + "' cannot be written to");
            }
        } else {
            File parent = tmpFile.getParentFile();
            if (parent != null) {
                if (!parent.mkdirs() && !parent.isDirectory()) {
                    throw new IOException("Directory '" + parent + "' could not be created");
                }
            }
        }
        return tmpFile;
    }
    /**
     * 日期转化为字符串,格式为yyyy-MM-dd HH:mm:ss
     */
    private static String getCnDate(Date date) {
        String format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * Excel 导出，POI实现
     *
     * @param fileName    文件名
     * @param sheetName   sheet页名称
     * @param columnNames 表头列表名
     * @param sheetTitle  sheet页Title
     * @param objects     目标数据集
     */
    public static File writeExcel(HttpServletResponse response,String directory, String fileName, String sheetName, List<String> columnNames,
                                  String sheetTitle, List<List<Object>> objects, boolean append) throws  IOException {
        File tmpFile = assertFile(directory, fileName);
        return exportExcel(response,tmpFile, sheetName, columnNames, sheetTitle, objects, append);
    }
    /**
     * Excel 导出，POI实现，先写入Excel标题，与writeExcelData配合使用
     * 先使用writeExcelTitle再使用writeExcelData
     *
     * @param directory   目录
     * @param fileName    文件名
     * @param sheetName   sheetName
     * @param columnNames 列名集合
     * @param sheetTitle  表格标题
     * @param append      是否在现有的文件追加
     * @return file
     * @throws ReportInternalException
     * @throws IOException
     */
    public static File writeExcelTitle(String directory, String fileName, String sheetName, List<String> columnNames,
                                       String sheetTitle, boolean append) throws  IOException {
        File tmpFile = assertFile(directory, fileName);
        return exportExcelTitle(tmpFile, sheetName, columnNames, sheetTitle, append);
    }

    /**
     * Excel 导出，POI实现，写入Excel数据行列，与writeExcelTitle配合使用
     * 先使用writeExcelTitle再使用writeExcelData
     *
     * @param directory 目录
     * @param fileName  文件名
     * @param sheetName sheetName
     * @param objects   数据信息
     * @return file
     * @throws ReportInternalException
     * @throws IOException
     */
    public static File writeExcelData(String directory, String fileName, String sheetName, List<List<Object>> objects)
            throws  IOException {
        File tmpFile = assertFile(directory, fileName);
        return exportExcelData(tmpFile, sheetName, objects);
    }

    /**
     * 导出字符串数据
     *
     * @param file        文件名
     * @param columnNames 表头
     * @param sheetTitle  sheet页Title
     * @param append      是否追加写文件
     * @return file
     * @throws
     */
    private static File exportExcelTitle(File file, String sheetName, List<String> columnNames,
                                         String sheetTitle, boolean append) throws  IOException {
        // 声明一个工作薄
        Workbook workBook;
        if (file.exists() && append) {
            workBook = new XSSFWorkbook(new FileInputStream(file));
        } else {
            workBook = new XSSFWorkbook();
        }
        Map<String, CellStyle> cellStyleMap = styleMap(workBook);
        // 表头样式
        CellStyle headStyle = cellStyleMap.get("head");
        // 生成一个表格
        Sheet sheet = workBook.getSheet(sheetName);
        if (sheet == null) {
            sheet = workBook.createSheet(sheetName);
        }
        //最新Excel列索引,从0开始
        int lastRowIndex = sheet.getLastRowNum();
        if (lastRowIndex > 0) {
            lastRowIndex++;
        }
        // 设置表格默认列宽度
        sheet.setDefaultColumnWidth(DEFAULT_COLUMN_SIZE);
        // 合并单元格
        sheet.addMergedRegion(new CellRangeAddress(lastRowIndex, lastRowIndex, 0, columnNames.size() - 1));
        // 产生表格标题行
        Row rowMerged = sheet.createRow(lastRowIndex);
        lastRowIndex++;
        Cell mergedCell = rowMerged.createCell(0);
        mergedCell.setCellStyle(headStyle);
        mergedCell.setCellValue(new XSSFRichTextString(sheetTitle));
        // 产生表格表头列标题行
        Row row = sheet.createRow(lastRowIndex);
        for (int i = 0; i < columnNames.size(); i++) {
            Cell cell = row.createCell(i);
            cell.setCellStyle(headStyle);
            RichTextString text = new XSSFRichTextString(columnNames.get(i));
            cell.setCellValue(text);
        }
        try {
            OutputStream ops = new FileOutputStream(file);
            workBook.write(ops);
            ops.flush();
            ops.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 导出字符串数据
     *
     * @param file    文件名
     * @param objects 目标数据
     * @return
     * @throws ReportInternalException
     */
    private static File exportExcelData(File file, String sheetName, List<List<Object>> objects) throws  IOException {
        // 声明一个工作薄
        Workbook workBook;
        if (file.exists()) {
            workBook = new XSSFWorkbook(new FileInputStream(file));
        } else {
            workBook = new XSSFWorkbook();
        }

        Map<String, CellStyle> cellStyleMap = styleMap(workBook);
        // 正文样式
        CellStyle contentStyle = cellStyleMap.get("content");
        //正文整数样式
        CellStyle contentIntegerStyle = cellStyleMap.get("integer");
        //正文带小数整数样式
        CellStyle contentDoubleStyle = cellStyleMap.get("double");
        // 生成一个表格
        Sheet sheet = workBook.getSheet(sheetName);
        if (sheet == null) {
            sheet = workBook.createSheet(sheetName);
        }
        //最新Excel列索引,从0开始
        int lastRowIndex = sheet.getLastRowNum();
        if (lastRowIndex > 0) {
            lastRowIndex++;
        }
        // 设置表格默认列宽度
        sheet.setDefaultColumnWidth(DEFAULT_COLUMN_SIZE);
        // 遍历集合数据,产生数据行,前两行为标题行与表头行
        for (List<Object> dataRow : objects) {
            Row row = sheet.createRow(lastRowIndex);
            lastRowIndex++;
            for (int j = 0; j < dataRow.size(); j++) {
                Cell contentCell = row.createCell(j);
                Object dataObject = dataRow.get(j);
                if (dataObject != null) {
                    if (dataObject instanceof Integer) {
                        contentCell.setCellStyle(contentIntegerStyle);
                        contentCell.setCellValue(Integer.parseInt(dataObject.toString()));
                    } else if (dataObject instanceof Double) {
                        contentCell.setCellStyle(contentDoubleStyle);
                        contentCell.setCellValue(Double.parseDouble(dataObject.toString()));
                    } else if (dataObject instanceof Long && dataObject.toString().length() == 13) {
                        contentCell.setCellStyle(contentStyle);
                        contentCell.setCellValue(getCnDate(new Date(Long.parseLong(dataObject.toString()))));
                    } else if (dataObject instanceof Date) {
                        contentCell.setCellStyle(contentStyle);
                        contentCell.setCellValue(getCnDate((Date) dataObject));
                    } else {
                        contentCell.setCellStyle(contentStyle);
                        contentCell.setCellValue(dataObject.toString());
                    }
                } else {
                    contentCell.setCellStyle(contentStyle);
                    // 设置单元格内容为字符型
                    contentCell.setCellValue("");
                }
            }
        }
        try {
            OutputStream ops = new FileOutputStream(file);
            workBook.write(ops);
            ops.flush();
            ops.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 导出字符串数据，至浏览器中
     *
     * @param file        文件名
     * @param columnNames 表头
     * @param sheetTitle  sheet页Title
     * @param objects     目标数据
     * @param append      是否追加写文件
     * @return
     * @throws
     */
    private static File exportExcel(HttpServletResponse response,File file, String sheetName, List<String> columnNames,
                                    String sheetTitle, List<List<Object>> objects, boolean append) throws IOException {
        // 声明一个工作薄
        XSSFWorkbook workBook;
        if (file.exists() && append) {
            // 声明一个工作薄
            workBook = new XSSFWorkbook(new FileInputStream(file));
        } else {
            workBook = new XSSFWorkbook();
        }
        Map<String, CellStyle> cellStyleMap = styleMap(workBook);
        // 表头样式
        CellStyle headStyle = cellStyleMap.get("head");
        // 正文样式
        CellStyle contentStyle = cellStyleMap.get("content");
        //正文整数样式

        CellStyle contentIntegerStyle = cellStyleMap.get("integer");
        //正文带小数整数样式
        CellStyle contentDoubleStyle = cellStyleMap.get("double");
        // 生成一个表格
        Sheet sheet = workBook.getSheet(sheetName);
        if (sheet == null) {
            sheet = workBook.createSheet(sheetName);
        }
        //最新Excel列索引,从0开始
        int lastRowIndex = sheet.getLastRowNum();
        if (lastRowIndex > 0) {
            lastRowIndex++;
        }
        // 设置表格默认列宽度
        sheet.setDefaultColumnWidth(DEFAULT_COLUMN_SIZE);
        // 合并单元格
        sheet.addMergedRegion(new CellRangeAddress(lastRowIndex, lastRowIndex, 0, columnNames.size() - 1));
        // 产生表格标题行
        Row rowMerged = sheet.createRow(lastRowIndex);
        lastRowIndex++;
        Cell mergedCell = rowMerged.createCell(0);
        mergedCell.setCellStyle(headStyle);
        mergedCell.setCellValue(new XSSFRichTextString(sheetTitle));
        // 产生表格表头列标题行
        Row row = sheet.createRow(lastRowIndex);
        lastRowIndex++;
        for (int i = 0; i < columnNames.size(); i++) {
            Cell cell = row.createCell(i);
            cell.setCellStyle(headStyle);
            RichTextString text = new XSSFRichTextString(columnNames.get(i));
            cell.setCellValue(text);
        }
        // 遍历集合数据,产生数据行,前两行为标题行与表头行
        for (List<Object> dataRow : objects) {
            row = sheet.createRow(lastRowIndex);
            lastRowIndex++;
            for (int j = 0; j < dataRow.size(); j++) {
                Cell contentCell = row.createCell(j);
                Object dataObject = dataRow.get(j);
                if (dataObject != null) {
                    if (dataObject instanceof Integer) {
                        contentCell.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
                        contentCell.setCellStyle(contentIntegerStyle);
                        contentCell.setCellValue(Integer.parseInt(dataObject.toString()));
                    } else if (dataObject instanceof Double) {
                        contentCell.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
                        contentCell.setCellStyle(contentDoubleStyle);
                        contentCell.setCellValue(Double.parseDouble(dataObject.toString()));
                    } else if (dataObject instanceof Long && dataObject.toString().length() == 13) {
                        contentCell.setCellType(XSSFCell.CELL_TYPE_STRING);
                        contentCell.setCellStyle(contentStyle);
                        contentCell.setCellValue(getCnDate(new Date(Long.parseLong(dataObject.toString()))));
                    } else if (dataObject instanceof Date) {
                        contentCell.setCellType(XSSFCell.CELL_TYPE_STRING);
                        contentCell.setCellStyle(contentStyle);
                        contentCell.setCellValue(getCnDate((Date) dataObject));
                    } else {
                        contentCell.setCellType(XSSFCell.CELL_TYPE_STRING);
                        contentCell.setCellStyle(contentStyle);
                        contentCell.setCellValue(dataObject.toString());
                    }
                } else {
                    contentCell.setCellStyle(contentStyle);
                    // 设置单元格内容为字符型
                    contentCell.setCellValue("");
                }
            }
        }

            setBrowser(response,workBook,file.getName());
//            OutputStream ops = new FileOutputStream(file);
//            workBook.write(ops);
//            ops.flush();
//            ops.close();

        return file;
    }

    /**
     * 创建单元格表头样式
     *
     * @param workbook 工作薄
     */
    private static CellStyle createCellHeadStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        // 设置边框样式
        style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        style.setBorderRight(XSSFCellStyle.BORDER_THIN);
        style.setBorderTop(XSSFCellStyle.BORDER_THIN);
        //设置对齐样式
        style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        // 生成字体
        Font font = workbook.createFont();
        // 表头样式
        style.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        font.setFontHeightInPoints((short) 12);
        font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        // 把字体应用到当前的样式
        style.setFont(font);
        return style;
    }

    /**
     * 创建单元格正文样式
     *
     * @param workbook 工作薄
     */
    private static CellStyle createCellContentStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        // 设置边框样式
        style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        style.setBorderRight(XSSFCellStyle.BORDER_THIN);
        style.setBorderTop(XSSFCellStyle.BORDER_THIN);
        //设置对齐样式
        style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        // 生成字体
        Font font = workbook.createFont();
        // 正文样式
        style.setFillPattern(XSSFCellStyle.NO_FILL);
        style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        font.setBoldweight(XSSFFont.BOLDWEIGHT_NORMAL);
        // 把字体应用到当前的样式
        style.setFont(font);
        return style;
    }

    /**
     * 单元格样式(Integer)列表
     */
    private static CellStyle createCellContent4IntegerStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        // 设置边框样式
        style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        style.setBorderRight(XSSFCellStyle.BORDER_THIN);
        style.setBorderTop(XSSFCellStyle.BORDER_THIN);
        //设置对齐样式
        style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        // 生成字体
        Font font = workbook.createFont();
        // 正文样式
        style.setFillPattern(XSSFCellStyle.NO_FILL);
        style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        font.setBoldweight(XSSFFont.BOLDWEIGHT_NORMAL);
        // 把字体应用到当前的样式
        style.setFont(font);
        style.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));//数据格式只显示整数
        return style;
    }

    /**
     * 单元格样式(Double)列表
     */
    private static CellStyle createCellContent4DoubleStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        // 设置边框样式
        style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        style.setBorderRight(XSSFCellStyle.BORDER_THIN);
        style.setBorderTop(XSSFCellStyle.BORDER_THIN);
        //设置对齐样式
        style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        // 生成字体
        Font font = workbook.createFont();
        // 正文样式
        style.setFillPattern(XSSFCellStyle.NO_FILL);
        style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        font.setBoldweight(XSSFFont.BOLDWEIGHT_NORMAL);
        // 把字体应用到当前的样式
        style.setFont(font);
        style.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0.00"));//保留两位小数点
        return style;
    }

    /**
     * 单元格样式列表
     */
    private static Map<String, CellStyle> styleMap(Workbook workbook) {
        Map<String, CellStyle> styleMap = new LinkedHashMap<>();
        styleMap.put("head", createCellHeadStyle(workbook));
        styleMap.put("content", createCellContentStyle(workbook));
        styleMap.put("integer", createCellContent4IntegerStyle(workbook));
        styleMap.put("double", createCellContent4DoubleStyle(workbook));
        return styleMap;
    }
    /***
     * @description: 输出到浏览器
     * @param response 相应
     * @param workbook 文件内容
     * @param fileName 文件名字
     * @return: void
     * @author: Andy
     * @time: 2020/3/26 17:31
     */
    private static void setBrowser(HttpServletResponse response, XSSFWorkbook workbook, String fileName) {
        try {
            response.setContentType("application/ms-excel;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename="
                    .concat(String.valueOf(URLEncoder.encode(fileName, "UTF-8"))));
            response.flushBuffer();
            OutputStream out = response.getOutputStream();
            workbook.write(out);// 将数据写出去
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /***
     * @description: poi导出excel
     * @param response 响应
     * @param path 生成的文件路径
     * @param fileName 文件名
     * @param sheetName 工作蒲名
     * @param headers 表格头部
     * @param data 表格数据
     * @return: void
     * @author: Andy
     * @time: 2020/3/27 14:40
     */
    public static void Export(HttpServletResponse response, String path, String fileName,String sheetName, String sheetTitleName,List<String> columnNames, List<List<Object>> data) throws IOException {
        //写入标题--第二种方式
        ExcelXlsxPoiUtils.writeExcelTitle(path, fileName, sheetName, columnNames, sheetTitleName, false);
        try {
            //写入数据--第二种方式
            ExcelXlsxPoiUtils.writeExcelData(path, fileName, sheetName, data);

            //直接写入数据--非浏览器下载
            //ExcelXlsxPoiUtils.writeExcel(path, fileName, sheetName, columnNames, sheetTitleName, data, false);
            ExcelXlsxPoiUtils.writeExcel(response,path, fileName, sheetName, columnNames, sheetTitleName, data, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("导出解析成功!");
    }
    /***
     * @description: excel导入
     * @param filePath 文件路径
     * @return: java.util.List<java.lang.Object[]>
     * @author: Andy
     * @time: 2020/3/27 15:06
     */
    public static List<Object[]> importExcel(String filePath) {
        log.info("导入解析开始，fileName:{}",filePath);
        if(StringUtil.isEmpty(filePath)){
            //导入文件不存在
            return null;
        }
        try {
            List<Object[]> list = new ArrayList<>();
            //获得输入流
            InputStream inputStream = new FileInputStream(filePath);
            //获取workbook对象
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            //获取sheet的行数
            int rows = sheet.getPhysicalNumberOfRows();
            for (int i = 0; i < rows; i++) {
                //过滤表头行
                if (i == 0) {
                    continue;
                }
                //获取当前行的数据
                Row row = sheet.getRow(i);
                Object[] objects = new Object[row.getPhysicalNumberOfCells()];
                int index = 0;
                for (Cell cell : row) {
                    if (cell.getCellType()==0) {
                        objects[index] = (int) cell.getNumericCellValue();
                    }
                    if (cell.getCellType() == 1) {
                        objects[index] = cell.getStringCellValue();
                    }
                    if (cell.getCellType()== 4) {
                        objects[index] = cell.getBooleanCellValue();
                    }
                    if (cell.getCellType()==5) {
                        objects[index] = cell.getErrorCellValue();
                    }
                    index++;
                }
                list.add(objects);
            }
            inputStream.close();//是否需要关闭
            log.info("导入文件解析成功！");
            return list;
        }catch (Exception e){
            log.info("导入文件解析失败！");
            e.printStackTrace();
        }
        return null;
    }
    //测试导入
    public static void main(String[] args) {
        try {
            String fileName = "D:/export/a.xlsx";
            List<Object[]> list = importExcel(fileName);
//            for (int i = 0; i < list.size(); i++) {
//                User user = new User();
//                user.setId((Integer) list.get(i)[0]);
//                user.setUsername((String) list.get(i)[1]);
//                user.setPassword((String) list.get(i)[2]);
//                user.setEnable((Integer) list.get(i)[3]);
//                System.out.println(user.toString());
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

/*    public static void main(String[] args) throws IOException {
        String sheetName = "测试Excel格式";
        String sheetTitle = "测试Excel格式1";
        List<String> columnNames = new LinkedList<>();
        columnNames.add("日期-String");
        columnNames.add("日期-Date");
        columnNames.add("时间戳-Long");
        columnNames.add("客户编码");
        columnNames.add("整数");
        columnNames.add("带小数的正数");

        //写入标题--第二种方式
        ExcelXlsxPoiUtils.writeExcelTitle("D:\\export", "a", sheetName, columnNames, sheetTitle, false);

        List<List<Object>> objects = new LinkedList<>();
        for (int i = 0; i < 1000; i++) {
            List<Object> dataA = new LinkedList<>();
            dataA.add("2016-09-05 17:27:25");
            dataA.add(new Date(1451036631012L));
            dataA.add(1451036631012L);
            dataA.add("000628");
            dataA.add(i);
            dataA.add(1.323 + i);
            objects.add(dataA);
        }
        try {
            //写入数据--第二种方式
            ExcelXlsxPoiUtils.writeExcelData("D:\\export", "a", sheetName, objects);

            //直接写入数据--第一种方式
            ExcelXlsxPoiUtils.writeExcel("D:\\export", "a", sheetName, columnNames, sheetTitle, objects, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
}
