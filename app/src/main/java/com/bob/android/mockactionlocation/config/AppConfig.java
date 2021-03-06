package com.bob.android.mockactionlocation.config;

import android.os.Environment;

/**
 * @package com.bob.android.mockactionlocation.config
 * @fileName AppConfig
 * @Author Bob on 2018/12/21 17:35.
 * @Describe TODO
 */
public class AppConfig {

    /**
     * @Field testMode : 运行模式 测试与正式区分
     */
    public static boolean testMode = false;

    /**
     * @Fields BASE_URL:Web根路径
     */
    public static final String TEST_URL = "http://172.20.26.195:8081/";
    public static final String RELEASE_URL = "https://qpszzs.police.sh.cn/";

    public static final String BASE_URL = testMode?TEST_URL:RELEASE_URL;

    /**
     * @Fields KEY_PASSWORD:AES加密密码
     */
    public static final String PACKAGE_NAME = "com.bob.android.mockactionlocation";

    /**
     * @Fields ENCRYPT_REQUEST_PARAM:加密参数名
     */
    public static final String ENCRYPT_REQUEST_PARAM = "jsonParam";

    /**
     * @Fields ENCRYPT_KEY_PARAM:加密秘钥
     */
    public static final String ENCRYPT_KEY_PARAM = "keyParam";

    /**
     * @Fields UNENCRYPTED_REQUEST_PARAM:非加密参数名
     */
    public static final String UNENCRYPTED_REQUEST_PARAM = "fileParam";

    public static String DEFAULT_CITY = "中国";
    public static final String EXTRA_TIP = "ExtraTip";
    public static String MAP_KEY_WORD = "1c35b2ddc7b93dbe13f081cec661b19b";
    /**
     * @Fields PERMISSION_REQUEST_CODE:请求权限的返回CODE
     */
    public static final int PERMISSION_REQUEST_CODE = 10001;

    /**
     * @Fields APP_FOLDER_NAME:程序目录
     */
    public static final String APP_FOLDER_NAME = "mock_action_location";

    /**
     * @Fields TEMP_FOLDER_NAME:临时文件文件夹目录
     */
    public static final String TEMP_FOLDER_NAME = "temp";

    /**
     * @Fields TEMP_FOLDER_NAME:临时文件文件夹目录
     */
    public static final String FILE_FOLDER_NAME = "files";
    /**
     * 崩溃日志保存地址
     */
    public static final String CRASH_LOG_PATH = "sdcard/crash/";

    /**
     * @Fields ROOT_PATH : 设备存储根路径
     */
    public static final String ROOT_PATH = Environment
            .getExternalStorageDirectory().getPath();

    public static final String getTempPath() {
        return ROOT_PATH.concat("/").concat(APP_FOLDER_NAME).concat("/").concat(TEMP_FOLDER_NAME);
    }

    public static final String UPOAD_IMAGE_PATH =AppConfig.BASE_URL+"/app/showImage?url=";

}
