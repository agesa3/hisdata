package com.agesadev.hisdata.hisdata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@RestController
public class ExportController {

    @Autowired
    private ExportService exportService;

    @GetMapping("/export")
    public ResponseEntity<String> exportToCSV() {
        File csvFile = exportService.exportToCSV();
        if (csvFile != null) {
            String fileUrl = csvFile.getAbsolutePath();
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + csvFile.getName() + "\"")
                    .body(fileUrl);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to generate CSV file");
        }
    }
}