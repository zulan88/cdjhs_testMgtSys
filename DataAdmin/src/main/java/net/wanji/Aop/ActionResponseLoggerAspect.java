package net.wanji.Aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.elasticsearch.action.ActionResponse;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : yangliang
 * @create 2023/1/3 13:49
 */
/*@Aspect
@Component
public class ActionResponseLoggerAspect {

    private static final Logger logger = LoggerFactory.getLogger(ActionResponseLoggerAspect.class);

    @AfterReturning(pointcut="execution(* net.wanji.web.controller.dataset.VehicleRsuInfoController.*(..)))", returning="result")
    public void afterReturning(JoinPoint joinPoint , Object result)  {
        if (result instanceof ActionResponse) {
            ActionResponse m = (ActionResponse) result;

            logger.info("ActionResponse returned with message [{}]", m.toString());
        }
    }
}*/
