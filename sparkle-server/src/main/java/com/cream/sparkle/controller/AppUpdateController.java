package com.cream.sparkle.controller;

import com.cream.sparkle.service.AppUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class AppUpdateController {

    private final AppUpdateService appUpdateService;

    @Autowired
    public AppUpdateController(AppUpdateService appUpdateService) {
        this.appUpdateService = appUpdateService;
    }

    @GetMapping("/latestVersionNumber")
    public String latestVersionNumber() {
        String latestVersionNumber = this.appUpdateService.getLatestVersionNumber();
        if (latestVersionNumber == null) {
            // todo 自定义项目异常，以及全局异常处理
            throw new RuntimeException("No latest version number found");
        }
        return latestVersionNumber;
    }


    /**
     * todo Ret包装注意
     */
    @GetMapping("/downloadVersion")
    public ResponseEntity<Resource> downloadLatestVersion(String versionNumber) throws IOException {
        return this.appUpdateService.downloadLatestVersion(versionNumber);
    }
}
