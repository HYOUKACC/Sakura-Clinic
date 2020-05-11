package com.hcc.config;


import com.hcc.mapper.MedicineItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;

@Component
@EnableScheduling
@Async
public class TimerScheduled {

    @Autowired
    private MedicineItemMapper medicineItemMapper;
//    @Scheduled(cron = "* * * * * *") // 每秒执行 * * * * * ? *
//    public void run() throws InterruptedException {
//        Thread.sleep(6000);
//        System.out.println(Thread.currentThread().getName()+"=====>>>>>使用cron  {}"+(System.currentTimeMillis()/1000));
//    }

    // 0/5 * * * * ? 每五秒执行
//    @Scheduled(cron = "0,5 * * * * *") // 每0,5秒的时候执行
//    public void run1() throws InterruptedException {
//        Thread.sleep(6000);
//        System.out.println(Thread.currentThread().getName()+"=====>>>>>使用cron  {}"+(System.currentTimeMillis()/1000));
//    }

    // 0 0 8,9,15,16 * * ?
    // cron = "0 0 9,15 1/1 * ?") // 每天9点和15点执行
    @Scheduled(cron = "0 0 9,15 1/1 * ?") // 每天9点和15点执行
    public void runExpiryDateCheck() throws InterruptedException {

        // 每日过期药品检查
        Calendar calendar = Calendar.getInstance();//默认是当前日期
        // 当前日期加三个月
        //calendar.add(Calendar.MONTH,3);
        String expiryDate=calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.DATE);
        medicineItemMapper.expiryDateCheck(expiryDate);


//        MedicineItem medicineItem=new MedicineItem();
//        medicineItem.setExpiryDate(calendar.getTime());
//        //System.out.println(calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.DATE));
//        //System.out.println(medicineItem);
    }


}
