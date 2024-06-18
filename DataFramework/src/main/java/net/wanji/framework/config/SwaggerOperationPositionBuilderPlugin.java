package net.wanji.framework.config;

import com.google.common.collect.Lists;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSort;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import springfox.documentation.service.StringVendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spi.service.contexts.OperationContext;

/**
 * @author: guanyuduo
 * @date: 2023/12/4 11:16
 * @descriptoin:
 */

@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 100)
public class SwaggerOperationPositionBuilderPlugin implements OperationBuilderPlugin {
    @Override
    public void apply(OperationContext context) {
        int position = Integer.MAX_VALUE;
        //首先查找ApiOperation注解
        java.util.Optional<ApiOperation> api = context.findAnnotation(ApiOperation.class);
        if (api.isPresent()) {
            //判断postion是否有值
            int posit = api.get().position();
            if (posit != 0) {
                position = posit;
            } else {
                java.util.Optional<ApiOperationSort> apiOperationSortOptional = context.findAnnotation(ApiOperationSort.class);
                if (apiOperationSortOptional.isPresent()) {
                    position = apiOperationSortOptional.get().value();
                }
            }
        } else {
            java.util.Optional<ApiOperationSort> apiOperationSortOptional = context.findAnnotation(ApiOperationSort.class);
            if (apiOperationSortOptional.isPresent()) {
                position = apiOperationSortOptional.get().value();
            }
        }

        context.operationBuilder().extensions(Lists.newArrayList(new StringVendorExtension("x-order", String.valueOf(position))));
    }

    @Override
    public boolean supports(DocumentationType delimiter) {
        return true;
    }

}
