package com.zhkj.lc.order.service;

import com.zhkj.lc.common.util.R;
import com.zhkj.lc.order.model.entity.OrdCommonGoods;
import com.zhkj.lc.order.model.entity.OrdOrder;

import java.io.IOException;

public interface OrdAdditionalRecordingService {
    R ordAdditionalRecording(OrdOrder ordOrder, Integer tenantId) throws IOException;

    R ordAdditionalRecordingPH(OrdCommonGoods ordCommonGoods, Integer tenantId) throws IOException;
}
