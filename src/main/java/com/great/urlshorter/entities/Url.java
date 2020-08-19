package com.great.urlshorter.entities;


import org.springframework.util.DigestUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @author Great
 */
@Entity
public class Url implements Serializable {

    @Id
    @Min(value = 1, message = "服务器错误，请稍后重试")
    private long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    @NotEmpty(message = "请输入要压缩的url")
    private String url;

    @Column(name = "url_hash", columnDefinition = "CHAR", length = 32)
    private String urlHash;

    @Column(name = "created_at", length = 10)
    private int createdAt;

    public Url() {
    }

    public Url(long id, String url) {
        this.id = id;
        this.url = url;
        this.urlHash = DigestUtils.md5DigestAsHex(url.getBytes());
        this.createdAt = (int) (System.currentTimeMillis() / 1000L);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
        this.setUrlHash(DigestUtils.md5DigestAsHex(url.getBytes()));
    }

    public String getUrlHash() {
        return urlHash;
    }

    public void setUrlHash(String urlHash) {
        this.urlHash = urlHash;
    }

    public int getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(int createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Url{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
