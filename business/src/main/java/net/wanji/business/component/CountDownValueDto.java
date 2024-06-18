package net.wanji.business.component;

import lombok.Data;

import java.util.concurrent.CountDownLatch;

/**
 * @author: guanyuduo
 * @date: 2023/10/13 11:18
 * @descriptoin:
 */
@Data
public class CountDownValueDto {
    private CountDownLatch countDownLatch;
    private Object value;

    public CountDownValueDto() {
        this.countDownLatch = new CountDownLatch(1);
    }
}
