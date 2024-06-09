/*
 * Copyright (c) 2024. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.acme.acmemall.utils;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.multi.GenericMultipleBarcodeReader;
import com.google.zxing.multi.qrcode.QRCodeMultiReader;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;


/**
 * @description: 解析二维码
 * @author: ihpangzi
 * @time: 2024/6/8 15:03
 */
@Slf4j
public class QRCodeUtils {
    public static void main(String[] args) {
//        String path = "https://7072-prod-7g7eijryfa0dd373-1314032717.tcb.qcloud.la/trace/images/upload/IMG_20240608_204800.jpg";
//        String path = "https://7072-prod-7g7eijryfa0dd373-1314032717.tcb.qcloud.la/trace/images/upload/IMG_20240608_205929.jpg";
//        String path = "https://7072-prod-7g7eijryfa0dd373-1314032717.tcb.qcloud.la/trace/images/upload/IMG_20240608_210004.jpg";
        String path = "https://7072-prod-7g7eijryfa0dd373-1314032717.tcb.qcloud.la/trace/images/upload/IMG_20240608_204754.jpg";
        getMutiQRCode(path);
    }

    public static String[] getMutiQRCode(String path) {
        String content = null;
        BufferedImage image = getImage(path);
        try {
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            Binarizer binarizer = new HybridBinarizer(source);
            BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
            Map<DecodeHintType, Object> hints = getDecodeHint();

            Result[] results = new QRCodeMultiReader().decodeMultiple(binaryBitmap, hints);
            log.info("content:");
            for (int i = 0; i < results.length; i++) {
                content = new String(("No" + i + " QR_CODE： " + results[i].getText() + "\n").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
                log.info(content);
                parseResult(results[i].getText());
            }
        } catch (NotFoundException ex) {
            try {
                LuminanceSource source = new BufferedImageLuminanceSource(image);
                Binarizer binarizer = new HybridBinarizer(source);
                BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
                Map<DecodeHintType, Object> hints = getDecodeHint();

                GenericMultipleBarcodeReader mutiBarcodeReader = new GenericMultipleBarcodeReader(new MultiFormatReader());
                Result[] result2 = mutiBarcodeReader.decodeMultiple(binaryBitmap, hints);
                log.info("content1:" + result2.length);
                // 识别结果处理
                String[] sns = extractSNs(result2);
                Arrays.stream(sns).forEach(item -> {
                    log.info(item);
                });
            } catch (Exception ex1) {
                // ignore
                ex1.printStackTrace();
            }
        }
        return new String[0];
    }

    // 使用Java 8的流来简化数组处理
    private static String[] extractSNs(Result[] results) {
        return Arrays.stream(results)
                .filter(result -> result.getText().length() >= 12)
                .sorted(Comparator.comparing(Result::getText))
                .map(Result::getText)
                .toArray(String[]::new);
    }

    private static Map<DecodeHintType, Object> getDecodeHint() {
        Map<DecodeHintType, Object> hints = new EnumMap<>(DecodeHintType.class);

        hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(DecodeHintType.POSSIBLE_FORMATS,
                EnumSet.allOf(BarcodeFormat.class));
        //设置优化精度
        hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
        //设置复杂模式开启（我使用这种方式就可以识别微信的二维码了）
        hints.put(DecodeHintType.PURE_BARCODE, Boolean.TRUE);
        return hints;
    }

    private static BufferedImage getImage(String path) {
        BufferedImage image = null;
        try {
            URL url = new URL(path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                image = ImageIO.read(inputStream);
            }
            connection.disconnect();
        } catch (IOException e) {
            log.error("Error loading image from URL: {}", path, e);
        }
//        return toGrayscale(image);
        return image;
    }

    private static void parseResult(String content) {
        try {
            URL url = new URL(content);
            String query = URLDecoder.decode(url.getQuery(), "UTF-8");
            log.info("query:{}", query);
//            appid=wxcb591b8a49800e1e&path=pages%2Fstart%2Fstart.html%3Fseq%3D240500001

            String query1 = query.substring(query.indexOf("?") + 1);
            log.info("query1:{}", query1);
        } catch (Exception e) {
            log.error("错误", e);
        }
    }
}
