package com.great.urlshorter.repositories;

import com.great.urlshorter.entities.Url;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Great
 */
@Repository
public interface UrlRepository extends JpaRepository<Url, Long> {

    /**
     * find by url
     *
     * @param url string
     * @return url
     */
    Url findByUrlHash(String url);

    /**
     * find by id
     *
     * @param id long
     * @return url
     */
    Url findById(long id);

    /**
     * 查询是否已存在
     *
     * @param url url
     * @return boolean
     */
    Boolean existsByUrlHash(String url);
}
