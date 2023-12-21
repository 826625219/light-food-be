package com.qsj.qsjMain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qsj.qsjMain.model.entity.Shop;
import com.qsj.qsjMain.model.enums.ShopRunningStatus;
import com.qsj.qsjMain.model.mapper.ShopMapper;
import com.qsj.qsjMain.model.vo.ShopListVO;
import com.qsj.qsjMain.remote.service.EWXAppRemoteService;
import com.qsj.qsjMain.remote.service.model.vo.EWXUserInfoVO;
import com.qsj.qsjMain.service.ShopService;
import com.qsj.qsjMain.utils.DistanceUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * 用户服务实现类
 */
@Service
@Slf4j
public class ShopServiceImpl extends ServiceImpl<ShopMapper, Shop>
        implements ShopService {

    private final EWXAppRemoteService ewxAppRemoteService;

    public ShopServiceImpl(EWXAppRemoteService ewxAppRemoteService) {
        this.ewxAppRemoteService = ewxAppRemoteService;
    }

    @Override
    public boolean checkShopRunning(Long shopId) {
        Shop shop = getById(shopId);
        return shop.getStatus() == ShopRunningStatus.RUNNING.getStatusCode();
    }

    @Override
    public boolean checkShopRunning(Shop shop) {
        return shop.isRunning();
    }

    @Override
    public List<ShopListVO> getShop(Double latitude, Double longitude) {
        QueryWrapper<Shop> queryWrapper = new QueryWrapper<>();
        List<Shop> shops = list(queryWrapper);
        List<ShopListVO> shopListVOList = new ArrayList<>();
        for (Shop shop : shops) {
            ShopListVO shopListVO = new ShopListVO();
            shopListVO.setDistance(DistanceUtils.getDistance(longitude.toString(), latitude.toString(),
                    shop.getLongitude().toString(), shop.getLatitude().toString()));
            shopListVO.setAddressDetail(shop.getAddressDetail());
            shopListVO.setLongitude(shop.getLongitude());
            shopListVO.setLatitude(shop.getLatitude());
            shopListVO.setName(shop.getName());
            shopListVO.setStatus(shop.getStatus());
            shopListVO.setShopId(shop.getId());
            shopListVO.setPhone(shop.getPhone());
            shopListVOList.add(shopListVO);
        }
        Collections.sort(shopListVOList);

        return shopListVOList;
    }

    public void startShop(Long shopId) {
        LambdaUpdateWrapper<Shop> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Shop::getId, shopId);
        updateWrapper.set(Shop::getStatus, ShopRunningStatus.RUNNING.getStatusCode());
        update(updateWrapper);
    }

    public boolean autoStartShops() {
        LambdaUpdateWrapper<Shop> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Shop::getAutoStatusLock, false);
        wrapper.set(Shop::getStatus, ShopRunningStatus.RUNNING.getStatusCode());
        return update(wrapper);
    }

    public boolean autoCloseShops() {
        LambdaUpdateWrapper<Shop> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Shop::getAutoStatusLock, false);
        wrapper.set(Shop::getStatus, ShopRunningStatus.REST.getStatusCode());
        return update(wrapper);
    }

    public void stopShop(Long shopId) {
        LambdaUpdateWrapper<Shop> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Shop::getId, shopId);
        updateWrapper.set(Shop::getStatus, ShopRunningStatus.REST.getStatusCode());
        update(updateWrapper);
    }

    public void lockShop(Long shopId) {
        LambdaUpdateWrapper<Shop> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Shop::getId, shopId);
        updateWrapper.set(Shop::getAutoStatusLock, 1);
        update(updateWrapper);
    }

    public void unlockShop(Long shopId) {
        LambdaUpdateWrapper<Shop> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Shop::getId, shopId);
        updateWrapper.set(Shop::getAutoStatusLock, 0);
        update(updateWrapper);
    }


    public void stopDelivery(Long shopId) {
        LambdaUpdateWrapper<Shop> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Shop::getId, shopId);
        updateWrapper.set(Shop::getStopDelivery, 1);
        update(updateWrapper);
    }

    public void startDelivery(Long shopId) {
        LambdaUpdateWrapper<Shop> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Shop::getId, shopId);
        updateWrapper.set(Shop::getStopDelivery, 0);
        update(updateWrapper);
    }

    public List<Shop> extractEWXUserMgtShops(String userId){
        try {
            Response<EWXUserInfoVO> response = ewxAppRemoteService.getTokenApiRemoteService().getUserInfo(userId).execute();
            EWXUserInfoVO ewxUserInfoVO = response.body();
            if(ewxUserInfoVO == null){
                return new ArrayList<>();
            }
            String mgmtShop = ewxUserInfoVO.getAttr("mgmtShop");
            if(mgmtShop == null || mgmtShop.equals("")){
                return new ArrayList<>();
            }
            if(mgmtShop.equals("ALL")) {
                return list();
            }
            List<String> mgtShops = List.of(mgmtShop.split(","));
            LambdaQueryWrapper<Shop> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(Shop::getId, mgtShops);
            return list(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Boolean isSuperUser(String userId){
        try {
            Response<EWXUserInfoVO> response = ewxAppRemoteService.getTokenApiRemoteService().getUserInfo(userId).execute();
            EWXUserInfoVO ewxUserInfoVO = response.body();
            if(ewxUserInfoVO == null){
                return false;
            }
            String mgmtShop = ewxUserInfoVO.getAttr("mgmtShop");
            log.info("mgmtShop: {}", mgmtShop);
            if(mgmtShop == null || mgmtShop.equals("")){
                return false;
            }
            return mgmtShop.equals("ALL");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
