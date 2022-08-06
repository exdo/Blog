package xyz.idaoteng.myblog.blog.entity.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import xyz.idaoteng.myblog.common.po.PersistentObjects;

@Data
@TableName("article")
//文章表
public class Article implements PersistentObjects {
    //文章ID
    @TableId
    private Long articleId;
    //首次发布时间
    private String firstReleaseDate;
    //上次修改时间
    private String lastModifiedTime;
    //分类
    private String classification;
    //文章标题
    private String title;
    //文章内容摘要
    private String summary;
    //文章内容
    private String content;
    //是否公开
    private Boolean isPublic;
    //是否已经丢入回收站
    private Boolean abandoned;
}
