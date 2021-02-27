package com.zhkj.lc.order.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.netflix.discovery.converters.Auto;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.common.util.excel.ExcelUtil;
import com.zhkj.lc.common.util.support.Convert;
import com.zhkj.lc.common.vo.DriverVO;
import com.zhkj.lc.order.dto.CommonGoodsVO;
import com.zhkj.lc.order.dto.CommonOrdForExport;
import com.zhkj.lc.order.feign.TrunkFeign;
import com.zhkj.lc.order.mapper.OrdCommonGoodsMapper;
import com.zhkj.lc.order.mapper.OrdOrderMapper;
import com.zhkj.lc.order.model.entity.OrdCommonGoods;
import com.zhkj.lc.order.model.entity.OrdExConDTO;
import com.zhkj.lc.order.model.entity.OrdExceptionCondition;
import com.zhkj.lc.order.mapper.OrdExceptionConditionMapper;
import com.zhkj.lc.order.model.entity.OrdOrder;
import com.zhkj.lc.order.service.IOrdExceptionConditionService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author cb
 * @since 2019-01-11
 */
@Service
public class OrdExceptionConditionServiceImpl extends ServiceImpl<OrdExceptionConditionMapper, OrdExceptionCondition> implements IOrdExceptionConditionService {

    @Autowired
    private OrdExceptionConditionMapper ordExceptionConditionMapper;

    @Autowired
    private OrdCommonGoodsMapper ordCommonGoodsMapper;

    @Autowired
    private TrunkFeign trunkFeign;

    @Autowired
    private OrdOrderMapper ordOrderMapper;
    @Override
    public Page selectExConditionPage(Query objectQuery, OrdExConDTO ordExConDTO) {
        List<OrdExceptionCondition> exConditions = ordExceptionConditionMapper.getExConditions(objectQuery,ordExConDTO);
        //处理查询数据
        exConditions = executeExport(exConditions,ordExConDTO.getTenantId());
        objectQuery.setRecords(exConditions);
        return objectQuery;
    }


    public static String getPlace(String place) {
        String [] splitStrings;
        if (null != place){
            splitStrings =  place.split("/");
            for (int i = 0; i < splitStrings.length; i++){
                if (splitStrings[i] != null && splitStrings[i].contains("市")){
                    place = splitStrings[i];
                    break;
                }
            }
        }
        return place;
    }

    @Override
    public Boolean exportCondition(HttpServletRequest request, HttpServletResponse response, String ids, Integer tenantId) {
        try {
            String []exids = {};
            Integer []ycId = {};
            if(ids!=null&&!ids.equals("")){
                exids = ids.split(",");
                ycId = new Integer[exids.length];
                for (int i = 0;i<ycId.length;i++){
                    ycId[i] = Integer.parseInt(exids[i]);
                }
            }
            List<OrdExceptionCondition> ordExceptionConditions = ordExceptionConditionMapper.selectExportEXConList(ycId);
            //处理导出的异常数据
            ordExceptionConditions = executeExport(ordExceptionConditions,tenantId);
            DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            String excelName = fmt.format(new Date())+"-异常情况";
            ExcelUtil<OrdExceptionCondition> util = new ExcelUtil<>(OrdExceptionCondition.class);
            util.exportExcel(request,response, ordExceptionConditions,excelName,null);
        }catch (Exception e){
            return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }

    private List<OrdExceptionCondition> executeExport(List<OrdExceptionCondition> ordExceptionConditions,Integer tenantId) {
        for (OrdExceptionCondition ex:ordExceptionConditions) {
            //获取订单号
            String orderId = ex.getOrderId();
            //根据订单编号获取订单的发货地和送货地，组合为路线
            //根据driverId获取司机的姓名电话
            //判断订单类型
            if(orderId.substring(0,2).equals("PH")){
                //处理路线
                CommonGoodsVO commonGoodsVO = ordCommonGoodsMapper.selectPhOrdByOrderId(orderId);
                if(commonGoodsVO != null) {
                    String sendPlace = getPlace(commonGoodsVO.getSendGoodsPlace());
                    String pickPlace = getPlace(commonGoodsVO.getPickGoodsPlace());
                    String route = sendPlace + "-" + pickPlace;
                    ex.setOrderRoute(route);
                    //处理司机信息
                    DriverVO driverVO = new DriverVO();
                    DriverVO driver = new DriverVO();
                    driver.setDriverId(commonGoodsVO.getMdriverId());
                    driver.setTenantId(tenantId);
                    List<DriverVO> driverVOs = trunkFeign.selectAllDriver(driver);
                    if (driverVOs != null && driverVOs.size() == 1) {
                        driverVO = driverVOs.get(0);
                    }
                    ex.setDriver(driverVO.getDriverName());
                    ex.setDriverPhone(driverVO.getPhone());
                }
            }
            else {
                OrdOrder ordOrder = ordOrderMapper.selectOrderBaseById(orderId,tenantId);
                if(ordOrder != null) {
                    String sendPlace = getPlace(ordOrder.getSendGoodsPlace());
                    String pickPlace = getPlace(ordOrder.getPickupGoodsPlace());
                    String route = sendPlace + "-" + pickPlace;
                    ex.setOrderRoute(route);
                    //处理司机信息
                    DriverVO driverVO = new DriverVO();
                    DriverVO driver = new DriverVO();
                    driver.setDriverId(ordOrder.getDriverId());
                    driver.setTenantId(tenantId);
                    List<DriverVO> driverVOs = trunkFeign.selectAllDriver(driver);
                    if (driverVOs != null && driverVOs.size() == 1) {
                        driverVO = driverVOs.get(0);
                    }
                    ex.setDriver(driverVO.getDriverName());
                    ex.setDriverPhone(driverVO.getPhone());
                }
            }

        }
        return ordExceptionConditions;
    }

    @Override
    public Boolean deleteByIds(String ids) {
        try {
            String []exId = Convert.toStrArray(ids);
            ordExceptionConditionMapper.deleteByExIds(exId);
        }catch (Exception e){
            return  false;
        }
        return true;
    }

    @Override
    public Page selectExPageByOrderId(Query query,String orderId, Integer tenantId) {
        List<OrdExceptionCondition> exceptionConditions = ordExceptionConditionMapper.getExConditionsByOrderId(query,orderId);
        exceptionConditions = executeExport(exceptionConditions, tenantId);
        query.setRecords(exceptionConditions);
        return query;
    }

    @Override
    public OrdExceptionCondition selectExConditionById(Integer id, Integer tenantId) {
        OrdExceptionCondition condition = new OrdExceptionCondition();
        condition.setId(id);
        OrdExceptionCondition ex = ordExceptionConditionMapper.selectOne(condition);
        //分割图片路径为字符串数组
        if(ex.getOecFile()!=null){
        ex.setPaths(ex.getOecFile().split(","));}
        List<OrdExceptionCondition>list = new ArrayList<>();
        list.add(ex);
        list = executeExport(list, tenantId);
        return list.get(0);
    }
}
