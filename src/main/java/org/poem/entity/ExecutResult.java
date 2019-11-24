package org.poem.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 执行的结果
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExecutResult {

    /**
     * 是否成功
     */
    private Boolean success;

    /**
     * 错误日志
     */
    private String errorResult;

    /**
     * 成功的日志
     */
    private String successResult;
}
