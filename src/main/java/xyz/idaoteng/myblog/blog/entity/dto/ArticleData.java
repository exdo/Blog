package xyz.idaoteng.myblog.blog.entity.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;

@Data
public class ArticleData {
    @NotBlank
    private String classification;
    @NotNull
    private Boolean isPublic;
    @NotBlank
    private String title;
    @Length(max = 150)
    private String summary;
    @NotBlank
    private String content;

    private String articleId;
    private ArrayList<String> tags;
}
