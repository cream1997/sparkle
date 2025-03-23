package com.cream.sparkle.global;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
public class GlobalResBodyAdvice implements ResponseBodyAdvice<Object> {

    /**
     * fixme
     * 对于下载的接口不应用下面的包装，但是这样判断可能不严谨因为返回值类型为ResponseEntity不一定都是下载,因为从类的名字就能看出，它
     * 叫响应体，很笼统，后续需要更精准判断，比如再判断一层泛型，或者判断方法名称、接口url等
     */
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        Class<?> parameterType = returnType.getParameterType();
        return parameterType != ResponseEntity.class;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (body instanceof Ret) {
            return body;
        } else {
            return Ret.ok(body);
        }
    }
}
