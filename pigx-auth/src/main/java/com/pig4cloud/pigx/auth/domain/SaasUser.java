package com.pig4cloud.pigx.auth.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 扩展用户信息
 * @Author: Waylon
 * @Date: 2019/10/23
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaasUser implements Serializable {

    private int id;
    private String userName;
    private String password;
    private String mobile;
    private String email;
    private short sex;
    private Date birthday;
    private Date createTime;
}
