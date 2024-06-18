package net.wanji.web.controller.business;

import io.swagger.annotations.Api;
import net.wanji.business.service.ScenarioService;
import net.wanji.common.config.WanjiConfig;
import net.wanji.common.core.domain.AjaxResult;
import net.wanji.common.utils.file.FileUploadUtils;
import net.wanji.common.utils.file.MimeTypeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @Auther: guanyuduo
 * @Date: 2023/9/11 13:48
 * @Descriptoin:
 */
@Api(tags = "OpenScenario")
@RestController
@RequestMapping("/scenario")
public class ScenarioController {

    @Autowired
    private ScenarioService scenarioService;

    @PostMapping("/upload")
    public AjaxResult uploadFile(@RequestParam("file") MultipartFile file) throws Exception {
        try {
            // 上传文件路径
            String filePath = WanjiConfig.getUploadPath();
            // 上传并返回新文件名称
            AjaxResult ajax = AjaxResult.success();
            ajax.put("originalFilename", file.getOriginalFilename());
            Map<String, String> map = FileUploadUtils.uploadScenario(filePath, file, MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION);
            ajax.putAll(map);
            ScenarioService.nameMap.put(file.getOriginalFilename(), map.get("xoscName"));
            return ajax;
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    @PostMapping("/start")
    public AjaxResult uploadFile(@RequestParam("fileName") String fileName) throws Exception {
        scenarioService.start(fileName);
        return AjaxResult.success();
    }
}
