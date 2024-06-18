package net.wanji.common.filter;
import com.alibaba.fastjson.JSONObject;
import net.wanji.common.utils.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilterUtil {

  public static JSONObject getParam(HttpServletRequest request)
      throws URISyntaxException {
    String uri = request.getRequestURI();
    URIBuilder uriBuilder = new URIBuilder(uri);
    uriBuilder.build();
    List<NameValuePair> queryParams = uriBuilder.getQueryParams();
    Map<String,String> pamearMap = queryParams.stream()
        .collect(HashMap::new, (m, v) -> m.put(v.getName(), v.getValue()), HashMap::putAll);
    JSONObject params;
    String content = JSONObject.toJSONString(request.getParameterMap());
    if(StringUtils.isNotEmpty(content)){
      params = JSONObject.parseObject(content);
    }else {
      params = new JSONObject();
    }
    params.putAll(pamearMap);
    params.put("baseUrl",uriBuilder.getPath());
    return params;
  }
}
