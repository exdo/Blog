package xyz.idaoteng.myblog.blog.entity.vo;

import lombok.Data;
import lombok.experimental.Accessors;
import xyz.idaoteng.myblog.common.po.PersistentObjects;

import java.util.ArrayList;


@Data
@Accessors(chain = true)
public class ArticleCatalog implements PersistentObjects {
    //文章ID
    private String articleId;
    //文章标题
    private String title;
    //是否公开
    private Boolean isPublic;
    //首次发布时间
    private String firstReleaseDate;
    //上次修改时间
    private String lastModifiedTime;
    //分类
    private String classification;
    //标签
    private ArrayList<String> tags;
}
