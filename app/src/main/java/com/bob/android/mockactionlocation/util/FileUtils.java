package com.bob.android.mockactionlocation.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.os.Environment;

import com.alibaba.fastjson.JSON;
import com.bob.android.mockactionlocation.config.AppConfig;
import com.bob.android.mockactionlocation.entity.CityEntity;
import com.bob.android.mockactionlocation.entity.ProvinceEntity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

/**
 * @package com.bob.android.mockactionlocation.util
        * @fileName FileUtils
        * @Author Bob on 2018/12/23 11:19.
        * @Describe TODO
 */
public class FileUtils {
    /**
     * @param filePath 文件路径
     * @return 如果是文件则返回文件是否存在，如果是文件夹则返回false
     * @Description ：判断文件是否存在
     */
    public static boolean isFileExist(String filePath) {
        if (StringUtils.isBlank(filePath)) {
            return false;
        }

        File file = new File(filePath);
        return (file.exists() && file.isFile());
    }

    public static boolean copyFileFromAssets(Context context, String fileName, String path) {
        boolean copyIsFinish = false;
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            AssetManager assetManager = context.getAssets();
            is= assetManager.open(fileName);
            File file = new File(path);
            File parentFile = file.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }
            if(!file.exists()){
                file.createNewFile();
            }
            fos = new FileOutputStream(file);
            byte[] temp = new byte[1024];
            int i = 0;
            while ((i = is.read(temp)) > 0) {
                fos.write(temp, 0, i);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            copyIsFinish = true;
        }
        return copyIsFinish;
    }

    /**
     * @param bitmap         待保存图片
     * @param targetFilePath 图片保存目标地址
     * @return 保存成功返回true，否则返回false
     * @Description 保存图片
     */
    public static boolean saveBitmap(Bitmap bitmap, String targetFilePath) {
        makeFile(targetFilePath);
        File file = new File(targetFilePath);
        if (file.exists()) {
            try {
                FileOutputStream fos = new FileOutputStream(file);
                if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)) {
                    fos.flush();
                    fos.close();
                }
                return true;
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
                return false;
            } catch (IOException e2) {
                e2.printStackTrace();
                return false;
            }
        }
        return false;
    }

    /**
     * @param filePath 文件路径
     * @return 创建成功返回true，否则返回false
     * @Description 创建文件，如果上层文件夹不存在，则会自动创建文件夹
     */
    public static boolean makeFile(String filePath) {
        String folderName = getFolderName(filePath);
        makeDirs(folderName);
        File file = new File(filePath);
        if ((file.exists() && file.isDirectory())) {
            file.delete();
        }
        if (!file.exists()) {
            try {
                return file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public static String getFolderName(String filePath) {

        if (StringUtils.isEmpty(filePath)) {
            return filePath;
        }

        int filePosi = filePath.lastIndexOf(File.separator);
        return (filePosi == -1) ? "" : filePath.substring(0, filePosi);
    }

    /**
     * @param filePath 文件路径或者文件夹路径
     * @return 创建文件夹成功返回true，否则返回false
     * @Description 创建文件夹
     */
    public static boolean makeDirs(String filePath) {
        File file = new File(filePath);
        if (file.isFile()) {
            String folderName = getFolderName(filePath);
            if (StringUtils.isEmpty(folderName)) {
                return false;
            }
            File folder = new File(folderName);
            return (folder.exists() && folder.isDirectory()) ? true : folder
                    .mkdirs();
        } else {
            return (file.exists() && file.isDirectory()) ? true : file.mkdirs();
        }
    }

    /**
     * @param path 文件或者文件夹全路径
     * @return 删除成功返回true，否则返回false
     * @Description 删除文件或者文件夹，如果是文件则删除文件，如果是文件夹则递归删除文件夹下面的所有文体，删除文件后删除文件夹
     */
    public static boolean deleteFile(String path) {
        if (StringUtils.isBlank(path)) {
            return true;
        }
        File file = new File(path);
        if (!file.exists()) {
            return true;
        }
        if (file.isFile()) {
            return file.delete();
        }
        if (!file.isDirectory()) {
            return false;
        }
        for (File f : file.listFiles()) {
            if (f.isFile()) {
                f.delete();
            } else if (f.isDirectory()) {
                deleteFile(f.getAbsolutePath());
            }
        }
        return file.delete();
    }


    /**
     *  从assets目录中复制整个文件夹内容
     *  @param  context  Context 使用CopyFiles类的Activity
     *  @param  oldPath  String  原文件路径  如：/aa
     *  @param  newPath  String  复制后路径  如：xx:/bb/cc
     * private String fileName = "amap_adcode_citycode.xls";
        private String localFilePath = AppConfig.ROOT_PATH.concat("/")
    .   concat(AppConfig.APP_FOLDER_NAME).concat("/").concat(AppConfig.FILE_FOLDER_NAME) + "/amap_adcode_citycode.xls";
        String cityStr = FileUtils.readFileReturnJson(mContext,fileName,localFilePath);
     */
    public void copyFilesFassets(Context context,String oldPath,String newPath) {
        try {
            String fileNames[] = context.getAssets().list(oldPath);//获取assets目录下的所有文件及目录名
            if (fileNames.length > 0) {//如果是目录
                File file = new File(newPath);
                file.mkdirs();//如果文件夹不存在，则递归
                for (String fileName : fileNames) {
                    copyFilesFassets(context,oldPath + "/" + fileName,newPath+"/"+fileName);
                }
            } else {//如果是文件
                InputStream is = context.getAssets().open(oldPath);
                FileOutputStream fos = new FileOutputStream(new File(newPath));
                byte[] buffer = new byte[1024];
                int byteCount=0;
                while((byteCount=is.read(buffer))!=-1) {//循环从输入流读取 buffer字节
                    fos.write(buffer, 0, byteCount);//将读取的输入流写入到输出流
                }
                fos.flush();//刷新缓冲区
                is.close();
                fos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取asset文件夹下的excel表格，转换成json
     * @param context
     * @param fileName 在asset文件夹下的文件名称
     * @param localFilePath 想要读取到本地的绝对路径
     * @return
     *
     * 使用方法
     *   FileUtils.copyFileFromAssets(context,"amap_adcode_citycode.xls",
     *       AppConfig.ROOT_PATH.concat("/").concat(AppConfig.APP_FOLDER_NAME).concat("/").concat(AppConfig.FILE_FOLDER_NAME)+ "/amap_adcode_citycode.xls");
     *       String filePath = AppConfig.ROOT_PATH.concat("/").concat(AppConfig.APP_FOLDER_NAME).concat("/").concat(AppConfig.FILE_FOLDER_NAME)+ "/amap_adcode_citycode.xls";
     * @Fields ROOT_PATH : 设备存储根路径
     * ROOT_PATH = Environment.getExternalStorageDirectory().getPath();
     */
    public static String readAssetExcelFileReturnJson(Context context,String fileName,String localFilePath){
        copyFileFromAssets(context,fileName,localFilePath);
        File readFile = new File(localFilePath);
        if (!readFile.exists()) {
            return null;
        }
        List<CityEntity> cityEntities = new ArrayList<>();
        try {
            FileInputStream inStream = new FileInputStream(readFile);
            Workbook wb = Workbook.getWorkbook(inStream);
            Sheet sheet = wb.getSheet(0);
            int row = sheet.getRows();
            for (int i = 1; i < row; i++) {
                Cell chinese = sheet.getCell(0, i);
                Cell adcode = sheet.getCell(1, i);
                Cell citycode = sheet.getCell(2, i);
                CityEntity cityEntity = new CityEntity();
                cityEntity.chinese = chinese.getContents();
                cityEntity.adcode = adcode.getContents();
                cityEntity.citycode = citycode.getContents();
                cityEntities.add(cityEntity);
            }
            wb.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<CityEntity> finalMenus = new ArrayList<>();
        for (CityEntity cityEntity : cityEntities) {
           /* if (TextUtils.isEmpty(cityEntity.citycode)) {
                cityEntity.menus = getSubMenuByPid(menuBean.menuId, allMenus);
                if ("0".equals(menuBean.parentId)) { // 一级菜单
                    finalMenus.add(menuBean);
                }
            }*/
            finalMenus.add(cityEntity);
        }
        ProvinceEntity menus = new ProvinceEntity();
        menus.setCityEntityList(finalMenus);
        return JSON.toJSONString(menus);
    }
}
