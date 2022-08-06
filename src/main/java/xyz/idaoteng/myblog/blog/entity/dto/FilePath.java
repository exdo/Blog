package xyz.idaoteng.myblog.blog.entity.dto;

import lombok.Data;

import java.util.HashMap;

@Data
public class FilePath {
    private String[] errFiles;
    private HashMap<String, String> succMap;
}
