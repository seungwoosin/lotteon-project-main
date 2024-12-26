package com.lotteon.service.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Log4j2
@Service
public class ImageService {

    @Value("${file.upload-dir}")
    private String uploadPath;


    @PostConstruct
    public void init() {
        // 파일 업로드 디렉토리가 존재하는지 확인하고 없으면 생성합니다
        Path path = Paths.get(uploadPath);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                throw new RuntimeException("Could not create upload directory", e);
            }
        }
    }


    private String upload(MultipartFile file) { //실질적 업로드 메서드
        log.info("file : " +file.toString());
        String oName = file.getOriginalFilename();
        log.info("oName : " + oName);
        String ext = oName.substring(oName.lastIndexOf("."));
        log.info("ext : " + ext);
        String sName = UUID.randomUUID() + ext;
        log.info("sName : " + sName);

        // 업로드 경로 설정
        Path path = Paths.get(uploadPath).resolve(sName);
        // 파일 저장
        try {
            Files.copy(file.getInputStream(), path);

            return sName;
        } catch (IOException e) {
            log.error(e);
        }
        return null;
    }

    public String uploadImage(MultipartFile file) { // 단일 이미지 업로드

        if (!file.isEmpty()) {
            return upload(file);
        }
        return null;
    }

    public List<String> uploadImages(List<MultipartFile> files) { // 여러 이미지 업로드

        // 첨부 파일 정보 객체 리스트 생성
        List<String> uploadedFiles = new ArrayList<>();

        for (MultipartFile file : files) {
            String path = null;
            if (!file.isEmpty()) {
                path = upload(file);
            }
            uploadedFiles.add(path);
        }
        log.info(uploadedFiles);
        return uploadedFiles;
    }
}
