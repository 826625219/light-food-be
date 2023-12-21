package com.qsj.qsjMain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qsj.qsjMain.model.entity.UserAddressBook;
import com.qsj.qsjMain.model.mapper.UserAddressBookMapper;
import com.qsj.qsjMain.model.vo.CreateUpdateAddressVO;
import com.qsj.qsjMain.service.UserAddressBookService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;


/**
 * 用户服务实现类
 */
@Service
public class UserAddressBookServiceImpl extends ServiceImpl<UserAddressBookMapper, UserAddressBook>
        implements UserAddressBookService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createOrUpdateAddressBook(Long userId, CreateUpdateAddressVO vo) {
        if(vo.getId() == null){
            // 地址簿不存在
            UserAddressBook userAddressBook = new UserAddressBook();
            BeanUtils.copyProperties(vo, userAddressBook);
            userAddressBook.setUserId(userId);
            save(userAddressBook);
            if (userAddressBook.getIsDefault()) {
                update(new UserAddressBook().setIsDefault(false), new QueryWrapper<UserAddressBook>().eq("user_id", userId).ne("id", userAddressBook.getId()));
            }
            return true;
        }
        // 地址簿存在
        UserAddressBook userAddressBook = getById(vo.getId());

        if(Objects.isNull(userAddressBook)){
            // 地址簿不存在
            return false;
        }

        if(!Objects.equals(userAddressBook.getUserId(), userId)){
            // 地址簿不属于当前用户
            return false;
        }

        BeanUtils.copyProperties(vo, userAddressBook);
        updateById(userAddressBook);
        if (userAddressBook.getIsDefault()) {
            update(new UserAddressBook().setIsDefault(false), new QueryWrapper<UserAddressBook>().eq("user_id", userId).ne("id", userAddressBook.getId()));
        }
        return true;
    }

    @Override
    public boolean removeAddressBook(Long userId, Long addressId) {
        UserAddressBook userAddressBook = getById(addressId);
        if(Objects.isNull(userAddressBook)){
            // 地址簿不存在
            return false;
        }

        if(!Objects.equals(userAddressBook.getUserId(), userId)){
            // 地址簿不属于当前用户
            return false;
        }
        return removeById(addressId);
    }


    @Override
    public List<UserAddressBook> getAddressBookList(Long userId) {
        QueryWrapper<UserAddressBook> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserAddressBook::getUserId, userId);
        queryWrapper.orderBy(true, false, "is_default");
        return list(queryWrapper);
    }
}
