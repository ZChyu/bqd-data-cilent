package com.bqd.utils.file;

import com.bqd.data.controller.QdbController;
import com.bqd.utils.aes.AESUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 顾风桂
 * @date 2020/5/21 14:37
 * @Description:
 */
public class FileUtil {
    private static ArrayList<String> dirlist = new ArrayList<String>();
    private static ArrayList<String> filelist = new ArrayList<String>();
//    private static String aesKeys = "05CI9M9qCeXnRpd+cgLpCjfVXht5g7mbGTa8rnpR2Kg=";
    private static String encryptDir = "/encrypt/";
    private static final Logger logger = LoggerFactory.getLogger(QdbController.class);

    public static void main(String[] args) {
        try {
            AESUtils.decryptFile("05CI9M9qCeXnRpd+cgLpCjfVXht5g7mbGTa8rnpR2Kg=",
                    "d:/bqd_provident_ability_20200519.txt",
                    "d:/bqd_provident_ability_20200519.txt.ok");
        } catch (Exception e) {
            e.printStackTrace();
        }
//        System.out.println();
    }

    /**
     * 通过目录文件某一路径下所有的目录及其文件
     *
     * @param filePath 目录
     * @param flag     递归删除(false非递归;ture递归)
     */
    public static ArrayList<String> getFiles(String filePath, boolean flag) {
        File root = new File(filePath);
        File[] files = root.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                if (flag == false) continue;
                //递归调用
                getFiles(file.getAbsolutePath(), flag);
                dirlist.add(file.getAbsolutePath());
            } else {
                filelist.add(file.getAbsolutePath());
            }
        }
        return filelist;
    }

    /**
     * 通过删除某一路径下所有的目录及其文件
     *
     * @param fileList 目录+文件名
     * @param prefix   删除符合前缀名文件
     */
    public static int delFiles(List<String> fileList, String prefix) {
        File file = null;
        for (String list : fileList) {
            // 获取系统路径分隔符
            String[] splitLen = list.split("/");

            // 文件名前缀符合要求，则删除文件
            if (splitLen[splitLen.length - 1].startsWith(prefix) == true) {
                file = new File(list);
                file.delete();
                logger.info(list + "已清理文件完成！");
            } else {
                logger.info(list + "未清理文件完成！");
            }
        }
        filelist.clear();
        return 1;
    }

    public static int encryptFiles(List<String> fileList, String prefix, String aesKeys) throws Exception {
        String newFileName = null;
        logger.info("开始文件加密！");
        for (String list : fileList) {
            // 获取系统路径分隔符
            String[] splitLen = list.split("/");
            // 文件名前缀符合要求，则删除文件
            if (splitLen[splitLen.length - 1].startsWith(prefix) == true) {
                // 文件放入当前目录的下级目录
                newFileName = list.substring(0, list.lastIndexOf("/")) + encryptDir + splitLen[splitLen.length - 1];
                AESUtils.encryptFile(aesKeys, list, newFileName);
                logger.info(list + "文件加密完成！");
            }
        }
        logger.info("结束文件加密！");
        filelist.clear();
        return 1;

    }
}