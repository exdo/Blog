package xyz.idaoteng.myblog.blog.entity.dto;

import lombok.Data;

@Data
public class ResponseForVditorUpload {
    private String msg;
    private int code;
    private FilePath data;
}
