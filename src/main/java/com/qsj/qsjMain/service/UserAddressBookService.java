package com.qsj.qsjMain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qsj.qsjMain.model.entity.UserAddressBook;
import com.qsj.qsjMain.model.vo.CreateUpdateAddressVO;

import java.util.List;

/**
 *
 */
public interface UserAddressBookService extends IService<UserAddressBook> {

    boolean createOrUpdateAddressBook(Long userId, CreateUpdateAddressVO vo);


    boolean removeAddressBook(Long userId, Long addressId);
    List<UserAddressBook> getAddressBookList(Long userId);
}
