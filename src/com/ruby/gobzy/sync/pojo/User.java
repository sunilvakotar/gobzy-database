package com.ruby.gobzy.sync.pojo;

/**
 * Created with IntelliJ IDEA.
 * User: Sunil Vakotar
 * Date: 12/26/13
 * Time: 4:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class User {
    private Integer id;
    private String username;
    private String password;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
