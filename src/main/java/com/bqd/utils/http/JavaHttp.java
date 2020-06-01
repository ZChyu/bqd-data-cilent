package com.bqd.utils.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

/**
 * Created by Chyu
 * Date on 2020/5/19 14:44
 * Email 604641446@qq.com
 */
public class JavaHttp {
    public JSONObject run(String url) throws IOException {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
//        StringEntity postingString = new StringEntity(param.toString());// json传递
//        post.setEntity(postingString);
        post.setHeader("Content-type", "application/json");
        HttpResponse response = httpClient.execute(post);
        InputStream inputStream=response.getEntity().getContent();
        String content = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"))
                .lines().parallel().collect(Collectors.joining(System.lineSeparator()));
        return JSON.parseObject(content);

    }

}

