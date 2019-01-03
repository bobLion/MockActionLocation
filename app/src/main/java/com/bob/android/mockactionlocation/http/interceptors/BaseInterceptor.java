package com.bob.android.mockactionlocation.http.interceptors;

import com.bob.android.mockactionlocation.application.GlobalApplication;
import com.bob.android.mockactionlocation.config.AppConfig;
import com.bob.android.mockactionlocation.encrypt.AESEncrypt;
import com.bob.android.mockactionlocation.encrypt.AppEncryptUtils;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * @package com.bob.android.mockactionlocation.http.interceptors
 * @fileName BaseInterceptor
 * @Author Bob on 2019/1/3 9:45.
 * @Describe TODO
 */
public class BaseInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        /*String credentials = ":";
        String basic = "Basic " +_WRAP);*/
        String ip = GlobalApplication.getInstance().getIp();
//        String authorization = GlobalApplication.getInstance().getUserInfoEntity()==null?"":GlobalApplication.getInstance().getUserInfoEntity().getAuthorization();
        Request originalRequest = chain.request();
        String cacheControl = originalRequest.cacheControl().toString();
        Request.Builder requestBuilder = originalRequest.newBuilder() //Basic Authentication,也可用于token验证,OAuth验证
//                .header("Authorization", authorization)
                .header("Accept", "application/json")
                .addHeader("packageName", AppConfig.PACKAGE_NAME)
                .addHeader("ip", ip)
                .addHeader("os", "Android").method(originalRequest.method(), originalRequest.body());
        if (originalRequest.body() instanceof FormBody) {
            FormBody.Builder newFormBody = new FormBody.Builder();
            FormBody oldFormBody = (FormBody) originalRequest.body();
            String key = AESEncrypt.randomKey();
            for (int i = 0; i < oldFormBody.size(); i++) {
                String name = oldFormBody.encodedName(i);
                String value = oldFormBody.value(i);

                if (name.equals(AppConfig.ENCRYPT_REQUEST_PARAM)) {
                    try {
                        //加密内容
                        newFormBody.addEncoded(name, AppEncryptUtils.encodeParam(key, value));
                        newFormBody.addEncoded(AppConfig.ENCRYPT_KEY_PARAM, AppEncryptUtils.encodeKey(key));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (name.equals(AppConfig.UNENCRYPTED_REQUEST_PARAM)) {
                    //图片信息不加密
                    newFormBody.addEncoded(name, value);
                } else if (name.equals(AppConfig.ENCRYPT_KEY_PARAM)) {
//                    //加密密钥
//                    try {
//                        newFormBody.addEncoded(name, AppEncryptUtils.encodeKey(key));
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
                } else {
                    newFormBody.addEncoded(name, value);
                }
            }
            requestBuilder.method(originalRequest.method(), newFormBody.build());
        } else if (originalRequest.body() instanceof MultipartBody) { // 文件
//            RequestBody newRequestBody;
//            MultipartBody requestBody = (MultipartBody) originalRequest.body();
//            MultipartBody.Builder multipartBodybuilder = new MultipartBody.Builder();
//            if (requestBody != null) {
//                for (int i = 0; i < requestBody.size(); i++) {
//                    MultipartBody.Part part = requestBody.part(i);
//                    multipartBodybuilder.addPart(part);
//
//          /*
//           上传文件时，请求方法接收的参数类型为RequestBody或MultipartBody.Part参见ApiService文件中uploadFile方法
//           RequestBody作为普通参数载体，封装了普通参数的value; MultipartBody.Part即可作为普通参数载体也可作为文件参数载体
//           当RequestBody作为参数传入时，框架内部仍然会做相关处理，进一步封装成MultipartBody.Part，因此在拦截器内部，
//           拦截的参数都是MultipartBody.Part类型
//           */
//
//          /*
//           1.若MultipartBody.Part作为文件参数载体传入，则构造MultipartBody.Part实例时，
//           需使用MultipartBody.Part.createFormData(String name, @Nullable String filename, RequestBody body)方法，
//           其中name参数可作为key使用(因为你可能一次上传多个文件，服务端可以此作为区分)且不能为null，
//           body参数封装了包括MimeType在内的文件信息，其实例创建方法为RequestBody.create(final @Nullable MediaType contentType, final File file)
//           MediaType获取方式如下：
//           String fileType = FileUtil.getMimeType(file.getAbsolutePath());
//           MediaType mediaType = MediaType.parse(fileType);
//
//           2.若MultipartBody.Part作为普通参数载体，建议使用MultipartBody.Part.createFormData(String name, String value)方法创建Part实例
//            name可作为key使用，name不能为null,通过这种方式创建的实例，其RequestBody属性的MediaType为null；当然也可以使用其他方法创建
//           */
//
//          /*
//           提取非文件参数时,以RequestBody的MediaType为判断依据.
//           此处提取方式简单暴力。默认part实例的RequestBody成员变量的MediaType为null时，part为非文件参数
//           前提是:
//           a.构造RequestBody实例参数时，将MediaType设置为null
//           b.构造MultipartBody.Part实例参数时,推荐使用MultipartBody.Part.createFormData(String name, String value)方法，或使用以下方法
//            b1.MultipartBody.Part.create(RequestBody body)
//            b2.MultipartBody.Part.create(@Nullable Headers headers, RequestBody body)
//            若使用方法b1或b2，则要求
//
//           备注：
//           您也可根据需求修改RequestBody的MediaType，但尽量保持外部传入参数的MediaType与拦截器内部添加参数的MediaType一致，方便统一处理
//           */
//                    MediaType mediaType = part.body().contentType();
//                   String  normalParamValue1 = getParamContent(requestBody.part(i).body());
//                    if (mediaType == null) {
//                        String normalParamKey;
//                        String normalParamValue;
//                        try {
//                            normalParamValue = getParamContent(requestBody.part(i).body());
//                            Headers headers = part.headers();
//                            if (!TextUtils.isEmpty(normalParamValue) && headers != null) {
//                                for (String name : headers.names()) {
//                                    String headerContent = headers.get(name);
//                                    if (!TextUtils.isEmpty(headerContent)) {
//                                        String[] normalParamKeyContainer = headerContent.split("name=\"");
//                                        if (normalParamKeyContainer.length == 2) {
//                                            normalParamKey = normalParamKeyContainer[1].split("\"")[0];
//                                            break;
//                                        }
//                                    }
//                                }
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }
//
//            newRequestBody = multipartBodybuilder.build();
//            requestBuilder.method(originalRequest.method(), newRequestBody);
        } else {

        }

        Request request = requestBuilder.build();

        Response originalResponse = chain.proceed(request);
        Response.Builder responseBuilder = //Cache control设置缓存
                originalResponse.newBuilder().header("Cache-Control", cacheControl);
        return responseBuilder.build();

    }

    /**
     * 获取常规post请求参数
     */
    private String getParamContent(RequestBody body) throws IOException {
        Buffer buffer = new Buffer();
        body.writeTo(buffer);
        return buffer.readUtf8();
    }
}
