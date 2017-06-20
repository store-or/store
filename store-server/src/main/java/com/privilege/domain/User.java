package com.privilege.domain;

import com.core.dao.IdDO;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 */
@Table(name = "`user`")
@Entity
@SequenceGenerator(name = "seq_gen",sequenceName = "seq_user")
public class User extends IdDO {

    public static final int USER_ENABLE = 1;
    public static final int USER_UNABLE = 0;

    private String loginName;
    private String trueName;
    private String password;
    private Date createTime;
    private Date lastLoginTime;
    private String email;
    private Integer status;
    private String salt;

    @Column(name = "login_name")
    @NotEmpty(message = "不能为空")
    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    @Column(name = "true_name")
    @NotEmpty(message = "不能为空")
    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }

    @Pattern(message = "数字、大小写字母和特殊字符组成,至少8位" , regexp = "(?=^.{8,}$)(?=.*[.~!@#$%^&*\\-+_=])(?![.\\n])(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9]).*$")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "create_time")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Column(name = "last_login_time")
    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    @NotEmpty(message = "不能为空")
    @Pattern(regexp = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*", message = "邮件格式不符合")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
}