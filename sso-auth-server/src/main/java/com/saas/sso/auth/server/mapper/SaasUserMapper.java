package com.saas.sso.auth.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.saas.sso.auth.server.domain.SaasUser;
import org.apache.ibatis.annotations.Param;

/**
 * @Description:
 * @Author: Waylon
 * @Date: 2019/10/23
 */

public interface SaasUserMapper extends BaseMapper<SaasUser> {

    //@Cacheable(value = "auth-sso",key = "#a0",unless = "#result == null")
    SaasUser selectByUserName(@Param("userName") String userName);
}
