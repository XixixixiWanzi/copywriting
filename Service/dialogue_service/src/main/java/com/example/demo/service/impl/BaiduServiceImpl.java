package com.example.demo.service.impl;

import com.example.CWException;
import com.example.demo.service.IBaiduService;
import com.example.demo.util.BaiduProps;
import com.example.demo.util.JsonStrUtil;
import com.example.reback.ResultCode;
import io.jsonwebtoken.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@Service
public class BaiduServiceImpl implements IBaiduService {
    private final Integer TIME = 72;
    @Autowired
    private BaiduProps baiduProps;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void getNLPToken() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Accept", "application/json");
            HttpEntity<String> requestEntity = new HttpEntity<>(headers);
            RestTemplate restTemplate = new RestTemplate();
            String fullUrl = baiduProps.getTokenUrl() +
                    "&client_id=" + baiduProps.getNlpKey() +
                    "&client_secret=" + baiduProps.getNlpSecret();
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(fullUrl, requestEntity, String.class);
            String responseBody = responseEntity.getBody();
            try {
                JsonNode jsonNode = objectMapper.readTree(responseBody);
                String accessToken = jsonNode.get("access_token").asText();
                System.out.println(accessToken);
                redisTemplate.opsForValue().set("NLPToken", accessToken ,TIME , TimeUnit.HOURS);
            } catch (IOException e) {
                e.printStackTrace(); //
                throw new CWException(ResultCode.SERVER_ERROR.setMessage("系统token 更新 出错"));
            }
        } catch (Exception e) {
            throw new CWException(ResultCode.SERVER_ERROR.setMessage("系统token 更新 出错"));
        }
    }
    @Override
    public void getErnieToken(){
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Accept", "application/json");
            HttpEntity<String> requestEntity = new HttpEntity<>(headers);
            RestTemplate restTemplate = new RestTemplate();
            String fullUrl = baiduProps.getTokenUrl() +
                    "&client_id=" + baiduProps.getWenxinKey() +
                    "&client_secret=" + baiduProps.getWenxinSecret();
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(fullUrl, requestEntity, String.class);
            String responseBody = responseEntity.getBody();
            try {
                JsonNode jsonNode = objectMapper.readTree(responseBody);
                String accessToken = jsonNode.get("access_token").asText();
                System.out.println(accessToken);
                redisTemplate.opsForValue().set("ERNIEPToken", accessToken ,TIME , TimeUnit.HOURS);
            } catch (IOException e) {
                e.printStackTrace(); //
                throw new CWException(ResultCode.SERVER_ERROR.setMessage("系统token 更新 出错"));
            }
        } catch (Exception e) {
            throw new CWException(ResultCode.SERVER_ERROR.setMessage("系统token 更新 出错"));
        }
    }

    @Override
    public HashMap<String, ArrayList<String>> getKeyWords(String text, HashMap<String, String> queries) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

//            参数构建部分
            ArrayList<HashMap<String ,String>> queryList = new ArrayList<>();
            for (Map.Entry<String, String> entry : queries.entrySet()) {
                String value = entry.getValue();
                HashMap<String ,String> query = new HashMap<>();
                query.put("query", value);
                queryList.add(query);
            }
            ArrayList<HashMap> contentList = new ArrayList();
            HashMap<String, Object> content = new HashMap<>();
            content.put("content", text);
            content.put( "query_list", queryList);
            contentList.add(content);
            HashMap<String ,ArrayList> params = new HashMap<>();
            params.put( "content_list", contentList);

            String paramStr = JsonStrUtil.obj2JsonStr(params);

//          请求部分
            HttpEntity<String> requestEntity = new HttpEntity<>(paramStr,headers);


            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baiduProps.getTxtMonetUrl())
                    .queryParam("access_token", redisTemplate.opsForValue().get("NLPToken"));
            String fullUrl = builder.build().toUriString();
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(fullUrl, requestEntity, String.class);

//            解析部分
            String responseBody = responseEntity.getBody();

            HashMap<String, Object> res = JsonStrUtil.JsonStr2Obj(responseBody, HashMap.class);
            ArrayList<HashMap<String, Object>> resultsList = (ArrayList<HashMap<String, Object>>) res.get("results_list");
            ArrayList<HashMap<String, ArrayList<HashMap<String, Object>>>> results = (ArrayList<HashMap<String, ArrayList<HashMap<String, Object>>>>) resultsList.get(0).get("results");

//           构造返回值部分
            HashMap<String, ArrayList<String>> anwser = new HashMap<>();
            String[] keys = queries.keySet().toArray(new String[0]);
            int index = 0;
            for (HashMap<String, ArrayList<HashMap<String, Object>>> result : results) { //query 级
                ArrayList<String> queryAnswer = new ArrayList<>();
                anwser.put(keys[index++], queryAnswer);
                for (HashMap<String, Object> item : result.get("items")) { // items 是一个列表，有多个回答
                    String ans = (String) item.get("text");
                    double prob = (Double) item.get("prob");
                    if (prob > 0.2)
                        queryAnswer.add(ans);
                }
            }
            System.out.println(JsonStrUtil.obj2JsonStr(anwser));
            return anwser;

        } catch (Exception e) {
            e.printStackTrace();
            throw new CWException(ResultCode.SERVER_ERROR.setMessage("获取关键字出错"));
        }
    }


    @Override
    public String getPromptText(Map<String,String> params, String promptId) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baiduProps.getPromptUrl())
                    .queryParam("access_token", redisTemplate.opsForValue().get("ERNIEPToken"))
                    .queryParam("id", promptId)
                    .queryParam("festival", params.get("festival"))
                    .queryParam("relation", params.get("relation"));

            String fullUrl = builder.build().toUriString();

            ResponseEntity<String> responseEntity = restTemplate.getForEntity(fullUrl, String.class);

            String responseBody = responseEntity.getBody();
            System.out.println("responseBody = " + responseBody);
            try {
                Map<String,Map<String,String>> res = JsonStrUtil.JsonStr2Obj(responseBody, HashMap.class);
                String content = res.get("result").get("content");
                return content;
            } catch (IOException e) {
                e.printStackTrace(); //
                throw new CWException(ResultCode.SERVER_ERROR.setMessage("获取prompt出错"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new CWException(ResultCode.SERVER_ERROR.setMessage("获取prompt出错"));
        }
    }

    @Override
    public String getWriting(String query) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, String> mes1 = new HashMap<>();
            mes1.put("role", "user");
            mes1.put( "content", query);
            List<Object> messages = new ArrayList<>();
            messages.add(mes1);
            Map<String, Object> map = new HashMap<>();
            map.put("messages", messages);
            String params = JsonStrUtil.obj2JsonStr(map);

            HttpEntity<String> requestEntity = new HttpEntity<>(params,headers);

            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baiduProps.getErnieBotUrl())
                    .queryParam("access_token", redisTemplate.opsForValue().get("ERNIEPToken"));
            String fullUrl = builder.build().toUriString();

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(fullUrl, requestEntity, String.class);
            String responseBody = responseEntity.getBody();
            System.out.println("responseBody = " + responseBody);
            try {
                HashMap<String, String> res = JsonStrUtil.JsonStr2Obj(responseBody, HashMap.class);
                String content = res.get("result");
                return content;
            } catch (IOException e) {
                e.printStackTrace();
                throw new CWException(ResultCode.SERVER_ERROR.setMessage("获取prompt出错"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new CWException(ResultCode.SERVER_ERROR.setMessage("获取prompt出错"));
        }
    }
}
