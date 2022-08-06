package xyz.idaoteng.myblog.blog.entity.po;

import lombok.Data;

import java.util.ArrayList;

@Data
public class ArticleTag {
    //文章标题
    private String title;
    //文章标签
    private ArrayList<String> tags;
}
