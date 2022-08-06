package xyz.idaoteng.myblog.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.idaoteng.myblog.blog.dao.ArticleMapper;
import xyz.idaoteng.myblog.blog.entity.vo.ArticleSummary;
import xyz.idaoteng.myblog.blog.service.ArticleGeneralService;
import xyz.idaoteng.myblog.common.dto.ResponseResult;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {
    private final ArticleMapper articleMapper;

    @Autowired
    public CategoryController(ArticleMapper articleMapper) {
        this.articleMapper = articleMapper;
    }

    @GetMapping("/all")
    public ResponseResult getAllCategory() {
        List<String> categories = articleMapper.selectAllCategory();
        if (categories == null) {
            return new ResponseResult(432, "没有任何分类");
        } else {
            return new ResponseResult(200).setContent(categories);
        }
    }

    @PostMapping("/summaryWithCategory/{categoryName}/of/{index}")
    public ResponseResult getSummaryWithCategory(@PathVariable("categoryName") String categoryName,
                                                 @PathVariable("index") int index) {
        int total = articleMapper.selectTotalCategories(categoryName);
        if (total == 0) {
            return new ResponseResult(432, "分类不存在");
        }
        Integer start = ArticleGeneralService.getStartIndex(index, total);
        if (start == null) {
            return new ResponseResult(432, "该分类没有对应的文章");
        } else {
            List<String> titles = articleMapper.selectTitlesWithCategory(categoryName, start, ArticleGeneralService.PER_SIZE);
            List<ArticleSummary> summaries = articleMapper.selectSummaryWithTitle(titles);
            return new ResponseResult(200, String.valueOf(total)).setContent(summaries);
        }
    }
}