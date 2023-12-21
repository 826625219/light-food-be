package com.qsj.qsjMain.service.impl.internalBusiness;

import com.qsj.qsjMain.service.impl.ShopServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class ShopStartSACommandHandler extends BaseCommandHandler {

    private final ShopServiceImpl shopService;

    public ShopStartSACommandHandler(ShopServiceImpl shopService) {
        this.shopService = shopService;
    }

    @Override
    public String handle(String msg, String fromUser) {
        if(!shopService.isSuperUser(fromUser)){
            return "您没有权限进行此操作！";
        }
        String[] split = msg.split(" ");
        String shopId = split[1];
        shopService.startShop(Long.parseLong(shopId));
        return String.format("已经将门店ID为: %s 的门店设置为营业中状态!", shopId);
    }

    @Override
    public String getPattern() {
        return "设置开始营业 (\\d+)";
    }

}
