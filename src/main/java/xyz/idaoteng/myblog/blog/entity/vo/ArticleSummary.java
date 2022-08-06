package xyz.idaoteng.myblog.blog.entity.vo;

import lombok.Data;

import java.util.ArrayList;

@Data
public class ArticleSummary {
    //文章ID
    private String articleId;
    //文章标题
    private String title;
    //文章内容摘要
    private String summary;
    //首次发布时间
    private String firstReleaseDate;
    //上次修改时间
    private String lastModifiedTime;
    //分类
    private String classification;
    //标签
    private ArrayList<String> tags;
}
