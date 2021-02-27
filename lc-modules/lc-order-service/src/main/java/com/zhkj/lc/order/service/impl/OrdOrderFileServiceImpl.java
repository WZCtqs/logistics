package com.zhkj.lc.order.service.impl;

import com.zhkj.lc.order.model.entity.OrdOrderFile;
import com.zhkj.lc.order.mapper.OrdOrderFileMapper;
import com.zhkj.lc.order.service.IOrdOrderFileService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单文件表(派车单、附件、运输拍照) 服务实现类
 * </p>
 *
 * @author wzc
 * @since 2018-12-07
 */
@Service
public class OrdOrderFileServiceImpl extends ServiceImpl<OrdOrderFileMapper, OrdOrderFile> implements IOrdOrderFileService {

    @Autowired
    private OrdOrderFileMapper ordOrderFileMapper;

    @Override
    public OrdOrderFile selectOrderFileById(String orderId) {
        return ordOrderFileMapper.selectOrderFileById(orderId);
    }

    @Override
    public boolean insertOrderFile(OrdOrderFile orderFile) {
        return ordOrderFileMapper.insertOrderFile(orderFile);
    }

    @Override
    public boolean updateOrderFile(OrdOrderFile orderFile) {
        return ordOrderFileMapper.updateOrderFile(orderFile);
    }
}
