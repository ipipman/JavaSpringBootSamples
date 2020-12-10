package com.ipman.sb2accountservice.config;

import com.ipman.sb2accountservice.interceptor.SeataRestTemplateInterceptor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Created by ipipman on 2020/12/9.
 *
 * @version V1.0
 * @Package com.ipman.sb2accountservice.config
 * @Description: (用一句话描述该文件做什么)
 * @date 2020/12/9 5:34 下午
 */
@Configuration
@NoArgsConstructor
public class SeataRestTemplateAutoConfiguration {

    @Autowired(required = false)
    private Collection<RestTemplate> restTemplates;

    @Autowired
    private SeataRestTemplateInterceptor seataRestTemplateInterceptor;



    /**
     * 通过HTTP拦截器，获取XID传递
     *
     * @return
     */
    @Bean
    public SeataRestTemplateInterceptor seataRestTemplateInterceptor() {
        return new SeataRestTemplateInterceptor();
    }

    /**
     * 添加XID的HTTP拦截器
     */
    @PostConstruct
    public void init() {
        if (this.restTemplates != null) {
            Iterator var1 = this.restTemplates.iterator();

            while (var1.hasNext()) {
                RestTemplate restTemplate = (RestTemplate) var1.next();
                List<ClientHttpRequestInterceptor> interceptors = new ArrayList(restTemplate.getInterceptors());
                interceptors.add(this.seataRestTemplateInterceptor);
                restTemplate.setInterceptors(interceptors);
            }
        }
    }


}
