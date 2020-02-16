package com.couragehe.gmall.user.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.couragehe.gmall.bean.UmsMember;
import com.couragehe.gmall.bean.UmsMemberReceiveAddress;
import com.couragehe.gmall.service.UserService;
import com.couragehe.gmall.user.mapper.UmsMemberReceiveAddressMapper;
import com.couragehe.gmall.user.mapper.UserMapper;
import com.couragehe.gmall.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;

    @Autowired
    UmsMemberReceiveAddressMapper umsMemberReceiveAddressMapper;

    @Autowired
    RedisUtil redisUtil;

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
        UmsMemberReceiveAddress umsMemberReceiveAddress = new UmsMemberReceiveAddress();
        umsMemberReceiveAddress.setMemberId(memberId);
        List<UmsMemberReceiveAddress> umsMemberReceiveAddresses = umsMemberReceiveAddressMapper.select(umsMemberReceiveAddress);

        return umsMemberReceiveAddresses;
    }

    @Override
    public UmsMember login(UmsMember umsMember) {
        Jedis jedis = null;
        try{
            jedis = redisUtil.getJedis();
            if(jedis !=null){
                String umsMemberStr = jedis.get("user:"+umsMember.getPassword()+":info");

                if(StringUtils.isNotBlank(umsMemberStr)){
                    //密码正确
                    UmsMember umsMemberFromCache = JSON.parseObject(umsMemberStr,UmsMember.class);
                    return umsMemberFromCache;
                }
            }

            //密码错误
            //缓存中没有
            //jedis链接不上
            //开启数据库
            UmsMember umsMemberFromDb = loginFromDb(umsMember);
            if(umsMemberFromDb != null){
                jedis.setex("user:"+umsMemberFromDb.getPassword()+":info",60*60*4,JSON.toJSONString(umsMemberFromDb));
            }
            return umsMemberFromDb;

        }finally {
            jedis.close();
        }

    }

    @Override
    public void addUserToken(String token, String memberId) {
        Jedis jedis = null;
        try {
            jedis = redisUtil.getJedis();
            jedis.setex("user:"+memberId+":token",60*60*2,token);
        }finally {
            jedis.close();
        }
    }

    @Override
    public void addOauthUser(UmsMember umsMember) {
        userMapper.insertSelective(umsMember);
    }

    /**
     * 检查该用户是否存在
     * @param umsMember
     */
    @Override
    public UmsMember checkOauthUser(UmsMember umsMember) {
        UmsMember umsMember1 = userMapper.selectOne(umsMember);
        return umsMember1;
    }

    @Override
    public UmsMember getOauthUser(UmsMember umsMember) {
        UmsMember umsMember1 = userMapper.selectOne(umsMember);
        return umsMember1;
    }

    private UmsMember loginFromDb(UmsMember umsMember) {
        UmsMember umsMember1 =  userMapper.selectOne(umsMember);
        return umsMember1;
    }
}
