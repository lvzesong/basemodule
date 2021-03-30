package com.base.basemodule.di.serviceImpl;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.base.basemodule.BaseApplication;
import com.base.basemodule.di.service.IRetrofitService;
import com.base.basemodule.net.cookie.PersistentCookieJar;
import com.base.basemodule.net.cookie.cache.SetCookieCache;
import com.base.basemodule.net.cookie.persistence.SharedPrefsCookiePersistor;
import com.base.basemodule.net.logger.JsonUtil;
import com.base.basemodule.net.logger.Logutil;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by lzs on 2019/5/10.
 * E-Mail Address：343067508@qq.com
 **/
@Route(path = "base/net/Retrofit", name = "Retrofit服务")
public class RetrofitServiceImpl implements IRetrofitService {
    private static final String TAG = "RetrofitModule";
    private static final String COOKIES = "COOKIES";

    private PersistentCookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(BaseApplication.getApplication()));
    private static Retrofit retrofit;

    @Override
    public Retrofit getRetrofit() {
        if (retrofit == null) {
            HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor(new HttpLogger());
            logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder()
                    .addNetworkInterceptor(BaseApplication.getApplication().getAppConfig().getNetConfig().getInterceptor() != null ? BaseApplication.getApplication().getAppConfig().getNetConfig().getInterceptor() : new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request originalRequest = chain
                                    .request()
                                    .newBuilder()
                                    .addHeader("Connection", "close")
                                    .addHeader("Accept-Encoding", "identity")
                                    .build();
                            Response response = chain.proceed(originalRequest);
                            return response;
                        }
                    })
                    //.cookieJar() //cookie  domain 需一致
                    // .addInterceptor(new LogInterceptor())
                    .addInterceptor(logInterceptor)
                    //.cache(new Cache())
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(false) //取消自动重连
                    // .addNetworkInterceptor(new StethoInterceptor())
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BaseApplication.getApplication().getAppConfig().getNetConfig().getBaseUrl())
                    //.addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(BaseApplication.getApplication().getAppConfig().getNetConfig().getJsonConverterFactory() == null ? GsonConverterFactory.create() : BaseApplication.getApplication().getAppConfig().getNetConfig().getJsonConverterFactory())//
                    //.addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }

    @Override
    public void init(Context context) {

    }

    private class HttpLogger implements HttpLoggingInterceptor.Logger {
        private StringBuilder mMessage = new StringBuilder();

        @Override
        public void log(String message) {
            // 请求或者响应开始
            if (message.startsWith("--> POST")) {
                mMessage.setLength(0);
            }
            // 以{}或者[]形式的说明是响应结果的json数据，需要进行格式化
            if ((message.startsWith("{") && message.endsWith("}"))
                    || (message.startsWith("[") && message.endsWith("]"))) {
                message = JsonUtil.formatJson(message);
            }
            mMessage.append(message.concat("\n"));
            // 请求或者响应结束，打印整条日志
            if (message.startsWith("<-- END HTTP")) {
                Logutil.e(mMessage.toString(), null);
                mMessage.setLength(0);
            }
        }
    }
}
