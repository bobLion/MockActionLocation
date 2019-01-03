package com.bob.android.mockactionlocation.http;

import com.bob.android.mockactionlocation.config.AppConfig;
import com.bob.android.mockactionlocation.http.convertor.ResponseConverterFactory;
import com.bob.android.mockactionlocation.http.interceptors.BaseInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * @package com.bob.android.mockactionlocation.http
 * @fileName RestCreator
 * @Author Bob on 2019/1/3 9:44.
 * @Describe TODO
 */
public class RestCreator {

    /**
     * 构建全局Retrolfit
     */
    private static final class RetrolfitHolder {
        private static final Retrofit RESTROFIT_CLIENT = new Retrofit.Builder()
                .baseUrl(AppConfig.BASE_URL)
                .client(OKHttpHolder.OK_HTTP_HOLDER)
                .addConverterFactory(ResponseConverterFactory.create())
                .build();
    }

    /**
     * 构建全局Okhttp
     */
    private static final class OKHttpHolder {
        static BaseInterceptor interceptor = new BaseInterceptor();
        private static final OkHttpClient OK_HTTP_HOLDER = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .retryOnConnectionFailure(true)
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();
    }

    /**
     * Service接口
     */
    private static final class RestServiceHolder {
        private static final RestService REST_SERVICE =
                RetrolfitHolder.RESTROFIT_CLIENT.create(RestService.class);
    }

    public static RestService getRestService() {
        return RestServiceHolder.REST_SERVICE;
    }
}
