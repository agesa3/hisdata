package com.agesadev.hisdata.hisdata;

import com.opencsv.CSVWriter;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

@Service
public class ExportService {

    @Value("${api.url}")
    private String apiUrl;

    @Value("${api.username}")
    private String apiUsername;

    @Value("${api.password}")
    private String apiPassword;

    @Value("${csv.file.path}")
    private String csvFilePath;

    public File exportToCSV() {
        HttpHeaders headers = new HttpHeaders();
        String auth = apiUsername + ":" + apiPassword;
        String authHeader = "Basic " + Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
        headers.set("Authorization", authHeader);

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<List> response = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, List.class);

        try {
            File csvFile = new File(csvFilePath);
            CSVWriter csvWriter = new CSVWriter(new OutputStreamWriter(FileUtils.openOutputStream(csvFile), StandardCharsets.UTF_8));

            List<String> headers = response.getBody().get(0);
            csvWriter.writeNext(headers.toArray(new String[0]));

            for (int i = 1; i < response.getBody().size(); i++) {
                List<String> row = response.getBody().get(i);
                csvWriter.writeNext(row.toArray(new String[0]));
            }

            csvWriter.flush();
            csvWriter.close();
            return csvFile;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}