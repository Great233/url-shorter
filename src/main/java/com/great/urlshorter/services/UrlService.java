package com.great.urlshorter.services;

import com.great.urlshorter.entities.Url;
import org.springframework.stereotype.Service;

/**
 * @author Great
 */
public interface UrlService {

    /**
     * 通过62进制id获取url
     *
     * @param id id
     * @return Url
     */
    Url getByEncodedId(String id);

    /**
     * 通过原始 URL 获取 Url 对象
     *
     * @param url 原始 url
     * @return Url
     */
    Url getByUrl(String url);

    /**
     * 保存 url
     *
     * @param url url
     * @return 62进制id
     */
    String save(Url url);
}
