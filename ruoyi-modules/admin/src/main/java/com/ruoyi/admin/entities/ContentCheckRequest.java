package com.ruoyi.admin.entities;

import lombok.Data;

import java.util.List;
import java.util.Map;
@Data
public class ContentCheckRequest {
    private String model;
    private List<Map<String, String>> messages;
    private double temperature;
    private int max_tokens;

    // Getters and Setters

    public ContentCheckRequest(String model, List<Map<String, String>> messages, double temperature, int max_tokens) {
        this.model = model;
        this.messages = messages;
        this.temperature = temperature;
        this.max_tokens = max_tokens;
    }
}
