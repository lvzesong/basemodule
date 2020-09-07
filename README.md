# basemodule

## 项目介绍
**基于google jetpack 和 databinding 封装的mvvm框架,使用到的第三方框架有Rxjava3,Retrofit3,okhttp3,Arouter,Glide,fresco等,便于快速开发功能界面**


### 使用方法
1. 继承MyBaseApplication,实现createAppConfig方法，其中AppConfig需要设置一个NetConfig，NetConfig包含RESTFul的BaseUrl,也可以设置请求拦截器（withNetWorkInterceptor），需要自己根据项目的接口数据结构实现网络请求响应的json转换器（withJsonConverterFactory）


implementation 'com.github.lvzesong:basemodule:Tag'
