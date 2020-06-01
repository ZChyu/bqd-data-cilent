package com.bqd.data.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.bqd.data.entity.addrAndPort;
import com.bqd.data.entity.qdbSecurity;
import com.bqd.data.mapper.qdbMapper;
import com.bqd.data.service.qdbService;
import com.bqd.utils.aes.AESUtils;
import com.bqd.utils.file.FileUtil;
import com.bqd.utils.http.JavaHttp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.bqd.utils.file.FileDownIO.saveUrlAs;

/**
 * Created by Chyu
 * Date on 2020/5/19 14:44
 * Email 604641446@qq.com
 */

@Service
public class QdbServiceImpl implements qdbService {
    @Autowired
    private qdbMapper qdbMapper;
    private static final Logger logger = LoggerFactory.getLogger(QdbServiceImpl.class);


    @Override
    public qdbSecurity getScurityKeyAndPath() {
        return qdbMapper.getScurityKeyAndPath();
    }

    @Override
    public void getStatusAndFile() throws IOException {
        JavaHttp javaHttp = new JavaHttp();
        //获取服务端的ip、端口、请求路径
        addrAndPort res = qdbMapper.getStatusAndFile();
        System.err.println(res);
        //组合结果表状态请求地址
        String url = res.getIp() + res.getPort() + res.getStatusfunc();
        //下载请求地址
        String postUrl = res.getIp() + res.getPort() + res.getDownloadfunc();
        //本地文件保存地址
        qdbSecurity qd_saveLocalPath = qdbMapper.getScurityKeyAndPath();

        JSONObject jsonInfo = javaHttp.run(url);
        Iterator iter = jsonInfo.entrySet().iterator();
        String file_format = res.getFile_format();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String fileName = entry.getKey().toString();
            int status = Integer.parseInt(entry.getValue().toString());
            if (status == 1) {
                String photoUrl = postUrl + fileName + file_format;   //文件URL地址
                File files = saveUrlAs(photoUrl, qd_saveLocalPath.getFilePath(), fileName+ file_format, "GET");
                logger.info(fileName+"-下载成功！");
            }else {
                logger.info(fileName+"-生产结果表失败！");
            }
        }


    }

    @Override
    public String decryptFile(String filePath,String aesStr) {
        String f_name = "";
        // 2、获取目录文件列表
        List<String> fileList = FileUtil.getFiles(filePath, false);
        for (String file: fileList) {
            try {
                f_name = file.substring(file.lastIndexOf("\\") + 1);
//            //加密测试
//            AESUtils.encryptFile(aesStr,
//                    file,
//                    filePath + "enc_"+ f_name);
                //解密
                AESUtils.decryptFile(aesStr, file, filePath + "dec_" + f_name);
            }catch (Exception e){
                logger.info(e.getMessage());
            }
        }
        return "decryptFile success";
    }

}
