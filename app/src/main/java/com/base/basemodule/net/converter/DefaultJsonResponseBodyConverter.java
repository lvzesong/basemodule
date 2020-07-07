package com.base.basemodule.net.converter;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import okhttp3.ResponseBody;
import retrofit2.Converter;

final class DefaultJsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final TypeAdapter<T> adapter;

    DefaultJsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) {
//        try {
//            JSONObject object = new JSONObject(value.string());
//            //Log.e("lzs", "JsonResponseBodyConverter :" + object.toString());
//            int code = object.getInt("code");

//            if (code != ApiConstants.SUCCESS) {
//                ApiException apiException = new ApiException(object.optString("msg"));
//                apiException.setCode(code);
//                if (object.has("code")) {
//                    String code2 = object.getString("code");
//                    if ((ApiConstants.AUTH_FAILED + "").equals(code2)) {
//                        //登录权限认证失败,通知事件,重新登录
//                        Intent intent = new Intent();
//                        intent.setAction(ApiConstants.AUTH_FAILED + "");
//                        LocalBroadcastManager.getInstance(BaseApplication.getApplication()).sendBroadcast(intent);
//                        // LiveDataBus.get().with(ApiConstants.AUTH_FAILED + "").setValue("");
//                    }
//                    try {
//                        //将 code 值传递给 apiex
//                        int codeInt = Integer.valueOf(code2);
//                        apiException.setCode(codeInt);
//                    } catch (Exception e) {
//
//                    }
//                }
        //请求数据不成功,其他情况
//                throw apiException;
//            } else {
//                ByteArrayInputStream inputStream = null;
//                Object obj = object.get("data");
//                if (obj instanceof JSONArray) {
//                    JSONArray dataObject = object.getJSONArray("data");
//                    inputStream = new ByteArrayInputStream(dataObject.toString().getBytes());
//                } else if (obj instanceof JSONObject) {
//                    JSONObject dataObject = object.getJSONObject("data");
//                    inputStream = new ByteArrayInputStream(dataObject.toString().getBytes());
//                } else if (obj instanceof String) {
//                    //接口有问题
//                    ApiException apiException = new ApiException("Data parsing exception");
//                    throw apiException;
//                }
//                Reader reader = new InputStreamReader(inputStream);
//                JsonReader jsonReader = gson.newJsonReader(reader);
//                T result = adapter.read(jsonReader);
//                if (jsonReader.peek() != JsonToken.END_DOCUMENT) {
//                    throw new JsonIOException("JSON document was not fully consumed.");
//                }
//                return result;
//            }
//        } catch (Exception e) {
//            // Log.e("lzs", "JsonResponseBodyConverter:" + e.getMessage() + "   " + e.getClass().getSimpleName());
//            throw new ApiException(e);
//        }
        return null;
    }
}
