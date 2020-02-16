package com.couragehe.gmall.service;


import com.couragehe.gmall.bean.UmsMember;
import com.couragehe.gmall.bean.UmsMemberReceiveAddress;

import java.util.List;

public interface UserService {
    List<UmsMember> getAllUser();

    List<UmsMemberReceiveAddress> getReceiveAddressByMemberId(String memberId);

    UmsMember login(UmsMember umsMember);

    void addUserToken(String token, String memberId);

    void addOauthUser(UmsMember umsMember);

    UmsMember checkOauthUser(UmsMember umsMember);

    UmsMember getOauthUser(UmsMember umsMember);
}
