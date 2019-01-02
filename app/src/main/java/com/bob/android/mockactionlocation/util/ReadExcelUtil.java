package com.bob.android.mockactionlocation.util;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.bob.android.mockactionlocation.config.AppConfig;
import com.bob.android.mockactionlocation.entity.CityEntity;
import com.bob.android.mockactionlocation.entity.ProvinceEntity;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

/**
 * @package com.bob.android.mockactionlocation.util
 * @fileName ReadExcelUtil
 * @Author Bob on 2018/12/23 11:22.
 * @Describe TODO
 */
public class ReadExcelUtil {
    public static String readFile(Context context){
        FileUtils.copyFileFromAssets(context,"amap_adcode_citycode.xls",
                AppConfig.ROOT_PATH.concat("/")
                        .concat(AppConfig.APP_FOLDER_NAME).concat("/").concat(AppConfig.FILE_FOLDER_NAME)+ "/amap_adcode_citycode.xls");
        String filePath = AppConfig.ROOT_PATH.concat("/").concat(AppConfig.APP_FOLDER_NAME).concat("/").concat(AppConfig.FILE_FOLDER_NAME)
                + "/amap_adcode_citycode.xls";
        File readFile = new File(filePath);
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
