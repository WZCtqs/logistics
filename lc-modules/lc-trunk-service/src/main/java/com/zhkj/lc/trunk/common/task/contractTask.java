package com.zhkj.lc.trunk.common.task;

import com.zhkj.lc.trunk.model.Announcement;
import com.zhkj.lc.trunk.model.Contract;
import com.zhkj.lc.trunk.model.TruDriver;
import com.zhkj.lc.trunk.model.TruTruck;
import com.zhkj.lc.trunk.service.AnnouncementService;
import com.zhkj.lc.trunk.service.IContractService;
import com.zhkj.lc.trunk.service.ITruDriverService;
import com.zhkj.lc.trunk.service.ITruTruckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @Author: wchx
 * @Date: 2018/12/26 11:41
 */
@Component
public class contractTask {
    @Autowired
    IContractService contractService;
    @Autowired
    ITruDriverService truDriverService;
    @Autowired
    private AnnouncementService announcementService;
    @Autowired
    private ITruTruckService truTruckService;
    /**
     * 合同期限状态监测修改
     */
    @Scheduled(cron = "0 0 0 * * ?")//每天凌晨执行
    void contractMonitor(){
        System.out.println("定时任务开始......");
        long begin = System.currentTimeMillis();
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 3);//当前时间加三个月，即三个月后的时间    
        Date time = calendar.getTime();
        //执行数据库操作...
        Contract contracta = new Contract();
        contracta.setStatus("2");
        List<Contract> contracts = contractService.selectAllUnexpiredContract(contracta);
        if (contracts.size() > 0){
            for (Contract contract:contracts) {
                if (null != contract.getExpiryDate()) {
                    Date signDate = contract.getSignDate();//签订时间
                    Date expiryDate = contract.getExpiryDate();//到期时间
                    Date remindDate = contract.getRemindDate();//提醒时间
                    Boolean flag = date.after(expiryDate);
                    if (remindDate.after(expiryDate)) {
                       // if (flag) {
                            if ("1".equals(contract.getStatus())) {
                                Announcement announcement = new Announcement();
                                announcement.setTenantId(contract.getTenantId());
                                announcement.setTitle("合同期限状态监测");
                                announcement.setType("2");
                                announcement.setContent("编号为 " + contract.getContractNumber() + " 的合同已到期，请及时核对！");
                                announcement.setCreateTime(new Date());
                                announcementService.insert(announcement);
                                contract.setStatus("2");
                                contractService.updateById(contract);
                          //  }
                        } else {
                            if ("0".equals(contract.getStatus())) {
                                Announcement announcement = new Announcement();
                                announcement.setTenantId(contract.getTenantId());
                                announcement.setTitle("合同期限状态监测");
                                announcement.setType("2");
                                announcement.setContent("编号为 " + contract.getContractNumber() + " 的合同即将到期，请及时处理！");
                                announcement.setCreateTime(new Date());
                                announcementService.insert(announcement);
                                contract.setStatus("1");
                                contractService.updateById(contract);
                            }
                        }
                    }
                }
            }
        }
        long end = System.currentTimeMillis();
        System.out.println(contracts.size()+"````````````````"+"定时任务结束，共耗时：[" + (end-begin) / 1000 + "]秒");
    }

    /**
     * 司机从业、驾驶证审验日期监测通知
     * 提前三个月通知
     */
    @Scheduled(cron = "0 0 0 * * ?")//每天凌晨执行
    void licenseLevelTimeMonitor(){
        System.out.println("定时任务开始......");
        long begin = System.currentTimeMillis();
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 3);//当前时间加三个月，即三个月后的时间    
        Date time = calendar.getTime();//获取三个月后的时间
        //执行数据库操作...
        //驾驶证审验日期临期监测通知
        TruDriver truDriver = new TruDriver();
        List<TruDriver> truDrivers = truDriverService.selectAllDriver(truDriver);
        if (truDrivers.size() > 0) {
            for (TruDriver driver : truDrivers) {
                if (null != driver.getLicenseLevelTime()) {
                    Date licenseLevelTime = driver.getLicenseLevelTime();//驾驶证审验日期
                    if (time.after(licenseLevelTime)) {
                        if (date.before(licenseLevelTime)) {
                            //发送通知
                            Announcement announcement = new Announcement();
                            announcement.setTenantId(driver.getTenantId());
                            announcement.setTitle("驾驶证审验日期临期监测");
                            announcement.setDriverOwerId(driver.getDriverId());
                            announcement.setDriverName(driver.getDriverName());
                            announcement.setType("0");
                            announcement.setContent("手机号为" + driver.getPhone() + "的司机驾驶证审验日期到了，请及时审验！审验通过后找客服确认!");
                            announcement.setCreateTime(new Date());
                            announcementService.insert(announcement);
                            driver.setLicenseLevelStatus("1");
                            truDriverService.updateById(driver);
                        }else{
                            //发送通知
                            Announcement announcement = new Announcement();
                            announcement.setTenantId(driver.getTenantId());
                            announcement.setTitle("驾驶证审验日期临期监测");
                            announcement.setDriverOwerId(driver.getDriverId());
                            announcement.setDriverName(driver.getDriverName());
                            announcement.setType("0");
                            announcement.setContent("手机号为" + driver.getPhone() + "的司机驾驶证审验日期过了，请及时处理！处理完成后找客服确认!");
                            announcement.setCreateTime(new Date());
                            announcementService.insert(announcement);
                            driver.setLicenseLevelStatus("2");
                            truDriverService.updateById(driver);
                        }
                    }else{
                        driver.setLicenseLevelStatus("0");
                        truDriverService.updateById(driver);
                    }
                }
            }
        }

        //从业证审验日期临期监测通知
        TruDriver truDriverb = new TruDriver();
        List<TruDriver> truDriverbs = truDriverService.selectAllDriver(truDriverb);
        if (truDriverbs.size() > 0) {
            for (TruDriver driver : truDriverbs) {
                if (null != driver.getQualificationTime()) {
                    Date qualificationTime = driver.getQualificationTime();//从业证审验日期
                    if (time.after(qualificationTime)) {
                        if (date.before(qualificationTime)) {
                            //发送通知
                            Announcement announcement = new Announcement();
                            announcement.setTenantId(driver.getTenantId());
                            announcement.setTitle("从业证审验日期临期监测");
                            announcement.setDriverOwerId(driver.getDriverId());
                            announcement.setDriverName(driver.getDriverName());
                            announcement.setType("0");
                            announcement.setContent("手机号为" + driver.getPhone() + "的司机从业证审验日期到了，请及时审验！审验通过后找客服确认!");
                            announcement.setCreateTime(new Date());
                            announcementService.insert(announcement);
                            driver.setQualificationStatus("1");
                            truDriverService.updateById(driver);
                        }else{
                            //发送通知
                            Announcement announcement = new Announcement();
                            announcement.setTenantId(driver.getTenantId());
                            announcement.setTitle("从业证审验日期临期监测");
                            announcement.setDriverOwerId(driver.getDriverId());
                            announcement.setDriverName(driver.getDriverName());
                            announcement.setType("0");
                            announcement.setContent("手机号为" + driver.getPhone() + "的司机从业证审验日期过了，请及时处理！处理完成后找客服确认!");
                            announcement.setCreateTime(new Date());
                            announcementService.insert(announcement);
                            driver.setQualificationStatus("2");
                            truDriverService.updateById(driver);
                        }
                    } else{
                        driver.setQualificationStatus("0");
                        truDriverService.updateById(driver);
                    }
                }
            }
        }
        long end = System.currentTimeMillis();
        System.out.println(truDrivers.size()+"````````````````"+"定时任务结束，共耗时：[" + (end-begin) / 1000 + "]秒");
    }

    /**
     * 车辆、营运证、保险审验日期监测通知
     * 营运证提前一个月通知,保险提前二个月通知,车辆提前三个月通知
     */
    @Scheduled(cron = "0 0 0 * * ?")//每天凌晨执行
    void truckTimeMonitor(){
        System.out.println("定时任务开始......");
        long begin = System.currentTimeMillis();
        Date date = new Date();
        //执行数据库操作...
        TruTruck truTrucka = new TruTruck();
        truTrucka.setIsTrust("0");
        List<TruTruck> truTrucks = truTruckService.selectTruckList(truTrucka);
        //营运证审验日期提前一个月通知,临期监测通知
        if (truTrucks.size() > 0) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.MONTH, 1);//当前时间加一个月，即一个月后的时间    
            Date time = calendar.getTime();//获取一个月后的时间
            for (TruTruck truTruck : truTrucks) {
                if (null != truTruck.getOperationTime()) {
                    Date truckTime = truTruck.getOperationTime();//从业证审验日期
                    if (time.after(truckTime)) {
                        if (date.before(truckTime)) {
                            //发送通知
                            Announcement announcement = new Announcement();
                            announcement.setTenantId(truTruck.getTenantId());
                            announcement.setTitle("营运证审验日期临期监测");
                            announcement.setType("2");
                            announcement.setContent("车牌号为" + truTruck.getPlateNumber() + "的营运证审验日期到了，请及时通知车主审验！");
                            announcement.setCreateTime(new Date());
                            announcementService.insert(announcement);
                            truTruck.setOperationStatus("1");
                            truTruckService.updateById(truTruck);
                        }else{
                            //发送通知
                            Announcement announcement = new Announcement();
                            announcement.setTenantId(truTruck.getTenantId());
                            announcement.setTitle("营运证审验日期临期监测");
                            announcement.setType("2");
                            announcement.setContent("车牌号为" + truTruck.getPlateNumber() + "的营运证审验日期过了，请及时通知车主审验！");
                            announcement.setCreateTime(new Date());
                            announcementService.insert(announcement);
                            truTruck.setOperationStatus("2");
                            truTruckService.updateById(truTruck);
                        }
                    } else{
                        truTruck.setOperationStatus("0");
                        truTruckService.updateById(truTruck);
                    }
                }
            }
        }
        //保险审验日期提前两个月通知,临期监测通知
        if (truTrucks.size() > 0) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.MONTH, 2);//当前时间加两个月，即两个月后的时间    
            Date time = calendar.getTime();//获取两个月后的时间
            for (TruTruck truTruck : truTrucks) {
                if (null != truTruck.getPolicyNoTime()) {
                    Date truckTime = truTruck.getPolicyNoTime();//从业证审验日期
                    if (time.after(truckTime)) {
                        if (date.before(truckTime)) {
                            //发送通知
                            Announcement announcement = new Announcement();
                            announcement.setTenantId(truTruck.getTenantId());
                            announcement.setTitle("保险审验日期临期监测");
                            announcement.setType("2");
                            announcement.setContent("车牌号为" + truTruck.getPlateNumber() + "的保险审验日期到了，请及时通知车主审验！");
                            announcement.setCreateTime(new Date());
                            announcementService.insert(announcement);
                            truTruck.setPolicyNoStatus("1");
                            truTruckService.updateById(truTruck);
                        }else{
                            //发送通知
                            Announcement announcement = new Announcement();
                            announcement.setTenantId(truTruck.getTenantId());
                            announcement.setTitle("保险审验日期临期监测");
                            announcement.setType("2");
                            announcement.setContent("车牌号为" + truTruck.getPlateNumber() + "的保险审验日期过了，请及时通知车主审验！");
                            announcement.setCreateTime(new Date());
                            announcementService.insert(announcement);
                            truTruck.setPolicyNoStatus("2");
                            truTruckService.updateById(truTruck);
                        }
                    } else{
                        truTruck.setPolicyNoStatus("0");
                        truTruckService.updateById(truTruck);
                    }
                }
            }
        }
        //车辆审验日期提前三个月通知,临期监测通知
        if (truTrucks.size() > 0) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.MONTH, 3);//当前时间加三个月，即三个月后的时间    
            Date time = calendar.getTime();//获取三个月后的时间
            for (TruTruck truTruck : truTrucks) {
                if (null != truTruck.getTruckTime()) {
                    Date truckTime = truTruck.getTruckTime();//从业证审验日期
                    if (time.after(truckTime)) {
                        if (date.before(truckTime)) {
                            //发送通知
                            Announcement announcement = new Announcement();
                            announcement.setTenantId(truTruck.getTenantId());
                            announcement.setTitle("车辆审验日期临期监测");
                            announcement.setType("2");
                            announcement.setContent("车牌号为" + truTruck.getPlateNumber() + "的车辆审验日期到了，请及时通知车主审验！");
                            announcement.setCreateTime(new Date());
                            announcementService.insert(announcement);
                            truTruck.setTruckStatus("1");
                            truTruckService.updateById(truTruck);
                        }else{
                            //发送通知
                            Announcement announcement = new Announcement();
                            announcement.setTenantId(truTruck.getTenantId());
                            announcement.setTitle("车辆审验日期临期监测");
                            announcement.setType("2");
                            announcement.setContent("车牌号为" + truTruck.getPlateNumber() + "的车辆审验日期过了，请及时通知车主审验！");
                            announcement.setCreateTime(new Date());
                            announcementService.insert(announcement);
                            truTruck.setTruckStatus("2");
                            truTruckService.updateById(truTruck);
                        }
                    } else{
                        truTruck.setTruckStatus("0");
                        truTruckService.updateById(truTruck);
                    }
                }
            }
        }
        long end = System.currentTimeMillis();
        System.out.println(truTrucks.size()+"````````````````"+"定时任务结束，共耗时：[" + (end-begin) / 1000 + "]秒");
    }
}
