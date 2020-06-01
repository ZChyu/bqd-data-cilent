package com.bqd.data.controller;

import com.bqd.data.entity.qdbSecurity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Chyu
 * Date on 2020/5/19 14:44
 * Email 604641446@qq.com
 */

@RestController
public class QdbController {

    @Autowired
    private com.bqd.data.service.qdbService qdbService;

    private static final Logger logger = LoggerFactory.getLogger(QdbController.class);


    //qd本地文件下载相关代码
    @RequestMapping("/download")
    public String download(){
        try {
            qdbService.getStatusAndFile();
        }catch (Exception e){
            logger.error(e.getMessage());
            return e.getMessage();

        }
       return "download success";

    }

    //文件解密
    @RequestMapping("/decryptFile")
    public String decryptFile() {

        //获取密钥、解密路径，解密到当前路径下
        qdbSecurity qdbSecurity = qdbService.getScurityKeyAndPath();
        String res = qdbService.decryptFile(qdbSecurity.getFilePath(),qdbSecurity.getSecretKey());
        return res;
    }

}
