package com.qsj.qsjMain.service.impl.internalBusiness;

import com.qsj.qsjMain.model.entity.Shop;
import com.qsj.qsjMain.service.impl.ShopServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StopShopCommandHandler extends BaseCommandHandler {
    private final ShopServiceImpl shopService;

    public StopShopCommandHandler(ShopServiceImpl shopService) {
        this.shopService = shopService;
    }

    @Override
    public String handle(String msg, String fromUser) {
        List<Shop> shops = shopService.extractEWXUserMgtShops(fromUser);
        if (shops.size() == 0) {
            return "您没有管理的门店！";
        }
        if (shops.size() > 1) {
            return "您管理多个门店，请使用管理员指令进行操作！";
        }
        Long shopId = shops.get(0).getId();
        shopService.stopShop(shopId);
        return String.format("设置成功！门店ID为: %s 的门店已经结束营业!您辛苦了!", shopId);
    }

    @Override
    public String getPattern() {
        return "停止营业";
    }

    @Override
    public Boolean isRegex() {
        return false;
    }

}
