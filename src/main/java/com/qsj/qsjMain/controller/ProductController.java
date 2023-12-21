package com.qsj.qsjMain.controller;

import com.qsj.qsjMain.model.Message;
import com.qsj.qsjMain.model.entity.Product;
import com.qsj.qsjMain.service.impl.ProductServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@RestController
@RequestMapping("api/v1/product")
@Api(tags = "商品接口")
public class ProductController {
    private final ProductServiceImpl productService;

    public ProductController(ProductServiceImpl productService) {
        this.productService = productService;
    }

    @RequestMapping(value = "getProducts", method = RequestMethod.GET)
    @ApiOperation("获取商品列表")
    public Message<List<Product>> getProducts(@RequestAttribute @ApiIgnore Long userId,
                                              @ApiParam("产品名，模糊查询") @RequestParam(defaultValue = "") String productName,
                                              @ApiParam("店铺id") @RequestParam Long shopId) {
        return Message.ok(productService.getProductsByName(userId,productName, shopId));
    }
}
