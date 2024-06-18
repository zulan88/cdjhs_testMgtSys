package net.wanji.business.annotion;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @author hcy
 * @version 1.0
 * @annotationTypeName EvaluationProcessor
 * @description TODO
 * @date 2024/3/22 14:39
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface EvaluationProcessor {
  String value();
}
