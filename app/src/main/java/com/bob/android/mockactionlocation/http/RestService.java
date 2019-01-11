package com.bob.android.mockactionlocation.http;

import android.graphics.Bitmap;
import android.os.Environment;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bob.android.mockactionlocation.config.AppConfig;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * @package com.bob.android.mockactionlocation.http
 * @fileName RestService
 * @Author Bob on 2019/1/3 9:45.
 * @Describe TODO
 */
public interface RestService {
    /**
     * 用户登录
     * @param
     * @return
     */
    @FormUrlEncoded
    @POST("/sip3/login")
    Call<ResponseResult> login(@Field(AppConfig.ENCRYPT_REQUEST_PARAM) String jsonParam);

    /**
     * 获取人脸查人数据:静态人脸
     * @param
     * @return
     */
    @FormUrlEncoded
    @POST("/Analysisbsd/Compare")
    Call<ResponseResult> getRLCRInfo(@Field(AppConfig.ENCRYPT_REQUEST_PARAM) String jsonParam);

    /***
     * 普通接口请求示例
     */
    /*GetStaticFaceDataParam getStaticFaceDataParam = new GetStaticFaceDataParam();
        getStaticFaceDataParam.setIds("");
        getStaticFaceDataParam.setImgType(imgUrl == null? "1":"2");//1 base64 2 url
        getStaticFaceDataParam.setImg(base64Image);
        getStaticFaceDataParam.setThreshold(AppConfig.RLCR_SIMILARITY);//人脸图片查人：静态人脸相似度
        getStaticFaceDataParam.setPageNum("1");
        getStaticFaceDataParam.setPageSize("10");
    Call<ResponseResult> call = RestCreator.getRestService().getRLCRInfo(JSON.toJSONString(getStaticFaceDataParam));
        call.enqueue(new Callback<ResponseResult>() {
        @Override
        public void onResponse(Call<ResponseResult> call, Response<ResponseResult> response) {
            if(null != loadingDialog){
                loadingDialog.dismiss();
            }
            ResponseResult result = response.body();
            if (result != null && result.getResponseCode().equals(ResponseResult.SUCCEED_CODE)) {

            } else if (result != null && ResponseResult.LOAD_NO_MORE.equals(result.getResponseCode())) {
                ToastUtils.show(mContext, result.getMessage());
            } else if (result != null && ResponseResult.DATA_EMPTY_CODE.equals(result.getResponseCode())) {
                ToastUtils.show(mContext, result.getMessage());
            } else if (result != null && ResponseResult.PARAMS_ERROR_CODE.equals(result.getResponseCode())) {
                ToastUtils.show(mContext, result.getMessage());
            } else if (result != null && ResponseResult.SERVER_ERROR_CODE.equals(result.getResponseCode())) {
                ToastUtils.show(mContext, "未找到相似人脸！");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finish();
            }  else if (result != null && ResponseResult.IDENTITY_ERROR_CODE.equals(result.getResponseCode())) {
                ToastUtils.show(mContext, result.getMessage());
                ActivityManager.getInstance(
                        mContext).finishAllAcitivty();
                ActivityManager.getInstance(
                        mContext).goLogin(mContext);
            }else {
                ToastUtils.show(mContext, "未找到相似人脸！");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finish();
            }
        }

        @Override
        public void onFailure(Call<ResponseResult> call, Throwable t) {
            if(null != loadingDialog){
                loadingDialog.dismiss();
            }
            ToastUtils.show(mContext, getString(R.string.msg_server_error));
        }

    });*/


    /**
     * 上传布控图片
     * */
    @Multipart
    @POST("/zuul/Sys-Operation/upload")
    Call<ResponseResult> uploadControlPic(@Part MultipartBody.Part img);
    /***
     * 上传文件示例
     */
   /* imgPath = String.valueOf(msg.obj);
    uploadFile =(File) msg.obj;
    RequestBody requestFile = null;
    MultipartBody.Part part = null;
    boolean isPicDamage = checkImgDamage(uploadFile.getAbsolutePath());
                    if(isPicDamage){
        ToastUtils.show(mContext,"图片已损坏,请选择其他照片！");
        mImgControlMac.setImageDrawable(getResources().getDrawable(R.mipmap.broken_image));
        return;
    }
    uploadFile = new CompressHelper.Builder(mContext)
            .setMaxWidth(720)
                            .setMaxHeight(1080)
                            .setQuality(80)
                            .setCompressFormat(Bitmap.CompressFormat.JPEG)
                            .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory
                                    (Environment.DIRECTORY_PICTURES).getAbsolutePath())
            .build()
                            .compressToFile(uploadFile);
    requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), uploadFile);
    part = MultipartBody.Part.createFormData(AppConfig.CONTROL_UPLOAD_REQUEST_PARAM, uploadFile.getName(), requestFile);
                    if(null != part){
        Call<ResponseResult> call = RestCreator.getRestService().uploadControlPic(part);
        call.enqueue(new retrofit2.Callback<ResponseResult>() {

            @Override
            public void onResponse(Call<ResponseResult> call, Response<ResponseResult> response) {
                ResponseResult result = response.body();
                if (result != null && ResponseResult.SUCCEED_CODE.equals(result.getResponseCode())) {
                    Param param = JSON.parseObject(result.getContent(),Param.class);
                    dispositionImageURL = param.getUrl();
                }else if (result != null && ResponseResult.SERVER_ERROR_CODE.equals(result.getResponseCode())) {
                    ToastUtils.show(mContext, getString(R.string.msg_interface_error));
                }else{
                    dispositionImageURL = null;
                    Toast.makeText(mContext,"图片上传失败",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseResult> call, Throwable t) {
                dispositionImageURL = null;
                Toast.makeText(mContext,"图片上传失败",Toast.LENGTH_LONG).show();
            }
        });*/


    /**
     * 新增案件线索
     * @param
     * @return
     */
    @Multipart
    @POST("/ajxxapp/xs/save")
    Call<ResponseResult> addCaseClue(@Part MultipartBody.Part img,@Part (AppConfig.ENCRYPT_REQUEST_PARAM)
            RequestBody Content,@Part (AppConfig.ENCRYPT_KEY_PARAM) RequestBody key);
   /*

    UpdateCaseClueEntity entity = new UpdateCaseClueEntity();
        entity.setMblksj(mblksj).setMbcxsj(mbcxsj).setSpzl(spzl).setMbmc(mbmc).setMbms(mbms)
                .setXb(xb).setXm(xm).setZjhm(zjh).setSyks(sy).setKzks(xy).setFsw(fsw).setQzlb(qzlb)
                .setXylb(xylx).setZldj(zldj).setJd(jd).setWd(wd).setNlsx(nlsx).setNlxx(nlxx);

    File file = null;
    RequestBody requestFile = null;
    MultipartBody.Part part = null;
    UpdateCaseClueParam param = new UpdateCaseClueParam();
        if (null != stringBitmapPath && !"".equals(stringBitmapPath)) {
        File file1 = new File(stringBitmapPath);
        file = new CompressHelper.Builder(mContext)
                .setMaxWidth(720)
                .setMaxHeight(1080)
                .setQuality(80)
                .setCompressFormat(Bitmap.CompressFormat.JPEG)
                .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory
                        (Environment.DIRECTORY_PICTURES).getAbsolutePath())
                .build()
                .compressToFile(file1);
        requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        part = MultipartBody.Part.createFormData(AppConfig.CASE_CLUE_UNENCRYPTED_REQUEST_PARAM, file.getName(), requestFile);
        param.setFileName(file.getName());
    }else{
        ToastUtils.show(mContext, "请上传照片！");
        return;
    }

        param.setBzlx("1");
        param.setNbzlx("1");
        param.setFileName("picture.jpg");
        param.setFileType("1");
        param.setQzlx("2");
        param.setCjr(mUserInfoEntity.getUserCode());
        param.setFileData(JSON.toJSONString(entity));
        if(null == loadingDialog){
        loadingDialog = DialogManager.getLoadingDialog(mContext, true);
    }
        loadingDialog.show();
    String key = AESEncrypt.randomKey();
    Call<ResponseResult> call = null;
        try {
        call = RestCreator.getRestService()
                .addCaseClue(part, stringtoRequestBody(AppEncryptUtils.encodeParam(key, JSON.toJSONString(param), false)),
                        stringtoRequestBody(AppEncryptUtils.encodeKey(key, false)));
        call.enqueue(new Callback<ResponseResult>() {
            @Override
            public void onResponse(Call<ResponseResult> call, Response<ResponseResult> response) {
                if(null != loadingDialog){
                    loadingDialog.dismiss();
                }
                ResponseResult result = response.body();
                if (result != null && result.getResponseCode().equals(ResponseResult.SUCCEED_CODE)) {
                    ToastUtils.show(mContext, "上传成功！");
                    clearPage();
                } else if (result != null && ResponseResult.LOAD_NO_MORE.equals(result.getResponseCode())) {
                    ToastUtils.show(mContext, result.getMessage());
                } else if (result != null && ResponseResult.DATA_EMPTY_CODE.equals(result.getResponseCode())) {
                    ToastUtils.show(mContext, result.getMessage());
                } else if (result != null && ResponseResult.PARAMS_ERROR_CODE.equals(result.getResponseCode())) {
                    ToastUtils.show(mContext, result.getMessage());
                }else if (result != null && ResponseResult.SERVER_ERROR_CODE.equals(result.getResponseCode())) {
                    ToastUtils.show(mContext, getString(R.string.msg_interface_error));
                } else if (result != null && ResponseResult.IDENTITY_ERROR_CODE.equals(result.getResponseCode())) {
                    ToastUtils.show(mContext, result.getMessage());
                    ActivityManager.getInstance(
                            mContext).finishAllAcitivty();
                    ActivityManager.getInstance(
                            mContext).goLogin(mContext);
                } else {
                    ToastUtils.show(mContext, getString(R.string.msg_server_error));
                }
            }

            @Override
            public void onFailure(Call<ResponseResult> call, Throwable t) {
                if(null != loadingDialog){
                    loadingDialog.dismiss();
                }
                ToastUtils.show(mContext, "上传失败！");
                return;
            }
        });*/

}
