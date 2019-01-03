package com.bob.android.mockactionlocation.http.convertor;

import com.bob.android.mockactionlocation.http.ResponseResult;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * @package com.bob.android.mockactionlocation.http.convertor
 * @fileName ResponseConverterFactory
 * @Author Bob on 2019/1/3 9:56.
 * @Describe TODO
 */
public class ResponseConverterFactory extends Converter.Factory{
    public static final ResponseConverterFactory INSTANCE = new ResponseConverterFactory();
    public static ResponseConverterFactory create(){
        return INSTANCE;
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        if (type == ResponseResult.class){
            return ResponseConverter.INSTANCE;
        }
        return null;
    }
}
