package com.fh.shop.api.member.common;

public class KeyUtil {

    public static String buildMemberKey(Long id){
        return "member:"+id;
    }

    public static String buildImageCodeKey(String id){
        return "image:code"+id;
    }

    public static String buildActiveMemberKey(String id){
        return "active"+id;
    }

    public static String buildCartKey(Long memberId){
        return "cart"+memberId;
    }

    public static String buildTokentKey(String token){
        return "token:"+token;
    }
}
