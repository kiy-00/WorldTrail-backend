package com.ruoyi.system.domain.vo;

import lombok.Data;

@Data
public class EmailDTO {
    private String to;
    private String subject;
    private String text;
}
