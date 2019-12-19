package com.couragehe.gmall.service;


import com.couragehe.gmall.bean.UmsMember;
import com.couragehe.gmall.bean.UmsMemberReceiveAddress;

import java.util.List;

public interface UserService {
    List<UmsMember> getAllUser();

    List<UmsMemberReceiveAddress> getReceiveAddressByMemberId(String memberId);
}
