package com.gdgd.usercenter.model.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求体
 *
 * @author gdgd
 */
@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = -5905603898510535775L;

    private String userAccount;

    private String userPassword;

    private String checkPassword;

    private String planetCode;

}
