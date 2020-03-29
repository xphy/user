package com.user.base.util.Excel.POI;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
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
    @RequestMapping("/exportExcel")
    public void exportExcel(HttpServletResponse response) throws IOException {
        //设置表头
        String[] headers = {"id", "uid", "地址", "城市"};
        List<String[]> date = new ArrayList<>();
        String[] bbb = new String[]{"1","2","3","4"};
        date.add(bbb);
        ExcelXlsPoiUtils.Export(response, "D://Export/excel","人员名称","平台研发",headers,date);
    }

}
