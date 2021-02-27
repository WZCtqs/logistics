package com.zhkj.lc.order.mapper;

import com.zhkj.lc.order.model.entity.OrdOrderFile;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 订单文件表(派车单、附件、运输拍照) Mapper 接口
 * </p>
 *
 * @author wzc
 * @since 2018-12-07
 */
public interface OrdOrderFileMapper extends BaseMapper<OrdOrderFile> {
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

    Integer deleteByOrderId(@Param("orderId")String orderId, @Param("tenantId") Integer tenantId);
}
