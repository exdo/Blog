package xyz.idaoteng.myblog.blog.service;

import org.springframework.data.redis.core.RedisTemplate;
import xyz.idaoteng.myblog.blog.dao.ArticleMapper;

public class ArticleGeneralService {
    protected final String TOTAL = "total-articles";
    protected final String TOTAL_PUBLIC = "total-public-articles";
    protected final String TOTAL_ABANDONED = "total-abandoned-articles";
    public static final int PER_SIZE = 5;

    protected final RedisTemplate<String,Object> redisTemplate;
    protected final ArticleMapper articleMapper;

    public ArticleGeneralService(RedisTemplate<String, Object> redisTemplate, ArticleMapper articleMapper) {
        this.redisTemplate = redisTemplate;
        this.articleMapper = articleMapper;
        int total = articleMapper.getArticleCount();
        int totalPublic = articleMapper.getPublicArticleCount();
        int totalAbandoned = articleMapper.getAbandonedArticleCount();
        redisTemplate.opsForValue().set(TOTAL, total);
        redisTemplate.opsForValue().set(TOTAL_PUBLIC, totalPublic);
        redisTemplate.opsForValue().set(TOTAL_ABANDONED, totalAbandoned);
    }

    public Integer getStartIndex(int pageIndex, TotalType totalType) {
        Integer itemSize = null;
        switch (totalType) {
            case ALL_PUBLIC:
                itemSize = getTotalPublic();break;
            case ALL_NOT_ABANDONED:
                itemSize = getTotal();break;
            case ALL_ABANDONED:
                itemSize = getTotalAbandoned();break;
        }

        if (itemSize == null) {
            throw new RuntimeException("总数没能成功写入缓存或总数类型未定义");
        }
        int pageSize = (itemSize + PER_SIZE -1) / PER_SIZE;

        if (pageIndex <= 0 || pageIndex > pageSize) {
            return null;
        }

        return (pageIndex - 1) * PER_SIZE;
    }

    public static Integer getStartIndex(int pageIndex, int total) {
        int pageSize = (total + PER_SIZE -1) / PER_SIZE;

        if (pageIndex <= 0 || pageIndex > pageSize) {
            return null;
        }

        return (pageIndex - 1) * PER_SIZE;
    }

    public Integer getTotal() {
        return (Integer) redisTemplate.opsForValue().get(TOTAL);
    }

    public Integer getTotalPublic() {
        return (Integer) redisTemplate.opsForValue().get(TOTAL_PUBLIC);
    }

    public Integer getTotalAbandoned() {
        return (Integer) redisTemplate.opsForValue().get(TOTAL_ABANDONED);
    }
}
