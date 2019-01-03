package com.bob.android.mockactionlocation.http.convertor;

import com.alibaba.fastjson.JSON;
import com.bob.android.mockactionlocation.encrypt.AESEncrypt;
import com.bob.android.mockactionlocation.encrypt.RSAPublicKeyUtils;
import com.bob.android.mockactionlocation.http.ResponseResult;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * @package com.bob.android.mockactionlocation.http.convertor
 * @fileName ResponseConverter
 * @Author Bob on 2019/1/3 9:57.
 * @Describe TODO
 */
public class ResponseConverter implements Converter<ResponseBody, ResponseResult> {
    public static final ResponseConverter INSTANCE = new ResponseConverter();

    @Override
    public ResponseResult convert(ResponseBody value) throws IOException {
        String mResponseBody=value.string();
        ResponseResult mResponseResult = JSON.parseObject(mResponseBody, ResponseResult.class);

        String key = mResponseResult.getCheckCode();
        try {
            String decodeKey = RSAPublicKeyUtils.decodeKey(key);
            String content = AESEncrypt.decode(decodeKey, mResponseResult.getContent());
            mResponseResult.setContent(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mResponseResult;
    }
}
