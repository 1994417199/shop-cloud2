package com.fh.shop.api.member.biz;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fh.shop.api.member.common.Constants;
import com.fh.shop.api.member.common.KeyUtil;
import com.fh.shop.api.member.mapper.IMemberMapper;
import com.fh.shop.api.member.po.Member;
import com.fh.shop.api.member.vo.MemberVo;
import com.fh.shop.common.ResponseEnum;
import com.fh.shop.common.ServerResponse;
import com.fh.shop.util.Md5Util;
import com.fh.shop.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@Service("memberService")
@Transactional(rollbackFor = Exception.class)
public class IMemberServiceImpl implements IMemberService {
    @Autowired
    private IMemberMapper memberMapper;



    @Override
    public ServerResponse login(String memberName, String password) {
        //验证非空
        if(StringUtils.isEmpty(memberName) || StringUtils.isEmpty(password)){
            return ServerResponse.error(ResponseEnum.MEMBER_NAME_PASS_IS_NULL);
        }
        //严证用户名是否存在
        QueryWrapper<Member> memberQueryWrapper = new QueryWrapper<>();
        memberQueryWrapper.eq("memberName",memberName);
        Member member = memberMapper.selectOne(memberQueryWrapper);
        if(member==null){
            return ServerResponse.error(ResponseEnum.MENBER_NAME_IS_NULL);
        }
        //验证密码是否正确
        if(!Md5Util.md5(password).equals(member.getPassword())){
            return ServerResponse.error(ResponseEnum.MENBER_PASS_IS_NOT);
        }
        //根据用户名查询用户信息
        QueryWrapper<Member> memberQueryWrapper1 = new QueryWrapper<>();
        memberQueryWrapper1.eq("memberName",memberName);
        Member member1 = memberMapper.selectOne(memberQueryWrapper1);
        Integer activation = member1.getActivation();
        //为0说明未激活，进行提醒
        if(activation==0){
            String mail = member1.getMail();
            String uuid = UUID.randomUUID().toString();
            RedisUtil.setEx(KeyUtil.buildActiveMemberKey(uuid),5*60,member1.getId()+"");
            Map<String,String> result=new HashMap<>();
            result.put("mail",mail);
            result.put("uuid",uuid);
            return ServerResponse.error(ResponseEnum.MENBER_IS_NOT_OK,result);
        }
        //--------------生成签名
        //创建memberVO
        MemberVo memberVo = new MemberVo();
        memberVo.setId(member.getId());
        memberVo.setMemberName(member.getMemberName());
        memberVo.setNickName(member.getNickName());
        //将用户信息转为json
        String memberVoJson = JSON.toJSONString(memberVo);
        //生成签名
        String sign = Md5Util.sign(memberVoJson,Constants.SECRET);
        //--------------生成签名
        //将用户信息进行64编码
        String memberVoJsonbase64 = Base64.getEncoder().encodeToString(memberVoJson.getBytes());
        //将签名进行64编码
        String sigmBase64 = Base64.getEncoder().encodeToString(sign.getBytes());
        //将用户信息放到redis中
        RedisUtil.setEx(KeyUtil.buildMemberKey(member.getId()),Constants.TOKEN_EXPIRE,"");
        //将用户信息相应给前台信息中不能有密码
        //将用户信息和签名通过.分割成一个字符串传给前台x.y
        return ServerResponse.success(memberVoJsonbase64+"."+sigmBase64);
    }




}
