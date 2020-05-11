package com.hcc.util;

import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinHelper;
import org.springframework.stereotype.Repository;

@Repository // 交给spring来进行管理
public class PinYinTransImpl implements PinYinTrans {

    // 转换拼音首字母并大写
    @Override
    public String transToFirst(String str) {

        String result="";
        try {
            result = PinyinHelper.getShortPinyin(str);
        } catch (PinyinException e) {
            result="";
        }

        result=result.toUpperCase();

        return result;
    }
}
