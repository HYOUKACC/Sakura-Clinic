package com.hcc.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository // 交给spring来进行管理
public class WebUtilImpl implements WebUtil {


    // 根据网络接口，获取商品数据
    @Override
    public Map<String, String> getInfoByWeb(String medicinesBarcode) {

        String host = "https://localhost";
        String path = "/getBarcode";
        String method = "GET";
        String appcode = "123456";
        String imgurl="http://localhost";

        Map<String, String> headers = new HashMap<String, String>();

        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("Code", medicinesBarcode); // 放入要查询的条形码

        HashMap<String,String> finalResult=new HashMap<>(); // 返回一个药品信息对象

        try {
            // 获取接口数据
            HttpResponse response= HttpUtils.doGet(host, path, method, headers, querys);

            // 把响应体的字符串转换为json对象
            JSONObject jsonObject = JSONObject.parseObject(EntityUtils.toString(response.getEntity()));

            if(jsonObject.get("status").toString().equals("200") && jsonObject.get("message").toString().equals("查询成功！") ){ // 200，查询成功

                finalResult.put("status",jsonObject.get("status").toString()); // 存储状态码
                finalResult.put("message",jsonObject.get("message").toString()); // 存储message
                finalResult.put("medicinesBarcode",medicinesBarcode); // 存储条形码
                finalResult.put("oem",jsonObject.get("FirmName").toString()); // 存储生产厂商
                finalResult.put("medicinesName",jsonObject.get("ItemName").toString()); // 存储产品名

                PinYinTrans pinYinTrans=new PinYinTransImpl(); // 获取拼音转换工具
                String code=pinYinTrans.transToFirst(jsonObject.get("ItemName").toString()); // 转换产品简码
                finalResult.put("medicinesCode",code); // 存储产品简码

                // 存储图片链接
                JSONArray jsonImgArray=(JSONArray)jsonObject.get("Image"); // 把json内值转换为数组

                try {
                    if(jsonImgArray.size()>0 ){ // 如果接口数据有图片时，有链接时
                        String[] split =jsonImgArray.getJSONObject(0).get("Imageurl").toString().split("\\?"); // 用？号打断字符串，只要后面一部分
                        String url=imgurl+"?"+split[1]; // 拼接图片地址
                        finalResult.put("img",url); // 获取第一个中的imgurl
                    }else {
                        finalResult.put("img","empty"); // 直接设置为空
                    }
                }catch (Exception e){
                    finalResult.put("img","empty"); // 直接设置为空
                }


                //System.out.println(finalResult);

            }else { // 400错误，查询失败或200查询成功，但条码未备案
                finalResult.put("status","400");// 获取状态码
                finalResult.put("message",jsonObject.get("message").toString()); // 获取message

            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return finalResult;
    }
}
