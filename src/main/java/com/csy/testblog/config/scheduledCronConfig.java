package com.csy.testblog.config;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class scheduledCronConfig {
    @Scheduled(cron = "0/5 * * * * *")
    public void cron() {
        System.out.println(new Date());
    }

}
