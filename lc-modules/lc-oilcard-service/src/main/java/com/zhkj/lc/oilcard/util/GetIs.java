package com.zhkj.lc.oilcard.util;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class GetIs {

    public static String getIsSuggestRecharge(Integer monthRechargeNum, BigDecimal rechargeSum, BigDecimal monthRechargeSum){
        // 月充值订单量 * 10000 - 申请充值金额 - 月充值金额
        monthRechargeNum = monthRechargeNum == null ? 0 : monthRechargeNum;
        double rechargeSum1 = Double.parseDouble((rechargeSum == null ? "0" : rechargeSum.toPlainString()));
        double monthRechargeSum1 = Double.parseDouble((monthRechargeSum == null ? "0" : monthRechargeSum.toPlainString()));
        if (monthRechargeNum==0){
            return  "是";
        }else {
            if ((monthRechargeNum * 10000 - rechargeSum1 - monthRechargeSum1) >= 0) {
                return  "是";
            } else {
                return  "否";
            }
        }
    }

    public static Map<String,String> getIsPassAndRecharge(String isPassed, Date rechargeDate){
        String setIsPassed = null;
        String setIsRechargeed = null;
        if (isPassed.equals("0")){
            if("1".equals(isPassed)){
                setIsPassed = "通过";
                if (null != rechargeDate){
                    setIsRechargeed = "充值成功";
                }else {
                    setIsRechargeed = "待充值";
                }
            }else if ("2".equals(isPassed)){
                setIsPassed = "拒绝";
            }
        }else {
            setIsPassed = "未审核";
        }
        HashMap<String,String> map = new HashMap();
        map.put("isPassed",setIsPassed);
        map.put("isRechargeed",setIsRechargeed);
        return map;
    }

    public static Map<String,String> getMaps(Integer monthRechargeNum, BigDecimal rechargeSum, BigDecimal monthRechargeSum,String isPassed, Date rechargeDate){
        // 月充值订单量 * 10000 - 申请充值金额 - 月充值金额
        monthRechargeNum = monthRechargeNum == null ? 0 : monthRechargeNum;
        double rechargeSum1 = Double.parseDouble((rechargeSum == null ? "0" : rechargeSum.toPlainString()));
        double monthRechargeSum1 = Double.parseDouble((monthRechargeSum == null ? "0" : monthRechargeSum.toPlainString()));
        String isSuggestRecharge = "";
        if (monthRechargeNum==0){
            isSuggestRecharge = "是";
        }else {
            if ((monthRechargeNum * 10000 - rechargeSum1 - monthRechargeSum1) >= 0) {
				isSuggestRecharge = "是";
            } else {
				isSuggestRecharge = "否";
            }
        }
        String setIsPassed = null;
        String setIsRechargeed = null;
        if (!isPassed.equals("0")){
            if("1".equals(isPassed)){
                setIsPassed = "通过";
                if (null != rechargeDate){
                    setIsRechargeed = "充值成功";
                }else {
                    setIsRechargeed = "待充值";
                }
            }else if ("2".equals(isPassed)){
                setIsPassed = "拒绝";
            }
        }else {
            setIsPassed = "未审核";
        }
        HashMap<String,String> map = new HashMap();
        map.put("isPassed",setIsPassed);
        map.put("isRechargeed",setIsRechargeed);
        map.put("isSuggestRecharge",isSuggestRecharge);
        return map;
    }
}
