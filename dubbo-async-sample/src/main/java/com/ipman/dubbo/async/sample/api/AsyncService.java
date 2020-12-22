package com.ipman.dubbo.async.sample.api;

import javax.sql.rowset.spi.SyncResolver;
import java.util.concurrent.CompletableFuture;

/**
 * Created by ipipman on 2020/12/22.
 *
 * @version V1.0
 * @Package com.ipman.dubbo.async.sample.api
 * @Description: (用一句话描述该文件做什么)
 * @date 2020/12/22 11:33 上午
 */
public interface AsyncService {

    CompletableFuture<String> sayHello(String name);
}
