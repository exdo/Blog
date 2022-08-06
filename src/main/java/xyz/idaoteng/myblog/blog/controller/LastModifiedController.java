package xyz.idaoteng.myblog.blog.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xyz.idaoteng.myblog.blog.service.LastModifiedService;
import xyz.idaoteng.myblog.common.dto.ResponseResult;

@RestController
public class LastModifiedController {
    private final LastModifiedService lastModifiedService;

    public LastModifiedController(LastModifiedService lastModifiedService) {
        this.lastModifiedService = lastModifiedService;
    }

    @GetMapping("/api/hasNew")
    public ResponseResult hasNew(@RequestParam long lastRequestTime) {
        long hasNew = lastModifiedService.getLastModifiedTime() - lastRequestTime;
        if (hasNew > 0) {
            return new ResponseResult(200).setContent(1);
        } else {
            return new ResponseResult(200).setContent(0);
        }
    }
}