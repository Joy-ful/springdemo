package com.ruiyuan.springdemo.util;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;

public class YarnUserImpl  {
    @Value("${Acluster.url}")
    private String A_ip;
    @Value("${Acluster.cluster}")
    private String A_cluster;
    @Value("${Acluster.user}")
    private String A_usernamme;
    @Value("${Acluster.pass}")
    private String A_password;
    @Value("${Bcluster.url}")
    private String B_ip;
    @Value("${Bcluster.cluster}")
    private String B_cluster;
    @Value("${Bcluster.user}")
    private String B_usernamme;
    @Value("${Bcluster.pass}")
    private String B_password;

    public List getAOnlineUsers() {
        Object onlineUsers = null;
        List<Object> list = new ArrayList<Object>(); 
        HttpHeaders headers = new HttpHeaders();
        JSONObject yarnJson =null;
        headers.setContentType(MediaType.parseMediaType("application/json; charset=UTF-8"));
        headers.add("Authorization",
                "Basic " + new String(Base64.getEncoder().encode((A_usernamme + ":" + A_password).getBytes())));
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(null,
                headers);
        RestTemplate restTemplate = new RestTemplate();
        if(A_ip.equals("10.221.195.221")){
             yarnJson = restTemplate
                    .exchange("http://" + A_ip + ":7180/api/v18/clusters/" + A_cluster + "/services/yarn/yarnApplications", HttpMethod.GET,
                            requestEntity, JSONObject.class)
                    .getBody();
        }else{
            yarnJson= restTemplate
                    .exchange("http://" + A_ip + ":7180/api/v12/clusters/" + A_cluster + "/services/yarn/yarnApplications", HttpMethod.GET,
                            requestEntity, JSONObject.class)
                    .getBody();
        }
        JSONArray jsonArray = yarnJson.getJSONArray("applications");
        Iterator<Object> iterator = jsonArray.iterator();
        while(iterator.hasNext()){
            Object object= iterator.next();
            //json格式的String字符串转成map类型
            Gson gson = new Gson();
            HashMap mapObj=null;
            mapObj = gson.fromJson(object.toString(), HashMap.class);
            if(mapObj.get("state").equals("RUNNING")){
                onlineUsers = mapObj.get("user");
                list.add(onlineUsers);
            }
            //list去重，一个用户可能跑了多个任务
            HashSet h = new HashSet(list);   
            list.clear();   
            list.addAll(h);   
        }
        return list;
    }


    public List getBOnlineUsers() {
        Object onlineUsers = null;
        List<Object> list = new ArrayList<Object>(); 
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/json; charset=UTF-8"));
        headers.add("Authorization",
                "Basic " + new String(Base64.getEncoder().encode((B_usernamme+ ":" + B_password).getBytes())));
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(null,
                headers);
        RestTemplate restTemplate = new RestTemplate();
        JSONObject yarnJson = restTemplate
                .exchange("http://" + B_ip + ":7180/api/v12/clusters/" + B_cluster + "/services/yarn/yarnApplications", HttpMethod.GET,
                        requestEntity, JSONObject.class)
                .getBody();
        JSONArray jsonArray = yarnJson.getJSONArray("applications");
        Iterator<Object> iterator = jsonArray.iterator();
        while(iterator.hasNext()){
            Object object= iterator.next();
            //json格式的String字符串转成map类型
            Gson gson = new Gson();
            HashMap mapObj=null;
            mapObj = gson.fromJson(object.toString(), HashMap.class);
            if(mapObj.get("state").equals("RUNNING")){
                onlineUsers = mapObj.get("user");
                list.add(onlineUsers);
            }
            //list去重，一个用户可能跑了多个任务
            HashSet h = new HashSet(list);   
            list.clear();   
            list.addAll(h);   
        }
        return list;
}
}