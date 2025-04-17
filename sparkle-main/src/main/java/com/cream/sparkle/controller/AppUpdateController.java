package com.cream.sparkle.controller;

import com.cream.sparkle.global.error.RunErr;
import com.cream.sparkle.service.AppUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
            throw new RunErr("服务器错误");
        }
        return latestVersionNumber;
    }


    /**
     * todo Ret包装注意
     */
    @GetMapping("/downloadLatestVersion")
    public ResponseEntity<Resource> downloadLatestVersion(String versionNumber) {
        return this.appUpdateService.downloadLatestVersion(versionNumber);
    }
}
