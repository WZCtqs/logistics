package com.zhkj.lc.admin.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zhkj.lc.admin.model.entity.HelpInformation;
import com.zhkj.lc.common.util.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yl
 * @since 2019-02-19
 */
@Repository
public interface HelpInformationMapper extends BaseMapper<HelpInformation> {
    //根据id查询
    HelpInformation selectInformationById(Integer informationId);

    //分页查询全部
    List<HelpInformation> selectAll(Query query,HelpInformation information);

    //不分页查询全部
    List<HelpInformation> selectAll(HelpInformation information);

    //插入
    boolean insertInformation(HelpInformation information);

    //更新
    boolean updateInformation(HelpInformation information);

    //批量删除
    boolean deleteIds(HelpInformation information);

}
