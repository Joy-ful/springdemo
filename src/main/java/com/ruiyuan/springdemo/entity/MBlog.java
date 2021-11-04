package com.ruiyuan.springdemo.entity;

import java.util.Date;
import java.io.Serializable;

/**
 * (MBlog)实体类
 *
 * @author makejava
 * @since 2021-06-22 15:50:02
 */
public class MBlog implements Serializable {
    private static final long serialVersionUID = -40290405433559821L;

    private Long id;

    private Long userId;

    private String title;

    private String description;

    private String content;

    private Date created;

    private Integer status;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}
