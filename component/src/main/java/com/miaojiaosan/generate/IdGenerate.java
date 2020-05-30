package com.miaojiaosan.generate;

/**
 * @author miaojiaosan
 */
public class IdGenerate {

  /**
   * 起始的时间戳
   */
  private final static long START_STMP = 1480166465631L;

  /**
   * 每一部分占用的位数
   * 序列号占用的位数
   */
  private final static long SEQUENCE_BIT = 12;
  /**
   * 机器标识占用的位数
   */
  private final static long MACHINE_BIT = 5;
  /**
   * 数据中心占用的位数
   */
  private final static long DATA_CENTER_BIT = 5;

  /**
   * 每一部分的最大值
   */
  private final static long MAX_DATA_CENTER_NUM = ~(-1L << DATA_CENTER_BIT);
  private final static long MAX_MACHINE_NUM = ~(-1L << MACHINE_BIT);
  private final static long MAX_SEQUENCE = ~(-1L << SEQUENCE_BIT);

  /**
   * 每一部分向左的位移
   */
  private final static long MACHINE_LEFT = SEQUENCE_BIT;
  private final static long DATA_CENTER_LEFT = SEQUENCE_BIT + MACHINE_BIT;
  private final static long TIMESTAMP_LEFT = DATA_CENTER_LEFT + DATA_CENTER_BIT;

  /**
   * 数据中心
   */
  private long dataCenterId;
  /**
   * 机器标识
   */
  private long machineId;
  /**
   * 序列号
   */
  private long sequence = 0L;
  /**
   * 上一次时间戳
   */
  private long lastTimestamp = -1L;

  public IdGenerate(long dataCenterId, long machineId) {
    if (dataCenterId > MAX_DATA_CENTER_NUM || dataCenterId < 0) {
      throw new IllegalArgumentException("dataCenterId can't be greater than MAX_DATACENTER_NUM or less than 0");
    }
    if (machineId > MAX_MACHINE_NUM || machineId < 0) {
      throw new IllegalArgumentException("machineId can't be greater than MAX_MACHINE_NUM or less than 0");
    }
    this.dataCenterId = dataCenterId;
    this.machineId = machineId;
  }

  /**
   * 产生下一个ID
   *
   * @return id
   */
  public synchronized long nextId() {
    long currStmp = getNewstmp();
    if (currStmp < lastTimestamp) {
      throw new RuntimeException("Clock moved backwards.  Refusing to generate id");
    }

    if (currStmp == lastTimestamp) {
      //相同毫秒内，序列号自增
      sequence = (sequence + 1) & MAX_SEQUENCE;
      //同一毫秒的序列数已经达到最大
      if (sequence == 0L) {
        currStmp = getNextMill();
      }
    } else {
      //不同毫秒内，序列号置为0
      sequence = 0L;
    }

    lastTimestamp = currStmp;

    return
        (currStmp - START_STMP) << TIMESTAMP_LEFT //时间戳部分
        | dataCenterId << DATA_CENTER_LEFT       //数据中心部分
        | machineId << MACHINE_LEFT             //机器标识部分
        | sequence;                             //序列号部分
  }

  private long getNextMill() {
    long mill = getNewstmp();
    while (mill <= lastTimestamp) {
      mill = getNewstmp();
    }
    return mill;
  }

  private long getNewstmp() {
    return System.currentTimeMillis();
  }

  public static void main(String[] args) {
    IdGenerate snowFlake = new IdGenerate(1, 1);

    long start = System.currentTimeMillis();
    for (int i = 0; i < 100; i++) {
      System.out.println(snowFlake.nextId());
    }

    System.out.println(System.currentTimeMillis() - start);


  }
}
