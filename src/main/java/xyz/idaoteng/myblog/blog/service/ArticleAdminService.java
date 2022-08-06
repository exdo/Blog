package xyz.idaoteng.myblog.blog.service;

import xyz.idaoteng.myblog.blog.entity.dto.ArticleData;
import xyz.idaoteng.myblog.blog.entity.vo.ArticleCatalog;
import xyz.idaoteng.myblog.common.dto.ResponseResult;

import java.util.List;

public interface ArticleAdminService {
    List<ArticleCatalog> getArticleCatalog(int pageIndex);

    ResponseResult addArticle(ArticleData articleData);
}
