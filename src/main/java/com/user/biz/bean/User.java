package com.user.biz.bean;

import com.user.base.bean.BaseBean;
import lombok.*;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author ：mmzs
 * @date ：Created in 2020/1/3 19:29
 * @description：user model
 * @modified By：
 * @version: 1$
 */

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "user")
@Entity
@Data
public class User {
    @Id
    @Column(name = "u_id")
    private String uid;
    @Column(name = "u_name")
    private String userName;
    @Column(name = "l_name")
    private String loginName;
    @Column(name = "pwd")
    private String pwd;
    @Column(name = "state")
    private Integer state;
}
