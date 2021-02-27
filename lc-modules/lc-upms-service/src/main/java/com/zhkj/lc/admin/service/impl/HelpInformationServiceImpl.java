package com.zhkj.lc.admin.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zhkj.lc.admin.mapper.HelpInformationMapper;
import com.zhkj.lc.admin.model.entity.HelpInformation;
import com.zhkj.lc.admin.service.IHelpInformationService;
import com.zhkj.lc.common.util.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yl
 * @since 2019-02-19
 */
@Service
public class HelpInformationServiceImpl extends ServiceImpl<HelpInformationMapper, HelpInformation> implements IHelpInformationService {
    @Autowired
    private HelpInformationMapper helpInformationMapper;

    //根据id查询
    @Override
    public HelpInformation selectInformationById(Integer informationId){
        return helpInformationMapper.selectInformationById(informationId);
    }

    //分页查询全部
    @Override
    public Page selectAll(Query query, HelpInformation information){
        List<HelpInformation> list = helpInformationMapper.selectAll(query,information);
        query.setRecords(list);
        return query;
    }

    //不分页查询全部
    @Override
    public List<HelpInformation> selectAll(HelpInformation information) {
        return helpInformationMapper.selectAll(information);
    }

    //插入
    @Override
    public boolean insertInformation(HelpInformation information){
       return helpInformationMapper.insertInformation(information);
    }

    //更新
    @Override
    public boolean updateInformation(HelpInformation information){
        return helpInformationMapper.updateInformation(information);
    }

    //批量删除

    @Override
    public boolean deleteIds(HelpInformation information) {
        return helpInformationMapper.deleteIds(information);
    }
}
