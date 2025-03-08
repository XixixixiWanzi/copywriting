package com.example.demo.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public interface IBaiduService {
    void getNLPToken();

    void getErnieToken();


    HashMap<String, ArrayList<String>> getKeyWords(String text, HashMap<String, String> queries);

    String getPromptText(Map<String,String> params, String promptId);

    String getWriting(String query);



}
