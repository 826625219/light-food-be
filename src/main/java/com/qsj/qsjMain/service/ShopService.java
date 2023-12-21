package com.qsj.qsjMain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qsj.qsjMain.model.entity.Shop;
import com.qsj.qsjMain.model.vo.ShopListVO;

import java.util.List;

/**
 *
 */
public interface ShopService extends IService<Shop> {
    /**
     * 检查店铺是否在营业
     *
     * @param shopId 店铺id
     * @return true:在营业 false:不在营业
     */
    boolean checkShopRunning(Long shopId);

    boolean checkShopRunning(Shop shop);


    public List<ShopListVO> getShop(Double latitude, Double longitude);

}
