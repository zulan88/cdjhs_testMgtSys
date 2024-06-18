package net.wanji.business.domain.vo;

import lombok.Data;

@Data
public class RoleVo {

    public RoleVo(String name, String value){
        this.name = name;
        this.value = value;
    }

    String name;

    String value;

}
