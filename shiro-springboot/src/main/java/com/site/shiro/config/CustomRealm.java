package com.site.shiro.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * 自定义realm
 *
 */

@Slf4j
public class CustomRealm extends AuthorizingRealm {

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        log.info("-------身份授权方法--------");
        String username = (String) SecurityUtils.getSubject().getPrincipal();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        Set<String> stringSet = new HashSet<>();
        stringSet.add("user:show");
        stringSet.add("user:admin");
        info.setStringPermissions(stringSet);
        return info;
    }

    /**
     * 这里可以注入userService,为了方便演示，我就写死了帐号了密码
     * private UserService userService;
     * <p>
     * 获取即将需要认证的信息
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
       //log.info("-------身份认证方法--------");
       // String userName = (String) authenticationToken.getPrincipal();
       // String userPwd = new String((char[]) authenticationToken.getCredentials());
       // log.info("用户名==>"+userName+"========pwd===>"+userPwd);
       // //根据用户名从数据库获取密码
       // String password = "123";
       // if (userName == null) {
       //     throw new AccountException("用户名不正确");
       // } else if (!userPwd.equals(password )) {
       //     throw new AccountException("密码不正确");
       // }
       // return new SimpleAuthenticationInfo(userName, password,getName());

        System.out.println("-------身份认证方法--------");
        String userName = (String) authenticationToken.getPrincipal();
        String userPwd = new String((char[]) authenticationToken.getCredentials());
        //根据用户名从数据库获取密码
        String password = "123";
        if (userName == null) {
            throw new AccountException("用户名不正确");
        } else if (!userPwd.equals(userPwd)) {
            throw new AccountException("密码不正确");
        }
        //交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配
        return new SimpleAuthenticationInfo(userName, password,
                ByteSource.Util.bytes(userName + "salt"), getName());

    }

    public static String MD5Pwd(String username, String pwd) {
        // 加密算法MD5
        // salt盐 username + salt
        // 迭代次数
        String md5Pwd = new SimpleHash("MD5", pwd,
                ByteSource.Util.bytes(username + "salt"), 2).toHex();
        return md5Pwd;
    }

}
