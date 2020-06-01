package com.bqd.data.mapper;

import com.bqd.data.entity.addrAndPort;
import com.bqd.data.entity.qdbSecurity;
import org.apache.ibatis.annotations.*;

/**
 * Created by Chyu
 * Date on 2020/5/19 14:44
 * Email 604641446@qq.com
 */
@Mapper
public interface qdbMapper {

    @Select("select * from qdbSecurity")
    qdbSecurity getScurityKeyAndPath();

    @Select("select * from addrandport")
    addrAndPort getStatusAndFile();
}
