package com.base.basemodule.config;

import okhttp3.Interceptor;
import retrofit2.Converter;


/**
 * 网络相关配置
 */
public class NetConfig {
    //RESTful 的 baseurl  需/结尾
    private String baseUrl = "";

    public Converter.Factory getJsonConverterFactory() {
        return jsonConverterFactory;
    }

    Converter.Factory jsonConverterFactory;

    public Interceptor getInterceptor() {
        return interceptor;
    }

    //拦截器
    Interceptor interceptor;

    private NetConfig() {
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public static final class NetConfigBuilder {
        Converter.Factory jsonConverterFactory;
        //拦截器
        Interceptor interceptor;
        //RESTful 的 baseurl  需/结尾
        private String baseUrl = "";

        public NetConfigBuilder() {
        }

        public static NetConfigBuilder anNetConfig() {
            return new NetConfigBuilder();
        }

        public NetConfigBuilder withBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public NetConfigBuilder withJsonConverterFactory(Converter.Factory jsonConverterFactory) {
            this.jsonConverterFactory = jsonConverterFactory;
            return this;
        }

        public NetConfigBuilder withNetWorkInterceptor(Interceptor interceptor) {
            this.interceptor = interceptor;
            return this;
        }

        public NetConfig build() {
            NetConfig netConfig = new NetConfig();
            netConfig.setBaseUrl(baseUrl);
            netConfig.jsonConverterFactory = this.jsonConverterFactory;
            netConfig.interceptor = this.interceptor;
            return netConfig;
        }
    }
}
