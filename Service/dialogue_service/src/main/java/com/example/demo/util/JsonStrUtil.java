package com.example.demo.util;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonStrUtil {
    /***
     * 请收下我的膝盖，这段代码我可以评为年度最佳，夺走了R、Reback 、ResultCode在我心中的位置
     *
     *                   /88888888888888888888888888\
     *                   |88888888888888888888888888/
     *                    |~~____~~~~~~~~~"""""""""|
     *                   / \_________/"""""""""""""\
     *                  /  |              \         \
     *                 /   |  88    88     \         \
     *                /    |  88    88      \         \
     *               /    /                  \        |
     *              /     |   ________        \       |
     *              \     |   \______/        /       |
     *   /"\         \     \____________     /        |
     *   | |__________\_        |  |        /        /
     * /""""\           \_------'  '-------/       --
     * \____/,___________\                 -------/
     * ------*            |                    \
     *   ||               |                     \
     *   ||               |                 ^    \
     *   ||               |                | \    \
     *   ||               |                |  \    \
     *   ||               |                |   \    \
     *   \|              /                /"""\/    /
     *      -------------                |    |    /
     *      |\--_                        \____/___/
     *      |   |\-_                       |
     *      |   |   \_                     |
     *      |   |     \                    |
     *      |   |      \_                  |
     *      |   |        ----___           |
     *      |   |               \----------|
     *      /   |                     |     ----------""\
     * /"\--"--_|                     |               |  \
     * |_______/                      \______________/    )
     *                                               \___/
     */

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> String obj2JsonStr(T object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
    public static <T> T JsonStr2Obj(String jsonString, Class<T> valueType) {
        try {
            return objectMapper.readValue(jsonString, valueType);
        } catch (JsonProcessingException e) {
            System.out.println("e = " + e);
            throw new RuntimeException();
        }
    }

    public static void main(String[] args) {

        Map<String, String> question = new HashMap<>();
        question.put("poetry", "古诗词句子");
        String params =  obj2JsonStr(question);
        System.out.println("params = " + params);


//        mes1.put("role", "user");
//        mes1.put( "content", "介绍一下你自己");
//        List<Object> messages = new ArrayList<>();
//        messages.add(mes1);
//        Map<String, Object> map = new HashMap<>();
//        map.put("messages", messages);
//        String params =  obj2JsonStr(map);

//        System.out.println("params = " + params);

    }
}
