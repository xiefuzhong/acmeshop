package com.acme.acmemall.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @description: 素材
 * @author: ihpangzi
 * @time: 2024/1/9 13:11
 */
@Data
public class ProductMaterialsVo implements Serializable {
    private long id;
    private String FileID; // 文件ID cloudId
    private String fileName; //文件名称
    private String filePath; // 文件相对路径
    private String fileType; // 文件类型 image/
    private String fileUrl;  // 文件地址用于访问
    private long size;      // 文件地址 byte
    private String tempFilePath; // 上传地址(本地/微信)
    private long userId; // uploader上传人
    private Integer uploadType; // 文件来源：相册/文件系统
    private Date uploadTime = new Date();  // 上传时间
    private String remark; // 用途描述，关联商品ID,格式1,2,3
    private Integer groupType;
}
