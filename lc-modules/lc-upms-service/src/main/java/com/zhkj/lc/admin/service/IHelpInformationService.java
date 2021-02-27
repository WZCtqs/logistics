package com.zhkj.lc.admin.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.zhkj.lc.admin.model.entity.HelpInformation;
import com.zhkj.lc.common.util.Query;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yl
 * @since 2019-02-19
 */
public interface IHelpInformationService extends IService<HelpInformation> {
    //根据id查询
    HelpInformation selectInformationById(Integer informationId);

    //分页查询全部
    Page selectAll(Query query, HelpInformation information);

    //不分页查询全部
    List<HelpInformation> selectAll(HelpInformation information);

    //插入
    boolean insertInformation(HelpInformation information);

    //更新
    boolean updateInformation(HelpInformation information);

    //批量删除
    boolean deleteIds(HelpInformation information);

}
