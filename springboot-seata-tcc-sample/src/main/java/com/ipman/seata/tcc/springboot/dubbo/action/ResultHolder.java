package com.ipman.seata.tcc.springboot.dubbo.action;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by ipipman on 2020/12/10.
 *
 * @version V1.0
 * @Package com.ipman.seata.tcc.springboot.dubbo.action
 * @Description: (用一句话描述该文件做什么)
 * @date 2020/12/10 12:06 下午
 */
public class ResultHolder {

    public static Map<String, String> actionOneResults = new ConcurrentHashMap<>();

    public static Map<String, String> actionTwoResults = new ConcurrentHashMap<>();

    public static void setActionOneResult(String txId, String result) {
        actionOneResults.put(txId, result);
    }

    public static String getActionOneResult(String txId) {
        return actionOneResults.get(txId);
    }

    public static void setActionTwoResult(String txId, String result) {
        actionTwoResults.put(txId, result);
    }

    public static String getActionTwoResult(String txId) {
        return actionTwoResults.get(txId);
    }

}
