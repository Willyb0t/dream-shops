package com.willyb0t.dreamshops.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ApiResponse {
    private String Message;
    private Object data;
}
