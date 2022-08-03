package cn.xu.edustatistics.schedule;

import cn.xu.edustatistics.service.StatisticsDailyService;
import cn.xu.edustatistics.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ScheduleTask {

    @Autowired
    private StatisticsDailyService statisticsDailyService;

//    @Scheduled(cron = "0/5 * * * * ?")
//    public void task1(){
//        System.out.println("1234567");
//    }

    //每天凌晨1点执行
    @Scheduled(cron = "0 0 1 * * ?")
    public void task(){
        statisticsDailyService.registerCount(DateUtil.formatDate(DateUtil.addDays(new Date(),-1)));
    }



}
