package com.bqd.data.entity;

import lombok.Data;
/**
 * Created by Chyu
 * Date on 2020/5/19 14:44
 * Email 604641446@qq.com
 */
@Data
public class addrAndPort {
    private int id;
    private String ip;
    private int port;
    private String statusfunc;
    private String downloadfunc;
    private String file_format;
}
