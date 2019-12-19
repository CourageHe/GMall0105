package com.couragehe.gmall.user.service.impl;

import com.couragehe.gmall.bean.UmsMember;
import com.couragehe.gmall.bean.UmsMemberReceiveAddress;
import com.couragehe.gmall.service.UserService;
import com.couragehe.gmall.user.mapper.UmsMemberReceiveAddressMapper;
import com.couragehe.gmall.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;

    @Autowired
    UmsMemberReceiveAddressMapper umsMemberReceiveAddressMapper;
    @Override
    public List<UmsMember> getAllUser() {
        List<UmsMember> umsMembers = userMapper.selectAll();//selectAllUser();

        return umsMembers;
    }

    @Override
    public List<UmsMemberReceiveAddress> getReceiveAddressByMemberId(String memberId) {
        //返回泛型
//        Example e= new Example(UmsMemberReceiveAddress.class);
//        //定义查询规则
//        e.createCriteria().andEqualTo("memberId",memberId);
//         List<UmsMemberReceiveAddress> umsMemberReceiveAddresses = umsMemberReceiveAddressMapper.selectByExample(e);

        //封装的参数对象
        UmsMemberReceiveAddress umsMemberReceiveAddress= new UmsMemberReceiveAddress();
        umsMemberReceiveAddress.setMemberId(memberId);
        List<UmsMemberReceiveAddress> umsMemberReceiveAddresses= umsMemberReceiveAddressMapper.select(umsMemberReceiveAddress);

        return umsMemberReceiveAddresses;
    }
}
