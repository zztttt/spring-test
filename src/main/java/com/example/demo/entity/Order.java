package com.example.demo.entity;

import com.google.gson.JsonArray;
import jnr.ffi.annotations.In;
import lombok.Data;

import java.util.ArrayList;

@Data
public class Order {
    @Data
    public static class Space{
        public String venuePrice;
        public Integer count;
        public Integer status;
        public String scheduleTime;
        public String subSitename;
        public String tensity;
        public Integer venueNum;
    }
    public String venTypeId;
    public String venueId;
    public String fieldType;
    public String returnUrl;
    public String scheduleDate;
    public String week;
    public ArrayList<Space> spaces;
    public String tenSity;
}
