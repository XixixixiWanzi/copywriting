package com.example.demo.service.impl;

import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.core.http.HttpClient;
import com.aliyun.httpcomponent.httpclient.ApacheAsyncHttpClientBuilder;
import com.aliyun.sdk.service.dysmsapi20170525.AsyncClient;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsResponse;
import com.example.CWException;
import com.example.demo.service.IMsmService;
import com.example.demo.utils.AliyunPros;
import com.example.reback.ResultCode;
import com.nimbusds.jose.shaded.gson.Gson;
import darabonba.core.client.ClientOverrideConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


@Service
public class MsmServiceImpl implements IMsmService {
    @Autowired
    private AliyunPros aliyunPros;
    @Override
    public void send(String code, String phone) {
        StaticCredentialProvider provider = StaticCredentialProvider.create(Credential.builder()
                .accessKeyId(aliyunPros.getKeyId())
                .accessKeySecret(aliyunPros.getKeySecret())
                .build());
        AsyncClient client = AsyncClient.builder()
                .region("cn-beijing")
                .credentialsProvider(provider)

                .overrideConfiguration(
                        ClientOverrideConfiguration.create()
                                .setEndpointOverride(aliyunPros.getMsm().getEndpoint())
                )
                .build();
        SendSmsRequest sendSmsRequest = SendSmsRequest.builder()
                .signName(aliyunPros.getMsm().getSignName())
                .templateCode(aliyunPros.getMsm().getTemplateCode())
                .templateParam("{\"code\":\"" + code + "\"}")
//                .smsUpExtendCode("")
//                .outId("")
                .phoneNumbers(phone)
                .build();
        // Asynchronously get the return value of the API request
        CompletableFuture<SendSmsResponse> response = client.sendSms(sendSmsRequest);

        // Synchronously get the return value of the API request
        try{
            SendSmsResponse resp = response.get();
            if (!"OK".equals(resp.getBody().getMessage()))
                throw new CWException(ResultCode.SERVER_ERROR);
        }catch (InterruptedException | ExecutionException e) {
            // 请求发生异常的处理逻辑
            e.printStackTrace();
            throw new CWException(ResultCode.SERVER_ERROR);
        }

    }
}
