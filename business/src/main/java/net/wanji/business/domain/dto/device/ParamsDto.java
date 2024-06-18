package net.wanji.business.domain.dto.device;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @author: guanyuduo
 * @date: 2023/10/12 13:46
 * @descriptoin:
 */
@AllArgsConstructor
@Data
public class ParamsDto {

    private String id;

    private Object participantTrajectories;

}
