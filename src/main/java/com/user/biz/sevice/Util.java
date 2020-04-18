package com.user.biz.sevice;

import com.user.base.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

/**
 * @author ：mmzs
 * @date ：Created in 2020/4/17 13:29
 * @description：excel多线程导入
 * @modified By：
 * @version: 1$
 */
@Slf4j
@Component
public class Util {
    /***
     * @description: excel导入
     * @param filePath 文件路径
     * @return: java.util.List<java.lang.Object[]>
     * @author: Andy
     * @time: 2020/3/27 15:06
     */
    public static List<Map<String,Object>> importExcel(MultipartFile file) {

        //多线程编程需要一个线程安全的ArrayList
        List<Map<String,Object>> list = new ArrayList<>();
        try {
            //获得输入流
//            InputStream inputStream = new FileInputStream(filePath);
            //获取workbook对象
            Workbook workbook = WorkbookFactory.create(file.getInputStream());
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
                Map<String,Object> map = new HashMap<>();
                for (Cell cell : row) {
                    if (cell.getCellType()==1) {
                        map.put("id", cell.getNumericCellValue());
                    }
                    if (cell.getCellType() == 1) {
                        map.put("uname", cell.getStringCellValue());
                    }
                    if (cell.getCellType() == 1) {
                        map.put("lname", cell.getStringCellValue());
                    }
                    if (cell.getCellType() == 1) {
                        map.put("pwd", cell.getStringCellValue());
                    }
                    if (cell.getCellType() == 0) {
                        map.put("state", cell.getNumericCellValue());
                    }
                }
                list.add(map);
            }
//            inputStream.close();//是否需要关闭
            log.info("导入文件解析成功！");
        }catch (Exception e){
            log.info("导入文件解析失败！");
            e.printStackTrace();
        }
        return list;
    }
}
