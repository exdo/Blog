package xyz.idaoteng.myblog.blog.service;

import org.springframework.stereotype.Service;

@Service
public class LastModifiedService {
    private long lastModifiedTime = 0L;

    public void updateLastModifiedTime() {
        this.lastModifiedTime = System.currentTimeMillis();
    }

    public long getLastModifiedTime() {
        return this.lastModifiedTime;
    }
}
