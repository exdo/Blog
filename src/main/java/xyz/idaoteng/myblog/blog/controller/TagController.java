package xyz.idaoteng.myblog.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.idaoteng.myblog.blog.entity.vo.ArticleSummary;
import xyz.idaoteng.myblog.blog.service.TagService;
import xyz.idaoteng.myblog.common.dto.ResponseResult;

import java.util.List;

@RestController
@RequestMapping("/api/tag")
public class TagController {
    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping("/all")
    public ResponseResult allTags() {
        return new ResponseResult(200).setContent(tagService.getAllTags());
    }

    @PostMapping("/summaryWithTag/{tagName}/of/{index}")
    public ResponseResult getSummaryWithTag(@PathVariable("tagName") String tagName,
                                            @PathVariable("index") int index) {
        int total = tagService.getTotalWithTag(tagName);
        if (total == 0) {
            return new ResponseResult(432, "标签不存在");
        }
        List<ArticleSummary> summaries = tagService.getSummaryWithTag(tagName, index, total);
        if (summaries == null) {
            return new ResponseResult(432, "该标签没有对应的文章");
        }
        return new ResponseResult(200, String.valueOf(total)).setContent(summaries);
    }
}