package com.wwstation.gateway.components;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReturnData<T> {
    int code;
    String msg;
    String data;
}
