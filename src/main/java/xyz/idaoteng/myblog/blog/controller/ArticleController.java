package xyz.idaoteng.myblog.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.idaoteng.myblog.blog.entity.vo.ArticleContent;
import xyz.idaoteng.myblog.blog.entity.vo.ArticleSummary;
import xyz.idaoteng.myblog.blog.service.ArticleServiceImp;
import xyz.idaoteng.myblog.common.dto.ResponseResult;

import java.util.List;

@RestController
@RequestMapping("/api/article")
public class ArticleController {
    private final ArticleServiceImp articleService;

    @Autowired
    public ArticleController(ArticleServiceImp articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/id/{articleId}")
    public ResponseResult viewArticle(@PathVariable("articleId") String articleId) {
        ArticleContent content = articleService.getPublicArticleContent(articleId);
        if (content != null) {
            return new ResponseResult().setCode(200).setContent(content);
        } else {
            return new ResponseResult().setCode(404).setMessage("文章不存在或未公开");
        }
    }

    @GetMapping("/summary")
    public ResponseResult getPartSummary(@RequestParam("pageIndex") int index) {
        List<ArticleSummary> summaries = articleService.getArticleSummary(index);
        if (summaries != null) {
            return new ResponseResult().setCode(200).setContent(summaries);
        } else {
            return new ResponseResult().setCode(220).setMessage("没有更多了");
        }
    }

    @GetMapping("/previous")
    public ResponseResult getPreviousTitle(@RequestParam("articleId") String articleId) {
        ArticleSummary summary = articleService.getPrevious(articleId);
        if (summary == null) return new ResponseResult(404, "没有更多了");
        return new ResponseResult(200).setContent(summary);
    }

    @GetMapping("/next")
    public ResponseResult getNextTitle(@RequestParam("articleId") String articleId) {
        ArticleSummary summary = articleService.getNext(articleId);
        if (summary == null) return new ResponseResult(404, "没有更多了");
        return new ResponseResult(200).setContent(summary);
    }

    @GetMapping("/totalPublic")
    public ResponseResult getTotalArticle() {
        int total = articleService.getTotalPublic();
        return new ResponseResult().setCode(200).setContent(total);
    }
}