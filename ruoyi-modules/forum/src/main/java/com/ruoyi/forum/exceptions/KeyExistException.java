package com.ruoyi.forum.exceptions;
// 异常类，用于处理键已存在的情况
public class KeyExistException extends RuntimeException {
    public KeyExistException(String message) {
        super(message);
    }
}
