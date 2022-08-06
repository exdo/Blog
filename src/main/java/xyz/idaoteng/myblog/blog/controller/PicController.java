package xyz.idaoteng.myblog.blog.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import xyz.idaoteng.myblog.blog.entity.dto.FilePath;
import xyz.idaoteng.myblog.blog.entity.dto.ResponseForVditorUpload;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

@RestController
public class PicController {
    private String picFilePath;

    //网站根路径（前端用Nginx做web服务器）
    @Value("${website-root-path:}")
    public void setPicFilePath(String path) {
        if ("".equals(path)) {
            throw new RuntimeException("没有配置网站根路径");
        } else {
            char separatorChar = File.separatorChar;
            //为文件路径末尾添加 路径分隔符+pic
            char lastCharInPath = path.charAt(path.length() -1);
            if (separatorChar != lastCharInPath) {
                path = path + separatorChar + "pic";
            } else {
                path = path + "pic";
            }
            this.picFilePath = path;
            //网站根路径下创建文件夹pic
            File baseDir = new File(path);
            boolean mkdirSuccess;
            if (baseDir.exists()) {
                mkdirSuccess = true;
            } else {
                mkdirSuccess = baseDir.mkdirs();
            }
            if (!mkdirSuccess) {
                throw new RuntimeException("未能成功创建文件夹");
            }
        }
    }

    @PostMapping(value = "/api/admin/article/pic", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseForVditorUpload addPicInArticle(@RequestPart("pic") MultipartFile picFile) {
        ResponseForVditorUpload res = new ResponseForVditorUpload();
        String fileName =  picFile.getOriginalFilename();
        if (fileName == null) {
            res.setCode(401);
            res.setMsg("上传失败");
            return res;
        }
        File file = new File(picFilePath, fileName);

        try {
            picFile.transferTo(file);
        } catch (IOException e) {
            e.printStackTrace();
            res.setCode(401);
            res.setMsg("上传失败");
            FilePath filePath = new FilePath();
            filePath.setErrFiles(new String[] {fileName});
            res.setData(filePath);
            return res;
        }
        res.setCode(200);
        res.setMsg("上传成功");
        FilePath filePath = new FilePath();
        HashMap<String, String> suss = new HashMap<>(1);
        //返回图片路径
        String PIC_HTTP_PATH = "/pic/";
        String picPath = PIC_HTTP_PATH + fileName;
        suss.put(fileName, picPath);
        filePath.setSuccMap(suss);
        res.setData(filePath);
        return res;
    }
}