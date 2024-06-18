package net.wanji.business.domain.dto;

import lombok.Data;
import net.wanji.business.common.Constants.BatchGroup;
import net.wanji.business.common.Constants.DeleteGroup;
import net.wanji.business.common.Constants.InsertGroup;
import net.wanji.business.common.Constants.QueryGroup;
import net.wanji.business.common.Constants.UpdateGroup;
import net.wanji.business.domain.PartConfigSelect;
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
public class TjCaseDto {

    @NotNull(message = "请选择一条数据", groups = {UpdateGroup.class, DeleteGroup.class})
    private Integer id;

    @NotNull(message = "请选择所属文件夹", groups = {InsertGroup.class, UpdateGroup.class})
    private Integer treeId;

    @NotNull(message = "请选择场景", groups = {InsertGroup.class})
    private Integer sceneDetailId;

//    @NotBlank(message = "请输入测试说明", groups = {UpdateGroup.class})
    private String testTarget;

    @NotNull(message = "请配置角色", groups = {UpdateGroup.class})
    private List<PartConfigSelect> partConfigSelects;

    @NotEmpty(message = "请选择至少一条数据", groups = {BatchGroup.class})
    private List<Integer> ids;

    private String remark;

    private Integer isGen;
}
