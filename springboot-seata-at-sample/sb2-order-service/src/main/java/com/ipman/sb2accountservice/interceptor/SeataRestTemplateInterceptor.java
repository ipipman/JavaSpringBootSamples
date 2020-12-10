package com.ipman.sb2accountservice.interceptor;

import io.seata.core.context.RootContext;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.HttpRequestWrapper;

import java.io.IOException;

/**
 * Created by ipipman on 2020/12/9.
 *
 * @version V1.0
 * @Package com.ipman.sb2accountservice.interceptor
 * @Description: (用一句话描述该文件做什么)
 * @date 2020/12/9 5:38 下午
 */
public class SeataRestTemplateInterceptor implements ClientHttpRequestInterceptor {

    /**
     * 通过HTTP连接器实现XID 的传递
     *
     * @param httpRequest
     * @param bytes
     * @param clientHttpRequestExecution
     * @return
     * @throws IOException
     */
    @Override
    public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] bytes, ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {
        HttpRequestWrapper requestWrapper = new HttpRequestWrapper(httpRequest);
        String xid = RootContext.getXID();
        if (StringUtils.isNotEmpty(xid)) {
            requestWrapper.getHeaders().add(RootContext.KEY_XID, xid);
        }
        return clientHttpRequestExecution.execute(requestWrapper, bytes);
    }
}
