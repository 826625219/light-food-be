package com.qsj.qsjMain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qsj.qsjMain.model.entity.SkuMapping;
import com.qsj.qsjMain.model.mapper.SkuMappingMapper;
import com.qsj.qsjMain.service.SkuMappingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * sku映射服务实现类
 */
@Slf4j
@Service
public class SkuMappingServiceImpl extends ServiceImpl<SkuMappingMapper, SkuMapping>
        implements SkuMappingService {

    @Override
    public Long getItemIdByExternalSku(String sku, Long shopId, SkuMapping.MapType externalType) {
        LambdaQueryWrapper<SkuMapping> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SkuMapping::getShopId, shopId);
        if (externalType == SkuMapping.MapType.MT) {
            queryWrapper.eq(SkuMapping::getMtSkuId, sku);
            SkuMapping skuMapping = this.getOne(queryWrapper, false);
            if(skuMapping == null) {
                return null;
            }
            return skuMapping.getItemId();
        }
        if (externalType == SkuMapping.MapType.ELE) {
            queryWrapper.eq(SkuMapping::getEleSkuId, sku);
            SkuMapping skuMapping = this.getOne(queryWrapper, false);
            if(skuMapping == null) {
                return null;
            }
            return skuMapping.getItemId();
        }
        return Long.parseLong(sku);
    }

    @Override
    public List<Long> getItemIdsByExternalSku(String sku, Long shopId, SkuMapping.MapType externalType) {
        LambdaQueryWrapper<SkuMapping> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SkuMapping::getShopId, shopId);
        if (externalType == SkuMapping.MapType.MT) {
            queryWrapper.eq(SkuMapping::getMtSkuId, sku);
            List<SkuMapping> skuMappings = this.list(queryWrapper);
            for(SkuMapping skuMapping : skuMappings) {
                if(skuMapping.getItemId() == null) {
                    log.debug("美团sku id:{} 货品被显式设置为null, shopId: {}", skuMapping.getMtSkuId(), skuMapping.getShopId());
                    return new ArrayList<>();
                }
            }
            return skuMappings.stream().map(SkuMapping::getItemId).collect(Collectors.toList());
        }

        return new ArrayList<>();
    }


}
