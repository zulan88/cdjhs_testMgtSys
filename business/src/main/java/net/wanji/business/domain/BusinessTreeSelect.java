package net.wanji.business.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.wanji.business.entity.TjDevice;
import net.wanji.business.entity.TjFragmentedScenes;
import net.wanji.business.entity.TjResources;
import net.wanji.business.entity.TjScenelibTree;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Auther: guanyuduo
 * @Date: 2023/6/28 10:08
 * @Descriptoin:
 */
@NoArgsConstructor
@Data
public class BusinessTreeSelect {

    private Integer id;

    private String name;

    private Integer status;

    private Integer parentId;

    private Integer level;

    private Integer isFolder;

    private String attribute1;

    private String attribute2;

    private String attribute3;

    private String attribute4;

    private String attribute5;

    private List<BusinessTreeSelect> children;

    public BusinessTreeSelect(TjFragmentedScenes scenes) {
        this.id = scenes.getId();
        this.name = scenes.getName();
        this.status = scenes.getStatus();
        this.parentId = scenes.getParentId();
        this.level = scenes.getLevel();
        this.isFolder = scenes.getIsFolder();
        this.attribute1 = scenes.getAttribute1();
        this.attribute2 = scenes.getAttribute2();
        this.attribute3 = scenes.getAttribute3();
        this.attribute4 = scenes.getAttribute4();
        this.attribute5 = scenes.getAttribute5();
        this.children = CollectionUtils.emptyIfNull(scenes.getChildren()).stream().map(BusinessTreeSelect::new)
                .collect(Collectors.toList());
    }

    public BusinessTreeSelect(TjScenelibTree scenes) {
        this.id = scenes.getId();
        this.name = scenes.getName();
        this.status = scenes.getStatus();
        this.parentId = scenes.getParentId();
        this.level = scenes.getLevel();
        this.isFolder = scenes.getIsFolder();
        this.attribute1 = scenes.getAttribute1();
        this.attribute2 = scenes.getAttribute2();
        this.attribute3 = scenes.getAttribute3();
        this.attribute4 = scenes.getAttribute4();
        this.attribute5 = scenes.getAttribute5();
        this.children = CollectionUtils.emptyIfNull(scenes.getChildren()).stream().map(BusinessTreeSelect::new)
                .collect(Collectors.toList());
    }

    public BusinessTreeSelect(TjResources resources) {
        this.id = resources.getId();
        this.name = resources.getName();
        this.status = resources.getStatus();
        this.parentId = resources.getParentId();
        this.level = resources.getLevel();
        this.attribute1 = resources.getAttribute1();
        this.attribute2 = resources.getAttribute2();
        this.attribute3 = resources.getAttribute3();
        this.attribute4 = resources.getAttribute4();
        this.attribute5 = resources.getAttribute5();
        this.children = CollectionUtils.emptyIfNull(resources.getChildren()).stream().map(BusinessTreeSelect::new)
                .collect(Collectors.toList());
    }

    public BusinessTreeSelect(TjDevice device) {
        this.id = device.getId();
        this.name = device.getName();
        this.status = device.getStatus();
        this.parentId = device.getParentId();
        this.level = device.getLevel();
        this.attribute1 = device.getAttribute1();
        this.attribute2 = device.getAttribute2();
        this.attribute3 = device.getAttribute3();
        this.attribute4 = device.getAttribute4();
        this.attribute5 = device.getAttribute5();
        this.children = CollectionUtils.emptyIfNull(device.getChildren()).stream().map(BusinessTreeSelect::new)
                .collect(Collectors.toList());
    }
}

