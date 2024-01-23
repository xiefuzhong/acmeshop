package com.acme.acmemall.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @description:保价信息
 * @author: ihpangzi
 * @time: 2024/1/23 20:20
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Insured {
    private int use_insured; // 是否保价，0 表示不保价，1 表示保价
    private long insured_value; // 保价金额，单位是分，比如: 10000 表示 100 元
}
