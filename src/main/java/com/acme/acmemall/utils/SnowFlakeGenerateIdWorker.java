package com.acme.acmemall.utils;

import lombok.SneakyThrows;
import org.apache.commons.lang.math.RandomUtils;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.Random;

/**
 * @description:雪花ID
 * @author: ihpangzi
 * @time: 2023/2/24 13:55
 */
public class SnowFlakeGenerateIdWorker {

    /**
     * 开始时间截
     */
    private final long twepoch = 1420041600000L;

    /**
     * 机器id所占的位数
     */
    private final long workerIdBits = 5L;

    /**
     * 数据标识id所占的位数
     */
    private final long datacenterIdBits = 5L;

    /**
     * 支持的最大机器id，结果是31 (这个移位算法可以很快的计算出几位二进制数所能表示的最大十进制数)
     */
    private final long maxWorkerId = -1L ^ (-1L << workerIdBits);

    /**
     * 支持的最大数据标识id，结果是31
     */
    private final long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);

    /**
     * 序列在id中占的位数
     */
    private final long sequenceBits = 12L;

    /**
     * 机器ID向左移12位
     */
    private final long workerIdShift = sequenceBits;

    /**
     * 数据标识id向左移17位(12+5)
     */
    private final long datacenterIdShift = sequenceBits + workerIdBits;

    /**
     * 时间截向左移22位(5+5+12)
     */
    private final long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;

    /**
     * 生成序列的掩码，这里为4095 (0b111111111111=0xfff=4095)
     */
    private final long sequenceMask = -1L ^ (-1L << sequenceBits);

    /**
     * 工作机器ID(0~31)
     */
    private long workerId;

    /**
     * 数据中心ID(0~31)
     */
    private long datacenterId;

    /**
     * 毫秒内序列(0~4095)
     */
    private long sequence = 0L;

    /**
     * 上次生成ID的时间截
     */
    private long lastTimestamp = -1L;


    /**
     * 构造函数
     *
     * @param workerId     工作ID (0~31)
     * @param datacenterId 数据中心ID (0~31)
     */
    public SnowFlakeGenerateIdWorker(long workerId, long datacenterId) {
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
        }
        if (datacenterId > maxDatacenterId || datacenterId < 0) {
            throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", maxDatacenterId));
        }
        if (workerId == 0) {
            this.workerId = getWorkId();
        }
        if (datacenterId == 0) {
            this.datacenterId = getCenterId();
        }

    }

    private Long getWorkId() {
        try {
            String host = Inet4Address.getLocalHost().getHostAddress();
            return Long.valueOf(host.hashCode() % 32);
        } catch (UnknownHostException e) {
            return RandomUtils.nextLong(new Random(32));
        }
    }

    private Long getCenterId() {
        try {
            String host = Inet4Address.getLocalHost().getHostName();
            return Long.valueOf(host.hashCode() % 32);
        } catch (UnknownHostException e) {
            return RandomUtils.nextLong(new Random(32));
        }
    }

    /**
     * 获得下一个ID (该方法是线程安全的)
     *
     * @return long
     */
    @SneakyThrows
    public synchronized long nextId() {
        Thread.sleep(10);
        long timestamp = timeGen();
        timestamp = generateId(timestamp);
        return ((timestamp - twepoch) << timestampLeftShift) //
                | (datacenterId << datacenterIdShift) //
                | (workerId << workerIdShift) //
                | sequence;
    }

    @SneakyThrows
    private long generateId(long timestamp) {
        Thread.sleep(10);
        //如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过这个时候应当抛出异常
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(
                    String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }
        //如果是同一时间生成的，则进行毫秒内序列
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            //毫秒内序列溢出
            if (sequence == 0)
                //阻塞到下一个毫秒,获得新的时间戳
                timestamp = tilNextMillis(lastTimestamp);
        } else//时间戳改变，毫秒内序列重置
        {
            sequence = 0L;
        }
        //上次生成ID的时间截
        lastTimestamp = timestamp;
        return timestamp;
    }

    /**
     * 获得下一个ID (string)
     **/
    @SneakyThrows
    public synchronized String generateNextId() {
        Thread.sleep(10);
        long timestamp = timeGen();
        timestamp = generateId(timestamp);
        //移位并通过或运算拼到一起组成64位的ID
        return String.valueOf(((timestamp - twepoch) << timestampLeftShift)
                | (datacenterId << datacenterIdShift)
                | (workerId << workerIdShift)
                | sequence);
    }

    /**
     * 阻塞到下一个毫秒，直到获得新的时间戳
     *
     * @param lastTimestamp 上次生成ID的时间截
     * @return 当前时间戳
     */
    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    /**
     * 返回以毫秒为单位的当前时间
     *
     * @return 当前时间(毫秒)
     */
    protected long timeGen() {
        return System.currentTimeMillis();
    }
    /**
     * 生成订单的编号order_sn
     */
    public static String generateId() {
        SnowFlakeGenerateIdWorker snowFlake = new SnowFlakeGenerateIdWorker(0, 0);
        return String.valueOf(snowFlake.generateId(System.currentTimeMillis()));
    }


    public static void main(String[] args) {
        SnowFlakeGenerateIdWorker snowFlakeGenerateIdWorker = new SnowFlakeGenerateIdWorker(0, 0);
        for (int i = 0; i < 100; i++) {
            System.out.println(SnowFlakeGenerateIdWorker.generateId());
//            System.out.println(SnowFlakeGenerateIdWorker.generateOrderNumber());
//            System.out.println(snowFlakeGenerateIdWorker.nextId());
        }

    }

}