package net.wanji.business.domain.vo;

import lombok.Data;
import net.wanji.business.domain.Label;

import java.util.List;

@Data
public class TreeVo {

    List<Label> trees;

    Integer total;

}
