package com.zhkj.lc.order.service.impl;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.zhkj.lc.common.api.FileUtils;
import com.zhkj.lc.common.api.YunPianSMSUtils;
import com.zhkj.lc.common.config.Global;
import com.zhkj.lc.common.constant.CommonConstant;
import com.zhkj.lc.common.util.*;
import com.zhkj.lc.common.util.excel.ExcelUtil;
import com.zhkj.lc.common.util.http.HttpClientUtil;
import com.zhkj.lc.common.util.support.Convert;
import com.zhkj.lc.common.vo.*;
import com.zhkj.lc.order.controller.FileController;
import com.zhkj.lc.order.dto.*;
import com.zhkj.lc.order.feign.CommonGoodsTruckFeign;
import com.zhkj.lc.order.feign.SystemFeginServer;
import com.zhkj.lc.order.feign.TrunkFeign;
import com.zhkj.lc.order.mapper.*;
import com.zhkj.lc.order.model.entity.*;
import com.zhkj.lc.order.service.IOrdOrderService;
import com.zhkj.lc.order.utils.CommonUtils;
import javafx.print.PageLayout;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.ArrayUtils;
import org.apache.http.entity.ContentType;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 订单管理 服务实现类
 * </p>
 *
 * @author wzc
 * @blame Android Team
 * @since 2018-12-07
 */
@AllArgsConstructor
@Service
public class OrdOrderServiceImpl extends ServiceImpl<OrdOrderMapper, OrdOrder> implements IOrdOrderService {

    @Autowired
    private OrdOrderMapper ordOrderMapper;

    @Autowired
    private OrdCommonGoodsMapper commonGoodsMapper;

    @Autowired
    private OrdOrderFileMapper fileMapper;

    @Autowired
    private TrunkFeign trunkFeign;

    @Autowired
    private SystemFeginServer systemFeginServer;

    @Autowired
    private TruBusinessMapper truBusinessMapper;

    @Autowired
    private OrdOrderLogisticsMapper logisticsMapper;

    @Autowired private OrdExceptionFeeMapper ordExceptionFeeMapper;

    @Autowired private OrdExceptionConditionMapper ordExceptionConditionMapper;

    @Autowired private CommonGoodsTruckFeign commonGoodsTruckFeign;

    @Autowired private FileController fileController;

    @Autowired private OrdPickupArrivalAddMapper addMapper;

    private final RedisTemplate redisTemplate;

    private static  final String PERFIX = "CN";

    /**
     * 查询订单管理信息
     *
     * @param orderId 订单管理ID
     * @return 订单管理信息
     */
    @Override
    public OrdOrder selectOrderById(String orderId, Integer tenantId)
    {
        String [] array = {};
        SysDictVO sysDictVO = new SysDictVO();
        OrdOrder  ordOrder = ordOrderMapper.selectOrderById(orderId, tenantId);
        if(ordOrder == null) {return null;}
        ordOrder.setPickupGoodsPlaceArray(ordOrder.getPickupGoodsPlace()==null?array:Convert.toStrArray("/",ordOrder.getPickupGoodsPlace()));
        ordOrder.setSendGoodsPlaceArray(ordOrder.getSendGoodsPlace()==null?array:Convert.toStrArray("/",ordOrder.getSendGoodsPlace()));
        ordOrder.setPickupConPlaceArray(ordOrder.getPickupConPlace()==null?array:Convert.toStrArray("/",ordOrder.getPickupConPlace()));
        ordOrder.setReturnConPlaceArray(ordOrder.getReturnConPlace()==null?array:Convert.toStrArray("/",ordOrder.getReturnConPlace()));

        sysDictVO.setType("order_status");
        sysDictVO.setValue(ordOrder.getStatus());
        sysDictVO.setTenantId(tenantId);
        SysDictVO statusDec = systemFeginServer.selectDict(sysDictVO);
        ordOrder.setStatusDec(statusDec==null?"":statusDec.getLabel());
        DriverVO driverVO = new DriverVO();
        driverVO.setTenantId(tenantId);
        driverVO.setDriverId(ordOrder.getDriverId());
        List<DriverVO> driverVs = trunkFeign.selectAllDriver(driverVO);
        if(driverVs != null && driverVs.size() == 1){
            ordOrder.setDriverName(driverVs.get(0).getDriverName());
            ordOrder.setDriverPhone(driverVs.get(0).getPhone());
            ordOrder.setDriverVO(driverVs.get(0));
        }
        /*凭证文件处理*/
        if(ordOrder.getOrdOrderFile()!=null){
            String othersFile=null;
            if(ordOrder.getOrdOrderFile().getFileA()!=null){
                othersFile = ordOrder.getOrdOrderFile().getFileA();
            }if(ordOrder.getOrdOrderFile().getFileB()!=null){
                othersFile = othersFile+ "," + ordOrder.getOrdOrderFile().getFileB();
            }if(ordOrder.getOrdOrderFile().getFileC()!=null){
                othersFile = othersFile+ "," + ordOrder.getOrdOrderFile().getFileC();
            }if(ordOrder.getOrdOrderFile().getFileD()!=null){
                othersFile = othersFile+ "," + ordOrder.getOrdOrderFile().getFileD();
            }if(ordOrder.getOrdOrderFile().getFileE()!=null){
                othersFile = othersFile+ "," + ordOrder.getOrdOrderFile().getFileE();
            }if(ordOrder.getOrdOrderFile().getFileF()!=null){
                othersFile = othersFile+ "," + ordOrder.getOrdOrderFile().getFileF();
            }if(ordOrder.getOrdOrderFile().getFileG()!=null){
                othersFile = othersFile+ "," + ordOrder.getOrdOrderFile().getFileG();
            }if(ordOrder.getOrdOrderFile().getFileH()!=null){
                othersFile = othersFile+ "," + ordOrder.getOrdOrderFile().getFileH();
            }
            if(othersFile != null){
                String [] otherArray = Convert.toStrArray(othersFile);
                ordOrder.getOrdOrderFile().setOtherFiles(otherArray);
            }
        }
        /*异常情况*/
        if(ordOrder.getOrdExceptionConditions() != null){
            ordOrder.getOrdExceptionConditions().forEach(excep->{
                if(excep.getOecFile()!=null){
                    excep.setPaths(Convert.toStrArray(excep.getOecFile()));
                }
            });
        }
        /*签收凭证*/
        if(ordOrder.getOrdOrderLogistics() != null){
            for(OrdOrderLogistics l : ordOrder.getOrdOrderLogistics()){
                if(l.getOrderStatus().equals("9")){
                    if(l.getPhotos() != null){
                        ordOrder.setReceiptPng(Convert.toStrArray(l.getPhotos()));
                    }
                }
            }
        }
        /*处理凭证问题*/
        List<OrdPickupArrivalAdd> pickupadds = ordOrder.getPickupAdds();
        for(OrdPickupArrivalAdd add : pickupadds){
            add.setFileArray(add.getFiles()!=null ? add.getFiles().split(","):null);
            add.setSsqArray(add.getAddressCity()!=null?add.getAddressCity().split("/"):null);
        }
        List<OrdOrderLogistics> logistics = ordOrder.getOrdOrderLogistics();
        System.out.println("99999999999999999999999999999");
        System.out.println(logistics);
        for(OrdOrderLogistics log : logistics){
            /*去程状态为5的时候获取提箱凭证*/
            if(  log.getOrderStatus().equals("5")){
                OrdPickupArrivalAdd pickCN = new OrdPickupArrivalAdd();
                pickCN.setAddressCity(ordOrder.getPickupConPlace()==null ?"":ordOrder.getPickupConPlace().replaceAll("/","") );
                pickCN.setRemark(log.getRemark());
                SimpleDateFormat format=new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
                pickCN.setSuccessTime(log.getLogisticsTime());
                pickCN.setFileArray(log.getPhotos()!=null ? log.getPhotos().split(",") : null);
                pickCN.setAddressDetailPlace(ordOrder.getPickupConDetailplace());
                ordOrder.setPickHnAddress(pickCN);
            }
            if(ordOrder.getType().equals("去程") && log.getOrderStatus().equals("7")){
                OrdPickupArrivalAdd pickCN = new OrdPickupArrivalAdd();
                pickCN.setAddressCity(ordOrder.getPickupConPlace());
                pickCN.setAddressDetailPlace(ordOrder.getPickupConDetailplace());
                pickCN.setFileArray(log.getPhotos()!=null ? log.getPhotos().split(",") : null);
                ordOrder.setPickCnAddress(pickCN);
            }
            /*去程状态为11的时候获取还箱凭证*/
            if(ordOrder.getType().equals("回程") && log.getOrderStatus().equals("11")){
                OrdPickupArrivalAdd returnCN = new OrdPickupArrivalAdd();
                returnCN.setAddressCity(ordOrder.getReturnConPlace());
                returnCN.setAddressDetailPlace(ordOrder.getReturnConDetailplace());
                returnCN.setFileArray(log.getPhotos()!=null ? log.getPhotos().split(",") : null);
                ordOrder.setReturnCnAddress(returnCN);
            }
        }
        List<OrdPickupArrivalAdd> arrivalAdds = ordOrder.getArrivalAdds();
        for(OrdPickupArrivalAdd add : arrivalAdds){
            add.setFileArray(add.getFiles()!=null ? add.getFiles().split(","):null);
            add.setSsqArray(add.getAddressCity()!=null?add.getAddressCity().split("/"):null);
        }
        return ordOrder;
    }

    @Override
    public OrdOrder selectOrderBaseById(String orderId, Integer tenantId) {
        return ordOrderMapper.selectOrderBaseById(orderId, tenantId);
    }

    /**
     * 查询订单管理列表
     *
     * @param query 订单管理信息
     * @return 订单管理集合
     */
    @Override
    public Page selectOrderList(Query query, OrderSearch orderSearch)
    {
        Integer tenantId = orderSearch.getTenantId();
        /*模糊查询获取车辆ids*/
        ArrayList<String> trucks = getPlates(orderSearch.getTruckAttribute());
        /*模糊查询获取客户ids*/
        ArrayList<Integer> customers = getCustomers(orderSearch.getCustomerName());
        if(customers.size()==0 && orderSearch.getCustomerName()!=null && orderSearch.getCustomerName().length()>0){
            return query.setRecords(null);
        }
        Integer[] customerIds = new Integer[customers.size()];
        customers.toArray(customerIds);
        orderSearch.setCustomerIds(customerIds);
        String [] plates =  new String[trucks.size()];
        trucks.toArray(plates);
        List<SysDictVO> types = null;
        List<SysDictVO> statuss = null;
        List<SysDictVO> attrs = null;
        orderSearch.setPlates(plates);
        List<OrdOrder> ordOrders = ordOrderMapper.selectOrderListByPage(query,orderSearch);
        if(ordOrders.size()>0){
            types = systemFeginServer.findDictByType("order_type", tenantId);
            statuss = systemFeginServer.findDictByType("order_status", tenantId);
            attrs = systemFeginServer.findDictByType("truck_attribute", tenantId);
        }
        //TODO 根据司机id查询
        ordOrders = getOrderDataList(ordOrders,types,statuss,attrs,tenantId);

            for (OrdOrder ordOrder : ordOrders) {
                if(StringUtils.isNotEmpty(ordOrder.getUpstreamId())) {
                    if (ordOrder.getType().equals("去程")) {
                        //去程取提货日期
                        ordOrder.setPickupOrSendGoodsDate(ordOrder.getPickupGoodsDate());

                    } else {
                        ordOrder.setPickupOrSendGoodsDate(ordOrder.getSendGoodsDate());
                    }
                }

            }

        return query.setRecords(ordOrders);
    }

    /**
     *
     * 功能描述: 订单集合处理
     *
     * @param ordOrders 订单集合
     * @param types 订单类型集合
     * @param statuss 订单状态集合
     * @param attrs 车辆类型集合
     * @return java.util.List<com.zhkj.lc.order.model.entity.OrdOrder>
     * @auther wzc
     * @date 2018/12/25 9:16
     */
    public List<OrdOrder>getOrderDataList(List<OrdOrder>ordOrders,List<SysDictVO> types,List<SysDictVO> statuss,List<SysDictVO> attrs, Integer tenantId){
        for (int i = ordOrders.size()-1; i >= 0; i--){
            if(null != ordOrders.get(i).getDriverId()){
                DriverVO driver= new DriverVO();
                DriverVO driverVO = new DriverVO();
                driver.setDriverId(ordOrders.get(i).getDriverId());
                driver.setTenantId(tenantId);
                List<DriverVO> driverVOS = trunkFeign.selectAllDriver(driver);
                if(driverVOS != null && driverVOS.size() ==1){
                    driverVO = driverVOS.get(0);
                }
                ordOrders.get(i).setDriverName(driverVO==null?"":driverVO.getDriverName());
                ordOrders.get(i).setDriverPhone(driverVO==null?"":driverVO.getPhone());
                ordOrders.get(i).setDriverVO(driverVO);
            }
            if(null != ordOrders.get(i) && ordOrders.get(i).getCustomerId() != null){
                CustomerVO customer = new CustomerVO();
                CustomerVO customerVO = new CustomerVO();
                customer.setTenantId(tenantId);
                customer.setCustomerId(ordOrders.get(i).getCustomerId());
                List<CustomerVO> customerVOS = trunkFeign.selectAllForFegin(customer);
                if(customerVOS != null &&customerVOS.size() ==1){
                    customerVO = customerVOS.get(0);
                }
                ordOrders.get(i).setCustomerVO(customerVO);
                ordOrders.get(i).setCustomerName(customerVO==null?"":customerVO.getCustomerName());
            }
            if(ordOrders.get(i).getPlateNumber()!=null){
                //如果存在车辆
                TruckVO truckVO = new TruckVO();
                truckVO.setTenantId(tenantId);
                truckVO.setPlateNumber(ordOrders.get(i).getPlateNumber());
                List<TruckVO> trucks = trunkFeign.selectTruckList(truckVO);
                if(trucks != null && trucks.size() == 1){
                    ordOrders.get(i).setTruckAttribute(getLabel(attrs, trucks.get(0).getAttribute()));
                }
                if(Integer.parseInt(ordOrders.get(i).getStatus()) >=3 && Integer.parseInt(ordOrders.get(i).getStatus())< 11) {
                    ordOrders.get(i).setDriverAddress(getAddressByplateNumber(ordOrders.get(i).getPlateNumber()));
                }
            }
            ordOrders.get(i).setStatusDec(getLabel(statuss, ordOrders.get(i).getStatus()));
            ordOrders.get(i).setType(getLabel(types, ordOrders.get(i).getType()));
            /*处理凭证问题*/
            List<OrdPickupArrivalAdd> pickupadds = ordOrders.get(i).getPickupAdds();
            for(OrdPickupArrivalAdd add : pickupadds){
                add.setFileArray(add.getFiles()!=null ? add.getFiles().split(","):null);

            }
            List<OrdOrderLogistics> logistics = ordOrders.get(i).getOrdOrderLogistics();
            for(OrdOrderLogistics log : logistics){
                /*去程状态为5的时候获取提箱凭证*/
                if(ordOrders.get(i).getType().equals("去程") && log.getOrderStatus().equals("5")){
                    OrdPickupArrivalAdd pickCN = new OrdPickupArrivalAdd();
                    pickCN.setAddressCity(ordOrders.get(i).getPickupConPlace());
                    pickCN.setAddressDetailPlace(ordOrders.get(i).getPickupConDetailplace());
                    pickCN.setFileArray(log.getPhotos()!=null ? log.getPhotos().split(",") : null);
                    ordOrders.get(i).setPickCnAddress(pickCN);
                }
                /*去程状态为5的时候获取提箱凭证*/
                if(ordOrders.get(i).getType().equals("回程") && log.getOrderStatus().equals("11")){
                    OrdPickupArrivalAdd returnCN = new OrdPickupArrivalAdd();
                    returnCN.setAddressCity(ordOrders.get(i).getReturnConPlace());
                    returnCN.setAddressDetailPlace(ordOrders.get(i).getReturnConDetailplace());
                    returnCN.setFileArray(log.getPhotos()!=null ? log.getPhotos().split(",") : null);
                    ordOrders.get(i).setReturnCnAddress(returnCN);
                }
            }
            List<OrdPickupArrivalAdd> arrivalAdds = ordOrders.get(i).getArrivalAdds();
            for(OrdPickupArrivalAdd add : arrivalAdds){
                add.setFileArray(add.getFiles()!=null ? add.getFiles().split(","):null);
            }
        }
        return ordOrders;
    }

    public List<OrdOrderForExport>getOrderDataListForExport(List<OrdOrderForExport>ordOrders,List<SysDictVO> types,List<SysDictVO> statuss,List<SysDictVO> attrs,Integer tenantId){
        for (int i = ordOrders.size()-1; i >= 0; i--){
            if(null != ordOrders.get(i).getDriverId()){
                DriverVO driver= new DriverVO();
                DriverVO driverVO = new DriverVO();
                driver.setDriverId(ordOrders.get(i).getDriverId());
                driver.setTenantId(tenantId);
                List<DriverVO> driverVOS = trunkFeign.selectAllDriver(driver);
                if(driverVOS != null && driverVOS.size() ==1){
                    driverVO = driverVOS.get(0);
                }
                ordOrders.get(i).setDriverName(driverVO==null?"":driverVO.getDriverName());
            }
            if(null != ordOrders.get(i) && ordOrders.get(i).getCustomerId() != null){
                CustomerVO customer = new CustomerVO();
                CustomerVO customerVO = new CustomerVO();
                customer.setTenantId(tenantId);
                customer.setCustomerId(ordOrders.get(i).getCustomerId());
                List<CustomerVO> customerVOS = trunkFeign.selectAllForFegin(customer);
                if(customerVOS != null &&customerVOS.size() ==1){
                    customerVO = customerVOS.get(0);
                }
                ordOrders.get(i).setCustomerName(customerVO==null?"":customerVO.getCustomerName());
            }
            if(ordOrders.get(i).getPlateNumber()!=null){
                //如果存在车辆
                TruckVO truckVO = new TruckVO();
                truckVO.setTenantId(tenantId);
                truckVO.setPlateNumber(ordOrders.get(i).getPlateNumber());
                List<TruckVO> trucks = trunkFeign.selectTruckList(truckVO);
                if(trucks != null && trucks.size() == 1){
                    ordOrders.get(i).setTruckAttribute(getLabel(attrs, trucks.get(0).getAttribute()));
                }
            }
            ordOrders.get(i).setStatusDec(getLabel(statuss, ordOrders.get(i).getStatus()));
            ordOrders.get(i).setType(getLabel(types, ordOrders.get(i).getType()));

            /*提送货地址处理*/
            /*提货地*/
            List<OrdPickupArrivalAdd> pickupAdds = addMapper.selectPickupByOrderId(ordOrders.get(i).getOrderId());
            if(pickupAdds.size()!=0){
                for(OrdPickupArrivalAdd pickupAdd:pickupAdds){
                    if(pickupAdd.getSort()==0){
                        ordOrders.get(i).setPickupAdd1(pickupAdd.getContacts()+"-"+pickupAdd.getAddressCity()+pickupAdd.getAddressDetailPlace());
                        ordOrders.get(i).setConsignorPhone1(pickupAdd.getContactsPhone());
                        ordOrders.get(i).setPickupGoodsDate1(pickupAdd.getAddressCity().split(" ")[0]);
                    }else if(pickupAdd.getSort()==1){
                        ordOrders.get(i).setPickupAdd2(pickupAdd.getContacts()+"-"+pickupAdd.getAddressCity()+pickupAdd.getAddressDetailPlace());
                        ordOrders.get(i).setConsignorPhone2(pickupAdd.getContactsPhone());
                        ordOrders.get(i).setPickupGoodsDate2(pickupAdd.getAddressCity().split(" ")[0]);
                    } else if(pickupAdd.getSort()==2){
                        ordOrders.get(i).setPickupAdd3(pickupAdd.getContacts()+"-"+pickupAdd.getAddressCity()+pickupAdd.getAddressDetailPlace());
                        ordOrders.get(i).setConsignorPhone3(pickupAdd.getContactsPhone());
                        ordOrders.get(i).setPickupGoodsDate3(pickupAdd.getAddressCity().split(" ")[0]);
                    }
                }
            }
            /*送货地*/
            List<OrdPickupArrivalAdd> arrivalAdds = addMapper.selectArrivalByOrderId(ordOrders.get(i).getOrderId());
            if(arrivalAdds.size()!=0){
                for(OrdPickupArrivalAdd arrivalAdd:arrivalAdds){
                    if(arrivalAdd.getSort()==0){
                        ordOrders.get(i).setArrivalAdd1(arrivalAdd.getContacts()+"-"+arrivalAdd.getAddressCity()+arrivalAdd.getAddressDetailPlace());
                        ordOrders.get(i).setConsigneePhone1(arrivalAdd.getContactsPhone());
                        ordOrders.get(i).setSendGoodsDate1(arrivalAdd.getAddressCity().split(" ")[0]);
                    } else if(arrivalAdd.getSort()==1){
                        ordOrders.get(i).setArrivalAdd2(arrivalAdd.getContacts()+"-"+arrivalAdd.getAddressCity()+arrivalAdd.getAddressDetailPlace());
                        ordOrders.get(i).setConsigneePhone2(arrivalAdd.getContactsPhone());
                        ordOrders.get(i).setSendGoodsDate2(arrivalAdd.getAddressCity().split(" ")[0]);
                    } else if(arrivalAdd.getSort()==2){
                        ordOrders.get(i).setArrivalAdd3(arrivalAdd.getContacts()+"-"+arrivalAdd.getAddressCity()+arrivalAdd.getAddressDetailPlace());
                        ordOrders.get(i).setConsigneePhone3(arrivalAdd.getContactsPhone());
                        ordOrders.get(i).setSendGoodsDate3(arrivalAdd.getAddressCity().split(" ")[0]);
                    }
                }
            }
        };
        return ordOrders;
    }

    /**
     *
     * 功能描述: 根据车辆类型获取车辆集合
     *
     * @param truckAttribute 车辆类型（自有、挂靠、外调）
     * @return java.util.ArrayList<java.lang.String>
     * @auther wzc
     * @date 2019/1/4 8:09
     */
    public ArrayList<String>getPlates(String truckAttribute){
        ArrayList<String> trucks = new ArrayList();
        if(truckAttribute!= null && truckAttribute!= ""){
            TruckVO truckVO = new TruckVO();
            truckVO.setAttribute(truckAttribute);
            List<TruckVO> voList = trunkFeign.selectTruckList(truckVO);
            for(int j =0;j<voList.size();j++){
                trucks.add(voList.get(j).getPlateNumber());
            }
        }
        return trucks;
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
     * 新增订单管理
     *
     * @param order 订单管理信息
     * @return 结果
     */
    @Override
    public String insertOrder(OrdOrder order) throws IOException {
        String orderId = getOrderId();
        order.setOrderId(orderId);
        /*处理数组地址数据*/
        order.setSendGoodsPlace(StringUtils.join(order.getSendGoodsPlaceArray(),"/"));
        order.setPickupGoodsPlace(StringUtils.join(order.getPickupConPlaceArray(),"/"));
        order.setPickupConPlace(StringUtils.join(order.getPickupConPlaceArray(),"/"));
        order.setReturnConPlace(StringUtils.join(order.getReturnConPlaceArray(),"/"));
        ordOrderMapper.insert(order);
        /*处理地址信息*/
        List<OrdPickupArrivalAdd> arrivalAdds = order.getArrivalAdds();
        for(OrdPickupArrivalAdd add : arrivalAdds){
            add.setTenantId(order.getTenantId());
            add.setOrderId(orderId);
            //接收数组类型字段，并以“/”进行分割
            StringBuffer sb = new StringBuffer();
            String[] array = add.getSsqArray();
            for(int i=0; i< array.length;i++){
                if(array[i].contains("市辖区")){
                    array[i] = array[0];
                }
                add.setAddressCity(sb.append(array[i]+"/").toString());
            }
            /*if(add.getAddressCity().contains("市辖区")) {
                String [] city = add.getAddressCity().split("/");
                add.setAddressCity(add.getAddressCity().replace("市辖区", city[0]));
            }*/
            addMapper.insert(add);
        }
        List<OrdPickupArrivalAdd> pickupAdds = order.getPickupAdds();
        for(OrdPickupArrivalAdd add : pickupAdds){
            add.setTenantId(order.getTenantId());
            add.setOrderId(orderId);
            //接收数组类型字段，并以“/”进行分割
            StringBuffer sf = new StringBuffer();
            String[] pickArray = add.getSsqArray();
            for(int i=0; i< pickArray.length;i++){
                if(pickArray[i].contains("市辖区")){
                    pickArray[i] = pickArray[0];
                }
                add.setAddressCity(sf.append(pickArray[i]+"/").toString());
            }
            /*if(add.getAddressCity().contains("市辖区")) {
                String [] city = add.getAddressCity().split("/");
                add.setAddressCity(add.getAddressCity().replace("市辖区", city[0]));
            }*/
            addMapper.insert(add);
        }

        /*保存上传文件url*/
        OrdOrderFile file = order.getOrdOrderFile();
        uploadOtherFiles(file,orderId, order.getTenantId(), true);
        /*生成PDF文件*/
        String pdfPath = createPDFofSendTruckList(order);
        /*将生成的PDF派车单转成jpg格式文件*/
        if(pdfPath != null){
            if(PDFUtils.pdf2Image(pdfPath,Global.IMGPATH)){
                /*转MultipartFile*/
                File imgFile = new File(Global.IMGPATH+orderId+".jpg");
                FileInputStream in_file = new FileInputStream(imgFile);
                MultipartFile multi = new MockMultipartFile(imgFile.getName(), imgFile.getName(),
                        ContentType.APPLICATION_OCTET_STREAM.toString(), in_file);
                /*转 MultipartFile 结束*/
                /*文件上传*/
                String pcdPath = fileController.localUpload(multi).getData();
                if(!pcdPath.equals("erroe")){
                    OrdOrderFile ordOrderFile = new OrdOrderFile();
                    ordOrderFile.setOrderId(orderId);
                    ordOrderFile.setSendTruckFile(pcdPath);
                    fileMapper.updateOrderFile(ordOrderFile);
                    /*删除本地图片和PDF文件*/
                    PDFUtils.deleteDirectory(Global.IMGPATH+orderId+".jpg");
                }
                in_file.close();
                /*删除临时文件*/
                PDFUtils.deleteDirectory(Global.LINSHI+orderId+".jpg");
            }
        }
        /*将提箱单文件和公章合成*/
        /*下载提箱单文件和公章文件*/
        if(file!=null && file.getPickupCnFile()!=null && file.getOfficialSeal() != null) {
//            PDFUtils.downloadFromUrl(order.getOrdOrderFile().getPickupCnFile(), Global.TXDPATH);
//            PDFUtils.downloadFromUrl(order.getOrdOrderFile().getOfficialSeal(), Global.GZPATH);
            String pickFileName = PDFUtils.getFileNameFromUrl(order.getOrdOrderFile().getPickupCnFile());
            String sealFileName = PDFUtils.getFileNameFromUrl(order.getOrdOrderFile().getOfficialSeal());
            PDFUtils.copyFile(Global.FILE_PATH_ORDER+pickFileName, Global.TXDPATH,pickFileName);
            PDFUtils.copyFile(Global.FILE_PATH_ORDER+sealFileName, Global.GZPATH,sealFileName);
            if(PDFUtils.createOficeSign(Global.TXDPATH + pickFileName, Global.GZTXDPATH+"txdgz"+pickFileName, Global.GZPATH +sealFileName)){
                /*获取生成的带公章提箱单的文件名*/
                String filename = ("txdgz" + pickFileName).replace(".pdf",".jpg");
                /*将PDF转成jpg格式*/
                if(PDFUtils.pdf2Image(Global.GZTXDPATH + "txdgz" + pickFileName,Global.IMGPATH)){
                    /*转 MultipartFile 开始*/
                    File imgFile = new File(Global.IMGPATH + filename);
                    FileInputStream in_file = new FileInputStream(imgFile);
                    MultipartFile multi = new MockMultipartFile(imgFile.getName(), imgFile.getName(),
                            ContentType.APPLICATION_OCTET_STREAM.toString(), in_file);
                    /*转 MultipartFile 结束*/
                    String gztxdPaths = fileController.localUpload(multi).getData();
                    if(!gztxdPaths.equals("error")){
                        System.out.println("上传 “公章提箱单” 图片保存地址：" + gztxdPaths);
                        OrdOrderFile ordOrderFile = new OrdOrderFile();
                        ordOrderFile.setOrderId(orderId);
                        ordOrderFile.setPickupCnNewfile(gztxdPaths);
                        fileMapper.updateOrderFile(ordOrderFile);
                    }
                    /*删除临时文件*/
//                    PDFUtils.deleteDirectory(dire);
                    in_file.close();
                    /*删除下载文件*/
//                    PDFUtils.deleteDirectory(Global.TXDPATH + file.getPickupCnFile());
//                    PDFUtils.deleteDirectory(Global.GZPATH + file.getOfficialSeal());
                    PDFUtils.deleteDirectory(Global.TXDPATH + pickFileName);
                    PDFUtils.deleteDirectory(Global.GZPATH + sealFileName);
                    /*删除本地jpg文件*/
                    PDFUtils.deleteDirectory(Global.IMGPATH + filename);
                }
            }
        }
        return orderId;
    }

    /**
     * 修改订单管理
     *
     * @param order 订单管理信息
     * @return 结果
     */
    @Override
    @Transactional
    public boolean update(OrdOrder order) throws IOException {
        /*处理地址信息*/
        order.setReturnConPlace(StringUtils.join(order.getReturnConPlaceArray(),"/"));
        addMapper.deleteByOrderId(order.getOrderId());
        List<OrdPickupArrivalAdd> arrivalAdds = order.getArrivalAdds();
        for(OrdPickupArrivalAdd add : arrivalAdds){
            add.setTenantId(order.getTenantId());
            add.setOrderId(order.getOrderId());
            //接收数组类型字段，并以“/”进行分割
            StringBuffer sb = new StringBuffer();
            String[] array = add.getSsqArray();
            for(int i=0; i< array.length;i++){
                if(array[i].contains("市辖区")){
                    array[i] = array[0];
                }
                add.setAddressCity(sb.append(array[i]+"/").toString());
            }
            /*if(add.getAddressCity().contains("市辖区")) {
                String [] city = add.getAddressCity().split("/");
                add.setAddressCity(add.getAddressCity().replace("市辖区", city[0]));
            }*/
            addMapper.insert(add);
        }
        List<OrdPickupArrivalAdd> pickupAdds = order.getPickupAdds();
        for(OrdPickupArrivalAdd add : pickupAdds){
            add.setTenantId(order.getTenantId());
            add.setOrderId(order.getOrderId());
            //接收数组类型字段，并以“/”进行分割
            StringBuffer sf = new StringBuffer();
            String[] pickArray = add.getSsqArray();
            for(int i=0; i< pickArray.length;i++){
                if(pickArray[i].contains("市辖区")){
                    pickArray[i] = pickArray[0];
                }
                add.setAddressCity(sf.append(pickArray[i]+"/").toString());
            }
            /*if(add.getAddressCity().contains("市辖区")) {
                String [] city = add.getAddressCity().split("/");
                add.setAddressCity(add.getAddressCity().replace("市辖区", city[0]));
            }*/
            addMapper.insert(add);
        }

        /*保存上传文件url*/
        OrdOrderFile file = order.getOrdOrderFile();
        uploadOtherFiles(file,order.getOrderId(), order.getTenantId(), false);
        /*生成PDF文件*/
        String pdfPath = createPDFofSendTruckList(order);
        /*将生成的PDF派车单转成jpg格式文件*/
        if(pdfPath != null){
            if(PDFUtils.pdf2Image(pdfPath,Global.IMGPATH)){
                /*转MultipartFile*/
                File imgFile = new File(Global.IMGPATH+order.getOrderId()+".jpg");
                FileInputStream in_file = new FileInputStream(imgFile);
                MultipartFile multi = new MockMultipartFile(imgFile.getName(), imgFile.getName(),
                        ContentType.APPLICATION_OCTET_STREAM.toString(), in_file);
                /*转 MultipartFile 结束*/
                /*文件上传*/
                String pcdPath = fileController.localUpload(multi).getData();
                if(!pcdPath.equals("erroe")){
                    OrdOrderFile ordOrderFile = new OrdOrderFile();
                    ordOrderFile.setOrderId(order.getOrderId());
                    ordOrderFile.setSendTruckFile(pcdPath);
                    fileMapper.updateOrderFile(ordOrderFile);
                    /*删除本地图片和PDF文件*/
                    PDFUtils.deleteDirectory(Global.IMGPATH+order.getOrderId()+".jpg");
                }
                in_file.close();
                /*删除临时文件*/
                PDFUtils.deleteDirectory(Global.LINSHI+order.getOrderId()+".jpg");
            }
        }
        /*将提箱单文件和公章合成*/
        /*下载提箱单文件和公章文件*/
        if(file!=null && file.getPickupCnFile()!=null && file.getOfficialSeal() != null) {
//            PDFUtils.downloadFromUrl(order.getOrdOrderFile().getPickupCnFile(), Global.TXDPATH);
//            PDFUtils.downloadFromUrl(order.getOrdOrderFile().getOfficialSeal(), Global.GZPATH);
            String pickFileName = PDFUtils.getFileNameFromUrl(order.getOrdOrderFile().getPickupCnFile());
            String sealFileName = PDFUtils.getFileNameFromUrl(order.getOrdOrderFile().getOfficialSeal());
            PDFUtils.copyFile(Global.FILE_PATH_ORDER+pickFileName, Global.TXDPATH,pickFileName);
            PDFUtils.copyFile(Global.FILE_PATH_ORDER+sealFileName, Global.GZPATH,sealFileName);
            if(PDFUtils.createOficeSign(Global.TXDPATH + pickFileName, Global.GZTXDPATH+"txdgz"+pickFileName, Global.GZPATH +sealFileName)){
                /*获取生成的带公章提箱单的文件名*/
                String filename = ("txdgz" + pickFileName).replace(".pdf",".jpg");
                /*将PDF转成jpg格式*/
                if(PDFUtils.pdf2Image(Global.GZTXDPATH + "txdgz" + pickFileName,Global.IMGPATH)){
                    /*转 MultipartFile 开始*/
                    File imgFile = new File(Global.IMGPATH + filename);
                    FileInputStream in_file = new FileInputStream(imgFile);
                    MultipartFile multi = new MockMultipartFile(imgFile.getName(), imgFile.getName(),
                            ContentType.APPLICATION_OCTET_STREAM.toString(), in_file);
                    /*转 MultipartFile 结束*/
                    String gztxdPaths = fileController.localUpload(multi).getData();
                    if(!gztxdPaths.equals("error")){
                        System.out.println("上传 “公章提箱单” 图片保存地址：" + gztxdPaths);
                        OrdOrderFile ordOrderFile = new OrdOrderFile();
                        ordOrderFile.setOrderId(order.getOrderId());
                        ordOrderFile.setPickupCnNewfile(gztxdPaths);
                        fileMapper.updateOrderFile(ordOrderFile);
                    }
                    /*删除临时文件*/
//                    PDFUtils.deleteDirectory(dire);
                    in_file.close();
                    /*删除下载文件*/
//                    PDFUtils.deleteDirectory(Global.TXDPATH + file.getPickupCnFile());
//                    PDFUtils.deleteDirectory(Global.GZPATH + file.getOfficialSeal());
                    PDFUtils.deleteDirectory(Global.TXDPATH + pickFileName);
                    PDFUtils.deleteDirectory(Global.GZPATH + sealFileName);
                    /*删除本地jpg文件*/
                    PDFUtils.deleteDirectory(Global.IMGPATH + filename);
                }
            }
        }
        return ordOrderMapper.updateOrder(order);
    }

    @Override
    public boolean updateOrderStatus(OrdOrder order)
    {
        return ordOrderMapper.updateOrder(order);
    }

    /**
     * 删除订单管理对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public boolean deleteOrderByIds(String ids, Integer tenantId, String login){
        try {
            String[] idsArray = Convert.toStrArray(ids);
            for (int i = 0; i < idsArray.length; i++){
                ordExceptionFeeMapper.deleteByOrderId(CommonConstant.STATUS_DEL,tenantId, login, idsArray[i]);
                ordExceptionConditionMapper.deleteByOrderId(CommonConstant.STATUS_DEL,tenantId, idsArray[i]);
                /*查询该订单下司机的订单数*/
                OrdOrder order = ordOrderMapper.selectOrderBaseById(idsArray[i], tenantId);
                if(order != null && order.getDriverId() != null){
                    //获取正在进行的普货订单
                    CommonGoodsVO commonGoodsVO = commonGoodsMapper.selectProcOrdByDriverId(order.getDriverId());
                    //获取正在进行的集装箱订单
                    OrdOrder ordOrder = ordOrderMapper.seleteNotEndOrderByDriverId(order.getDriverId());
                    if(commonGoodsVO == null && ordOrder != null){
                        /*如果都不存在订单，将司机状态改为空闲*/
                        DriverVO driverVO = trunkFeign.selectDriverStatus(order.getDriverId(), tenantId);
                        if(driverVO != null && driverVO.getStatus().equals("1")){

                            Integer[] driverIds = {ordOrder.getDriverId()};
                            driverVO.setStatus(CommonConstant.SJKX);
                            driverVO.setDriverIds(driverIds);
                            commonGoodsTruckFeign.updateDriverSta(driverVO);
                        }
                    }
                }
            }
            return ordOrderMapper.deleteOrderByIds(CommonConstant.STATUS_DEL, tenantId, login, idsArray);
        }catch (Exception e){
            return false;
        }
    }

    /**
     *
     * 功能描述: 导入集装箱订单
     *
     * @param file	导入文件
     * @param loginName	登陆者
     * @return boolean
     * @auther wzc
     * @date 2019/1/4 15:25
     */
    @Override
    @Transactional
    public R<Boolean> importOrder(MultipartFile file, String loginName, Integer tenantId) throws Exception {
        InputStream inputStream = file.getInputStream();
        try {
            ImportParams params = new ImportParams();
            params.setHeadRows(1);
            params.setTitleRows(1);
            List<OrdOrderForImport> orderImports = ExcelImportUtil.importExcel(inputStream,OrdOrderForImport.class,params);
            inputStream.close();
            /*导入数据check*/
            List<OrdOrderForImport> list = new ArrayList<OrdOrderForImport>();
            for(OrdOrderForImport orderImport:orderImports){
                if(CommonUtils.checkObjAllFieldsIsNull(orderImport)){
                    continue;
                }
                CustomerVO customerVO = getCustomerByName(orderImport.getCustomerName(),tenantId);
                if(customerVO != null) {
                    orderImport.setCustomerId(customerVO.getCustomerId());
                    list.add(orderImport);
                }else {
                    return new R<>(Boolean.FALSE,"系统中未录入客户信息或者客户名称不正确！");
                }
            };
            OrdOrder order=null;
            for (OrdOrderForImport orderImport:list){
                String orderId=getOrderId();
                order=new OrdOrder();
                BeanUtils.copyProperties(orderImport,order);
                order.setOrderId(orderId);
                order.setStatus(CommonConstant.ORDER_CG);
                order.setCreateBy(loginName);
                order.setCreateTime(new Date());
                order.setTenantId(tenantId);
                ordOrderMapper.insert(order);

                //提送货地数据插入附表
                insertAddress(orderImport.getPickupAdd1(),orderId,tenantId,"0",0,orderImport.getConsignorPhone1(),orderImport.getPickupGoodsDate1());
                insertAddress(orderImport.getPickupAdd2(),orderId,tenantId,"0",1,orderImport.getConsignorPhone2(),orderImport.getPickupGoodsDate2());
                insertAddress(orderImport.getPickupAdd3(),orderId,tenantId,"0",2,orderImport.getConsignorPhone3(),orderImport.getPickupGoodsDate3());
                insertAddress(orderImport.getArrivalAdd1(),orderId,tenantId,"1",0,orderImport.getConsigneePhone1(),orderImport.getSendGoodsDate1());
                insertAddress(orderImport.getArrivalAdd2(),orderId,tenantId,"1",1,orderImport.getConsigneePhone2(),orderImport.getSendGoodsDate2());
                insertAddress(orderImport.getArrivalAdd3(),orderId,tenantId,"1",2,orderImport.getConsigneePhone3(),orderImport.getSendGoodsDate3());
            }
        }catch (Exception e){
            return new R<>(Boolean.FALSE,e.getMessage());
        }
        return new R<>(Boolean.TRUE);
    }

    /**
     *
     * 功能描述: 订单导出功能
     *
     * @param response
     * @param orderSearch
     * @return boolean
     * @auther wzc
     * @date 2018/12/21 8:21
     */
    @Override
    public boolean exportOrder(HttpServletResponse response, OrderSearch orderSearch) {

        List<OrdOrderForExport> ordOrders = new ArrayList<>();

        ArrayList<Integer> customers = getCustomers(orderSearch.getCustomerName());
        Integer[] customerIds = new Integer[customers.size()];
        orderSearch.setCustomerIds(customerIds);
        ArrayList<String> trucks = getPlates(orderSearch.getTruckAttribute());
        String [] plates =  new String[trucks.size()];
        trucks.toArray(plates);
        List<SysDictVO> types = null;
        List<SysDictVO> statuss = null;
        List<SysDictVO> attrs = null;
        orderSearch.setPlates(plates);
        if(orderSearch.getIds() != null && orderSearch.getIds() != ""){
            OrderSearch orderSearch1 = new OrderSearch();
            orderSearch1.setStatus(orderSearch.getStatus());
            orderSearch1.setOrderIds(Convert.toStrArray(orderSearch.getIds()));
            ordOrders = ordOrderMapper.selectOrderListForExport(orderSearch1);
        }else {
            ordOrders = ordOrderMapper.selectOrderListForExport(orderSearch);
        }
        if(ordOrders.size()>0){
            types = systemFeginServer.findDictByType("order_type", orderSearch.getTenantId());
            statuss = systemFeginServer.findDictByType("order_status", orderSearch.getTenantId());
            attrs = systemFeginServer.findDictByType("truck_attribute", orderSearch.getTenantId());
        }
        //TODO 根据司机id查询
        ordOrders = getOrderDataListForExport(ordOrders,types,statuss,attrs,orderSearch.getTenantId());
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        String excelName = fmt.format(new Date())+"-集装箱订单信息";
        ExcelUtil<OrdOrderForExport> util = new ExcelUtil<>(OrdOrderForExport.class);
        util.exportExcel(response, ordOrders,excelName,null);
        return true;
    }

    /**
     * 查询订单管理信息
     *
     * @param orderId 订单管理ID
     * @return 订单管理信息
     */
    @Override
    public OrdOrderForApp selectOrderByOrderIdForApp(String orderId, Integer tenantId)
    {
        String [] array = {};
        SysDictVO sysDictVO = new SysDictVO();
        OrdOrderForApp ordOrder = ordOrderMapper.selectOrderByOrderIdForApp(orderId, tenantId);
        ordOrder.setPickupGoodsPlaceArray(ordOrder.getPickupGoodsPlace()==null?array:Convert.toStrArray("/",ordOrder.getPickupGoodsPlace()));
        ordOrder.setSendGoodsPlaceArray(ordOrder.getSendGoodsPlace()==null?array:Convert.toStrArray("/",ordOrder.getSendGoodsPlace()));
        ordOrder.setPickupConPlaceArray(ordOrder.getPickupConPlace()==null?array:Convert.toStrArray("/",ordOrder.getPickupConPlace()));
        ordOrder.setReturnConPlaceArray(ordOrder.getReturnConPlace()==null?array:Convert.toStrArray("/",ordOrder.getReturnConPlace()));
        sysDictVO.setType("order_status");
        sysDictVO.setValue(ordOrder.getStatus());
        sysDictVO.setTenantId(tenantId);
        SysDictVO statusDec = systemFeginServer.selectDict(sysDictVO);
        ordOrder.setStatusDec(statusDec==null?"":statusDec.getLabel());
        DriverVO driverVO = new DriverVO();
        driverVO.setTenantId(tenantId);
        driverVO.setDriverId(ordOrder.getDriverId());
        List<DriverVO> driverVs = trunkFeign.selectAllDriver(driverVO);
        if(driverVs != null && driverVs.size() == 1){
            ordOrder.setDriverName(driverVs.get(0).getDriverName());
            ordOrder.setDriverPhone(driverVs.get(0).getPhone());
            ordOrder.setDriverVO(driverVs.get(0));
        }
        CustomerVO customerVO = new CustomerVO();
        customerVO.setCustomerId(ordOrder.getCustomerId());
        customerVO.setTenantId(tenantId);
        List<CustomerVO> customerVOS = trunkFeign.selectAllForFegin(customerVO);
        if(customerVOS != null && customerVOS.size() == 1){
            ordOrder.setCustomerName(customerVOS.get(0).getCustomerName());
        }
        //解析订单运踪中的订单状态：提货，签收，获取两个状态的上传的凭证
        //提货凭证
        String[] thImgs = {};
        //签收
        String[] qsImgs = {};
        //提箱凭证
        String[] txImgs = {};
        //还箱凭证
        String[] hxImgs = {};
        for (OrdOrderLogistics ool : ordOrder.getOrdOrderLogistics()) {
            if (ool.getOrderStatus().equals(CommonConstant.ORDER_DTH)) {
                //获取提箱凭证
                if(ool.getPhotos()!=null&&!ool.getPhotos().equals("")){
                    txImgs = ool.getPhotos()!=null?ool.getPhotos().split(","):null;
                }
            } else if (ool.getOrderStatus().equals(CommonConstant.ORDER_YHX)) {
                //获取还箱凭证
                if(ool.getPhotos()!=null&&!ool.getPhotos().equals("")){
                    hxImgs = ool.getPhotos()!=null?ool.getPhotos().split(","):null;
                }
            }
        }
        for(OrdPickupArrivalAdd add : ordOrder.getPickupAdds()){
            if(add.getFiles()!=null && add.getFiles() != ""){
                String[] a = add.getFiles().split(",");
                thImgs = (String[]) ArrayUtils.addAll(thImgs, a);
            }
        }
        for(OrdPickupArrivalAdd add : ordOrder.getArrivalAdds()){
            if(add.getFiles()!=null && add.getFiles() != ""){
                String[] a = add.getFiles().split(",");
                qsImgs = (String[]) ArrayUtils.addAll(qsImgs, a);
            }
        }
        ordOrder.setReceiptImgList(qsImgs);
        ordOrder.setPickGoodsImgList(thImgs);
        ordOrder.setPickCnImgList(txImgs);
        ordOrder.setReturnCnImgList(hxImgs);
        //处理异常情况和异常费用
        if(ordOrder.getOrdExceptionConditions()!=null&&ordOrder.getOrdExceptionConditions().size()>0){
            for (OrdExceptionCondition exc:ordOrder.getOrdExceptionConditions()) {
                exc.setPaths(exc.getOecFile()!=null?exc.getOecFile().split(","):null);
            }
        }
        if(ordOrder.getOrdExceptionFees()!=null&&ordOrder.getOrdExceptionFees().size()>0){
            for (OrdExceptionFee exf:ordOrder.getOrdExceptionFees()) {
                //处理图片
                exf.setImgUrlFile(exf.getImgUrl()!=null?exf.getImgUrl().split(","):null);
                //处理异常费用类型
                sysDictVO.setType("exception_fee_type");
                sysDictVO.setValue(exf.getExceptionFeeType());
                sysDictVO.setTenantId(tenantId);
                String type = systemFeginServer.selectDict(sysDictVO).getLabel();
                exf.setExceptionFeeType(type);
            }
        }
        /*凭证文件处理*/
        if(ordOrder.getOrdOrderFile()!=null){
            String othersFile=null;
            if(ordOrder.getOrdOrderFile().getFileA()!=null){
                othersFile = ordOrder.getOrdOrderFile().getFileA();
            }if(ordOrder.getOrdOrderFile().getFileB()!=null){
                othersFile = othersFile+ "," + ordOrder.getOrdOrderFile().getFileB();
            }if(ordOrder.getOrdOrderFile().getFileC()!=null){
                othersFile = othersFile+ "," + ordOrder.getOrdOrderFile().getFileC();
            }if(ordOrder.getOrdOrderFile().getFileD()!=null){
                othersFile = othersFile+ "," + ordOrder.getOrdOrderFile().getFileD();
            }if(ordOrder.getOrdOrderFile().getFileE()!=null){
                othersFile = othersFile+ "," + ordOrder.getOrdOrderFile().getFileE();
            }if(ordOrder.getOrdOrderFile().getFileF()!=null){
                othersFile = othersFile+ "," + ordOrder.getOrdOrderFile().getFileF();
            }if(ordOrder.getOrdOrderFile().getFileG()!=null){
                othersFile = othersFile+ "," + ordOrder.getOrdOrderFile().getFileG();
            }if(ordOrder.getOrdOrderFile().getFileH()!=null){
                othersFile = othersFile+ "," + ordOrder.getOrdOrderFile().getFileH();
            }
            if(othersFile != null){
                String [] otherArray = Convert.toStrArray(othersFile);
                ordOrder.setFileList(otherArray);
            }
            else{
                String []otherArr = {};
                ordOrder.setFileList(otherArr);
            }
        }
        else{
            String []otherArr = {};
            ordOrder.setFileList(otherArr);
        }
        return ordOrder;
    }

    @Override
    public List<OrderSearch> selectOrderByDriverId(OrdOrder ordOrder) {
        return ordOrderMapper.selectOrderByDriverId(ordOrder);
    }

    @Override
    public List<OrderSearch> selectOrderByDriverIdFeign(OrdOrder ordOrder){
        return ordOrderMapper.selectOrderByDriverIdFeign(ordOrder);
    }

    /**
     *
     * 功能描述:根据value获取订单类型label
     *
     * @param dicts	类型集合
     * @param value	类型值
     * @return java.lang.String
     * @auther wzc
     * @date 2018/12/26 9:29
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

    public String createPDFofSendTruckList(OrdOrder order) {
        //* 查询司机信息*//*
        DriverVO driverVO = new DriverVO();
        driverVO.setDriverId(order.getDriverId());
        driverVO.setTenantId(order.getTenantId());
        List<DriverVO> driverVOS = trunkFeign.selectAllDriver(driverVO);
        if (driverVOS != null && driverVOS.size() == 1) {
            driverVO = driverVOS.get(0);
        }
        //* 查询客户信息*//*
        CustomerVO customerVO = new CustomerVO();
        customerVO.setCustomerId(order.getCustomerId());
        customerVO.setTenantId(order.getTenantId());
        List<CustomerVO> customerVOS = trunkFeign.selectAllForFegin(customerVO);
        if (customerVOS != null && customerVOS.size() == 1) {
            customerVO = customerVOS.get(0);
        }
        String pdfName = order.getOrderId() + ".pdf";// PDF文件名
        PdfReader pdfReader = null;
        PdfStamper pdfStamper;
        PDFUtils.createDirectory(Global.PDFPATH);
        try {
            ClassPathResource resource = null;
            if(order.getType().equals("0")) {
                resource = new ClassPathResource("static/pdf/sendTruckPdf_going.pdf");
            }else if(order.getType().equals("1")) {
                resource = new ClassPathResource("static/pdf/sendTruckPdf_back.pdf");
            }
            pdfReader = new PdfReader(resource.getInputStream());// 读取pdf模板

            pdfStamper = new PdfStamper(pdfReader, new FileOutputStream(
                    Global.PDFPATH + pdfName));
            AcroFields form = pdfStamper.getAcroFields();
            /*公共属性值*/
            form.setField("classDate",order.getClassDate()!=null?DateUtils.dateTime(order.getClassDate()):"");
            form.setField("classOrder",order.getClassOrder()!=null?order.getClassOrder():"");
            form.setField("carrier",order.getCarrier()!=null?order.getCarrier():"");
            form.setField("productName",order.getProductName()!=null?order.getProductName():"");
            form.setField("weight",order.getWeight()!=null?order.getWeight():"");
            String containerType = order.getContainerType()!=null?order.getContainerType():"";
            form.setField("driverName",driverVO!=null?(driverVO.getDriverName()+"/"+driverVO.getPhone()):"");
            form.setField("idCardNumber",driverVO!=null?driverVO.getIdcardNumber():"");
            form.setField("plateNumber",order.getPlateNumber());
            form.setField("customerName",customerVO!=null?customerVO.getCustomerName():"");

            if(order.getContainerNum()!=null){
                containerType+=" * "+order.getContainerNum();
            }
            form.setField("containerType",containerType);
            /*公共属性值*/
            /*去程*/
            if(order.getType().equals("0")){
                OrdPickupArrivalAdd add = order.getPickupAdds().get(0);
                form.setField("pickupGoodsDate",add.getPlanTime()!=null?DateUtils.dateTime(add.getPlanTime()):"");
                form.setField("consignor", add.getContacts()+"/"+add.getContactsPhone());
                form.setField("pickupGoodsPlace",add.getAddressCity()+add.getAddressDetailPlace());
                form.setField("receiverRemark",order.getReceiverRemark());
            }else if(order.getType().equals("1")){
                OrdPickupArrivalAdd add = order.getArrivalAdds().get(0);
                form.setField("consignee", add.getContacts()+"/"+add.getContactsPhone());
                form.setField("sendGoodsPlace",add.getAddressCity()+add.getAddressDetailPlace());
                form.setField("containerNo",order.getContainerNo()!=null?order.getContainerNo():"");
                form.setField("returnConPlace",(order.getReturnConPlace()!=null?order.getReturnConPlace():"")+(order.getReturnConDetailplace()!=null?order.getReturnConDetailplace():""));
                form.setField("sealNumber", order.getSealNumber()!=null?order.getSealNumber():"");
            }

            //生成二维码
            if(StringUtils.isNotEmpty(order.getReceiptCode())) {
                File outputFile = new File(Global.FILE_PATH_ORDER + order.getOrderId() + ".jpg");
                ZxingCoreUtils.writeToFile(order.getReceiptCode(), outputFile);//获取订舱id生成二维码
                /*PDF表单添加二维码--开始*/
                // 通过域名获取所在页和坐标，左下角为起点
                int pageNo_a = form.getFieldPositions("core_a").get(0).page;
                Rectangle signRect_a = form.getFieldPositions("core_a").get(0).position;
                float x_a = signRect_a.getLeft();
                float y_a = signRect_a.getBottom();
                Image image_a = Image.getInstance(Global.FILE_PATH_ORDER + order.getOrderId() + ".jpg");
                // 获取操作的页面
                PdfContentByte under_a = pdfStamper.getOverContent(pageNo_a);
                // 根据域的大小缩放图片
                image_a.scaleToFit(signRect_a.getWidth(), signRect_a.getHeight());
                // 添加图片
                image_a.setAbsolutePosition(x_a, y_a);
                under_a.addImage(image_a);
                //-----------------------------------
                // 通过域名获取所在页和坐标，左下角为起点
                int pageNo_b = form.getFieldPositions("core_b").get(0).page;
                Rectangle signRect_b = form.getFieldPositions("core_b").get(0).position;
                float x_b = signRect_b.getLeft();
                float y_b = signRect_b.getBottom();
                Image image_b = Image.getInstance(Global.FILE_PATH_ORDER + order.getOrderId() + ".jpg");
                // 获取操作的页面
                PdfContentByte under = pdfStamper.getOverContent(pageNo_b);
                // 根据域的大小缩放图片
                image_b.scaleToFit(signRect_b.getWidth(), signRect_b.getHeight());
                // 添加图片
                image_b.setAbsolutePosition(x_b, y_b);
                under.addImage(image_b);
                /*PDF表单添加二维码--结束*/
            }
            pdfStamper.setFormFlattening(true);// 如果为false那么生成的PDF文件还能编辑，一定要设为true
            pdfStamper.close();
            pdfReader.close();
            return Global.PDFPATH + pdfName;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }


    public String getOrderId(){
        Map map = ordOrderMapper.getOrderId(PERFIX,"");
        if(map.get("orderId")==null){
            throw new RuntimeException();
        }
        return map.get("orderId").toString();
    }

    @Override
    public OrdOrder seleteNotEndOrderByDriverId(Integer driverId){
        OrdOrder ordOrder = ordOrderMapper.seleteNotEndOrderByDriverId(driverId);
        if(ordOrder!=null){
            SysDictVO dictVO = new SysDictVO();
            dictVO.setType("order_status");
            dictVO.setValue(ordOrder.getStatus());
//            ordOrder.setStatusDec(systemFeginServer.selectDict(dictVO).getLabel());
        }
        return ordOrder;
    }

    @Override
    public List<CommonGoodsWxSelect> selectListForWX(CommonOrdSearch ordSearch) {
        if(ordSearch.getStatus() != null && ordSearch.getStatus() !=""){
            ordSearch.setStatusArray(Convert.toStrArray(ordSearch.getStatus()));
        }
        List<CommonGoodsWxSelect> orders = ordOrderMapper.selectListForWX(ordSearch);
        if(orders!=null) {
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

    @Override
    public CommonGoodsWxSelect selectByOrderIdForWX(String orderId, Integer tenantId) {
        CommonGoodsWxSelect goods = ordOrderMapper.selectByOrderIdForWX(orderId, tenantId);
        /*获取物流信息*/
        if(goods != null) {
            SysDictVO sysDictVO = new SysDictVO();
            sysDictVO.setType("order_status");
            sysDictVO.setValue(goods.getStatus());
            sysDictVO.setTenantId(tenantId);
            SysDictVO sysDictVOS = systemFeginServer.selectDict(sysDictVO);
            goods.setStatus(sysDictVOS!=null?sysDictVOS.getLabel():goods.getStatus());
            List<OrdOrderLogistics> logistics = logisticsMapper.selectOrderLogisticsList(orderId);
            goods.setLogistics(logistics);
        }
        //todo 获取司机当前位置信息 经纬度数据

        return goods;
    }

    /**
     * 判断是否存在签收码
     * @param orderId
     * @param receiptCode
     * @return
     */
    @Override
    public Integer hasReceiptCode( String orderId, String receiptCode) {
        return ordOrderMapper.hasRecCode(orderId,receiptCode);
    }

    @Override
    public String insertUpStreamOrder(OrdOrder ordOrder) {
        /*送货详细地址--进行省市区数据查询*/
        if(ordOrder.getSendGoodsDetailplace() != null){
            String sendGoodsPlace = BaiDuMapUtils.getProvince(ordOrder.getSendGoodsDetailplace());
            if(sendGoodsPlace != null){
                ordOrder.setSendGoodsPlace(sendGoodsPlace);
            }
        }
        /*提货货详细地址--进行省市区数据查询*/
        if(ordOrder.getPickupGoodsDetailplace() != null){
            String pickupGoodsPlace = BaiDuMapUtils.getProvince(ordOrder.getPickupGoodsDetailplace());
            if(pickupGoodsPlace != null){
                ordOrder.setPickupGoodsPlace(pickupGoodsPlace);
            }
        }
        /*还箱详细地址--进行省市区数据查询*/
        if(ordOrder.getReturnConDetailplace() != null){
            String returnCnPlace = BaiDuMapUtils.getProvince(ordOrder.getReturnConDetailplace());
            if(returnCnPlace != null){
                ordOrder.setReturnConPlace(returnCnPlace);
            }
        }
        /*提箱详细地址--进行省市区数据查询*/
        if(ordOrder.getPickupConDetailplace() != null){
            String pickCnPlace = BaiDuMapUtils.getProvince(ordOrder.getPickupConDetailplace());
            if(pickCnPlace != null){
                ordOrder.setPickupConPlace(pickCnPlace);
            }
        }
        ordOrder.setStatus(CommonConstant.ORDER_CG);
        ordOrder.setOrderId(getOrderId());
        if(ordOrderMapper.insertOrder(ordOrder)){
            return ordOrder.getOrderId();
        }
        return null;
    }

    @Override
    public boolean updateOrderByUp(OrdOrderDTO ordOrderDTO) {
        return ordOrderMapper.updateOrderByUp(ordOrderDTO);
    }

    /**
     * 修改订单管理
     *
     * @param order 订单管理信息
     * @return 结果
     */
    @Override
    public boolean cancelByUpstreamId(OrdOrder order)
    {
        return ordOrderMapper.cancelByUpstreamId(order);
    }

    @Override
    public Page<OrdOrder> selectUpstreamOrder(Query query, OrdOrder ordOrder) {
        List<OrdOrder> ordOrders = ordOrderMapper.selectUpstreamOrder(query,ordOrder);
        ordOrders.forEach(order->{
            switch (order.getStatus()){
                case "01" : order.setStatusDec("计划中");break;
                case "02" : order.setStatusDec("已确认");break;
                case "03" : order.setStatusDec("已撤销");break;
            }
            String typeNum = "";
            typeNum += order.getContainerType() != null?order.getContainerType():"";
            typeNum += "*";
            typeNum += order.getContainerNum()!= null?order.getContainerNum():"";
            order.setContainerType(typeNum);
        });
        return query.setRecords(ordOrders);
    }

    @Override
    public boolean upStreamOrderExport(HttpServletResponse response, OrderSearch orderSearch) {
        if(orderSearch.getIds() != null && orderSearch.getIds() != ""){
            orderSearch.setOrderIds(Convert.toStrArray(orderSearch.getIds()));
        }
        List<UpStreamOrder> ordOrders = ordOrderMapper.selectUpStreamOrderExport(orderSearch);
        ordOrders.forEach(order->{
            switch (order.getStatus()){
                case "01" : order.setStatusDec("计划中");break;
                case "02" : order.setStatusDec("已确认");break;
                case "03" : order.setStatusDec("已撤销");break;
            }
            String typeNum = "";
            typeNum += order.getContainerType() != null?order.getContainerType():"";
            typeNum += "*";
            typeNum += order.getContainerNum()!= null?order.getContainerNum():"";
            order.setContainerType(typeNum);
            switch (order.getType()){
                case "0":
                    order.setPeople(order.getConsignor());
                    order.setPeoplePhone(order.getConsignorPhone());
                    order.setType("去程");
                    break;
                case "1":
                    order.setPeople(order.getConsignee());
                    order.setPeoplePhone(order.getConsigneePhone());
                    order.setType("回程");
                    break;
            }
        });
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        String excelName = fmt.format(new Date())+"-上游订单信息";
        ExcelUtil<UpStreamOrder> util = new ExcelUtil<>(UpStreamOrder.class);
        util.exportExcel(response, ordOrders,excelName,null);
        return true;
    }

    /**
     * 车辆业务统计分页查询
     *
     * @param plateNumber
     * @param feeMonth
     * @return
     */
    @Override
    public List<TruBusiness> selectTruckBusiness( String plateNumber, String feeMonth,Integer tenantId) {
        List<OrderFee> feeList = truBusinessMapper.selectOrderFeeList(plateNumber,feeMonth, tenantId);
        //处理属于同一个车辆的订单
        List<TruBusiness> businessList = new ArrayList<>();
        List<OrderFee> feeSet = new ArrayList<>();
        HashSet<OrderFee> set = new HashSet<OrderFee>(feeList);
        //去除重复的车牌号
        feeSet.addAll(set);
        for(int i = 0;i<feeSet.size();i++){
            TruBusiness business = new TruBusiness();
            //设置司机信息
            DriverVO driverVO = getDriver(feeSet.get(i).getDriverId());
            business.setDriverPhone(driverVO.getPhone());
            business.setDriverName(driverVO.getDriverName());
           //设置初始订单数和初始费用合计为0
            business.setOrderNum(0);
            business.setPlateNumber(feeSet.get(i).getPlateNumber());
            business.setTotalFee(new BigDecimal(0));
            for (int j = 0;j<feeList.size();j++){
                //如果是同一个车辆的订单
                if(feeSet.get(i).equals(feeList.get(j))){
                    //订单数量+1
                    business.setOrderNum(business.getOrderNum()+1);
                    //费用相加
                    feeList.get(j).setExFee(feeList.get(j).getExFee()!=null?feeList.get(j).getExFee():new BigDecimal(0));
                    feeList.get(j).setTruckFee(feeList.get(j).getTruckFee()!=null?feeList.get(j).getTruckFee():new BigDecimal(0));
                    business.setTotalFee(business.getTotalFee().add(feeList.get(j).getTruckFee().add(feeList.get(j).getExFee())));
                }
            }
            businessList.add(business);
        }
        //回收垃圾
        feeList.clear();
        set.clear();
        feeSet.clear();
        return businessList;
    }

    //获取司机信息
    public DriverVO getDriver(Integer driverId){
        DriverVO driverVO = new DriverVO();
        driverVO.setDriverId(driverId);
        return trunkFeign.selectAllDriver(driverVO).get(0);
    }


    @Override
    public int countByOrderStatus(OrdOrder order) {
        return ordOrderMapper.countByOrderStatus(order);
    }

    @Override
    public List<OrdOrderLogisticsVo> selectOrder(OrdOrder ordOrder) {
        return ordOrderMapper.selectOrder(ordOrder);
    }

    @Override
    public Boolean updateOrderBalanceStatus(OrdOrder ordOrder) {
         return ordOrderMapper.updateOrderBalanceStatus(ordOrder);
    }

    @Override
    public List<Integer> countOrderNumber(Integer tenantId) {
        return ordOrderMapper.countOrderNumber(tenantId);
    }

    @Override
    public List<BigDecimal> countMoney(Integer tenantId) {
        return ordOrderMapper.countMoney(tenantId);
    }

    @Override
    public Boolean updateByOrderIds(String[] orderIds) {
        return ordOrderMapper.updateByOrderIds(orderIds);
    }

    @Override
    public OrdOrder selectOrderByOrderId(String orderId, Integer tenantId) {
        return ordOrderMapper.selectOrderByOrderId(orderId,tenantId);
    }

    @Override
    public Integer selectCountByDriver(Integer driverId, Integer tenantId, String startDate, String endDate) {
        return ordOrderMapper.selectCountByDriver(driverId, tenantId, startDate, endDate);
    }

    @Override
    @Transactional
    public Boolean updateBillByOrderIds(String[] orderIds, String login) {
        return ordOrderMapper.updateBillByOrderIds(orderIds,login);
    }

    @Override
    public Map getGPSList(String orderId, Integer tenantId) {
        String orderType = orderId.substring(0, 2);
        Map map = new HashMap();
        map.put("orderId",orderId);
        /*车牌号*/
        String plateNumber = null;
        /*接单时间*/
        String startDate = null;
        /*最终时间*/
        String endDate = null;

        List<SysDictVO>types = systemFeginServer.findDictByType("order_status", tenantId);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        switch (orderType){
            case "PH" :
                CommonGoodsVO commonGoodsVO = commonGoodsMapper.selectPhOrdByOrderId(orderId);
                if(commonGoodsVO != null){
                    if(null != commonGoodsVO.getOrdCommonTruck()){
                        map.put("plateNumber",commonGoodsVO.getOrdCommonTruck().getPlateNumber());
                        plateNumber = commonGoodsVO.getOrdCommonTruck().getPlateNumber();
                    }
                    map.put("status",getLabel(types,commonGoodsVO.getStatus()));
                    for(OrdOrderLogistics l : commonGoodsVO.getOrdOrderLogistics()){
                        /*获取开始接单时间*/
                        if(l.getOrderStatus().equals("5")){
                            startDate = format.format(l.getLogisticsTime());
                        }
                        /*如果已完成，获取结束时间*/
                        if(l.getOrderStatus().equals("9")){
                            endDate = format.format(l.getLogisticsTime());
                        }
                    }
                    if(endDate==null){
                        endDate = format.format(new Date());
                    }
                }
                break;
            case "CN" :
                OrdOrder ordOrder = ordOrderMapper.selectForGPSByOrderId(orderId);
                if(ordOrder != null){
                    boolean type = ordOrder.getType().equals("0");
                    map.put("plateNumber",ordOrder.getPlateNumber());
                    map.put("status",getLabel(types,ordOrder.getStatus()));
                    plateNumber = ordOrder.getPlateNumber();
                    for(OrdOrderLogistics l : ordOrder.getOrdOrderLogistics()){
                        /*去程*/
                        if (type) {
                            if(l.getOrderStatus().equals("3")){
                                startDate = format.format(l.getLogisticsTime());
                            }
                            if(l.getOrderStatus().equals("9")){
                                endDate = format.format(l.getLogisticsTime());
                            }
                        }else{
                            if(l.getOrderStatus().equals("5")){
                                startDate = format.format(l.getLogisticsTime());
                            }
                            if(l.getOrderStatus().equals("11")){
                                endDate = format.format(l.getLogisticsTime());
                            }
                        }
                    }
                    if(endDate==null){
                        endDate = format.format(new Date());
                    }
                }
                break;
        }
        if(plateNumber != null && startDate != null && endDate != null){
            try {
                Object value = redisTemplate.opsForValue().get("GPS_TRUCK_WAY"+ plateNumber);
                if(value != null){
                    return (Map)value;
                }
                String plate = URLEncoder.encode(plateNumber);
                String startTime = URLEncoder.encode(startDate);
                String endTime = URLEncoder.encode(endDate);
                String url = "http://171.15.132.161:8081/RoadGPS/road/getWork.do?plateNum=" + plate + "&startTime=" + startTime + "&endTime=" + endTime;
                String result = HttpClientUtil.doGet(url);
                //GSON直接解析成对象
                GPSVO resultBean = new Gson().fromJson(result, GPSVO.class);
                //对象中拿到集合
                List<GPSVO.Gps> gpsList = resultBean.getPolylinePathlist();
                map.put("gps", gpsList);
                map.put("time", gpsList.get(gpsList.size()-1).getReceivetime());
                String latLon = gpsList.get(gpsList.size()-1).getLat()+","+gpsList.get(gpsList.size()-1).getLon();
                map.put("address", BaiDuMapUtils.getAddressByLatLon(latLon));
                map.put("latLon",latLon);
                redisTemplate.opsForValue().set("GPS_TRUCK_WAY"+ plateNumber, map, 1800, TimeUnit.SECONDS);
                return map;
            }catch (Exception e){
            }
        }
        return map;
    }

    @Override
    @Transactional
    public R<Boolean> reportExcepFee(UpStreamOrderExcepFee orderExcepFee){
        try {
            /*修改订单信息*/
            OrdOrder ordOrder = new OrdOrder();
            ordOrder.setOrderId(orderExcepFee.getOrderId());
            ordOrder.setUpstreamReportFee(orderExcepFee.getCcmExceptionFees());
            ordOrder.setUpstreamReportRemark(orderExcepFee.getCcmExceptionDetails());
            ordOrder.setUpstreamYcDay(orderExcepFee.getCcmCarryDay());
            ordOrder.setUpstreamYcPrice(orderExcepFee.getCcmCarryPrice());
            OrdOrder param = new OrdOrder();
            param.setOrderId(orderExcepFee.getOrderId());
            ordOrderMapper.update(ordOrder, new EntityWrapper<>(param));
            /*调用接口上报异常费用*/
            Gson gson = new Gson();
            String json = gson.toJson(orderExcepFee);
            System.out.println(json);
            String url = "http://xxt.zhonghaokeji.cn/coc/PC/updateAbnormalFee.do";
            String result = HttpClientUtil.doPostJson(url, json);
            System.out.println(result);
        }catch (Exception e){
            //手动回滚事务
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new R<>(Boolean.FALSE);
        }
        return new R<>(Boolean.TRUE);
    }

    @Override
    @Transactional
    public Boolean returnExcepFee(UpStreamOrderExcepFee orderExcepFee){
        try {
            /*修改订单信息*/
            OrdOrder ordOrder = new OrdOrder();
            ordOrder.setUpstreamId(orderExcepFee.getUpstreamId());
            ordOrder.setPlateNumber(orderExcepFee.getPlateNumber());
            ordOrder.setUpstreamReturnFee(orderExcepFee.getCcmExceptionFees());
            ordOrder.setUpstreamReportRemark(orderExcepFee.getCcmExceptionDetails());
            ordOrder.setUpstreamYcDay(orderExcepFee.getCcmCarryDay());
            ordOrder.setUpstreamYcPrice(orderExcepFee.getCcmCarryPrice());
            OrdOrder param = new OrdOrder();
            param.setUpstreamId(orderExcepFee.getUpstreamId());
            param.setPlateNumber(orderExcepFee.getPlateNumber());
            param.setTenantId(orderExcepFee.getTenantId());
            ordOrderMapper.update(ordOrder, new EntityWrapper<>(param));
        }catch (Exception e){
            //手动回滚事务
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    @Override
    public OrderForGPSSystem getPlateNumberOrder(String plateNumner){
        List<OrderForGPSSystem> orders = ordOrderMapper.selectOrderByPlateNumberForGPSSystem(plateNumner);
        OrderForGPSSystem orderGPSSystem = null;
        if(orders != null && orders.size() > 0){
            orderGPSSystem = orders.get(0);
            /*获取运踪信息*/
            List<OrdOrderLogistics> logistics = logisticsMapper.selectOrderLogisticsList(orderGPSSystem.getOrderId());
            for(OrdOrderLogistics l : logistics){
                /*去程的情况下*/
                if(orderGPSSystem.getGocome() == 0){
                    switch (l.getOrderStatus()){
                        case "3": //接单时间
                            orderGPSSystem.setGetordertime(getLogisticsTime("3", logistics));
                            break;
                        case "5": //提箱时间
                            orderGPSSystem.setGetboxtime(getLogisticsTime("5", logistics));
                            break;
                        case "6": //到达提货地时间
                            orderGPSSystem.setArrivetime(getLogisticsTime("6", logistics));
                            break;
                        case "7"://提货完成时间
                            orderGPSSystem.setGetgoodtime(getLogisticsTime("7", logistics));
                            break;
                        case "9": //到达陆港时间
                            orderGPSSystem.setDestinationtime(getLogisticsTime("9", logistics));
                            break;
                    }
                }else {
                    switch (l.getOrderStatus()){
                        case "3": //接单时间
                            orderGPSSystem.setGetordertime(getLogisticsTime("3", logistics));
                            break;
                        case "6": //到达提货地时间
                            orderGPSSystem.setGetboxtime(getLogisticsTime("6", logistics));
                            break;
                        case "7": // 装货完成时间
                            orderGPSSystem.setArrivetime(getLogisticsTime("7", logistics));
                            break;
                        case "9": // 卸货时间
                            orderGPSSystem.setGetgoodtime(getLogisticsTime("9", logistics));
                            break;
                        case "11": // 还箱时间
                            orderGPSSystem.setDestinationtime(getLogisticsTime("11", logistics));
                            break;
                    }
                }
            }
        }
        return orderGPSSystem;
    }

    public String getLogisticsTime(String status, List<OrdOrderLogistics> logistics){
        String date = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(OrdOrderLogistics l : logistics){
            if(l.getOrderStatus().equals(status)){
                date = format.format(l.getLogisticsTime());
                break;
            }
        }
        return date;
    }

    @Override
    public String getAddressByplateNumber(String plateNumber){
        Object value = redisTemplate.opsForValue().get("GPS_DATA"+ plateNumber);
        if(value != null){
            return value.toString();
        }
        String plate = URLEncoder.encode(plateNumber);
        String url = "http://171.15.132.161:8081/RoadGPS/road/getNewLngLat.do?plateNum=" + plate;
        String result = HttpClientUtil.doGet(url);
        //GSON直接解析成对象
        GPSVO resultBean = new Gson().fromJson(result, GPSVO.class);
        if(resultBean!=null){
            String lat = resultBean.getPolylinePath().getLat();
            String lng = resultBean.getPolylinePath().getLng();
            String address = BaiDuMapUtils.getAddressByLatLon(lat+","+lng);
            redisTemplate.opsForValue().set("GPS_DATA"+plateNumber, address, 1800, TimeUnit.SECONDS);
            return address;
        }
        return null;
    }

    @Override
    public R<Boolean> smsSendAgain(String orderId, Integer tenantId, String phone, Integer sort, String plate){
        if (!systemFeginServer.selectIsSend(tenantId)) {
            return new R<>(Boolean.FALSE, "未开启短信发送功能！");
        }
        SysSmsTempVO s = systemFeginServer.selectSmsSetByTplId(tenantId, CommonConstant.TPL_ID_RCODE);
        if(!s.getIsSendReceice().equals("0")){
            return new R<>(Boolean.FALSE, "未开启短信发送功能！");
        }
        OrdPickupArrivalAdd add = new OrdPickupArrivalAdd();
        add.setAddType("1");
        add.setOrderId(orderId);
        add.setSort(sort);
        add = addMapper.selectOne(add);
        boolean flag = false;
        R<Boolean> result = null;
        if(add!=null){
            try {
                result = YunPianSMSUtils.sendConsigneeReceiptCode(phone, add.getReceiptCode(), orderId, sort, plate);
                flag = result.getData();
            } catch (IOException e) {
                e.printStackTrace();
            }
            add.setIsSendOk(flag?"1":"0");
            addMapper.updateById(add);
        }
        return new R<>(Boolean.TRUE, flag ? "发送成功！": result.getMsg());
    }

    @Override
    public BigDecimal getRate(Integer tenantId) {
        TaxRateVO taxRateVO = trunkFeign.getRate(tenantId);
        if(taxRateVO != null){
            return taxRateVO.getTaxRate();
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
        customer.setCustomerName(customerName.trim());
        List<CustomerVO> customerVOS = trunkFeign.getCustomerList(customer);
        if(customerVOS != null &&customerVOS.size() ==1){
            customerVO = customerVOS.get(0);
            return customerVO;
        }
        return null;
    }

    public void uploadOtherFiles(OrdOrderFile file, String orderId, Integer tenantId, boolean isInsert){
        if(file != null) {
            file.setFileA(null);
            file.setFileB(null);
            file.setFileC(null);
            file.setFileD(null);
            file.setFileE(null);
            file.setFileF(null);
            file.setFileG(null);
            file.setFileH(null);
            file.setFileI(null);
            file.setFileG(null);
            if (file != null && file.getOtherFiles() != null && file.getOtherFiles().length > 0) {
                String[] files = file.getOtherFiles();
                for (int i = 0; i < files.length; i++) {
                    switch (i) {
                        case 0:
                            file.setFileA(files[i]);
                            break;
                        case 1:
                            file.setFileB(files[i]);
                            break;
                        case 2:
                            file.setFileC(files[i]);
                            break;
                        case 3:
                            file.setFileD(files[i]);
                            break;
                        case 4:
                            file.setFileE(files[i]);
                            break;
                        case 5:
                            file.setFileF(files[i]);
                            break;
                        case 6:
                            file.setFileG(files[i]);
                            break;
                        case 7:
                            file.setFileH(files[i]);
                            break;
                        case 8:
                            file.setFileI(files[i]);
                            break;
                        case 9:
                            file.setFileG(files[i]);
                            break;
                    }
                }
            }
            file.setOrderId(orderId);
            file.setTenantId(tenantId);
            if (isInsert) {
                fileMapper.insertOrderFile(file);
            } else {
                fileMapper.deleteByOrderId(orderId, tenantId);
                fileMapper.insertOrderFile(file);
            }
        }
    }

    public static void deleteDirectory(String path) {
        try {
            File file = new File(path);
            if (file.delete()) {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * //多个提送货地插入
     * @param adress 包含联系人电话-地址-详细地址等信息
     * @param orderId
     * @param tenantId
     * @param addType 插入类型
     * @param sort
     * @param phone
     * @param planTime 预计取提货时间
     * @return
     */
    public int insertAddress(String adress,String orderId,Integer tenantId,String addType,Integer sort,String phone,Date planTime){
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
            add.setPlanTime(planTime);
            return addMapper.insert(add);
        }else{
            return 0;
        }
    }
}
