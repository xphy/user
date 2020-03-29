package com.user.base.util.Excel.POI;

import com.monitorjbl.xlsx.StreamingReader;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

/**
 * @author ：mmzs
 * @date ：Created in 2020/3/26 16:51
 * @description：excel bean对象
 * @modified By：
 * @version: 1$
 */
public class ExcelBean {
    public static void importData(String path) throws Exception {

        FileInputStream in = new FileInputStream(path);
        Workbook wk = StreamingReader.builder()
                .rowCacheSize(100)
                .bufferSize(4096)
                .open(in);
        Sheet sheet = wk.getSheetAt(0);


        String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

        String dbURL = "jdbc:sqlserver://10.122.44.129:1433; DatabaseName=RepairCost";
        Class.forName(driverName);
        Connection conn = DriverManager.getConnection(dbURL,"sa","q.123456");
        //System.out.println("");
        conn.setAutoCommit(false);

        String sql = "insert into [RepairCost].[Middle].[Base_External_Billing_Report] values (?,?,?,?,?,?,?,?,?,?," +
                "?,?,?,?,?,?,?,?,?,?," +
                "?,?,?,?,?,?,?,?,?,?," +
                "?,?,?,?,?,?,?,?,?,?," +
                "?,?,?,?,?,?,?,?,?,?," +
                "?,?,?,?,?,?,?,?,?,?," +
                "?,?,?,?,?,?,?,?,?,?," +
                "?,?,?,?,?,?,?,?,?,?," +
                "?,?,?,?,?,?,?,?,?,?," +
                "?,?,?,?,?,?,?,?,?,?," +
                "?,?,?,?,?,?,?,?,?,?," +
                "?,?,?,?,?,?,?,?,?,?," +
                "?,?,?,?,?,?,?,?,?,?," +
                "?)";

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
