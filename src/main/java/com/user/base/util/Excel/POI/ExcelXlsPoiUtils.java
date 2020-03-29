package com.user.base.util.Excel.POI;

import com.user.base.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ：mmzs
 * @date ：Created in 2020/3/26 16:36
 * @description：poi导入导出Excel，Excel有.xls,2003最多只允许存储65536条数据,HSSFWorkbook
 * @modified By：xphy
 * @version: 1$
 */
@Slf4j
public class ExcelXlsPoiUtils {

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
    public static void Export(HttpServletResponse response, String path, String fileName,String sheetName,String[] headers, List<String[]> data) throws IOException {
        //设置文件名称
        String fileNameTime = getFileName(path, fileName);
        //实例化HSSFWorkbook
        HSSFWorkbook workbook = new HSSFWorkbook();
        //创建一个Excel表单，参数为sheet的名字
        HSSFSheet sheet = workbook.createSheet("sheet");
        //设置表头
        setTitle(workbook, sheet, headers);
        //设置单元格并赋值
        setData(sheet, data);
        //设置浏览器下载
        setBrowser(response, workbook, fileNameTime+".xls");
        log.info("导出解析成功!");
    }

    /**
     * 方法名：setTitle
     * 功能：设置表头
     * 描述：
     * 创建人：typ
     * 创建时间：2018/10/19 10:20
     * 修改人：
     * 修改描述：
     * 修改时间：
     */
    private static void setTitle(HSSFWorkbook workbook, HSSFSheet sheet, String[] str) {
        try {
            //创建表头
            HSSFRow row = sheet.createRow(0);
            //设置列宽，setColumnWidth的第二个参数要乘以256，这个参数的单位是1/256个字符宽度
            for (int i = 0; i <= str.length; i++) {
                sheet.setColumnWidth(i, 15 * 256);
            }
            //设置为居中加粗,格式化时间格式
            HSSFCellStyle style = workbook.createCellStyle();
            // 设置字体
            Font font = workbook.createFont();
            // 设置字体大小
            font.setFontHeightInPoints((short) 11);
            // 字体加粗
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            // 设置字体名字
            font.setFontName("Courier New");
            style.setWrapText(true);// 设置自动换行
            style.setFont(font);
            style.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy h:mm"));
            //创建表头名称
            HSSFCell cell;
            for (int j = 0; j < str.length; j++) {
                cell = row.createCell(j);
                cell.setCellValue(str[j]);
                cell.setCellStyle(style);
            }
        } catch (Exception e) {
            log.info("导出时设置表头失败！");
            e.printStackTrace();
        }
    }

    /**
     * 方法名：setData
     * 功能：表格赋值
     * 描述：
     * 创建人：typ
     * 创建时间：2018/10/19 16:11
     * 修改人：
     * 修改描述：
     * 修改时间：
     */
    private static void setData(HSSFSheet sheet, List<String[]> data) {
        try {
            int rowNum = 1;
            for (int i = 0; i < data.size(); i++) {
                HSSFRow row = sheet.createRow(rowNum);
                for (int j = 0; j < data.get(i).length; j++) {
                    row.createCell(j).setCellValue(data.get(i)[j]);
                }
                rowNum++;
            }
            log.info("表格赋值成功！");
        } catch (Exception e) {
            log.info("表格赋值失败！");
            e.printStackTrace();
        }
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
    private static void setBrowser(HttpServletResponse response, HSSFWorkbook workbook, String fileName) {
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
    /**
     * 生成excel文件
     * @param path 生成excel路径
     * @param fileName 文件名
     */
    private static String getFileName (String path,String fileName){
        String resultName = fileName;
        long  timeNew =  System.currentTimeMillis()/ 1000;
        File file = new File(path);
        if (file.exists()) {
            //如果文件存在
            resultName =  resultName + timeNew;
            //file.delete();
        }
      return resultName;
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
            String fileName = "D:/export/文件名.xls";
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



}
