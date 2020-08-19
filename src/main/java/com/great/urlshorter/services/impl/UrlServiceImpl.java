package com.great.urlshorter.services.impl;

import com.great.urlshorter.entities.Url;
import com.great.urlshorter.repositories.UrlRepository;
import com.great.urlshorter.services.UrlService;
import com.great.urlshorter.utils.Base62;
import com.great.urlshorter.utils.SnowFlake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author Great
 */
@Service("UrlService")
public class UrlServiceImpl implements UrlService {

    @Resource
    private UrlRepository urlRepository;

    private final SnowFlake snowFlake;

    private final RedisTemplate<String, Object> redisTemplate;

    private final long DEFAULT_TIMEOUT = 60 * 60;

    @Autowired
    public UrlServiceImpl(RedisTemplate<String, Object> redisTemplate,
                          SnowFlake snowFlake) {
        this.redisTemplate = redisTemplate;
        this.snowFlake = snowFlake;
    }

    @Override
    public Url getByEncodedId(String id) {
        long realId = getOriginId(id);
        Url url = getFromRedis(realId);
        if (url != null) {
            return url;
        }
        url = urlRepository.findById(realId);
        if (url != null) {
            saveToRedis(url);
        }
        return url;
    }

    private long getOriginId(String id) {
        return Base62.to10(id);
    }

    private String getEncodedId(long id) {
        return Base62.to62(id);
    }

    protected Url getFromRedis(long id) {
        if (redisTemplate.hasKey(String.valueOf(id))) {
            extend(id);
        }
        return (Url) redisTemplate.opsForValue().get(String.valueOf(id));
    }

    protected void saveToRedis(Url url) {
        redisTemplate.opsForValue().set(String.valueOf(url.getId()),
                url,
                DEFAULT_TIMEOUT,
                TimeUnit.SECONDS
        );
    }

    public void extend(long id) {
        redisTemplate.expire(String.valueOf(id), DEFAULT_TIMEOUT, TimeUnit.SECONDS);
    }

    @Override
    public Url getByUrl(String url) {
        return urlRepository.findByUrlHash(DigestUtils.md5DigestAsHex(url.getBytes()));
    }

    public Boolean exists(Url url) {
        return urlRepository.existsByUrlHash(DigestUtils.md5DigestAsHex(url.getUrl().getBytes()));
    }

    @Override
    public String save(Url url) {
        if (exists(url)) {
            Url urlObject = getByUrl(url.getUrl());
            return getEncodedId(urlObject.getId());
        }
        url.setId(snowFlake.nextId());
        url.setCreatedAt((int) (System.currentTimeMillis() / 1000));
        urlRepository.save(url);
        saveToRedis(url);
        return getEncodedId(url.getId());
    }
}
