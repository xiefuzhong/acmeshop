package com.acme.acmemall.model;

import com.acme.acmemall.model.enums.Statisticsdimension;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @description:交易统计
 * @author: ihpangzi
 * @time: 2024/04/29 12:00
 */
@Data
public class TradeStatisticsVo implements Serializable {
    private String type; //   交易类型

    private String title; //   交易标题
    private Integer num; //   交易数量
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "##0.00")
    private BigDecimal amount; //交易金额

    private long amountRate; //交易金额

    public BigDecimal getAmount() {
        if (amount == null) {
            this.amount = BigDecimal.ZERO;
        }
        return amount.setScale(2, RoundingMode.HALF_UP);
    }

    public long getAmountRate() {
        if (amount == null) {
            return 0L;
        }
        return amount.multiply(new BigDecimal(100)).longValue();
    }

    public String getTitle() {
        return Statisticsdimension.getTitle(type);
    }

}