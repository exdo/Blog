package xyz.idaoteng.myblog.blog.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import xyz.idaoteng.myblog.blog.entity.po.Article;
import xyz.idaoteng.myblog.blog.entity.vo.ArticleCatalog;
import xyz.idaoteng.myblog.blog.entity.vo.ArticleContent;
import xyz.idaoteng.myblog.blog.entity.vo.ArticleSummary;

import java.util.List;

@Mapper
public interface ArticleMapper extends BaseMapper<Article> {
    @Select("select count(*) from article where abandoned=false")
    int getArticleCount();

    @Select("select count(*) from article where is_public=true and abandoned=false")
    int getPublicArticleCount();

    @Select("select count(*) from article where abandoned=true")
    int getAbandonedArticleCount();

    @Select("select title from article where title=#{title}")
    String selectTitle(String title);

    @Select("select summary from article where article_id=#{id}")
    String selectSummaryText(long id);

    @Select("select article_id from article where article_id>#{id} order by article_id limit 1")
    Long selectPreviousId(long id);

    @Select("select article_id from article where article_id<#{id} order by article_id desc limit 1")
    Long selectNextId(long id);

    @Select("select is_public from article where article_id=#{id}")
    Boolean selectIsPublic(long id);

    @Update("update article set title=#{title} where article_id=#{articleId}")
    int updateArticleTitle(@Param("articleId") long articleId, @Param("title") String newTitle);

    @Update("update article set abandoned=#{abandoned} where article_id=#{id}")
    int updateAbandonedStatus(@Param("id") long id, @Param("abandoned") boolean abandoned);

    List<ArticleCatalog> selectArticleCatalog(@Param("start") int start, @Param("count") int count);

    List<ArticleCatalog> selectDroppedArticleCatalog(@Param("start") int start, @Param("count") int count);

    List<ArticleSummary> selectArticleSummaries(@Param("start") int start, @Param("count") int count);

    List<ArticleSummary> selectSummaryWithTitle(@Param("titles") List<String> titles);

    ArticleContent selectArticleContent(@Param("id") long id);

    @Select("select classification from article where is_public=true group by classification")
    List<String> selectAllCategory();

    @Select("select count(classification) from article where classification=#{categoryName} and is_public=true")
    int selectTotalCategories(@Param("categoryName") String categoryName);

    @Select("select title from article where classification=#{categoryName} and is_public=true limit #{start},#{size}")
    List<String> selectTitlesWithCategory(@Param("categoryName") String categoryName,
                                     @Param("start") int start,
                                     @Param("size") int size);

    ArticleSummary selectArticleSummaryById(long id);
}