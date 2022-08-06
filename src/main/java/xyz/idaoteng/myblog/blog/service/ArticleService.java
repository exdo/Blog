package xyz.idaoteng.myblog.blog.service;

import xyz.idaoteng.myblog.blog.entity.vo.ArticleContent;
import xyz.idaoteng.myblog.blog.entity.vo.ArticleSummary;

import java.util.List;

public interface ArticleService {
    List<ArticleSummary> getArticleSummary(int pageIndex);

    ArticleContent getPublicArticleContent(String id);
}
