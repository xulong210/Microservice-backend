package cn.xu.eduuser.entity.vo;

import lombok.Data;

@Data
public class RegisterVo {

    private String nickname;

    private String mobile;

    private String password;

    //验证码
    private String code;

}
