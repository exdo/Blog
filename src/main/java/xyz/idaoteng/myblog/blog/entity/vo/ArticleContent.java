package xyz.idaoteng.myblog.blog.entity.vo;

import lombok.Data;

@Data
public class ArticleContent {
    //文章ID
    private String articleId;
    //是否公开
    private Boolean isPublic;
    //文章内容
    private String content;
}
