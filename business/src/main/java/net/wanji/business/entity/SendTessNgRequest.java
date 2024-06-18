package net.wanji.business.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@TableName("send_tessng_request")
public class SendTessNgRequest {

    String result;

    String resultUrl;

    String tessParam;

    String requestDate;

}
