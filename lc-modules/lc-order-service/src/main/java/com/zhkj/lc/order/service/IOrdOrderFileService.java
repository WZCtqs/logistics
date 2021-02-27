package com.zhkj.lc.order.service;

import com.zhkj.lc.order.model.entity.OrdOrderFile;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 订单文件表(派车单、附件、运输拍照) 服务类
 * </p>
 *
 * @author wzc
 * @since 2018-12-07
 */
public interface IOrdOrderFileService extends IService<OrdOrderFile> {
    /**
     * 查询订单文件(派车单、附件、运输拍照)信息
     *
     * @param orderId 订单文件(派车单、附件、运输拍照)ID
     * @return 订单文件(派车单、附件、运输拍照)信息
     */
    public OrdOrderFile selectOrderFileById(String orderId);

    /**
     * 新增订单文件(派车单、附件、运输拍照)
     *
     * @param orderFile 订单文件(派车单、附件、运输拍照)信息
     * @return 结果
     */
    public boolean insertOrderFile(OrdOrderFile orderFile);

    /**
     * 修改订单文件(派车单、附件、运输拍照)
     *
     * @param orderFile 订单文件(派车单、附件、运输拍照)信息
     * @return 结果
     */
    public boolean updateOrderFile(OrdOrderFile orderFile);

}
