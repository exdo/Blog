package xyz.idaoteng.myblog.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.idaoteng.myblog.blog.dao.ArticleMapper;
import xyz.idaoteng.myblog.blog.dao.TagMapper;
import xyz.idaoteng.myblog.blog.entity.vo.ArticleSummary;

import java.util.List;

@Service
public class TagService {
    private final TagMapper tagMapper;
    private final ArticleMapper articleMapper;

    @Autowired
    public TagService(TagMapper tagMapper, ArticleMapper articleMapper) {
        this.tagMapper = tagMapper;
        this.articleMapper = articleMapper;
    }

    public List<String> getAllTags() {
        return tagMapper.selectAllTags();
    }

    public int getTotalWithTag(String tagName) {
        return tagMapper.selectTotalWithTag(tagName);
    }

    public List<ArticleSummary> getSummaryWithTag(String tagName, int index, int total) {
        Integer start = ArticleGeneralService.getStartIndex(index, total);
        if (start == null) {
            return null;
        }
        List<String>  titles = tagMapper.selectTitlesWithTag(tagName, start, ArticleGeneralService.PER_SIZE);
        return articleMapper.selectSummaryWithTitle(titles);
    }
}
