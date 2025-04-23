package com.cream.sparkle.common.utils;

public class IdUtil {

    // 基准时间(可根据需要调整为系统上线时间)
    private static final long EPOCH = 1717200000000L;

    // todo 后续修改为读取配置
    // 节点ID(分布式环境下需要确保每个服务节点的nodeId唯一, 取值范围0~1023)
    private static final long NODE_ID = 1; //

    // 序列号(14位,每毫秒最多16384个ID); 最大值16383,超过会等待到下一毫秒
    private static long sequence = 0;
    private static long lastTimestamp = 0;

    // 位分配（总长度64位）
    private static final int NODE_ID_BITS = 10;   // 节点ID占10位
    private static final int SEQUENCE_BITS = 14;  // 序列号占14位

    // 最大值校验(防止配置越界)
    static {
        if (NODE_ID < 0 || NODE_ID >= (1 << NODE_ID_BITS)) {
            throw new IllegalArgumentException("节点ID超出允许范围");
        }
    }

    // 生成唯一ID(线程安全)
    public static synchronized long generateUniqueId() {
        long currentTimestamp = getCurrentTimestamp();

        if (currentTimestamp < lastTimestamp) {
            throw new RuntimeException("时钟回拨！请检查系统时间");
        }

        // 同一毫秒内生成序列号
        if (currentTimestamp == lastTimestamp) {
            sequence = (sequence + 1) & ((1 << SEQUENCE_BITS) - 1); // 递增并取模

            // 当前毫秒序列号耗尽
            if (sequence == 0) {
                currentTimestamp = waitUntilNextMillis(currentTimestamp);
            }
        } else {
            sequence = 0; // 新毫秒重置序列号
        }
        lastTimestamp = currentTimestamp;

        // 组装ID：时间戳(39位) | 节点ID(10位) | 序列号(14位)
        return (currentTimestamp << (NODE_ID_BITS + SEQUENCE_BITS))
                | (NODE_ID << SEQUENCE_BITS)
                | sequence;
    }

    private static long getCurrentTimestamp() {
        return System.currentTimeMillis() - EPOCH;
    }

    private static long waitUntilNextMillis(long currentTimestamp) {
        long timestamp = getCurrentTimestamp();
        while (timestamp <= currentTimestamp) {
            timestamp = getCurrentTimestamp();
        }
        return timestamp;
    }
}
