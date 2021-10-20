package com.example.demo.config;

import com.example.demo.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.ArrayList;
import java.util.Date;

@Configuration
@EnableScheduling
@Slf4j
public class ScheduleConfig {
    @Autowired
    private PostService postService;

    @Scheduled(cron = "0 0/5 0-2 * * ? ")
    public void test(){
        log.info("schedule at: {}", new Date());
    }

    //@Scheduled(cron = "0 0/5 0-2 * * ? ")
    public void scheduleTask(){
        try{
            ArrayList<String> locations = new ArrayList<>();
            ArrayList<String> timeRanges = new ArrayList<>();
            locations.add("场地12");
            locations.add("场地11");
            locations.add("场地10");
            locations.add("场地9");
            locations.add("场地8");
            locations.add("场地7");
            locations.add("场地6");
            locations.add("场地5");
            locations.add("场地4");
            locations.add("场地3");
            locations.add("场地2");
            locations.add("场地1");
            timeRanges.add("18:00-19:00");
            timeRanges.add("19:00-20:00");
            timeRanges.add("20:00-21:00");
            timeRanges.add("21:00-22:00");
            int len = timeRanges.size();

            for(int i = 0; i < len - 1; ++i){
                ArrayList<String> time = new ArrayList<>();
                time.add(timeRanges.get(i));
                time.add(timeRanges.get(i + 1));
                for(String location: locations){
                    Boolean result = postService.post(location, time);
                    if(result){
                        log.info("location: {}, time: {} success", location, time);
                        return;
                    }
                    log.error("location: {}, time: {} fail", location, time);
                }
            }
            log.error("===========attempt fail today===========");
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
