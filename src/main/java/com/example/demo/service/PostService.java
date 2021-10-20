package com.example.demo.service;

import com.google.gson.JsonObject;

import java.util.ArrayList;

public interface PostService {
    public Boolean post(String location, ArrayList<String> timeRanges) throws Exception;
}
