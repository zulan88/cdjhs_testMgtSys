package net.wanji.business.domain.vo;

import lombok.Data;

import java.util.List;

@Data
public class TagtoSceneVo {

    List<Integer> labellist;

    Integer choice;

    Integer fragmentedSceneId;

    Integer treeId;

}
