package net.wanji.framework.config;

import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.Swagger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.BuilderDefaults;
import springfox.documentation.service.ApiDescription;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiListing;
import springfox.documentation.service.Documentation;
import springfox.documentation.service.ModelNamesRegistry;
import springfox.documentation.service.ResourceListing;
import springfox.documentation.swagger2.mappers.ModelSpecificationMapperImpl;
import springfox.documentation.swagger2.mappers.SecurityMapper;
import springfox.documentation.swagger2.mappers.ServiceModelToSwagger2MapperImpl;
import springfox.documentation.swagger2.mappers.VendorExtensionsMapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.alibaba.excel.util.MapUtils.newTreeMap;

/**
 * @author {@link ServiceModelToSwagger2MapperImpl
 * the original author}
 * @author Yahuan Jin
 * @see ServiceModelToSwagger2MapperImpl
 * @since 2019.08.24
 */
@Component
public class SwaggerOriginalSortedServiceModelToSwagger2MapperImpl extends ServiceModelToSwagger2MapperImpl {

    @Autowired
    private ModelSpecificationMapperImpl modelSpecificationMapper;
    @Autowired
    private SecurityMapper securityMapper;

    @Autowired
    private VendorExtensionsMapper vendorExtensionsMapper;

    @Override
    public Swagger mapDocumentation(Documentation from) {
        if (from == null) {
            return null;
        }

        Swagger swagger = new Swagger();


        swagger.setVendorExtensions(vendorExtensionsMapper.mapExtensions(from.getVendorExtensions()));
        swagger.setSchemes(mapSchemes(from.getSchemes()));
        //path转换
        swagger.setPaths(mapApiListings(from.getApiListings()));
        swagger.setHost(from.getHost());
        swagger.setDefinitions(modelSpecificationMapper.modelsFromApiListings(from.getApiListings()));
        swagger.setSecurityDefinitions(securityMapper.toSecuritySchemeDefinitions(from.getResourceListing()));
        ApiInfo info = fromResourceListingInfo(from);
        if (info != null) {
            swagger.setInfo(mapApiInfo(info));
        }
        swagger.setBasePath(from.getBasePath());
        swagger.setTags(tagSetToTagList(from.getTags()));
        List<String> list2 = from.getConsumes();
        if (list2 != null) {
            swagger.setConsumes(new ArrayList<String>(list2));
        } else {
            swagger.setConsumes(null);
        }
        List<String> list3 = from.getProduces();
        if (list3 != null) {
            swagger.setProduces(new ArrayList<String>(list3));
        } else {
            swagger.setProduces(null);
        }

        return swagger;
    }

    private ApiInfo fromResourceListingInfo(Documentation documentation) {
        if (documentation == null) {
            return null;
        } else {
            ResourceListing resourceListing = documentation.getResourceListing();
            if (resourceListing == null) {
                return null;
            } else {
                return resourceListing.getInfo();
            }
        }
    }

    @Override
    protected Map<String, Path> mapApiListings(Map<String, List<ApiListing>> apiListings) {
        Map<String, Path> paths = newTreeMap();
        apiListings.values().stream().flatMap(Collection::stream).forEachOrdered((each) -> {
            Iterator var3 = each.getApis().iterator();

            while (var3.hasNext()) {
                ApiDescription api = (ApiDescription) var3.next();
                paths.put(api.getPath(), this.mapOperations(api, Optional.ofNullable((Path) paths.get(api.getPath())),
                        each.getModelNamesRegistry()));
            }

        });
        return paths;
    }


    private Path mapOperations(ApiDescription api, Optional<Path> existingPath, ModelNamesRegistry modelNamesRegistry) {
        Path path = (Path) existingPath.orElse(new Path());
        Iterator var5 = BuilderDefaults.nullToEmptyList(api.getOperations()).iterator();

        while (var5.hasNext()) {
            springfox.documentation.service.Operation each = (springfox.documentation.service.Operation) var5.next();
            Operation operation = this.mapOperation(each, modelNamesRegistry);
            path.set(each.getMethod().toString().toLowerCase(), operation);
        }

        return path;
    }


}
