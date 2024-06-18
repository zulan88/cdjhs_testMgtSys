package net.wanji.business.domain.dto;

import lombok.Data;
import net.wanji.business.common.Constants.BatchGroup;
import net.wanji.business.common.Constants.DeleteGroup;
import net.wanji.business.common.Constants.InsertGroup;
import net.wanji.business.common.Constants.QueryGroup;
import net.wanji.business.common.Constants.UpdateGroup;
import net.wanji.business.entity.TjCasePartConfig;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * @Auther: guanyuduo
 * @Date: 2023/6/29 13:45
 * @Descriptoin:
 */
@Data
public class TjDeviceDetailDto {

    @NotNull(message = "请选择页码", groups = QueryGroup.class)
    private Integer pageNum;

    @NotNull(message = "请选择页大小", groups = QueryGroup.class)
    private Integer pageSize;

    @NotNull(message = "请选择一个设备", groups = {DeleteGroup.class})
    private Integer deviceId;

    @NotBlank(message = "请填写设备名称", groups = {InsertGroup.class, UpdateGroup.class})
    private String deviceName;

//    @NotNull(message = "请选择所属文件夹", groups = {QueryGroup.class, InsertGroup.class, UpdateGroup.class})
    private String deviceType;

    @NotBlank(message = "请选择设备可支持的测试角色", groups = {InsertGroup.class, UpdateGroup.class})
    private String supportRoles;

    private String ip;

    private String serviceAddress;

    @NotBlank(message = "请设置数据通道", groups = {InsertGroup.class, UpdateGroup.class})
    private String dataChannel;

    @NotBlank(message = "请设置控制通道", groups = {InsertGroup.class, UpdateGroup.class})
    private String commandChannel;

    private Integer status;

    @NotEmpty(message = "请选择至少一个设备", groups = {BatchGroup.class})
    private List<Integer> deviceIds;

    private String attribute2;

    private Integer isInner;

    private String createdBy;

}
