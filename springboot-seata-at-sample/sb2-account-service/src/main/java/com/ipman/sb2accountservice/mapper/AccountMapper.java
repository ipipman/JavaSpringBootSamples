package com.ipman.sb2accountservice.mapper;

import com.ipman.sb2accountservice.entity.Account;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Created by ipipman on 2020/12/9.
 *
 * @version V1.0
 * @Package com.ipman.sb2accountservice.mapper
 * @Description: (用一句话描述该文件做什么)
 * @date 2020/12/9 5:49 下午
 */
@Mapper
public interface AccountMapper {

    Account selectByUserId(@Param("userId") String userId);

    int updateById(@Param("account") Account record);
}
