package com.sakib.io.litespring;

import com.sakib.io.litespring.enums.MethodType;
import lombok.Builder;
import lombok.Data;

import java.lang.reflect.Method;

@Data
@Builder(toBuilder = true)
public class ControllerMethod {
    private Class<?> clazz;
    private Object instance;
    private Method method;

    private MethodType methodType;

    private String url;
}
