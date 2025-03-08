package com.example.demo.service;

import java.util.concurrent.ExecutionException;

public interface IMsmService {
    void send(String code, String phone) throws ExecutionException, InterruptedException;
}
