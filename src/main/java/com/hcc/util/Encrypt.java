package com.hcc.util;


import org.apache.shiro.crypto.hash.Md5Hash;

// 加密算法
public  class Encrypt {

    // 注册加密
    public String registerEncryptPassword(String password){

        //MD5加密 密码，盐，散列次数
        Md5Hash md5Hash=new Md5Hash(password,"Sakura",2);

        // 返回加密过的散列密码，用于存储在数据库中
        return md5Hash.toString();
    }


}
