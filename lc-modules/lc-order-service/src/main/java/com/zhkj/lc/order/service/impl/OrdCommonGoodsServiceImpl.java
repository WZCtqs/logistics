package com.zhkj.lc.order.service.impl;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.zhkj.lc.common.api.YunPianSMSUtils;
import com.zhkj.lc.common.constant.CommonConstant;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.common.util.R;
import com.zhkj.lc.common.util.excel.ExcelUtil;
import com.zhkj.lc.common.util.support.Convert;
import com.zhkj.lc.common.vo.CustomerVO;
import com.zhkj.lc.common.vo.DriverVO;
import com.zhkj.lc.common.vo.SysDictVO;
import com.zhkj.lc.order.dto.*;
import com.zhkj.lc.order.feign.CommonGoodsTruckFeign;
import com.zhkj.lc.order.feign.SystemFeginServer;
import com.zhkj.lc.order.feign.TrunkFeign;
import com.zhkj.lc.order.mapper.*;
import com.zhkj.lc.order.model.entity.*;
import com.zhkj.lc.order.service.IOrdCommonGoodsService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zhkj.lc.order.service.IOrdOrderService;
import com.zhkj.lc.order.utils.CommonUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.Exception;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Driver;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author cb
 * @since 2019-01-03
 */
@Service
public class OrdCommonGoodsServiceImpl extends ServiceImpl<OrdCommonGoodsMapper, OrdCommonGoods> implements IOrdCommonGoodsService {


    private static org.slf4j.Logger logger = LoggerFactory.getLogger(OrdCommonGoodsServiceImpl.class);
    @Autowired
    private OrdCommonGoodsMapper ordCommonGoodsMapper;

    @Autowired
    private OrdOrderMapper ordOrderMapper;

    @Autowired
    private OrdCommonTruckMapper ordCommonTruckMapper;

    @Autowired
    private CommonGoodsInfoMapper commonGoodsInfoMapper;

    @Autowired
    private OrdCommonFileMapper ordCommonFileMapper;

    @Autowired
    private OrdOrderLogisticsMapper ordOrderLogisticsMapper;

    @Autowired
    private SystemFeginServer systemFeginServer;

    @Autowired
    private TrunkFeign trunkFeign;

    @Autowired
    private IOrdOrderService ordOrderService;

    @Autowired
    private CommonGoodsTruckFeign commonGoodsTruckFeign;

    @Autowired private OrdExceptionFeeMapper ordExceptionFeeMapper;
    @Autowired private OrdExceptionConditionMapper ordExceptionConditionMapper;

    @Autowired private OrdPickupArrivalAddMapper addMapper;

    @Override
    public Boolean addCommonOrder(OrdCommonGoods ordCommonGoods) {
        //获取订单编号
        try {
            Map map = ordCommonGoodsMapper.getPhOrderId("PH", "");
            ordCommonGoods.setMorderId(map.get("orderId").toString());
            //保存普货订单信息

            ordCommonGoods.setSendGoodsPlace(StringUtils.join(ordCommonGoods.getSendPlaceArray(), "/"));
            ordCommonGoods.setPickGoodsPlace(StringUtils.join(ordCommonGoods.getPickPlaceArray(), "/"));
            //设置订单状态为草稿
            ordCommonGoods.setStatus(CommonConstant.ORDER_CG);
            //保存主司机id
            Integer driverId = ordCommonGoods.getOrdCommonTruck().getMdriverId();
            ordCommonGoods.setDriverId(driverId);
            ordCommonGoodsMapper.insertCommonOrder(ordCommonGoods);

            /*处理地址信息*/
            List<OrdPickupArrivalAdd> arrivalAdds = ordCommonGoods.getArrivalAdds();
            for(OrdPickupArrivalAdd add : arrivalAdds){
                add.setTenantId(ordCommonGoods.getMtenantId());
                add.setOrderId(ordCommonGoods.getMorderId());
                if(add.getAddressCity().contains("市辖区")) {
                    String [] city = add.getAddressCity().split("/");
                    add.setAddressCity(add.getAddressCity().replace("市辖区", city[0]));
                }
                addMapper.insert(add);
            }
            List<OrdPickupArrivalAdd> pickupAdds = ordCommonGoods.getPickupAdds();
            for(OrdPickupArrivalAdd add : pickupAdds){
                add.setTenantId(ordCommonGoods.getMtenantId());
                add.setOrderId(ordCommonGoods.getMorderId());
                if(add.getAddressCity().contains("市辖区")) {
                    String [] city = add.getAddressCity().split("/");
                    add.setAddressCity(add.getAddressCity().replace("市辖区", city[0]));
                }
                addMapper.insert(add);
            }

            //保存货物信息
            List<CommonGoodsInfo> commonGoodsInfos = ordCommonGoods.getCommonGoodsInfos();
            for (CommonGoodsInfo commonGoodsInfo : commonGoodsInfos) {
                //设置货物的订单编号
                commonGoodsInfo.setOrderId(map.get("orderId").toString());
                //设置租户id
                commonGoodsInfo.setTenantId(ordCommonGoods.getMtenantId());
                commonGoodsInfoMapper.insert(commonGoodsInfo);
            }

            //保存车辆调度信息
            OrdCommonTruck ordCommonTruck = ordCommonGoods.getOrdCommonTruck();
            if(ordCommonTruck!=null){
                ordCommonTruck.setOrderId(map.get("orderId").toString());
                ordCommonTruck.setTenantId(ordCommonGoods.getMtenantId());
//                ordCommonTruck.setChargedMileage(ordCommonGoods.getMchargedMileage());
                ordCommonTruckMapper.insert(ordCommonTruck);
            }

            //保存文件信息
           /* if (ordCommonGoods.getOrdCommonFile() != null) {
                OrdCommonFile ordCommonFile = ordCommonGoods.getOrdCommonFile();
                ordCommonFile.setOrderId(map.get("orderId").toString());
                ordCommonFile.setTenantId(ordCommonGoods.getMtenantId());
                ordCommonFileMapper.insert(ordCommonFile);
            }*/
            logger.info("保存普货订单信息成功");

            return Boolean.TRUE;
        } catch (Exception e) {
            logger.info(e.getMessage());
            return Boolean.FALSE;
        }


    }

    /**
     * 分页查询普货订单管理
     *
     * @param objectQuery
     * @param commonOrdSearch
     * @return
     */
    @Override
    public Page selectManageGoodsList(Query objectQuery, CommonOrdSearch commonOrdSearch) {
        List<CommonGoodsVO> commonGoodsVOS = ordCommonGoodsMapper.selectManageGoodsList(objectQuery, commonOrdSearch);
        if(commonGoodsVOS.size()>0){
            for (CommonGoodsVO cgvo : commonGoodsVOS) {
                //获取客户名称
                CustomerVO customerVO = getCustomerById(cgvo.getCustomerId(),commonOrdSearch.getTenantId());
                if(null != customerVO){
                    cgvo.setCustomerName(customerVO.getCustomerName());
                }
                //计算总体积，总重量
                BigDecimal sumWeight = new BigDecimal(0);
                BigDecimal sumVolume = new BigDecimal(0);
                for (CommonGoodsInfo info : cgvo.getCommonGoodsInfos()) {
                    if (info.getWeight() != null)
                        sumVolume = sumVolume.add(info.getWeight());
                    if (info.getVolume() != null)
                        sumWeight = sumWeight.add(info.getVolume());
                }
                cgvo.setSumWeight(sumWeight);
                cgvo.setSumVolume(sumVolume);

                if(cgvo.getOrdCommonTruck()!=null){
                    List<DriverVO> driverVOS = getDriverVo(cgvo.getOrdCommonTruck(), commonOrdSearch.getTenantId());
                    cgvo.getOrdCommonTruck().setDriverVOS(driverVOS);
                }

            }
        }

        objectQuery.setRecords(commonGoodsVOS);
        return objectQuery;
    }

    @Override
    public Page selectCommonOrdPage(Query objectQuery, CommonOrdSearch commonOrdSearch) {
        List<OrdCommonGoodsVo> commonGoodsList = ordCommonGoodsMapper.selectCommonOrdList(objectQuery, commonOrdSearch);
        objectQuery.setRecords(commonGoodsList);
        return objectQuery;
    }


    @Override
    @Transactional
    public R<Boolean> importPhOrd(MultipartFile file,String loginName, Integer tenantId) {
        try {
            InputStream inputStream = file.getInputStream();
            //创建excel工具对象
            ImportParams params = new ImportParams();
            params.setHeadRows(1);
            params.setTitleRows(1);
            //从流中获取订单集合
            List<CommonGoodsExcelBean> commonGoodsExcelBeans = ExcelImportUtil.importExcel(inputStream, CommonGoodsExcelBean.class, params);
            //处理集合，获得去除重复订单号的集合，用于后续操作
            inputStream.close();
            List<CommonGoodsExcelBean> singleOrdList = new ArrayList<CommonGoodsExcelBean>();
            //去除重复的订单号元素
            HashSet<CommonGoodsExcelBean> set = new HashSet<CommonGoodsExcelBean>(commonGoodsExcelBeans);
            singleOrdList.addAll(set);
            //判断是否存在不可导入数据
            OrdCommonGoods ordCommonGood=null;
            List<CommonGoodsExcelBean> list = new ArrayList<CommonGoodsExcelBean>();
            for (CommonGoodsExcelBean commonGoodsExcelBean:singleOrdList) {
                if(CommonUtils.checkObjAllFieldsIsNull(commonGoodsExcelBean)){
                    continue;
                }
                CustomerVO customerVO = getCustomerByName(commonGoodsExcelBean.getCustomerName(),tenantId);
                if(customerVO != null) {
                    list.add(commonGoodsExcelBean);
                }else {
                    return new R<>(Boolean.FALSE,"系统中未录入客户信息或者客户名称不正确");
                }
            }
            for (CommonGoodsExcelBean commonGoodsExcelBean:list) {
                //存入订单信息
                CustomerVO customerVO = getCustomerByName(commonGoodsExcelBean.getCustomerName(),tenantId);
                ordCommonGood=new OrdCommonGoods();
                Map<String, String> orderIdmap = ordCommonGoodsMapper.getPhOrderId("PH", "");
                String orderId = orderIdmap.get("orderId");
                BeanUtils.copyProperties(commonGoodsExcelBean, ordCommonGood);
                ordCommonGood.setCustomerId(customerVO.getCustomerId());
                ordCommonGood.setMorderId(orderId);
                ordCommonGood.setStatus(CommonConstant.ORDER_CG);
                //租户id
                ordCommonGood.setMtenantId(tenantId);
                ordCommonGood.setCreateBy(loginName);
                ordCommonGoodsMapper.insertCommonOrder(ordCommonGood);

                //存入物品信息
                insertCommonGoodsInfo(commonGoodsExcelBean,orderId,tenantId);

                //存入多个提送货地
                insertAddress(commonGoodsExcelBean.getPickupAdd1(),orderId,tenantId,"0",0,commonGoodsExcelBean.getShipperPhone1());
                insertAddress(commonGoodsExcelBean.getPickupAdd2(),orderId,tenantId,"0",1,commonGoodsExcelBean.getShipperPhone2());
                insertAddress(commonGoodsExcelBean.getPickupAdd3(),orderId,tenantId,"0",2,commonGoodsExcelBean.getShipperPhone3());
                insertAddress(commonGoodsExcelBean.getArrivalAdd1(),orderId,tenantId,"1",0,commonGoodsExcelBean.getPickerPhone1());
                insertAddress(commonGoodsExcelBean.getArrivalAdd2(),orderId,tenantId,"1",1,commonGoodsExcelBean.getPickerPhone2());
                insertAddress(commonGoodsExcelBean.getArrivalAdd3(),orderId,tenantId,"1",2,commonGoodsExcelBean.getPickerPhone3());
            }
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            logger.info("导入问题："+e.getMessage());
            return new R<>(Boolean.FALSE);
        }
        return new R<>(Boolean.TRUE);
    }

    @Override
    public OrdCommonGoodsVo selectComOrdById(Integer id, Integer tenantId) {
        OrdCommonGoodsVo ordCommonGoods = ordCommonGoodsMapper.selectOneById(id);
        CustomerVO customerVO = getCustomerById(ordCommonGoods.getCustomerId(),tenantId);
        if(null != customerVO){
            ordCommonGoods.setCustomerName(customerVO.getCustomerName());
        }
        //处理地址为数组
        if (ordCommonGoods.getSendGoodsPlace() != null && !ordCommonGoods.getSendGoodsPlace().equals(""))
            ordCommonGoods.setSendPlaceArray(ordCommonGoods.getSendGoodsPlace().split("/"));
        if (ordCommonGoods.getPickGoodsPlace() != null && !ordCommonGoods.getPickGoodsPlace().equals(""))
            ordCommonGoods.setPickPlaceArray(ordCommonGoods.getPickGoodsPlace().split("/"));

        //处理司机信息
        if(ordCommonGoods.getOrdCommonTruck()!=null){
            List<DriverVO> driverVOS = getDriverVo(ordCommonGoods.getOrdCommonTruck(), tenantId);
            ordCommonGoods.getOrdCommonTruck().setDriverVOS(driverVOS);
        }
        //处理异常费用和异常清空图片

        if(ordCommonGoods.getExceptionConditions()!=null&&ordCommonGoods.getExceptionConditions().size()>0){
            //获取司机姓名
            DriverVO driverVO = new DriverVO();
            driverVO.setDriverId(ordCommonGoods.getDriverId());
            List<DriverVO> driverVOS = trunkFeign.getDriverList(driverVO);
            if(driverVOS!=null&&driverVOS.size()>0){
                driverVO = driverVOS.get(0);
            }
            for (OrdExceptionCondition exc:ordCommonGoods.getExceptionConditions()) {
                exc.setPaths(exc.getOecFile().split(","));
                exc.setDriver(driverVO.getDriverName()!=null?driverVO.getDriverName():"");
            }
        }
        if(ordCommonGoods.getExceptionFees()!=null&&ordCommonGoods.getExceptionFees().size()>0){
            for (OrdExceptionFee oef: ordCommonGoods.getExceptionFees()) {
                oef.setImgUrlFile(oef.getImgUrl()!=null?oef.getImgUrl().split(","):null);
            }
        }

        //提货和签收凭证

        String[]thImgs = {};
        String[]qsImgs = {};
        if(ordCommonGoods.getOrdOrderLogistics()!=null&&ordCommonGoods.getOrdOrderLogistics().size()>0){
            for (OrdOrderLogistics ool:ordCommonGoods.getOrdOrderLogistics()) {

                if(ool.getOrderStatus().equals(CommonConstant.ORDER_YSZ)){// 提货
                    if(ool.getPhotos()!=null&&!ool.getPhotos().equals(""))
                    thImgs = ool.getPhotos().split(",");
                }
                else if(ool.getOrderStatus().equals(CommonConstant.ORDER_YQS)){//签收
                    if(ool.getPhotos()!=null&&!ool.getPhotos().equals(""))
                    qsImgs = ool.getPhotos().split(",");
                }
            }

            ordCommonGoods.setQsImgs(qsImgs);
            ordCommonGoods.setThImgs(thImgs);
        }
        /*处理提送货地址凭证信息*/

        List<OrdPickupArrivalAdd> pickupadds = ordCommonGoods.getPickupAdds();
        for(OrdPickupArrivalAdd add : pickupadds){
            add.setFileArray(add.getFiles()!=null ? add.getFiles().split(","):null);
        }
        List<OrdPickupArrivalAdd> arrivalAdds = ordCommonGoods.getArrivalAdds();
        for(OrdPickupArrivalAdd add : arrivalAdds){
            add.setFileArray(add.getFiles()!=null ? add.getFiles().split(","):null);
        }

        return ordCommonGoods;
    }

    @Override
    @Transactional
    public Boolean updatePhOrd(OrdCommonGoods ordCommonGoods, Integer tenantId) {
        //更新普货订单基本信息
        try {

            ordCommonGoods.setSendGoodsPlace(StringUtils.join(ordCommonGoods.getSendPlaceArray(), "/"));
            ordCommonGoods.setPickGoodsPlace(StringUtils.join(ordCommonGoods.getPickPlaceArray(), "/"));

            if(ordCommonGoods.getOrdCommonTruck()!=null){
                Integer driverId = ordCommonGoods.getOrdCommonTruck().getMdriverId();
                //保存主司机id
                ordCommonGoods.setDriverId(driverId);
            }

            ordCommonGoodsMapper.updateCommonOrd(ordCommonGoods);

            //删除之前的货物信息
            commonGoodsInfoMapper.deleteByOrderId(ordCommonGoods.getMorderId());
            //更新普货的货物信息
            for (CommonGoodsInfo info : ordCommonGoods.getCommonGoodsInfos()) {
                info.setOrderId(ordCommonGoods.getMorderId());
                info.setTenantId(tenantId);
                commonGoodsInfoMapper.insert(info);
            }

            /*更新地址信息*/
            addMapper.deleteByOrderId(ordCommonGoods.getMorderId());
            List<OrdPickupArrivalAdd> arrivalAdds = ordCommonGoods.getArrivalAdds();
            for(OrdPickupArrivalAdd add : arrivalAdds){
                add.setTenantId(ordCommonGoods.getMtenantId());
                add.setOrderId(ordCommonGoods.getMorderId());
                if(add.getAddressCity().contains("市辖区")) {
                    String [] city = add.getAddressCity().split("/");
                    add.setAddressCity(add.getAddressCity().replace("市辖区", city[0]));
                }
                addMapper.insert(add);
            }
            List<OrdPickupArrivalAdd> pickupAdds = ordCommonGoods.getPickupAdds();
            for(OrdPickupArrivalAdd add : pickupAdds){
                add.setTenantId(ordCommonGoods.getMtenantId());
                add.setOrderId(ordCommonGoods.getMorderId());
                if(add.getAddressCity().contains("市辖区")) {
                    String [] city = add.getAddressCity().split("/");
                    add.setAddressCity(add.getAddressCity().replace("市辖区", city[0]));
                }
                addMapper.insert(add);
            }

            //更新车辆调度信息
            if (ordCommonGoods.getOrdCommonTruck().getOrderId() == null || ordCommonGoods.getOrdCommonTruck().getOrderId().equals("")) {
                ordCommonGoods.getOrdCommonTruck().setOrderId(ordCommonGoods.getMorderId());
                ordCommonGoods.getOrdCommonTruck().setTenantId(tenantId);
                ordCommonTruckMapper.insert(ordCommonGoods.getOrdCommonTruck());
            } else {
                ordCommonTruckMapper.updateById(ordCommonGoods.getOrdCommonTruck());
            }

        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    @Override
    @Transactional
    public Boolean deleteByIds(Map<Integer, String> map, Integer tenantId, String login) {
        //遍历map获取要删除的id
        try {
            OrdCommonGoods ordCommonGoodsOrd;
            OrdCommonGoods ordCommonGoods;
            for (Integer id : map.keySet()) {
                ordCommonGoodsOrd = ordCommonGoodsMapper.selectById(id);
                if (null != ordCommonGoodsOrd){
                    ordExceptionFeeMapper.deleteByOrderId(CommonConstant.STATUS_DEL,tenantId, login, ordCommonGoodsOrd.getMorderId());
                    ordExceptionConditionMapper.deleteByOrderId(CommonConstant.STATUS_DEL,tenantId, ordCommonGoodsOrd.getMorderId());
                    if(ordCommonGoodsOrd.getOrdCommonTruck() != null && ordCommonGoodsOrd.getOrdCommonTruck().getMdriverId()!= null){
                        //获取正在进行的普货订单
                        CommonGoodsVO commonGoodsVO = ordCommonGoodsMapper.selectProcOrdByDriverId(ordCommonGoodsOrd.getOrdCommonTruck().getMdriverId());
                        //获取正在进行的集装箱订单
                        OrdOrder ordOrder = ordOrderMapper.seleteNotEndOrderByDriverId(ordCommonGoodsOrd.getOrdCommonTruck().getMdriverId());
                        if(commonGoodsVO == null || ordOrder == null){
                            /*如果都不存在订单，将司机状态改为空闲*/
                            DriverVO driverVO = trunkFeign.selectDriverStatus(ordCommonGoodsOrd.getOrdCommonTruck().getMdriverId(), tenantId);
                            if(driverVO != null && driverVO.getStatus().equals("1")){
                                Integer[] driverIds = {ordCommonGoodsOrd.getOrdCommonTruck().getMdriverId()};
                                driverVO.setStatus(CommonConstant.SJKX);
                                driverVO.setDriverIds(driverIds);
                                commonGoodsTruckFeign.updateDriverSta(driverVO);
                            }
                        }
                    }
                    if(ordCommonGoodsOrd.getOrdCommonTruck() != null && ordCommonGoodsOrd.getOrdCommonTruck().getSdriverId()!= null){
                        //获取正在进行的普货订单
                        CommonGoodsVO commonGoodsVO = ordCommonGoodsMapper.selectProcOrdByDriverId(ordCommonGoodsOrd.getOrdCommonTruck().getSdriverId());
                        //获取正在进行的集装箱订单
                        OrdOrder ordOrder = ordOrderMapper.seleteNotEndOrderByDriverId(ordCommonGoodsOrd.getOrdCommonTruck().getSdriverId());
                        if(commonGoodsVO == null || ordOrder == null){
                            /*如果都不存在订单，将司机状态改为空闲*/
                            DriverVO driverVO = trunkFeign.selectDriverStatus(ordCommonGoodsOrd.getOrdCommonTruck().getSdriverId(), tenantId);
                            if(driverVO != null && driverVO.getStatus().equals("1")){
                                Integer[] driverIds = {ordCommonGoodsOrd.getOrdCommonTruck().getSdriverId()};
                                driverVO.setStatus(CommonConstant.SJKX);
                                driverVO.setDriverIds(driverIds);
                                commonGoodsTruckFeign.updateDriverSta(driverVO);
                            }
                        }
                    }
                }
                ordCommonGoods = new OrdCommonGoods();
                ordCommonGoods.setId(id);
                ordCommonGoods.setUpdateTime(new Date());
                ordCommonGoods.setDelFlag(CommonConstant.STATUS_DEL);
                ordCommonGoodsMapper.updateById(ordCommonGoods);
                //获得要删除的订单编号
                String orderId = map.get(id);
                //删除对应的车辆调度信息和货物信息
                OrdCommonTruck ordCommonTruck = new OrdCommonTruck();
                ordCommonTruck.setOrderId(orderId);
                ordCommonTruckMapper.delete(new EntityWrapper<>(ordCommonTruck));

                CommonGoodsInfo commonGoodsInfo = new CommonGoodsInfo();
                commonGoodsInfo.setOrderId(orderId);
                commonGoodsInfoMapper.delete(new EntityWrapper<>(commonGoodsInfo));
            }
        } catch (Exception e) {
            //手动回滚事务
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    @Override
    @Transactional
    public R<Boolean> sendDriverWhenAdding(OrdCommonGoods ordCommonGoods, Integer tenantId) {
        /*给司机派单前验证*/
        if(ordCommonGoods.getOrdCommonTruck().getMdriverId()!=null){
            DriverVO driverVO = trunkFeign.selectDriverStatus(ordCommonGoods.getOrdCommonTruck().getMdriverId(), tenantId);
            if(driverVO == null){
                return new R<>(Boolean.FALSE,"该司机不存在！");
            }else {
                if(!driverVO.getStatus().equals("2")){
                    return new R<>(Boolean.FALSE,driverVO.getDriverName()+"该司机处于请假状态或者在途状态，不能接单！");
                }
                if(driverVO.getDelFlag().equals("1")){
                    return new R<>(Boolean.FALSE,driverVO.getDriverName()+"该司机已被删除！");
                }
                if(!driverVO.getPlateNumber().equals(ordCommonGoods.getOrdCommonTruck().getPlateNumber())){
                    return new R<>(Boolean.FALSE,driverVO.getDriverName()+"该司机和车辆信息不匹配！");
                }
            }
        }
        ordCommonGoods.setUpdateTime(new Date());
        //更改订单状态为待接单
        ordCommonGoods.setStatus(CommonConstant.ORDER_DJD);
        //更新派车时间
        ordCommonGoods.setSendTruckDate(new Date());
        //订单编号为空，表示新增时发送司机
        if (ordCommonGoods.getMorderId() == null || ordCommonGoods.getMorderId().equals("")) {
            try {
                Map map = ordCommonGoodsMapper.getPhOrderId("PH", "");
                ordCommonGoods.setMorderId(map.get("orderId").toString());
                //保存普货订单信息

                ordCommonGoods.setSendGoodsPlace(StringUtils.join(ordCommonGoods.getSendPlaceArray(), "/"));
                ordCommonGoods.setPickGoodsPlace(StringUtils.join(ordCommonGoods.getPickPlaceArray(), "/"));
                // ordCommonGoods.setShipperCity(StringUtils.join(ordCommonGoods.getShipperCityArray(), "/"));
                //ordCommonGoods.setPickerCity(StringUtils.join(ordCommonGoods.getPickerCityArray(), "/"));
                //保存主司机id
                Integer driverId = ordCommonGoods.getOrdCommonTruck().getMdriverId();
                ordCommonGoods.setDriverId(driverId);
                //租户id
                ordCommonGoods.setMtenantId(tenantId);
                ordCommonGoodsMapper.insertCommonOrder(ordCommonGoods);
                /*处理地址信息*/
                List<OrdPickupArrivalAdd> arrivalAdds = ordCommonGoods.getArrivalAdds();
                for(OrdPickupArrivalAdd add : arrivalAdds){
                    add.setTenantId(ordCommonGoods.getMtenantId());
                    add.setOrderId(ordCommonGoods.getMorderId());
                    if(add.getAddressCity().contains("市辖区")) {
                        String [] city = add.getAddressCity().split("/");
                        add.setAddressCity(add.getAddressCity().replace("市辖区", city[0]));
                    }
                    addMapper.insert(add);
                }
                List<OrdPickupArrivalAdd> pickupAdds = ordCommonGoods.getPickupAdds();
                for(OrdPickupArrivalAdd add : pickupAdds){
                    add.setTenantId(ordCommonGoods.getMtenantId());
                    add.setOrderId(ordCommonGoods.getMorderId());
                    if(add.getAddressCity().contains("市辖区")) {
                        String [] city = add.getAddressCity().split("/");
                        add.setAddressCity(add.getAddressCity().replace("市辖区", city[0]));
                    }
                    addMapper.insert(add);
                }
                //保存货物信息
                List<CommonGoodsInfo> commonGoodsInfos = ordCommonGoods.getCommonGoodsInfos();
                for (CommonGoodsInfo commonGoodsInfo : commonGoodsInfos
                        ) {
                    //设置货物的订单编号
                    commonGoodsInfo.setOrderId(map.get("orderId").toString());
                    commonGoodsInfo.setTenantId(tenantId);
                    commonGoodsInfoMapper.insert(commonGoodsInfo);
                }

                //保存车辆调度信息
                OrdCommonTruck ordCommonTruck = ordCommonGoods.getOrdCommonTruck();
                ordCommonTruck.setOrderId(map.get("orderId").toString());
                ordCommonTruck.setTenantId(tenantId);
//                ordCommonTruck.setChargedMileage(ordCommonGoods.getMchargedMileage());
                ordCommonTruckMapper.insert(ordCommonTruck);

                //更新司机状态
                updateDriverStatus(ordCommonTruck);

                //保存文件信息
              /*  if (ordCommonGoods.getOrdCommonFile() != null) {
                    OrdCommonFile ordCommonFile = ordCommonGoods.getOrdCommonFile();
                    ordCommonFile.setOrderId(map.get("orderId").toString());
                    ordCommonFile.setTenantId(tenantId);
                    ordCommonFileMapper.insert(ordCommonFile);
                }*/
                //更新运踪信息
                OrdOrderLogistics ordOrderLogistics = new OrdOrderLogistics();
                ordOrderLogistics.setLogisticsMsg(CommonConstant.PC_MSG);
                ordOrderLogistics.setOrderId(map.get("orderId").toString());
                ordOrderLogistics.setOrderStatus(CommonConstant.ORDER_DJD);
                ordOrderLogistics.setTenantId(tenantId);

                ordOrderLogisticsMapper.insertOrderLogistics(ordOrderLogistics);

                //发生短信给司机

                DriverVO driverVO = new DriverVO();
                driverVO.setDriverId(ordCommonTruck.getMdriverId());
                List<DriverVO> driverVOs = trunkFeign.getDriverList(driverVO);
                if (driverVOs != null && driverVOs.size() == 1) {
                    driverVO = driverVOs.get(0);
                }
                if(driverVO!=null&&driverVO.getPhone()!=null){
                    if(systemFeginServer.selectIsSend(tenantId)){
                        SysSmsTempVO s = systemFeginServer.selectSmsSetByTplId(tenantId, CommonConstant.TPL_ID_NEW);
                        if(s!=null){
                            if(s.getIsSendDriver().equals("0")){
                                YunPianSMSUtils.sendDriverNewOrder(driverVO.getPhone(),ordCommonGoods.getMorderId());
                            }
                        }
                    }
                }

            } catch (Exception e) {
                //手动回滚事务
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return new R<>(Boolean.FALSE);
            }
        }
        //如果订单编号不为空，则表示编辑后发送司机
        if (ordCommonGoods.getMorderId() != null && !ordCommonGoods.getMorderId().equals("")) {

            try {
                ordCommonGoods.setDriverId(ordCommonGoods.getOrdCommonTruck().getMdriverId());
                ordCommonGoods.setSendGoodsPlace(StringUtils.join(ordCommonGoods.getSendPlaceArray(), "/"));
                ordCommonGoods.setPickGoodsPlace(StringUtils.join(ordCommonGoods.getPickPlaceArray(), "/"));
                // ordCommonGoods.setShipperCity(StringUtils.join(ordCommonGoods.getShipperCityArray(), "/"));
                //  ordCommonGoods.setPickerCity(StringUtils.join(ordCommonGoods.getPickerCityArray(), "/"));

                ordCommonGoodsMapper.updateCommonOrd(ordCommonGoods);
                //删除之前的货物信息
                //commonGoodsInfoMapper.deleteBatchIds(ordCommonGoods.getCommonGoodsInfos());
                commonGoodsInfoMapper.deleteByOrderId(ordCommonGoods.getMorderId());
                //更新普货的货物信息
                for (CommonGoodsInfo info : ordCommonGoods.getCommonGoodsInfos()) {
                    info.setOrderId(ordCommonGoods.getMorderId());
                    info.setTenantId(tenantId);
                    commonGoodsInfoMapper.insert(info);
                }
                //更新车辆调度信息
                if (ordCommonGoods.getOrdCommonTruck().getOrderId() == null || ordCommonGoods.getOrdCommonTruck().getOrderId().equals("")) {
                    ordCommonGoods.getOrdCommonTruck().setTenantId(tenantId);
                    ordCommonGoods.getOrdCommonTruck().setOrderId(ordCommonGoods.getMorderId());
                    ordCommonTruckMapper.insert(ordCommonGoods.getOrdCommonTruck());
                } else {
                    ordCommonTruckMapper.updateById(ordCommonGoods.getOrdCommonTruck());
                }

                //更新司机状态
                updateDriverStatus(ordCommonGoods.getOrdCommonTruck());

                //更新运踪信息
                OrdOrderLogistics ordOrderLogistics = new OrdOrderLogistics();
                ordOrderLogistics.setLogisticsMsg(CommonConstant.PC_MSG);
                ordOrderLogistics.setOrderId(ordCommonGoods.getMorderId());
                ordOrderLogistics.setOrderStatus(CommonConstant.ORDER_DJD);
                ordOrderLogistics.setTenantId(tenantId);
                ordOrderLogisticsMapper.insertOrderLogistics(ordOrderLogistics);

                //发生短信给司机

                DriverVO driverVO = new DriverVO();
                driverVO.setDriverId(ordCommonGoods.getOrdCommonTruck().getMdriverId());
                List<DriverVO> driverVOs = trunkFeign.getDriverList(driverVO);
                if (driverVOs != null && driverVOs.size() == 1) {
                    driverVO = driverVOs.get(0);
                }
                if(driverVO!=null&&driverVO.getPhone()!=null){
                    if(systemFeginServer.selectIsSend(tenantId)){
                        SysSmsTempVO s = systemFeginServer.selectSmsSetByTplId(tenantId, CommonConstant.TPL_ID_NEW);
                        if(s!=null){
                            if(s.getIsSendDriver().equals("0")){
                                YunPianSMSUtils.sendDriverNewOrder(driverVO.getPhone(),ordCommonGoods.getMorderId());
                            }
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return new R<>(Boolean.FALSE);
            }
        }

        return new R<>(Boolean.TRUE);
    }


    @Override
    @Transactional
    public R<Boolean> sendDriverAtManagePage(OrdCommonGoodsVo ordCommonGoods) {
        /*给司机派单前验证*/
        if(ordCommonGoods.getDriverId()!=null){
            DriverVO driverVO = trunkFeign.selectDriverStatus(ordCommonGoods.getDriverId(), ordCommonGoods.getMtenantId());
            if(driverVO == null){
                return new R<>(Boolean.FALSE,"该司机不存在！");
            }else {
                if(!driverVO.getStatus().equals("2")){
                    return new R<>(Boolean.FALSE,driverVO.getDriverName()+"该司机处于请假状态或者在途状态，不能接单！");
                }
                if(driverVO.getDelFlag().equals("1")){
                    return new R<>(Boolean.FALSE,driverVO.getDriverName()+"该司机已被删除！");
                }
            }
        }
        ordCommonGoods.setUpdateTime(new Date());
        ordCommonGoods.setStatus(CommonConstant.ORDER_DJD);
        ordCommonGoods.setSendTruckDate(new Date());
        OrdCommonGoods commonGoods = new OrdCommonGoods();
        BeanUtils.copyProperties(ordCommonGoods, commonGoods);
        try {
            ordCommonGoodsMapper.updateCommonOrd(commonGoods);
            //更新运踪信息
            OrdOrderLogistics ordOrderLogistics = new OrdOrderLogistics();
            ordOrderLogistics.setLogisticsMsg(CommonConstant.PC_MSG);
            ordOrderLogistics.setOrderId(ordCommonGoods.getMorderId());
            ordOrderLogistics.setOrderStatus(CommonConstant.ORDER_DJD);
            ordOrderLogistics.setTenantId(ordCommonGoods.getMtenantId());
            ordOrderLogisticsMapper.insertOrderLogistics(ordOrderLogistics);


            //获取司机手机号
            DriverVO driverVO = new DriverVO();
            driverVO.setDriverId(ordCommonGoods.getDriverId());
            List<DriverVO> driverVOs = trunkFeign.getDriverList(driverVO);
            if (driverVOs != null && driverVOs.size() == 1) {
                driverVO = driverVOs.get(0);
            }
            //发送司机短信
            if(driverVO!=null&&driverVO.getPhone()!=null){
                if(systemFeginServer.selectIsSend(ordCommonGoods.getMtenantId())){
                    SysSmsTempVO s = systemFeginServer.selectSmsSetByTplId(ordCommonGoods.getMtenantId(), CommonConstant.TPL_ID_NEW);
                    if(s!=null){
                        if(s.getIsSendDriver().equals("0")){
                            YunPianSMSUtils.sendDriverNewOrder(driverVO.getPhone(),ordCommonGoods.getMorderId());
                        }
                    }
                }
            }

           OrdCommonTruck ordCommonTruck = new OrdCommonTruck();
           BeanUtils.copyProperties(ordCommonGoods.getOrdCommonTruck(),ordCommonTruck);
           updateDriverStatus(ordCommonTruck);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new R<>(Boolean.FALSE);
        }
        return new R<>(Boolean.TRUE);
    }

    @Override
    public OrdCommonGoodsVo selectOneByOrderId(OrdCommonGoodsVo ordCommonGoodsVo) {
        return ordCommonGoodsMapper.selectByOrderId(ordCommonGoodsVo);
    }

    @Override
    public Boolean exportPhOrd(HttpServletRequest request, HttpServletResponse response, String ids, Integer tenantId) {
        try {
            String[] phid = Convert.toStrArray(ids);

            List<OrdCommonGoodsVo> ordCommonGoods = ordCommonGoodsMapper.selectExportGoodsList(phid);

            List<CommonOrdForExport> ordForExports = new ArrayList<>();
            //处理数据，初始化导出的集合
            for (OrdCommonGoodsVo cgvo : ordCommonGoods){
                cgvo.setCustomerName(getCustomerById(cgvo.getCustomerId(),tenantId).getCustomerName());
                //计算总体积，总重量
                BigDecimal sumWeight = new BigDecimal(0);
                BigDecimal sumVolume = new BigDecimal(0);
                for (CommonGoodsInfo info : cgvo.getCommonGoodsInfos()) {
                    if (info.getWeight() != null && info.getVolume() != null) {
                        sumVolume = sumVolume.add(info.getWeight());
                        sumWeight = sumWeight.add(info.getVolume());
                    }

                }
                cgvo.setSumWeight(sumWeight);
                cgvo.setSumVolume(sumVolume);
                CommonOrdForExport commonOrdForExport = new CommonOrdForExport();
                //给导出数据赋订单基本信息值
                BeanUtils.copyProperties(cgvo, commonOrdForExport);
                //结算方式
                if (cgvo.getBalanceWay().equals("0")) {
                    commonOrdForExport.setBalanceWayValue("单结");
                } else if (cgvo.getBalanceWay().equals("1")) {
                    commonOrdForExport.setBalanceWayValue("月结");
                }
                //是否开票
                if (cgvo.getIsInvoice() != null) {
                    if (cgvo.getIsInvoice().equals("0")) {
                        commonOrdForExport.setIsInvoiceValue("是");
                    } else if (cgvo.getIsInvoice().equals("1")) {
                        commonOrdForExport.setIsInvoiceValue("否");
                    }
                }

                commonOrdForExport.setStatusValue("草稿");

                //给要导出的数据的车辆调度赋值
                commonOrdForExport = executeExportInfo(cgvo, commonOrdForExport, tenantId);

                ordForExports.add(commonOrdForExport);
            }
            DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            String excelName = fmt.format(new Date()) + "-普货订单信息";
            ExcelUtil<CommonOrdForExport> util = new ExcelUtil<>(CommonOrdForExport.class);
            util.exportExcel(request, response, ordForExports, excelName, null);
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    /**
     * 处理车辆调度信息，初始化导出数据
     *
     * @param cgvo
     * @param commonOrdForExport
     * @return
     */
    private CommonOrdForExport executeExportInfo(OrdCommonGoodsVo cgvo, CommonOrdForExport commonOrdForExport, Integer tenantId) {
        //复制车辆调度信息
        if (cgvo.getOrdCommonTruck() != null) {
            BeanUtils.copyProperties(cgvo.getOrdCommonTruck(), commonOrdForExport);
            //司机信息处理
            List<DriverVO> driverVOS = getDriverVo(cgvo.getOrdCommonTruck(), tenantId);
            //主副司机都有
            if (driverVOS.size() == 2) {
                //主司机信息
                commonOrdForExport.setMdriver(driverVOS.get(0).getDriverName());
                commonOrdForExport.setMdriverPhone(driverVOS.get(0).getPhone());
                //副司机信息
                commonOrdForExport.setSdriver(driverVOS.get(1).getDriverName());
                commonOrdForExport.setSdriverPhone(driverVOS.get(1).getPhone());
            } else if (driverVOS.size() == 1) {
                //主司机信息
                commonOrdForExport.setMdriver(driverVOS.get(0).getDriverName());
                commonOrdForExport.setMdriverPhone(driverVOS.get(0).getPhone());
            }
            //应付结算方式
            if (commonOrdForExport.getPayType().equals("0")) {
                commonOrdForExport.setPayTypeValue("单结");
            } else if (commonOrdForExport.getPayType().equals("1")) {
                commonOrdForExport.setPayTypeValue("月结");
            }

            if (commonOrdForExport.getVehicleType() != null && !commonOrdForExport.getVehicleType().equals("")) {
                String label = getLabelByTypeAndValue("car_type", commonOrdForExport.getVehicleType(), tenantId).getLabel();
                commonOrdForExport.setVehicleAttribute(label + ":" + commonOrdForExport.getVehicleLength());
            }
            //车辆类型
            if (commonOrdForExport.getTruckType().equals("0")) {
                commonOrdForExport.setTruckTypeValue("自有车");
            } else if (commonOrdForExport.getTruckType().equals("1")) {
                commonOrdForExport.setTruckTypeValue("外调车");
            }

            /*提送货地址处理*/
            /*提货地*/
            List<OrdPickupArrivalAdd> pickupAdds = addMapper.selectPickupByOrderId(cgvo.getMorderId());
            if(pickupAdds.size()!=0){
                for(OrdPickupArrivalAdd pickupAdd:pickupAdds){
                    if(pickupAdd.getSort()==0){
                        commonOrdForExport.setPickupAdd1(pickupAdd.getContacts()+"-"+pickupAdd.getAddressCity()+pickupAdd.getAddressDetailPlace());
                        commonOrdForExport.setShipperPhone1(pickupAdd.getContactsPhone());
                    }else if(pickupAdd.getSort()==1){
                        commonOrdForExport.setPickupAdd2(pickupAdd.getContacts()+"-"+pickupAdd.getAddressCity()+pickupAdd.getAddressDetailPlace());
                        commonOrdForExport.setShipperPhone2(pickupAdd.getContactsPhone());
                    }else if(pickupAdd.getSort()==2){
                        commonOrdForExport.setPickupAdd3(pickupAdd.getContacts()+"-"+pickupAdd.getAddressCity()+pickupAdd.getAddressDetailPlace());
                        commonOrdForExport.setShipperPhone3(pickupAdd.getContactsPhone());
                    }
                }
            }
            /*送货地*/
            List<OrdPickupArrivalAdd> arrivalAdds = addMapper.selectArrivalByOrderId(cgvo.getMorderId());
            if(arrivalAdds.size()!=0){
                for(OrdPickupArrivalAdd arrivalAdd:arrivalAdds){
                    if(arrivalAdd.getSort()==0){
                        commonOrdForExport.setArrivalAdd1(arrivalAdd.getContacts()+"-"+arrivalAdd.getAddressCity()+arrivalAdd.getAddressDetailPlace());
                        commonOrdForExport.setPickerPhone1(arrivalAdd.getContactsPhone());
                    } else if(arrivalAdd.getSort()==1){
                        commonOrdForExport.setArrivalAdd2(arrivalAdd.getContacts()+"-"+arrivalAdd.getAddressCity()+arrivalAdd.getAddressDetailPlace());
                        commonOrdForExport.setPickerPhone2(arrivalAdd.getContactsPhone());
                    } else if(arrivalAdd.getSort()==2){
                        commonOrdForExport.setArrivalAdd3(arrivalAdd.getContacts()+"-"+arrivalAdd.getAddressCity()+arrivalAdd.getAddressDetailPlace());
                        commonOrdForExport.setPickerPhone3(arrivalAdd.getContactsPhone());
                    }
                }
            }

        }
        return commonOrdForExport;
    }

    @Override
    public Page selectCenterPage(Query objectQuery, CommonOrdSearch commonOrdSearch) {
        ArrayList<Integer> customers = getCustomers(commonOrdSearch.getCustomerName());
        Integer[] customerIds = new Integer[customers.size()];
        customers.toArray(customerIds);
        commonOrdSearch.setCustomerIds(customerIds);
        Integer tenantId = commonOrdSearch.getTenantId();
        List<OrdCommonGoodsVo> ordCommonGoods = ordCommonGoodsMapper.selectCommonOrdList(objectQuery, commonOrdSearch);
        if(ordCommonGoods.size()>0){
            //处理数据
            for (OrdCommonGoodsVo ocg : ordCommonGoods) {
                ocg.setCustomerName(getCustomerById(ocg.getCustomerId(),tenantId).getCustomerName());
                //计算总体积，总重量
                BigDecimal sumWeight = new BigDecimal(0);
                BigDecimal sumVolume = new BigDecimal(0);

                if(ocg.getCommonGoodsInfos()!=null&&ocg.getCommonGoodsInfos().size()>0){
                    for (CommonGoodsInfo info : ocg.getCommonGoodsInfos()) {
                        sumVolume = sumVolume.add(info.getWeight());
                        sumWeight = sumWeight.add(info.getVolume());
                    }
                    ocg.setSumWeight(sumWeight);
                    ocg.setSumVolume(sumVolume);
                }

                List<OrdPickupArrivalAdd> pickupadds = ocg.getPickupAdds();
                for(OrdPickupArrivalAdd add : pickupadds){
                    add.setFileArray(add.getFiles()!=null ? add.getFiles().split(","):null);
                }
                List<OrdPickupArrivalAdd> arrivalAdds = ocg.getArrivalAdds();
                for(OrdPickupArrivalAdd add : arrivalAdds){
                    add.setFileArray(add.getFiles()!=null ? add.getFiles().split(","):null);
                }

                //处理司机信息
                if(ocg.getOrdCommonTruck()!=null){
                    List<DriverVO> driverVOlist = getDriverVo(ocg.getOrdCommonTruck(), tenantId);
                    ocg.getOrdCommonTruck().setDriverVOS(driverVOlist);
                    if(Integer.parseInt(ocg.getStatus())>=5 && Integer.parseInt(ocg.getStatus())<9) {
                        ocg.setDriverAddress(ordOrderService.getAddressByplateNumber(ocg.getOrdCommonTruck().getPlateNumber()));
                    }
                }

                //初始化异常情况
                //存在异常，1.只有异常情况 2.只有异常费用 3.都有
                //1.异常情况
                if ((ocg.getExceptionConditions() != null && ocg.getExceptionConditions().size() > 0) && (ocg.getExceptionFees() != null && ocg.getExceptionFees().size() > 0)) {
                    ocg.setIfEx(CommonConstant.YC_ALL);
                } else if (ocg.getExceptionFees() != null && ocg.getExceptionFees().size() > 0) {
                    ocg.setIfEx(CommonConstant.YCFY);
                } else if (ocg.getExceptionConditions() != null && ocg.getExceptionConditions().size() > 0) {
                    ocg.setIfEx(CommonConstant.YCQK);
                } else {
                    //不存在异常
                    ocg.setIfEx(CommonConstant.WYC);

                }
            }
        }

        objectQuery.setRecords(ordCommonGoods);
        return objectQuery;
    }

    @Override
    public List<CommonOrdSearch> selectOrderByDriverId(OrdCommonGoods ordCommonGoods) {
        return ordCommonGoodsMapper.selectOrderByDriverId(ordCommonGoods);
    }

    @Override
    public List<CommonOrdSearch> selectOrderByDriverIdFeign(OrdCommonGoods ordCommonGoods) {
        return ordCommonGoodsMapper.selectOrderByDriverIdFeign(ordCommonGoods);
    }

    /**
     * 导出普货订单中心数据
     *
     * @param request
     * @param response
     * @param ids
     * @return
     */
    @Override
    public Boolean exportPHCenter(HttpServletRequest request, HttpServletResponse response, String ids, Integer tenantId) {
        try {
            String[] phid = Convert.toStrArray(ids);
            List<OrdCommonGoodsVo> ordCommonGoods = ordCommonGoodsMapper.selectCenterExportList(phid);
            /**循环处理集合
             1.应收结算方式 --
             2.应付结算方式--
             3.是否开票--
             4.车型--
             5.车辆类型（自有，外调）--
             6.订单状态
             7.司机信息
             **/
            //用于导出的集合
            List<CommonOrdForExport> exportList = new ArrayList<>();
            for (OrdCommonGoodsVo ocg : ordCommonGoods) {
                CommonOrdForExport phExport = new CommonOrdForExport();
                //复制订单信息和应收费用信息
                BeanUtils.copyProperties(ocg, phExport);
                phExport.setCustomerName(getCustomerById(ocg.getCustomerId(),tenantId).getCustomerName());
                //计算总体积，总重量
                BigDecimal sumWeight = new BigDecimal(0);
                BigDecimal sumVolume = new BigDecimal(0);
                for (CommonGoodsInfo info : ocg.getCommonGoodsInfos()) {
                    sumVolume = sumVolume.add(info.getWeight());
                    sumWeight = sumWeight.add(info.getVolume());
                }
                phExport.setSumWeight(sumWeight);
                phExport.setSumVolume(sumVolume);
                //处理应收费用信息部分字段
                //是否开发票
                if (ocg.getIsInvoice().equals("0")) {
                    phExport.setIsInvoiceValue("是");
                } else if (ocg.getIsInvoice().equals("1")) {
                    phExport.setIsInvoiceValue("否");
                }
                //应收结算方式
                if (ocg.getBalanceWay().equals("0")) {
                    phExport.setBalanceWayValue("单结");
                } else if (ocg.getBalanceWay().equals("1")) {
                    phExport.setBalanceWayValue("月结");
                }
                //订单状态，调用feign
                String status = getLabelByTypeAndValue("order_status", phExport.getStatus(), tenantId).getLabel();
                phExport.setStatusValue(status);
                //复制车辆调度信息
                phExport = executeExportInfo(ocg, phExport, tenantId);
                exportList.add(phExport);
            }
            DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            String excelName = fmt.format(new Date()) + "-普货订单中心";
            ExcelUtil<CommonOrdForExport> util = new ExcelUtil<>(CommonOrdForExport.class);
            util.exportExcel(request, response, exportList, excelName, null);

        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public SysDictVO getLabelByTypeAndValue(String type, String value, Integer tenantId) {
        SysDictVO condition = new SysDictVO();
        condition.setValue(value);
        condition.setType(type);
        condition.setTenantId(tenantId);
        return systemFeginServer.selectDict(condition);
    }

    public List<DriverVO> getDriverVo(OrdCommonTruckVO ordCommonTruckVO, Integer tenantId) {
        List<DriverVO> driverVOS = null;
        //获取司机的姓名电话
        if (ordCommonTruckVO.getMdriverId() != null && ordCommonTruckVO.getSdriverId() != null) {
            Integer mdriverId = ordCommonTruckVO.getMdriverId();
            Integer sdriverId = ordCommonTruckVO.getSdriverId();
            //根据主副司机的id去获取一个司机集合
            String zfId = mdriverId + "," + sdriverId;
            driverVOS = trunkFeign.getZFDriverInfo(zfId);
            //如果主司机和副司机的id一样，说明是一个人
            if (mdriverId.intValue() == sdriverId.intValue()) {
                driverVOS.add(driverVOS.get(0));
                return driverVOS;
            }
            //如果得到的集合的第一个就是主司机
            if (driverVOS.get(0).getDriverId() == mdriverId) {
                return driverVOS;
            } else {
                //调换位置
                Collections.swap(driverVOS, 0, 1);
                return driverVOS;
            }
        } else {
            DriverVO driverVO = new DriverVO();
            driverVO.setDriverId(ordCommonTruckVO.getMdriverId());
            driverVO.setTenantId(tenantId);
            List<DriverVO> driverVOs = trunkFeign.getDriverList(driverVO);
            if (driverVOs != null && driverVOs.size() == 1) {
                driverVO = driverVOs.get(0);
            }
            driverVOS = new ArrayList<>();
            driverVOS.add(driverVO);
            return driverVOS;
        }
    }

    /**
     * 根据司机id查询正在进行的订单
     *
     * @param driverId
     * @return
     */

    @Override
    public CommonGoodsVO selectNotEndPhOrdByDriverId(Integer driverId) {
        return ordCommonGoodsMapper.selectProcOrdByDriverId(driverId);
    }

    @Override
    public OrdCommonGoodsForApp selectDetailByOrderId(String orderId, Integer tenantId) {
        OrdCommonGoodsForApp ordCommonGoodsVo = ordCommonGoodsMapper.selectDetailByOrderId(orderId);
        /**
         * 1. 提货方式
         * 2. 运踪凭证
         */
        if (ordCommonGoodsVo.getPickGoodsWay().equals("0")) {
            ordCommonGoodsVo.setPickGoodsWay("上门提货");
        } else {
            ordCommonGoodsVo.setPickGoodsWay("派车直送");
        }
        ordCommonGoodsVo.setCustomerName(getCustomerById(ordCommonGoodsVo.getCustomerId(),tenantId).getCustomerName());

        String status = getLabelByTypeAndValue("order_status",ordCommonGoodsVo.getStatus(), tenantId).getLabel();
        ordCommonGoodsVo.setStatus(status);
        //解析订单运踪中的订单状态：提货，签收，获取两个状态的上传的凭证
        //提货凭证
        String[] thImgs = {};
        //签收
        String[] qsImgs = {};
        for (OrdOrderLogistics ool : ordCommonGoodsVo.getOrdOrderLogistics()) {
            //如果运踪是提货中
            if (ool.getOrderStatus().equals(CommonConstant.ORDER_YSZ)) {
                 //处理提货凭证图片
                if(ool.getPhotos()!=null&&!ool.getPhotos().equals("")){
                    thImgs = ool.getPhotos().split(",");
                }
            } else if (ool.getOrderStatus().equals(CommonConstant.ORDER_YQS)) {
                //获取签收凭证
                if(ool.getPhotos()!=null&&!ool.getPhotos().equals("")){
                    qsImgs = ool.getPhotos().split(",");
                }
            }
        }
        ordCommonGoodsVo.setReceiptImgList(qsImgs);
        ordCommonGoodsVo.setPickGoodsImgList(thImgs);
        //处理异常情况和异常费用
        if(ordCommonGoodsVo.getExceptionConditions()!=null&&ordCommonGoodsVo.getExceptionConditions().size()>0){
            for (OrdExceptionCondition exc:ordCommonGoodsVo.getExceptionConditions()) {
                exc.setPaths(exc.getOecFile()!=null ? exc.getOecFile().split(",") : new String[0]);
            }
        }
       if(ordCommonGoodsVo.getExceptionFees()!=null&&ordCommonGoodsVo.getExceptionFees().size()>0){
           for (OrdExceptionFee exf:ordCommonGoodsVo.getExceptionFees()) {
               //处理图片
               exf.setImgUrlFile(exf.getImgUrl()!=null?exf.getImgUrl().split(","):new String[0]);
               //处理异常费用类型
               String type = getLabelByTypeAndValue("exception_fee_type",exf.getExceptionFeeType(), tenantId).getLabel();
               exf.setExceptionFeeType(type);
           }
       }

        return ordCommonGoodsVo;
    }

    @Override
    public List<OrdCommonGoods> selectReceiveOrdCommonGoods(OrdCommonGoods ordCommonGoods) {
        return ordCommonGoodsMapper.selectReceiveOrdCommonGoods(ordCommonGoods);
    }

    @Override
    public List<OrdCommonGoods> selectDispatchOrdCommonGoods(OrdCommonGoods ordCommonGoods) {
        return ordCommonGoodsMapper.selectDispatchOrdCommonGoods(ordCommonGoods);
    }

    @Override
    public PhOrdForUpd selectUpdOrdForApp(String orderId) {
        PhOrdForUpd phOrdForUpd = ordCommonGoodsMapper.selectUPDOrdByOrderId(orderId);
        DriverVO driverVO = new DriverVO();
        driverVO.setDriverId(phOrdForUpd.getDriverId());
//        driverVO.setTenantId(tenantId);
        List<DriverVO> driverVOs = trunkFeign.getDriverList(driverVO);
        if (driverVOs != null && driverVOs.size() == 1) {
            driverVO = driverVOs.get(0);
        }
        phOrdForUpd.setDriverVO(driverVO);
        return phOrdForUpd;
    }

    /**
     * 功能描述: 小程序根据司机id查询已完成订单
     *
     * @param driverId
     * @return java.util.List<com.zhkj.lc.order.dto.OrderBaseInfoForApp>
     * @auther wzc
     * @date 2019/1/23 10:29
     */
    @Override
    public List<OrderBaseInfoForApp> selectCompletedOrderForApp(String driverId) {
        List<OrderBaseInfoForApp> list = commonGoodsInfoMapper.selectCompletedOrderForApp(driverId);
        List<OrderBaseInfoForApp> resultList = new ArrayList<>();
        list.forEach(order -> {
            OrderBaseInfoForApp info = new OrderBaseInfoForApp();
            List<String> goodsInfo = new ArrayList<>();
            switch (order.getOrderType()) {
                case "集装箱":
                    /*货品信息处理*/
                    String goodDesc = order.getGoodsName() + "/" + order.getWeight() + "千克";
                    goodsInfo.add(goodDesc);
                    break;
                case "普货":

                    /*货品信息处理*/
                    List<CommonGoodsInfo> goodsInfos = order.getCommonGoodsInfos();
                    goodsInfos.forEach(goods -> {
                        String phgoods = goods.getGoodsName() + "/" + goods.getWeight() + "千克";
                        goodsInfo.add(phgoods);
                    });
                    break;
            }
            /*运踪信息*/
            List<OrdOrderLogistics> logistics = order.getLogistics();
            for (OrdOrderLogistics log : logistics) {
                if (log.getOrderStatus().equals("7")) {//已提货时间
                    order.setSendGoodsDate(log.getLogisticsTime());
                }
                if (log.getOrderStatus().equals("9")) {//已签收时间
                    order.setReturnGoodsDate(log.getLogisticsTime());
                }
            }
            order.setGoodsInfo(goodsInfo);
            /*始发地-目的地处理*/
            Map map = addMapper.selectStartEndAdd(order.getOrderId());
            order.setOrdStart(map.get("pickupadd").toString());
            order.setOrdEnd(map.get("arrivaladd").toString());
            info.setNeedPayStatus(order.getNeedPayStatus());
            info.setOrderId(order.getOrderId());
            info.setStatus(order.getStatus());
            info.setOrderType(order.getOrderType());
            info.setOrdStart(order.getOrdStart());
            info.setOrdEnd(order.getOrdEnd());
            info.setGoodsInfo(order.getGoodsInfo());
            info.setPickGoodsWay(order.getPickGoodsWay());
            info.setSendGoodsDate(order.getSendGoodsDate());
            info.setReturnGoodsDate(order.getReturnGoodsDate());
            info.setPickupConPlace(order.getPickupConPlace());
            info.setPickupConDetailplace(order.getPickupConDetailplace());
            info.setReturnConPlace(order.getReturnConPlace());
            info.setReturnConDetailplace(order.getReturnConDetailplace());
            resultList.add(info);
        });

        return resultList;
    }

    @Override
    public int countByGoodsOrderStatus(OrdCommonGoods commonGoods) {
        return ordCommonGoodsMapper.countByGoodsOrderStatus(commonGoods);
    }

    @Override
    public List<OrdOrderLogisticsVo> selectOrder(OrdCommonGoods ordCommonGoods) {
        return ordCommonGoodsMapper.selectOrder(ordCommonGoods);
    }

    /**
     * 更新司机状态
     */
    public void updateDriverStatus(OrdCommonTruck ordCommonTruck){
        //如果主辅司机都存在，都更改状态
        if(ordCommonTruck.getMdriverId()!=null&&ordCommonTruck.getSdriverId()!=null){
            Integer[]driverIds = {ordCommonTruck.getMdriverId(),ordCommonTruck.getSdriverId()};
            //调用feign更新司机信息
            callTruckFeign(driverIds,CommonConstant.SJZT);
        }//只有主司机
        else if(ordCommonTruck.getMdriverId()!=null){
            //调用feign更新司机信息
            /*查询目前车辆下的司机信息*/
            List<DriverVO> status = trunkFeign.selectDriverStatusByPlateNumber(ordCommonTruck.getPlateNumber());
            Integer[] driverIds =  new Integer[status.size()];
            for(int i =0 ; i<status.size();i++){
                driverIds[i] = status.get(i).getDriverId();
            }
            callTruckFeign(driverIds,CommonConstant.SJZT);
        }

    }

    public void callTruckFeign(Integer[] driverIds,String status){
        DriverVO driverVO = new DriverVO();
        driverVO.setDriverIds(driverIds);
        driverVO.setStatus(status);
        commonGoodsTruckFeign.updateDriverSta(driverVO);
    }


    /*****************************************微信公众号端下单******************************************/

    @Override
    public Boolean wxAddCommonOrder(CommonGoodsForWXPublic ordCommonGoods) {
        /*订单信息*/
        OrdCommonGoods commonGoods = new OrdCommonGoods();
        /*货品信息*/
        CommonGoodsInfo goodsInfo = new CommonGoodsInfo();

        BeanUtils.copyProperties(ordCommonGoods, commonGoods);
        BeanUtils.copyProperties(ordCommonGoods, goodsInfo);

        //获取订单编号
        try {
            Map map = ordCommonGoodsMapper.getPhOrderId("PH", "");
            commonGoods.setMorderId(map.get("orderId").toString());
            //保存普货订单信息

            commonGoods.setShipperCity(commonGoods.getShipperCity().replace(" ","/"));
            commonGoods.setPickerCity(commonGoods.getPickerCity().replace(" ","/"));
            commonGoods.setSendGoodsPlace(commonGoods.getShipperCity().replace(" ","/"));
            commonGoods.setPickGoodsPlace(commonGoods.getPickerCity().replace(" ","/"));
            OrdPickupArrivalAdd pickAdd = new OrdPickupArrivalAdd();
            pickAdd.setOrderId(commonGoods.getMorderId());
            pickAdd.setSort(0);
            pickAdd.setTenantId(commonGoods.getMtenantId());
            pickAdd.setAddressCity(commonGoods.getShipperCity());
            pickAdd.setAddressDetailPlace(commonGoods.getShipperPlace());
            pickAdd.setAddType("0");
            pickAdd.setContacts(commonGoods.getShipper());
            pickAdd.setContactsPhone(commonGoods.getShipperPhone());
            addMapper.insert(pickAdd);
            OrdPickupArrivalAdd arrivAdd = new OrdPickupArrivalAdd();
            arrivAdd.setOrderId(commonGoods.getMorderId());
            arrivAdd.setSort(0);
            arrivAdd.setTenantId(commonGoods.getMtenantId());
            arrivAdd.setAddressCity(commonGoods.getPickerCity());
            arrivAdd.setAddressDetailPlace(commonGoods.getPickerPlace());
            arrivAdd.setAddType("1");
            arrivAdd.setContacts(commonGoods.getPicker());
            arrivAdd.setContactsPhone(commonGoods.getPickerPhone());
            addMapper.insert(arrivAdd);

            //设置订单状态为草稿
            commonGoods.setStatus(CommonConstant.ORDER_CG);
            ordCommonGoodsMapper.insertCommonOrder(commonGoods);
            //保存货物信息
            //设置货物的订单编号
            goodsInfo.setOrderId(map.get("orderId").toString());
            //设置租户id
            goodsInfo.setTenantId(ordCommonGoods.getTenantId());
            commonGoodsInfoMapper.insert(goodsInfo);


            logger.info(map.get("orderId").toString() + "订单下单成功");

            return Boolean.TRUE;
        } catch (Exception e) {
            logger.info(e.getMessage());
            return Boolean.FALSE;
        }
    }

    @Override
    public List<CommonGoodsWxSelect> selectListForWX(CommonOrdSearch ordSearch) {
        if(ordSearch.getStatus() != null && ordSearch.getStatus() !=""){
            ordSearch.setStatusArray(Convert.toStrArray(ordSearch.getStatus()));
        }
        List<CommonGoodsWxSelect> orders = ordCommonGoodsMapper.selectListForWX(ordSearch);
        if(orders!=null){
            List<SysDictVO> dictVOS = systemFeginServer.findDictByType("order_status", ordSearch.getTenantId());
            for(CommonGoodsWxSelect order:orders){
                order.setStatus(getLabel(dictVOS,order.getStatus()));
                // 收货人手机号不为空
                if(StringUtils.isNotEmpty(ordSearch.getPickerPhone())){
                    order.setFlag(1);
                    List<OrdPickupArrivalAdd> arrivalAdds = order.getArrivalAdds();
                    //去除非当前手机号地址信息
                    Iterator iterator = arrivalAdds.iterator();
                    while (iterator.hasNext()){
                        OrdPickupArrivalAdd add = (OrdPickupArrivalAdd) iterator.next();
                        if(!add.getContactsPhone().equals(ordSearch.getPickerPhone())){
                            iterator.remove();
                        }
                    }
                    String address = "";
                    String receiptCode = "";
                    for(int i=0; i< arrivalAdds.size(); i++){
                        OrdPickupArrivalAdd add = arrivalAdds.get(i);
                        if(add.getSuccessTime()== null){
                            address = add.getAddressCity();
                            receiptCode = add.getReceiptCode();
                            break;
                        }else if(add.getSuccessTime() != null){
                            if((i+1)==arrivalAdds.size()){
                                address = add.getAddressCity();
                                receiptCode = add.getReceiptCode();
                            }
                        }
                    }
                    order.setPickGoodsPlace(address);
                    order.setReceiptCode(receiptCode);
                    order.setSendGoodsPlace(order.getPickupAdds().get(0).getAddressCity());
                }
                // 发货人手机号不为空
                else if(StringUtils.isNotEmpty(ordSearch.getShipperPhone())) {
                    order.setFlag(0);
                    List<OrdPickupArrivalAdd> pickupAdds = order.getPickupAdds();
                    //去除非当前手机号地址信息
                    Iterator iterator = pickupAdds.iterator();
                    while (iterator.hasNext()){
                        OrdPickupArrivalAdd add = (OrdPickupArrivalAdd) iterator.next();
                        if(!add.getContactsPhone().equals(ordSearch.getShipperPhone())){
                            iterator.remove();
                        }
                    }
                    String address = "";
                    for(int i=0; i< pickupAdds.size(); i++){
                        OrdPickupArrivalAdd add = pickupAdds.get(i);
                        if(add.getSuccessTime()== null){
                            address = add.getAddressCity();
                            break;
                        }else if(add.getSuccessTime() != null){
                            if((i+1)==pickupAdds.size()){
                                address = add.getAddressCity();
                            }
                        }
                    }
                    order.setSendGoodsPlace(address);
                    order.setPickGoodsPlace(order.getArrivalAdds().get(order.getArrivalAdds().size()-1).getAddressCity());
                }
            }
        }
        return orders;
    }
    /**
     *
     * 功能描述:根据value获取订单类型label
     *
     * @param dicts	类型集合
     * @param value	类型值
     * @return java.lang.String
     * @auther wzc
     * @date 2019/02/22 19:29
     */
    public String getLabel(List<SysDictVO>dicts, String value){
        if(value==null ||("").equals(value)){
            return null;
        }
        for (SysDictVO dict : dicts) {
            if(value.equals(dict.getValue())){
                return dict.getLabel();
            }
        }
        return null;
    }
    /*根据订单编号查询*/
    @Override
    public CommonGoodsWxSelect selectByOrderIdForWX(String orderId, Integer tenantId){
        CommonGoodsWxSelect goods = ordCommonGoodsMapper.selectByOrderIdForWX(orderId, tenantId);
        /*获取物流信息*/
        if(goods != null) {
            SysDictVO sysDictVO = new SysDictVO();
            sysDictVO.setType("order_status");
            sysDictVO.setValue(goods.getStatus());
            sysDictVO.setTenantId(tenantId);
            SysDictVO sysDictVOS = systemFeginServer.selectDict(sysDictVO);
            goods.setStatus(sysDictVOS!=null?sysDictVOS.getLabel():goods.getStatus());
            List<OrdOrderLogistics> logistics = ordOrderLogisticsMapper.selectOrderLogisticsList(orderId);
            goods.setLogistics(logistics);
        }
        //todo 获取司机当前位置信息 经纬度数据

        return goods;
    }

    @Override
    public Boolean updateOrderBalanceStatus(OrdCommonGoods commonGoods) {
        return ordCommonGoodsMapper.updateOrderBalanceStatus(commonGoods);
    }

    @Override
    public List<Integer> countOrderNumber(Integer tenantId) {
        return ordCommonGoodsMapper.countOrderNumber(tenantId);
    }

    @Override
    public List<BigDecimal> countMoney(Integer tenantId) {
        return ordCommonGoodsMapper.countMoney(tenantId);
    }

    @Override
    public Boolean updateByOrderIds(String[] orderIds) {
        return ordCommonGoodsMapper.updateByOrderIds(orderIds);
    }

    @Override
    public PhOrdForUpd selectUPDOrdByOrderId(String orderId) {
        return ordCommonGoodsMapper.selectUPDOrdByOrderId(orderId);
    }

    @Override
    public PhOrdForUpd selectCommongoodsByOrderId(String orderId, Integer tenantId) {
        return ordCommonGoodsMapper.selectCommongoodsByOrderId(orderId,tenantId);
    }

    @Override
    public Integer selectCountByDriver(Integer driverId, Integer tenantId, String startDate, String endDate) {
        return ordCommonGoodsMapper.selectCountByDriver(driverId, tenantId, startDate, endDate);
    }

    @Override
    public Boolean updateCommonOrd(OrdCommonGoods ordCommonGoods) {
        return ordCommonGoodsMapper.updateCommonOrd(ordCommonGoods);
    }

    @Override
    @Transactional
    public Boolean updateBillByOrderIds(String[] orderIds, String login) {
        return ordCommonGoodsMapper.updateBillByOrderIds(orderIds, login);
    }

    @Override
    public Boolean updateCommonOrder(ReceiveBillCommonGoods ordCommonGoods) {
        return ordCommonGoodsMapper.updateCommonOrder(ordCommonGoods);
    }

    @Override
    public Integer selectProcOrdByDriverIds(Integer[] driverIds, Integer tenantId) {
        Integer countCG = ordCommonGoodsMapper.selectProcCGByDriverIds(driverIds, tenantId);
        Integer countOrd = ordOrderMapper.selectProcOrdByDriverIds(driverIds, tenantId);
        return countCG + countOrd;
    }

    @Override
    public List<Integer> selectTruckIdByProc(Integer tenantId) {
        List<CommonGoodsVO> plateNumberCG = ordCommonGoodsMapper.selectPlateNumberByProcCG(tenantId);
        List<OrdOrder> plateNumberOrd = ordOrderMapper.selectPlateNumberByProcOrd(tenantId);
        List<Integer> list = new ArrayList<Integer>();
        Integer m;
        if (null != plateNumberCG && plateNumberCG.size() > 0) {
            for (int i = 0; i < plateNumberCG.size(); i++) {
                if (null != plateNumberCG.get(i).getOrdCommonTruck()) {
                    m = commonGoodsTruckFeign.selectTruckIdBy(plateNumberCG.get(i).getMdriverId(), plateNumberCG.get(i).getOrdCommonTruck().getPlateNumber());
                    if (null != m) {
                        list.add(m);
                    }
                }
            }
        }
        if (null != plateNumberOrd && plateNumberOrd.size() > 0) {
            for (int i = 0; i < plateNumberOrd.size(); i++) {
                m = commonGoodsTruckFeign.selectTruckIdBy(plateNumberOrd.get(i).getDriverId(), plateNumberOrd.get(i).getPlateNumber());
                if (null != m) {
                    list.add(m);
                }
            }
        }
        return list;
    }

    @Override
    public List<DriverVO> selectPlateNumberByProc(Integer tenantId){
        List<CommonGoodsVO> plateNumberCG = ordCommonGoodsMapper.selectPlateNumberByProcCG(tenantId);
        List<OrdOrder> plateNumberOrd = ordOrderMapper.selectPlateNumberByProcOrd(tenantId);
        List<DriverVO> list = new ArrayList<DriverVO>();
        DriverVO driverVO = new DriverVO();
        if (null != plateNumberCG && plateNumberCG.size() > 0) {
            for (int i = 0; i < plateNumberCG.size(); i++) {
                if (null != plateNumberCG.get(i).getOrdCommonTruck()) {
                    driverVO.setDriverId(plateNumberCG.get(i).getMdriverId());
                    driverVO.setPlateNumber(plateNumberCG.get(i).getOrdCommonTruck().getPlateNumber());
                    list.add(driverVO);
                }
            }
        }
        if (null != plateNumberOrd && plateNumberOrd.size() > 0) {
            for (int i = 0; i < plateNumberOrd.size(); i++) {
                driverVO.setDriverId(plateNumberOrd.get(i).getDriverId());
                driverVO.setPlateNumber(plateNumberOrd.get(i).getPlateNumber());
                list.add(driverVO);
            }
        }
        return list;
    }

    /**
     * 根据客户id获取客户信息
     */
    public CustomerVO getCustomerById(Integer customerId, Integer tenantId){
        CustomerVO customer = new CustomerVO();
        CustomerVO customerVO = new CustomerVO();
        customer.setTenantId(tenantId);
        customer.setCustomerId(customerId);
        List<CustomerVO> customerVOS = trunkFeign.selectAllForFegin(customer);
        if(customerVOS != null &&customerVOS.size() ==1){
            customerVO = customerVOS.get(0);
            return customerVO;
        }
        return null;
    }
    /**
     * 根据客户名称获取客户信息（导入用）
     */
    public CustomerVO getCustomerByName(String customerName, Integer tenantId){
        CustomerVO customer = new CustomerVO();
        CustomerVO customerVO = new CustomerVO();
        customer.setTenantId(tenantId);
        customer.setCustomerName(customerName);
        List<CustomerVO> customerVOS = trunkFeign.getCustomerList(customer);
        if(customerVOS != null &&customerVOS.size() ==1){
            customerVO = customerVOS.get(0);
            return customerVO;
        }
        return null;
    }

    public ArrayList<Integer>getCustomers(String customerName){
        ArrayList<Integer> customers = new ArrayList();
        if(customerName!=null && customerName != ""){
            CustomerVO customerVO = new CustomerVO();
            customerVO.setCustomerName(customerName.trim());
            List<CustomerVO>customerVOS = trunkFeign.selectLikeAllForFegin(customerVO);
            for(CustomerVO c : customerVOS){
                customers.add(c.getCustomerId());
            }
        }
        return customers;
    }

    /**
     * //多个提送货地插入
     * @param adress 包含联系人电话-地址-详细地址等信息
     * @param orderId
     * @param tenantId
     * @param addType 插入类型
     * @param sort
     * @param phone
     * @return
     */
    public int insertAddress(String adress,String orderId,Integer tenantId,String addType,Integer sort,String phone){
        if(adress!=null&&adress!="") {
            OrdPickupArrivalAdd add = new OrdPickupArrivalAdd();
            add.setOrderId(orderId);
            add.setTenantId(tenantId);
            add.setAddType(addType);
            add.setSort(sort);
            add.setContactsPhone(phone);
            add.setContacts(adress.split("-")[0]);
            add.setAddressCity(adress.split("-")[1]);
            add.setAddressDetailPlace(adress.split("-")[2]);
            return addMapper.insert(add);
        }else{
            return 0;
        }
    }

    public int insertCommonGoodsInfo(CommonGoodsExcelBean commonGoodsExcelBean,String orderId,Integer tenantId){
        CommonGoodsInfo commonGoodsInfo =null;
        int goodsLength=commonGoodsExcelBean.getGoodsName().split("-").length;
            for(int i=0;i<goodsLength;i++){
                commonGoodsInfo=new CommonGoodsInfo();
                commonGoodsInfo.setOrderId(orderId);
                commonGoodsInfo.setTenantId(tenantId);
                commonGoodsInfo.setGoodsName(commonGoodsExcelBean.getGoodsName().split("-")[i]);
                commonGoodsInfo.setWeight(new BigDecimal(commonGoodsExcelBean.getWeight().split("-")[i]));
                commonGoodsInfo.setVolume(new BigDecimal(commonGoodsExcelBean.getVolume().split("-")[i]));
                commonGoodsInfo.setValue(new BigDecimal(commonGoodsExcelBean.getValue().split("-")[i]));
                commonGoodsInfo.setPackWay(commonGoodsExcelBean.getPackWay().split("-")[i]);
                commonGoodsInfo.setPackNum(Integer.valueOf(commonGoodsExcelBean.getPackNum().split("-")[i]));
                commonGoodsInfo.setGoodsRemarks(commonGoodsExcelBean.getGoodsRemarks().split("-")[i]);
                commonGoodsInfoMapper.insert(commonGoodsInfo);
            }
            return goodsLength;
    }
}
