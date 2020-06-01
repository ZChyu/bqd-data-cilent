package com.bqd.data.service;

import com.bqd.data.entity.qdbSecurity;

import java.io.IOException;

/**
 * Created by Chyu
 * Date on 2020/5/19 14:44
 * Email 604641446@qq.com
 */
public interface qdbService {

    // 获取秘钥
    qdbSecurity getScurityKeyAndPath();

    //获取结果表状态和文件
    void getStatusAndFile() throws IOException;

    //文件解密
    String decryptFile(String filePath,String aesStr);

}
