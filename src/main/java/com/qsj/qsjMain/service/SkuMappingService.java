package com.qsj.qsjMain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qsj.qsjMain.model.entity.Product;
import com.qsj.qsjMain.model.entity.SkuMapping;

import java.util.List;

public interface SkuMappingService extends IService<SkuMapping> {

    /**
     * 根据sku id获取itemId
     * @param sku 外部sku id
     * @param shopId 店铺id
     * @param externalType 外部类型
     * @return
     */
    Long getItemIdByExternalSku(String sku, Long shopId, SkuMapping.MapType externalType);

    /**
     * 根据sku id获取itemId，支持多个映射，用于套餐sku映射实现
     * @param sku 外部sku id
     * @param shopId 店铺id
     * @param externalType 外部类型
     * @return itemId列表
     */
    List<Long> getItemIdsByExternalSku(String sku, Long shopId, SkuMapping.MapType externalType);
}
