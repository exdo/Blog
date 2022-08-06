package xyz.idaoteng.myblog.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.idaoteng.auth.tools.RedisTemplateHolder;
import xyz.idaoteng.myblog.blog.dao.ArticleMapper;
import xyz.idaoteng.myblog.blog.entity.vo.ArticleContent;
import xyz.idaoteng.myblog.blog.entity.vo.ArticleSummary;

import java.util.List;

@Service
public class ArticleServiceImp extends ArticleGeneralService implements ArticleService{

    @Autowired
    public ArticleServiceImp(RedisTemplateHolder holder, ArticleMapper articleMapper) {
        super(holder.get(), articleMapper);
    }

    @Override
    public List<ArticleSummary> getArticleSummary(int pageIndex) {
        Integer start = getStartIndex(pageIndex, TotalType.ALL_PUBLIC);
        if (start == null) {
            return null;
        }

        return articleMapper.selectArticleSummaries(start, PER_SIZE);
    }

    public ArticleContent getPublicArticleContent(String id) {
        long articleId;
        try {
            articleId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            return null;
        }

        ArticleContent content = articleMapper.selectArticleContent(articleId);
        if (content == null || !content.getIsPublic()) {
            return null;
        }
        return content;
    }

    public ArticleSummary getPrevious(String id) {
        long articleId;
        try {
            articleId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            return null;
        }

        Long previousId = articleMapper.selectPreviousId(articleId);

        if (previousId == null) {
            return null;
        } else {
            return articleMapper.selectArticleSummaryById(previousId);
        }
    }

    public ArticleSummary getNext(String id) {
        long articleId;
        try {
            articleId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            return null;
        }

        Long nextId = articleMapper.selectNextId(articleId);

        if (nextId == null) {
            return null;
        } else {
            return articleMapper.selectArticleSummaryById(nextId);
        }
    }

}
