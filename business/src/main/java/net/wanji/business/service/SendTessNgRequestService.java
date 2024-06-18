package net.wanji.business.service;

import net.wanji.business.domain.param.TessParam;

public interface SendTessNgRequestService {
    void saveTessNgRequest(String result, String resultUrl, TessParam tessParam);
}
