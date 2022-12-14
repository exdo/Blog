package xyz.idaoteng.myblog.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.idaoteng.auth.tools.RedisTemplateHolder;
import xyz.idaoteng.auth.tools.RedisUid;
import xyz.idaoteng.myblog.blog.dao.ArticleMapper;
import xyz.idaoteng.myblog.blog.dao.TagMapper;
import xyz.idaoteng.myblog.blog.entity.dto.ArticleData;
import xyz.idaoteng.myblog.blog.entity.po.Article;
import xyz.idaoteng.myblog.blog.entity.vo.ArticleCatalog;
import xyz.idaoteng.myblog.blog.entity.vo.ArticleContent;
import xyz.idaoteng.myblog.common.dto.ResponseResult;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ArticleAdminServiceImp extends ArticleGeneralService implements ArticleAdminService{
    private final RedisUid redisUid;
    private final TagMapper tagMapper;
    private final LastModifiedService lastModifiedService;

    @Autowired
    public ArticleAdminServiceImp(RedisTemplateHolder holder, ArticleMapper articleMapper, RedisUid redisUid, TagMapper tagMapper, LastModifiedService lastModifiedService) {
        super(holder.get(), articleMapper);
        this.redisUid = redisUid;
        this.tagMapper = tagMapper;
        this.lastModifiedService = lastModifiedService;
    }

    public ArticleContent getArticleContent(String id) {
        long articleId;
        try {
            articleId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            return null;
        }

        return articleMapper.selectArticleContent(articleId);
    }

    public String getSummaryText(String id) {
        long articleId;
        try {
            articleId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            return null;
        }

        return articleMapper.selectSummaryText(articleId);
    }

    @Override
    public List<ArticleCatalog> getArticleCatalog(int pageIndex) {
        Integer start = getStartIndex(pageIndex, TotalType.ALL_NOT_ABANDONED);
        if (start == null) {
            return null;
        }

        List<ArticleCatalog> catalogs = articleMapper.selectArticleCatalog(start, PER_SIZE);
        if (catalogs == null) {
            return null;
        }
        for (ArticleCatalog articleCatalog : catalogs) {
            ArrayList<String> tags = tagMapper.selectTags(articleCatalog.getTitle());
            articleCatalog.setTags(tags);
        }
        return catalogs;
    }

    public List<ArticleCatalog> getDroppedArticleCatalog(int pageIndex) {
        Integer start = getStartIndex(pageIndex, TotalType.ALL_ABANDONED);
        if (start == null) {
            return null;
        }

        List<ArticleCatalog> catalogs = articleMapper.selectDroppedArticleCatalog(start, PER_SIZE);
        if (catalogs == null) {
            return null;
        }
        for (ArticleCatalog articleCatalog : catalogs) {
            ArrayList<String> tags = tagMapper.selectTags(articleCatalog.getTitle());
            articleCatalog.setTags(tags);
        }
        return catalogs;
    }

    public String generateArticleId() {
        return redisUid.nextId();
    }

    @Override
    public ResponseResult addArticle(ArticleData data) {
        String existsTitle = articleMapper.selectTitle(data.getTitle());
        if (existsTitle != null) {
            return new ResponseResult(203, "????????????????????????????????????");
        }
        //????????????
        Article article = new Article();
        if (data.getArticleId() == null) {
            article.setArticleId(Long.parseLong(redisUid.nextId()));
        } else {
            article.setArticleId(Long.parseLong(data.getArticleId()));
        }
        article.setAbandoned(false);
        article.setClassification(data.getClassification());
        article.setContent(data.getContent());
        article.setIsPublic(data.getIsPublic());
        article.setSummary(data.getSummary());
        article.setTitle(data.getTitle());
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        article.setFirstReleaseDate(date);
        article.setLastModifiedTime(date);
        int count = articleMapper.insert(article);

        if (count == 1) {
            //????????????
            ArrayList<String> tags = data.getTags();
            if (tags != null && !tags.isEmpty()) {
                tagMapper.insertTags(article.getTitle(), tags);
            }
            redisTemplate.opsForValue().increment(TOTAL, 1);
            if (data.getIsPublic()) {
                redisTemplate.opsForValue().increment(TOTAL_PUBLIC, 1);
            }
            lastModifiedService.updateLastModifiedTime();
            return new ResponseResult(200, "????????????");
        } else {
            throw new RuntimeException("??????????????????");
        }
    }

    public ResponseResult updateArticleTitle(String articleId, String newTitle) {
        String existsTitle = articleMapper.selectTitle(newTitle);
        if (existsTitle != null) {
            return new ResponseResult(203, "????????????????????????????????????");
        } else {
            long id;
            try {
                id = Long.parseLong(articleId);
            } catch (Exception e) {
                return new ResponseResult(403, "??????ID??????");
            }
            int count = articleMapper.updateArticleTitle(id, newTitle);
            if (count == 1) {
                lastModifiedService.updateLastModifiedTime();
                return new ResponseResult(200, "?????????????????????");
            } else {
                return new ResponseResult(204, "????????????????????????????????????");
            }
        }
    }

    public ResponseResult updateArticle(ArticleData articleData) {
        if (articleData.getArticleId() == null) {
            return new ResponseResult(403, "??????????????????ID");
        }
        Article article = new Article();
        long id;
        try {
            id = Long.parseLong(articleData.getArticleId());
        } catch (Exception e) {
            return new ResponseResult(403, "??????ID??????");
        }
        article.setArticleId(id);
        article.setTitle(articleData.getTitle());
        article.setContent(articleData.getContent());
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        article.setLastModifiedTime(date);
        article.setSummary(articleData.getSummary());
        article.setClassification(articleData.getClassification());
        article.setIsPublic(articleData.getIsPublic());
        int count = articleMapper.updateById(article);
        if (count == 1) {
            lastModifiedService.updateLastModifiedTime();
            return new ResponseResult(200, "??????????????????");
        } else {
            throw new RuntimeException("??????????????????");
        }
    }

    public ResponseResult deleteTag(String title, String tag) {
        int count = tagMapper.deleteTag(title, tag);
        if (count == 1) {
            lastModifiedService.updateLastModifiedTime();
            return new ResponseResult(200, "???????????????");
        } else {
            throw new RuntimeException("??????????????????");
        }
    }

    public ResponseResult addTag(String title, String tag) {
        int count = tagMapper.insertTags(title, Collections.singletonList(tag));
        if (count == 1) {
            lastModifiedService.updateLastModifiedTime();
            return new ResponseResult(200, "???????????????");
        } else {
            throw new RuntimeException("??????????????????");
        }
    }

    public ResponseResult deleteArticle(String articleId) {
        long id;
        try {
            id = Long.parseLong(articleId);
        } catch (Exception e) {
            return new ResponseResult(403, "??????ID??????");
        }
        boolean isPublic = articleMapper.selectIsPublic(id);
        int count = articleMapper.deleteById(id);
        if (count == 1) {
            redisTemplate.opsForValue().increment(TOTAL, -1);
            redisTemplate.opsForValue().increment(TOTAL_ABANDONED, -1);
            if (isPublic) {
                redisTemplate.opsForValue().increment(TOTAL_PUBLIC, -1);
            }
            lastModifiedService.updateLastModifiedTime();
            return new ResponseResult(200, "???????????????");
        } else {
            throw new RuntimeException("??????????????????");
        }
    }

    public ResponseResult updateAbandonedStatus(String articleId, boolean drop) {
        long id;
        try {
            id = Long.parseLong(articleId);
        } catch (Exception e) {
            return new ResponseResult(403, "??????ID??????");
        }

        int count = articleMapper.updateAbandonedStatus(id, drop);
        boolean isPublic = articleMapper.selectIsPublic(id);
        if (count == 1 && drop) {
            lastModifiedService.updateLastModifiedTime();

            redisTemplate.opsForValue().increment(TOTAL, -1);
            redisTemplate.opsForValue().increment(TOTAL_ABANDONED, 1);
            if (isPublic) {
                redisTemplate.opsForValue().increment(TOTAL_PUBLIC, -1);
            }

            return new ResponseResult(200, "????????????????????????");
        } else if (count == 1) {
            lastModifiedService.updateLastModifiedTime();

            redisTemplate.opsForValue().increment(TOTAL, 1);
            redisTemplate.opsForValue().increment(TOTAL_ABANDONED, -1);
            if (isPublic) {
                redisTemplate.opsForValue().increment(TOTAL_PUBLIC, 1);
            }

            return new ResponseResult(200, "???????????????");
        } else {
            throw new RuntimeException("???????????????????????????");
        }
    }
}
