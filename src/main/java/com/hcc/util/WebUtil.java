package com.hcc.util;

import java.util.Map;

public interface WebUtil {

    // 根据条形码，调用网络接口查询商品信息，里面有包含所有的信息
    public Map<String,String> getInfoByWeb(String medicinesBarcode);

}
