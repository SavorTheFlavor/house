package com.me.house.biz.service;

import com.google.common.collect.Lists;
import com.google.common.io.Files;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.List;

/**
 * Created by Administrator on 2018/8/5.
 */
@Service
public class FileService {

    @Value("${file.path}")
    private String filePath;

    public List<String> saveFiles(List<MultipartFile> files){
        List<String> paths = Lists.newArrayList();
        files.forEach(file -> {
            if(!file.isEmpty()){
                File localFile = null;
                try{
                    localFile = saveToLocal(file, filePath);
                    // eg. filePath/20180722/a.jpg --> /20180722/a.jpg, relative path
                    String path = StringUtils.substringAfter(localFile.getAbsolutePath(), filePath);
                    paths.add(path);
                }catch (Exception e){
                    throw new IllegalArgumentException(e);
                }
            }
        });
        return paths;
    }

    private File saveToLocal(MultipartFile file, String filePath) throws IOException {
        File newFile = new File(filePath + "/" + Instant.now().getEpochSecond() + "/" + file.getOriginalFilename());
        if(!newFile.exists()){
            newFile.getParentFile().mkdirs();
            newFile.createNewFile();
        }
        Files.write(file.getBytes(), newFile);
        return newFile;
    }

}
