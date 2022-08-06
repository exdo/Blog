package xyz.idaoteng.myblog.blog.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface TagMapper {
    int insertTags(@Param("title") String title, @Param("tagList") List<String> tags);

    @Select("select tag_name from article_tag where title_name=#{title}")
    ArrayList<String> selectTags(@Param("title") String title);

    @Delete("delete from article_tag where title_name=#{title} and tag_name=#{tag}")
    int deleteTag(@Param("title") String title, @Param("tag") String tag);

    @Select("select count(title_name) from article_tag where tag_name=#{tagName}")
    int selectTotalWithTag(@Param("tagName") String tagName);

    List<String> selectTitlesWithTag(@Param("tagName") String tagName,
                                     @Param("start") int start,
                                     @Param("size") int size);

    List<String> selectAllTags();
}