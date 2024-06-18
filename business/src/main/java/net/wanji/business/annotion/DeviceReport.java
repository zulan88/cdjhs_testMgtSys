package net.wanji.business.annotion;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @author glace
 * @version 1.0
 * @annotationTypeName DeviceReport
 * @description TODO
 * @date 2023/10/7 10:30
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface DeviceReport {
  String value();
}
