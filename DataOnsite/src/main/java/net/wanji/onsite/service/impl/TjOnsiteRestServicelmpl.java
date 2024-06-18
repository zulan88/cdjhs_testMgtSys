package net.wanji.onsite.service.impl;

import com.alibaba.fastjson.JSONObject;
import net.wanji.common.config.WanjiConfig;
import net.wanji.common.utils.DateUtils;
import net.wanji.onsite.service.TjOnsiteRestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Service
public class TjOnsiteRestServicelmpl implements TjOnsiteRestService {

    private static final Logger log = LoggerFactory.getLogger("DataOnsite");

    @Autowired
    private RestTemplate restTemplate;

    @Value("${onsite.url}")
    private String onsiteUrl;

    @Override
    public boolean upLodeFile(List<File> filesToUpload, String dictory) {
        String url = onsiteUrl+"/upload?path="+dictory;

        log.info("============================== onsiteUploadUrl：{}", url);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        for (File file : filesToUpload) {
            body.add("files", new FileSystemResource(file));
        }
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> response =
                restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
        return response.getStatusCodeValue() == 200;
    }

    @Override
    public Boolean routePlanOnsite(String task) {
        String url = onsiteUrl+"/routestart?task="+task;

        log.info("============================== onsiteRouteStartUrl：{}", url);
        ResponseEntity<String> response =
                restTemplate.exchange(url, HttpMethod.GET, null, String.class);
        if (response.getStatusCodeValue() == 200) {
            JSONObject result = JSONObject.parseObject(response.getBody(), JSONObject.class);
            if (Objects.isNull(result) || result.get("code") == null) {
                log.error("远程服务调用失败:{}", result.get("msg"));
                return false;
            }else {
                return true;
            }
        }
        return false;
    }

    @Override
    public void getOnsiteTrace(String task, String savePath) {
        String fileUrl = onsiteUrl+"/download/"+task;
        log.info("============================== onsiteTraceUrl：{}", fileUrl);
        ResponseEntity<byte[]> response = restTemplate.exchange(
                fileUrl,
                HttpMethod.GET,
                null,
                byte[].class
        );
        byte[] fileContent = response.getBody();

        try (FileOutputStream stream = new FileOutputStream(savePath)) {
            assert fileContent != null;
            stream.write(fileContent);
            log.info("File downloaded successfully to: " + savePath);
        } catch (IOException e) {
            log.info("Error while saving the file: " + e.getMessage());
        }
    }

}
