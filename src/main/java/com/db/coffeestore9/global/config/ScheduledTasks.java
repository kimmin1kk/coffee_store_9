package com.db.coffeestore9.global.config;

import java.sql.Timestamp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ScheduledTasks {

  /**
   * 초 분 시 일 월 요일 순서 ( * * * * * * )
   * [5 * * * * *] -> 매 5초마다 ex)  13:01:05, 13:02:05
   */
  private static final String EVERY_MINUTE = "0 * * * * *";
  private static final String EVERY_HOUR = "0 0 * * * *";
  private static final String EVERY_DAY = "0 0 0 * * *";
  private static final String EVERY_MONTH = "0 0 0 1 * *";

  private static final String TEST_STRING = "{}에 실행되었습니다.";

  @Scheduled(cron = EVERY_MINUTE)
  public void scheduledTasksMethod() {
    log.info("1분 마다 실행");
    log.info(TEST_STRING, new Timestamp(System.currentTimeMillis()));
  }

  @Scheduled(cron = EVERY_HOUR)
  public void scheduledTasksMethod2() {
    log.info("- 1시간 마다 실행 -");
    log.info(TEST_STRING, new Timestamp(System.currentTimeMillis()));
  }

  @Scheduled(cron = EVERY_DAY)
  public void scheduledTasksMethod3() {
    log.info("- 매일 실행 -");
    log.info(TEST_STRING, new Timestamp(System.currentTimeMillis()));
  }

  @Scheduled(cron = EVERY_MONTH)
  public void scheduledTasksMethod4() {
    log.info("- 매달 실행 -");
    log.info(TEST_STRING, new Timestamp(System.currentTimeMillis()));
  }

}
