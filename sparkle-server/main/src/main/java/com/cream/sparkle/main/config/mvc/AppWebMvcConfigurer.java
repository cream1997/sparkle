package com.cream.sparkle.main.config.mvc;

import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.support.config.FastJsonConfig;
import com.alibaba.fastjson2.support.spring6.http.converter.FastJsonHttpMessageConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

@Slf4j
@Configuration
public class AppWebMvcConfigurer implements WebMvcConfigurer {

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        //自定义配置...
        FastJsonConfig config = new FastJsonConfig();
        config.setDateFormat("yyyy-MM-dd HH:mm:ss");
        config.setReaderFeatures(JSONReader.Feature.FieldBased, JSONReader.Feature.SupportArrayToBean);
        // ※最后一个是配置long类型转换为string，避免前端丢失精度
        config.setWriterFeatures(JSONWriter.Feature.WriteMapNullValue, JSONWriter.Feature.PrettyFormat, JSONWriter.Feature.WriteLongAsString);

        converter.setFastJsonConfig(config);
        converter.setDefaultCharset(StandardCharsets.UTF_8);
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.APPLICATION_JSON));
        converters.addFirst(converter);

        /*
        todo 长期观察是否有其它代价
        为什么要做这一步？
        因为为了代码书写简洁，我将所有controller返回值的包装都放到了GlobalResBodyAdvice中进行(包装一层Result)
        这样做能比较简洁的达到我想要的效果，但是它忽略了一些spring应用的上下文耦合; 换句话说，返回值类型与实际不符会导致某些地方的运行不正常；
        以下处理就是在解决我发现的一处问题，在controller的返回值是String时,convert时优先选择了StringHttpMessageConverter,但它的实际值
        是我的包装类型，stringConvert无法处理;
        FastJsonConvert可以处理我的包装对象，同时它也可以处理String类型的返回；所以我选择将StringHttpMessageConverter从转换器列表中删除，
        以避免应用异常； 尽管这种方式可能不是最好，也许还会有一些其他代价，但我目前确实没办法了。修改如下后，目前一切正常！期待我发现新的问题，再来
        解决这个麻烦~
         */
        converters.removeIf(it -> it instanceof StringHttpMessageConverter);
    }

    /**
     * 跨域配置
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 完全放开跨域
        registry
                .addMapping("/**") //添加映射路径
                .allowCredentials(true) //是否发送Cookie
                .allowedOriginPatterns("*") //允许跨域的域名，*代表允许任何域名
                .allowedMethods("*") //允许任何方法（post、get等）
                .exposedHeaders("*"); //允许任何头
    }
}
