package com.acme.acmemall.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @description:图片附件
 * @author: ihpangzi
 * @time: 2024/1/16 19:11
 */
@Data
public class ImgAttachment implements Serializable {
    private long id;
    private String type; // banner/detail/gallery/primary/list/spec
    private String fileUrl;
    private int sortId;
}
