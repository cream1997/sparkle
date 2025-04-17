package com.cream.sparkle.service;

import com.cream.sparkle.global.error.RunErr;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AppUpdateService {

    @Value("${sparkle.publish-dir}")
    private String publishDir;
    private final Pattern versionMatchPattern = Pattern.compile("Sparkle-Windows-(\\d+\\.\\d+\\.\\d+)-Setup\\.exe");


    public String getLatestVersionNumber() {
        File folder = new File(publishDir);
        if (!folder.exists() || !folder.isDirectory()) {
            return null;
        }
        File[] files = folder.listFiles();
        if (files == null || files.length == 0) {
            return null;
        }
        return Arrays.stream(files)
                .map((file) -> getVersionNumber(file.getName()))
                .filter(Objects::nonNull)
                .max((version1, version2) -> {
                    String[] split1 = version1.split("\\.");
                    String[] split2 = version2.split("\\.");
                    for (int i = 0; i < split1.length; i++) {
                        int compare = Integer.compare(Integer.parseInt(split1[i]), Integer.parseInt(split2[i]));
                        if (compare != 0) {
                            return compare;
                        }
                    }
                    return 0;
                }).orElse(null);
    }


    private String getVersionNumber(String fileName) {
        Matcher matcher = versionMatchPattern.matcher(fileName);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    public ResponseEntity<Resource> downloadLatestVersion(String versionNumber) {
        FileSystemResource resource = getFileResource(versionNumber);
        if (resource != null && resource.exists()) {
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + resource.getFilename());
            try {
                return ResponseEntity.ok()
                        .headers(headers)
                        .contentLength(resource.contentLength())
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .body(resource);
            } catch (IOException e) {
                throw new RunErr("下载出错");
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private FileSystemResource getFileResource(String versionNumber) {
        // 根据versionNumber找出对应的安装包文件
        String fileName = "Sparkle-Windows-" + versionNumber + "-Setup.exe";
        Path path = Paths.get(publishDir).resolve(fileName).normalize();
        File file = path.toFile();
        if (file.exists()) {
            return new FileSystemResource(file);
        } else {
            return null;
        }
    }
}
