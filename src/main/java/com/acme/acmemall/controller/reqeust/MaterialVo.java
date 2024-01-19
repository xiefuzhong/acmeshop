package com.acme.acmemall.controller.reqeust;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2024/1/19 14:02
 */
@Getter
@Setter
public class MaterialVo implements Serializable {
    private String fileUrl;
    private int id;
    private int sortId;

    public String getFmtHtml() {
        return String.format("<p><img src=\"%s\" style=\"\" class=\"fr-fin\"></p>\n", fileUrl);
    }
}
