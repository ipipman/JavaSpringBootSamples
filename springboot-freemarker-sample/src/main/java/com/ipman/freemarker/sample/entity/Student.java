package com.ipman.freemarker.sample.entity;

/**
 * Created by ipipman on 2021/9/23.
 *
 * @version V1.0
 * @Package com.ipman.freemarker.sample.entity
 * @Description: (用一句话描述该文件做什么)
 * @date 2021/9/23 5:59 下午
 */
public class Student {

    private String idNo;

    private String name;

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Student{" +
                "idNo='" + idNo + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
