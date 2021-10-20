package com.example.demo.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.entity.Order;
import com.example.demo.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ScheduledExecutorService;

@Slf4j
@Component
public class PostServiceImpl implements PostService {
    private String destUrl = "https://sports.sjtu.edu.cn/venue/personal/ConfirmOrder";
    private String cookie = "JSESSIONID=30f4b202-6dcf-42d9-b5bc-29d5f77028a8;NSC_wt_tqpsut.tkuv.fev.do_2020=ffffffff097f1cec45525d5f4f58455e445a4a4229a0;rememberMe=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; Path=/; Domain=.sports.sjtu.edu.cn; Expires=Thu, 20 Oct 2022 13:48:33 GMT;";
    private String badmintonVenTypeId = "29942202-d2ac-448e-90b7-14d3c6be19ff";
    private String badmintonVenueId = "3b10ff47-7e83-4c21-816c-5edc257168c1";
    private String tableTennisVenTypeId = "28d3bea9-541d-4efb-ae46-e739a5f78d72";
    private String tableTennisVenueId = "3f009fce-10b4-4df6-94b7-9d46aef77bb9";

    @Override
    public Boolean post(String location, ArrayList<String> timeRanges) throws Exception{
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(destUrl);
        ResponseHandler handler = new BasicResponseHandler();

        Order order = buildOrder("羽毛球", location, timeRanges);
        String s = JSON.toJSONString(order);
        StringEntity body = new StringEntity(s, ContentType.APPLICATION_JSON);


        post.addHeader("Content-Type", "application/json");
        post.addHeader("User-Agent", "PostmanRuntime/7.26.8");
        post.addHeader("Accept", "*/*");
        post.addHeader("Accept-Encoding", "gzip, deflate, br");
        post.addHeader("Connection", "Keep-Alive");
        post.addHeader("Cookie", cookie);

        post.setEntity(body);
        HttpResponse response = client.execute(post);
        String responseBody = (String) handler.handleResponse(response);
        JSONObject json = JSONObject.parseObject(responseBody);
        int statusCode = response.getStatusLine().getStatusCode();
        log.info("responseBody: {}, statusCode: {}", responseBody, statusCode);

        if(statusCode != 200){
            log.error("status code is: {}", statusCode);
            return false;
        }
        if(json.getString("code") != null && json.getString("code").equals("500")){
            //log.error("status code is 500. {}", json.getString("msg"));
            return false;
        }

        return true;
    }

    private Order buildOrder(String type, String location, ArrayList<String> timeRanges) throws Exception{
        if(timeRanges.size() > 2){
            log.error("time range size > 2");
            return null;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 7);
        String targetDay = simpleDateFormat.format(calendar.getTime());
        String targetWeek = String.valueOf(calendar.get(Calendar.DAY_OF_WEEK) - 1);
        //log.info("targetDay: {}, targetWeek: {}", targetDay, targetWeek);

        ArrayList<Order.Space> spaces = new ArrayList<>();
        for(String timeRange: timeRanges){
            Order.Space space = new Order.Space();
            space.setVenuePrice("9");
            space.setCount(1);
            space.setStatus(1);
            space.setScheduleTime(timeRange);
            space.setSubSitename(location);
            space.setTensity("1");
            space.setVenueNum(1);
            spaces.add(space);
        }

        Order order = new Order();
        if(type.equals("羽毛球")){
            order.setVenTypeId(badmintonVenTypeId);
            order.setVenueId(badmintonVenueId);
        }else if(type.equals("乒乓球")){
            order.setVenTypeId(tableTennisVenTypeId);
            order.setVenueId(tableTennisVenueId);
        }else{
            log.error("no matching type!!!");
            return null;
        }
        order.setFieldType(type);
        order.setReturnUrl("https://sports.sjtu.edu.cn/#/paymentResult/1");
        order.setScheduleDate(targetDay);
        order.setWeek(targetWeek);
        order.setSpaces(spaces);
        order.setTenSity("紧张");

        return order;
    }
    public static void main(String[] args) throws Exception{

    }
}
