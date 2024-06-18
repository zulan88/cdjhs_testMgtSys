package net.wanji.business.component;

import net.wanji.business.annotion.DeviceReport;
import net.wanji.business.service.DeviceReportService;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Map;

/**
 * @author glace
 * @version 1.0
 * @className DeviceReportFactory
 * @description TODO
 * @date 2023/10/7 10:29
 **/
@Component
public class DeviceReportFactory {
  private static Map<String, Object> builderMap;

  @Resource
  private ApplicationContext applicationContext;

  @PostConstruct
  private void initBuilderMap() {
    builderMap = applicationContext.getBeansWithAnnotation(DeviceReport.class);
  }

  public <T extends DeviceReportService<?>> T create(Integer type) {
    Object o = builderMap.get(String.valueOf(type));
    if (null == o) {
      throw new RuntimeException(
          String.format("Not support such type :[%d]", type));
    }
    return (T) o;
  }

}
