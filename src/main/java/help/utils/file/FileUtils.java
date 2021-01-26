/*
 * Copyright © 2019 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 * CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.
 */
package help.utils.file;

import java.io.File;
import help.utils.time.DateUtil;

/**
 * Created by lgu2 on 11/28/2019.
 */
public class FileUtils {
    public static void sleep(double second){
        try {
            Thread.sleep((long) (second*1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static boolean deleteDir(File dir) {
        if(dir.isFile()){
            dir.delete();
        }else{
            File[] files = dir.listFiles();
            for (int i = 0; i < files.length; i++) {
                deleteDir(files[i]);
                files[i].delete();
            }
        }
        if(! dir.exists()) dir.mkdir();
        return dir.exists() && dir.list().length == 0;
    }

    public static boolean removeDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    public static String getFileName(String basePath){
        removeFolder(basePath, -7);
        String name = DateUtil.getNow("yyyyMMdd");
        File file = new File(basePath + File.separator + name);
        if(! file.exists()){
            return file.getAbsolutePath();
        }
        File[] tempList = new File(basePath).listFiles();
        int index = 1;
        int flag = 0;
        for (int i = 0; i < tempList.length; i++) {
            if(tempList[i].isDirectory() && tempList[i].getName().startsWith(name)){
                if(tempList[i].getName().contains("_")){
                    int pos = tempList[i].getName().indexOf("_");
                    pos = Integer.parseInt(tempList[i].getName().substring(pos+1));
                    if(pos > index){
                        index = pos;
                    }
                    flag++;
                }
            }
        }
        if(flag > 0) index++;
        return basePath + File.separator + name + "_" + index;
    }

    public static void removeFolder(String basePath, int day){
        String endDate = DateUtil.getNewDate(day);
        File baseFile = new File(basePath);
        if(baseFile.exists()){
            for (File file : baseFile.listFiles()) {
                if(file.isDirectory() && file.getName().startsWith(endDate)){
                    removeDir(file);
                }
            }
        }
    }
}
