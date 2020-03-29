package com.user.base.util.Excel.jdk8.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ：mmzs
 * @date ：Created in 2020/3/26 16:12
 * @description：excel导出实体类
 * @modified By：
 * @version: 1$
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExcelBean {
    @ExcelColumn(value = "cityCode", col = 1)
    private String userName;

    @ExcelColumn(value = "markId", col = 2)
    private String loginName;

    @ExcelColumn(value = "toaluv", col = 3)
    private String pwd;

    @ExcelColumn(value = "date", col = 4)
    private Integer state;

}
