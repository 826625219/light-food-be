package com.qsj.qsjMain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qsj.qsjMain.model.entity.NutritionPlan;
import com.qsj.qsjMain.model.entity.Product;
import com.qsj.qsjMain.model.entity.ShopProductControl;
import com.qsj.qsjMain.model.entity.User;
import com.qsj.qsjMain.model.mapper.ProductMapper;
import com.qsj.qsjMain.model.vo.OrderCreateItemVO;
import com.qsj.qsjMain.service.NutritionPlanService;
import com.qsj.qsjMain.service.ProductService;
import com.qsj.qsjMain.service.ShopProductControlService;
import com.qsj.qsjMain.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 用户服务实现类
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product>
        implements ProductService {

    private final ShopProductControlService shopProductControlService;
    private final UserService userService;

    private final NutritionPlanService nutritionPlanService;

    public ProductServiceImpl(ShopProductControlService shopProductControlService, UserService userService, NutritionPlanService nutritionPlanService) {
        this.shopProductControlService = shopProductControlService;
        this.userService = userService;
        this.nutritionPlanService = nutritionPlanService;
    }

    @Override
    public boolean canBuyProduct(List<OrderCreateItemVO> orderProducts, Long shopId) {
        return true;
    }

    @Override
    public List<Product> getProductsByName(Long userId, String productName, Long shopId) {
        QueryWrapper<Product> wrapper = new QueryWrapper<>();
        if (productName != null) {
            wrapper.lambda().eq(Product::getStatus, 0).like(Product::getName, productName);
        } else {
            wrapper.lambda().eq(Product::getStatus, 0);
        }
        List<Product> products = list(wrapper);
        // 叠加店铺商品控制信息
        shopProductControlService.list(new LambdaQueryWrapper<ShopProductControl>().eq(ShopProductControl::getShopId, shopId))
                .forEach(shopProductControl -> products.forEach(product -> {
                    if (product.getId().equals(shopProductControl.getProductId())) {
                        product.setShopStatus(shopProductControl.getStatus());
                    }
                }));
        User user = userService.getById(userId);
        NutritionPlan nutritionPlan = nutritionPlanService.getById(user.getNutritionPlanId());
        if(nutritionPlan==null){
            return products;
        }

        String nutritionPlanName = nutritionPlan.getName();

        for (Product product : products) {
            //

            if(product.getTag()==null){
                continue;
            }
            product.setRecommend(product.getTag().contains(nutritionPlanName));
        }
        return products;
    }
}
