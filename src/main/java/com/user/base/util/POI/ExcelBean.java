/*
package com.user.base.util.Excel.POI;

import com.monitorjbl.xlsx.StreamingReader;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.List;

*/
/**
 * @author ：mmzs
 * @date ：Created in 2020/3/26 16:51
 * @description：excel bean对象
 * @modified By：
 * @version: 1$
 *//*

public class ExcelBean {
    //测试导入
    public static void main(String[] args) {
        try {
            String fileName = "D:/export/a.xlsx";
              importData(fileName);
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
    public static void importData(String path) throws Exception {

        FileInputStream in = new FileInputStream(path);
        Workbook wk = StreamingReader.builder()
                .rowCacheSize(100)
                .bufferSize(4096)
                .open(in);

//        SXSSFWorkbook wk = new SXSSFWorkbook(wb);
//        Workbook wk = WorkbookFactory.create(in);
        Sheet sheet = wk.getSheetAt(0);


        String driverName = "";

        String dbURL = "";
        Class.forName(driverName);
        Connection conn = DriverManager.getConnection(dbURL,"","");
        //System.out.println("");
        conn.setAutoCommit(false);

        String sql = "insert into tablename values (?,?,?,?,?,?,?,?,?,?)";

//        String sql = "insert into [Source].[MasterData] values (?,?,?,?,?)";

        PreparedStatement st = conn.prepareStatement(sql);
//        for (Row row : sheet) {
//            System.out.println("" + row.getRowNum() + "");
//            for (int i = 0;) {
//
//            }
//            System.out.println(" ");
//        }

        Cell cell = null;
        int i = 0;
        for (Row row : sheet) {
            if(i>=4){
                System.out.println(""+i+":"+row.getLastCellNum());
                for (int j = 1; j < row.getLastCellNum(); j++) {
                    cell = row.getCell(j);
                    st.setObject(j,cell == null?null:cell.getStringCellValue());
                }
                st.addBatch();
                if(i%500==0){
                    st.executeBatch();
                    conn.commit();
                }
            }
            i++;
        }
        st.executeBatch();
        conn.commit();
        st.close();
        conn.close();

        System.out.println("文件读取完毕:"+path+" ");
    }
}
*/
