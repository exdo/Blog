package xyz.idaoteng.myblog.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xyz.idaoteng.myblog.blog.entity.dto.ArticleData;
import xyz.idaoteng.myblog.blog.entity.vo.ArticleCatalog;
import xyz.idaoteng.myblog.blog.entity.vo.ArticleContent;
import xyz.idaoteng.myblog.blog.service.ArticleAdminServiceImp;
import xyz.idaoteng.myblog.common.dto.ResponseResult;

import java.util.List;

@RestController
@RequestMapping("/api/admin/article")
public class ArticleAdminController {
    private final ArticleAdminServiceImp articleAdminService;

    @Autowired
    public ArticleAdminController(ArticleAdminServiceImp articleAdminService) {
        this.articleAdminService = articleAdminService;
    }

    @GetMapping("/id/{articleId}")
    public ResponseResult viewArticle(@PathVariable("articleId") String articleId) {
        ArticleContent content = articleAdminService.getArticleContent(articleId);
        if (content != null) {
            return new ResponseResult().setCode(200).setContent(content);
        } else {
            return new ResponseResult().setCode(404).setMessage("文章不存在或未公开");
        }
    }

    @GetMapping("/total")
    public ResponseResult getTotal() {
        int total = articleAdminService.getTotal();
        return new ResponseResult().setCode(200).setContent(total);
    }

    @GetMapping("/total/abandoned")
    public ResponseResult getTotalAbandoned() {
        int total = articleAdminService.getTotalAbandoned();
        return new ResponseResult(200).setContent(total);
    }

    @GetMapping("/catalog")
    public ResponseResult getPartArticleCatalogs(@RequestParam("pageIndex") int index) {
        List<ArticleCatalog> catalog = articleAdminService.getArticleCatalog(index);
        if (catalog != null) {
            return new ResponseResult().setCode(200).setContent(catalog);
        } else {
            return new ResponseResult().setCode(220).setMessage("没有更多了");
        }
    }

    @GetMapping("/summaryText")
    public ResponseResult getSummary(@RequestParam("articleId") String articleId) {
        String summary = articleAdminService.getSummaryText(articleId);
        if (summary == null) return new ResponseResult(404);
        return new ResponseResult(200).setContent(summary);
    }

    @GetMapping("/id")
    public ResponseResult getArticleId() {
        return new ResponseResult(200, articleAdminService.generateArticleId());
    }

    @PostMapping("/add")
    public ResponseResult addArticle(@RequestBody @Validated ArticleData articleData) {
        return articleAdminService.addArticle(articleData);
    }

    @GetMapping("/update/title")
    public ResponseResult updateTitle(@RequestParam(name = "title") String title, @RequestParam(name = "articleId") String articleId) {
        return articleAdminService.updateArticleTitle(articleId, title);
    }

    @GetMapping("/delete/tag")
    public ResponseResult deleteTag(@RequestParam(name = "title") String title, @RequestParam(name = "tag") String tag) {
        return articleAdminService.deleteTag(title, tag);
    }

    @GetMapping("/add/tag")
    public ResponseResult addTag(@RequestParam(name = "title") String title, @RequestParam(name = "tag") String tag) {
        return articleAdminService.addTag(title, tag);
    }

    @PostMapping("/update")
    public ResponseResult updateArticle(@RequestBody ArticleData data) {
        return articleAdminService.updateArticle(data);
    }

    @GetMapping("/delete")
    public ResponseResult deleteArticle(@RequestParam String articleId) {
        return articleAdminService.deleteArticle(articleId);
    }

    @GetMapping("/catalog/dropped")
    public ResponseResult getDroppedArticleCatalog(@RequestParam("pageIndex") int index) {
        List<ArticleCatalog> catalog = articleAdminService.getDroppedArticleCatalog(index);
        if (catalog != null) {
            return new ResponseResult().setCode(200).setContent(catalog);
        } else {
            return new ResponseResult().setCode(220).setMessage("没有更多了");
        }
    }

    @GetMapping("/drop")
    public ResponseResult dropArticle(@RequestParam String articleId) {
        return articleAdminService.updateAbandonedStatus(articleId, true);
    }

    @GetMapping("/recycle")
    public ResponseResult recycleArticle(@RequestParam String articleId) {
        return articleAdminService.updateAbandonedStatus(articleId, false);
    }
}
