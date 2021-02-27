package com.zhkj.lc.order.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zhkj.lc.order.mapper.BillMiddleMapper;
import com.zhkj.lc.order.mapper.OrdContainerTypeMapper;
import com.zhkj.lc.order.model.entity.BillMiddle;
import com.zhkj.lc.order.model.entity.OrdContainerType;
import com.zhkj.lc.order.service.IBillMiddleService;
import com.zhkj.lc.order.service.IOrdContainerTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 应收对账单表 服务实现类
 * </p>
 *
 * @author wzc
 * @since 2019-02-15
 */
@Service
public class OrdContainerTypeServiceImpl extends ServiceImpl<OrdContainerTypeMapper, OrdContainerType> implements IOrdContainerTypeService {

}
