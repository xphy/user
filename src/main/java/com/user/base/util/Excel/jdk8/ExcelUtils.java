/*
package com.user.base.until.Excel.POI;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

*/
/**
 * @author ：mmzs
 * @date ：Created in 2020/3/26 16:36
 * @description：poi导入导出Excel，Excel有.xls和.xlsx
 * @modified By：
 * @version: 1$
 *//*

public class ExcelPoiUtils {
    */
/**
     * Description: 1、将list中的data写入表格中
     *
     * @param messageService
     *            国际化对象
     * @param keyName
     *            导出文件的key值
     * @param list
     *            数据库查询出的Bean集合
     * @param fieldNames
     *            查询出的字段list
     * @return
     * @author: yizl
     * @date: Oct 30, 2018 1:08:08 PM
     *//*

    public static <T, S> SXSSFWorkbook export(LocaleMessageSourceService messageService,
                                              String keyName, List<T> list,
                                              List<String> fieldNames)
    {
        // 内存中保留 10000 条数据，以免内存溢出，其余写入 硬盘
        SXSSFWorkbook wb = new SXSSFWorkbook(2000);
        // 创建一个Excel的sheet
        Sheet sheet = wb.createSheet(messageService.getMessage(keyName));
        for (int j = 0; j <= list.size(); j++ )
        {
            Row row = sheet.createRow(j);// 第j行
            JSONObject jsonObj = null;
            if (j != 0)
            {

                jsonObj = (JSONObject)JSON.toJSON(list.get(j - 1));
            }
            for (int k = 0; k < fieldNames.size(); k++ )
            {
                // 第一列单元格
                Cell cell = row.createCell(k);
                // 第一行
                if (j == 0)
                {
                    CellStyle style = getColumnTopStyle(wb);
                    cell.setCellStyle(style);
                    // 第一列数据
                    cell.setCellValue(
                            //将表头国际化
                            messageService.getMessage(keyName + "." + fieldNames.get(k)));
                }
                else
                {
                    // 将bean对象转换成JSON对象
                    String content = jsonObj.getString(fieldNames.get(k));
                    if (content == null)
                    {
                        content = "";
                    }
                    CellStyle style = getBodyStyle(wb);
                    cell.setCellStyle(style);
                    cell.setCellValue(content);
                }
            }

        }

        */
/*
         * for (int k = 0; k < fieldNames.size(); k++ ) {
         * // poi提供的自动调整每列的宽度,但是不兼容中文调整
         * sheet.autoSizeColumn(k); }
         *//*

        setSizeColumn(sheet, fieldNames.size());
        return wb;
    }

    */
/**
     * Description: 1、调整列宽,兼容中文
     *
     * @param sheet
     * @param size
     * @author: yizl
     * @date: Nov 1, 2018 11:31:56 AM
     *//*

    private static void setSizeColumn(Sheet sheet, int size)
    {
        for (int columnNum = 0; columnNum < size; columnNum++ )
        {
            int columnWidth = sheet.getColumnWidth(columnNum) / 256;
            for (int rowNum = 0; rowNum < sheet.getLastRowNum(); rowNum++ )
            {
                Row currentRow;
                // 当前行未被使用过
                if (sheet.getRow(rowNum) == null)
                {
                    currentRow = sheet.createRow(rowNum);
                }
                else
                {
                    currentRow = sheet.getRow(rowNum);
                }

                if (currentRow.getCell(columnNum) != null)
                {
                    Cell currentCell = currentRow.getCell(columnNum);
                    if (currentCell.getCellType() == Cell.CELL_TYPE_STRING)
                    {
                        int length = currentCell.getStringCellValue().getBytes().length;
                        if (columnWidth < length)
                        {
                            columnWidth = length;
                        }
                    }
                }
            }

            // Excel的长度为字节码长度*256,*1.3为了处理数字格式
            columnWidth = (int)Math.floor(columnWidth * 256 * 1.3);
            //单元格长度大于20000的话也不美观,设置个最大长度
            columnWidth = columnWidth >= 20000 ? 20000 : columnWidth;
            //设置每列长度
            sheet.setColumnWidth(columnNum, columnWidth);
        }
    }

    */
/**
     * Description: 1、通过浏览器workbook以流的形式输出,为了处理中文表名路是你吗问题.
     *
     * @param workbook
     *            文件对象
     * @param request
     * @param response
     * @param fileName
     *            文件名
     * @author: yizl
     * @date: Oct 30, 2018 1:06:27 PM
     *//*

    public static void writeToResponse(SXSSFWorkbook workbook, HttpServletRequest request,
                                       HttpServletResponse response, String fileName)
    {
        try
        {
            String userAgent = request.getHeader("User-Agent");
            // 解决中文乱码问题
            String fileName1 = "Excel-" + fileName + ".xlsx";
            String newFilename = URLEncoder.encode(fileName1, "UTF8");
            // 如果没有userAgent，则默认使用IE的方式进行编码，因为毕竟IE还是占多数的
            String rtn = "filename=\"" + newFilename + "\"";
            if (userAgent != null)
            {
                userAgent = userAgent.toLowerCase();
                // IE浏览器，只能采用URLEncoder编码
                if (userAgent.indexOf(IE) != -1)
                {
                    rtn = "filename=\"" + newFilename + "\"";
                }
                // Opera浏览器只能采用filename*
                else if (userAgent.indexOf(OPERA) != -1)
                {
                    rtn = "filename*=UTF-8''" + newFilename;
                }
                // Safari浏览器，只能采用ISO编码的中文输出
                else if (userAgent.indexOf(SAFARI) != -1)
                {
                    rtn = "filename=\"" + new String(fileName1.getBytes("UTF-8"), "ISO8859-1")
                            + "\"";
                }
                // FireFox浏览器，可以使用MimeUtility或filename*或ISO编码的中文输出
                else if (userAgent.indexOf(FIREFOX) != -1)
                {
                    rtn = "filename*=UTF-8''" + newFilename;
                }
            }

            String headStr = "attachment;  " + rtn;
            response.setContentType("APPLICATION/ms-excel");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-Disposition", headStr);
            // 响应到客户端
            OutputStream os = response.getOutputStream();
            workbook.write(os);
        }
        catch (UnsupportedEncodingException e)
        {

            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    */
/**
     * Description: 1、列头单元格样式
     *
     * @param workbook
     * @return
     * @author: yizl
     * @date: Oct 30, 2018 1:22:44 PM
     *//*

    public static CellStyle getColumnTopStyle(SXSSFWorkbook workbook)
    {

        // 设置字体
        Font font = workbook.createFont();
        // 设置字体大小
        font.setFontHeightInPoints((short)11);
        // 字体加粗
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        // 设置字体名字
        font.setFontName("Courier New");
        // 设置样式;
        CellStyle style = workbook.createCellStyle();
        // 设置底边框;
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        // 设置底边框颜色;
        style.setBottomBorderColor(HSSFColor.BLACK.index);
        // 设置左边框;
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        // 设置左边框颜色;
        style.setLeftBorderColor(HSSFColor.BLACK.index);
        // 设置右边框;
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        // 设置右边框颜色;
        style.setRightBorderColor(HSSFColor.BLACK.index);
        // 设置顶边框;
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        // 设置顶边框颜色;
        style.setTopBorderColor(HSSFColor.BLACK.index);
        // 在样式用应用设置的字体;
        style.setFont(font);
        // 设置自动换行;
        style.setWrapText(false);
        // 设置水平对齐的样式为居中对齐;
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        // 设置垂直对齐的样式为居中对齐;
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        return style;
    }

    */
/**
     * Description: 1、设置表体的单元格样式
     *
     * @param workbook
     * @return
     * @author: yizl
     * @date: Oct 30, 2018 1:18:42 PM
     *//*

    private static CellStyle getBodyStyle(SXSSFWorkbook workbook)
    {
        // 创建单元格样式
        CellStyle cellStyle = workbook.createCellStyle();
        // 设置单元格居中对齐
        cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        // 设置单元格居中对齐
        cellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        // 创建单元格内容不显示自动换行
        cellStyle.setWrapText(false);
        // 设置单元格字体样式
        XSSFFont font = (XSSFFont)workbook.createFont();
        font.setFontName("Courier New");// 设置字体
        font.setFontHeight(11);// 设置字体的大小
        cellStyle.setFont(font);// 将字体添加到表格中去
        // 设置单元格边框为细线条
        cellStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
        return cellStyle;
    }
}
*/
