package com.user.base.util.POI;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @author ：mmzs
 * @date ：Created in 2020/3/26 16:43
 * @description：excel业务层
 * @modified By：
 * @version: 1$
 */
@RestController(ExcelController.BEAN_NAME)
@RequestMapping("/excel")
public class ExcelController {
    static final String BEAN_NAME = "com.user.base.until.Excel.POI.ExcelController";
    /**
     * @description: 导出xls
     * @param response
     * @return: void
     * @author: Andy
     * @time: 2020/4/5 11:59
     */
    @RequestMapping("/exportXls")
    public void exportExcel(HttpServletResponse response) throws IOException {
        //设置表头
        String[] headers = {"id", "uid", "地址", "城市"};
        List<String[]> date = new ArrayList<>();
        String[] bbb = new String[]{"1","2","3","4"};
        date.add(bbb);
        ExcelXlsPoiUtils.Export(response, "D://Export/excel","人员名称","平台研发",headers,date);
    }
    /**
     * @description: 导出xlsx
     * @param response
     * @return: void
     * @author: Andy
     * @time: 2020/4/5 11:59
     */
    @RequestMapping("/exportXlsx")
    public void exportExcelx(HttpServletResponse response) throws IOException {
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
            ExcelXlsxPoiUtils.writeExcel(response,"D:\\export", "a", sheetName, columnNames, sheetTitle, objects, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
