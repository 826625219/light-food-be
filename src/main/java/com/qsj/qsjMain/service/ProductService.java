package com.qsj.qsjMain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qsj.qsjMain.model.entity.Product;
import com.qsj.qsjMain.model.vo.OrderCreateItemVO;

import java.util.List;

/**
 *
 */
public interface ProductService extends IService<Product> {
    boolean canBuyProduct(List<OrderCreateItemVO> orderProducts, Long shopId);

    List<Product> getProductsByName(Long userId, String productName, Long shopId);
}
