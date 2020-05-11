package com.hcc.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import com.hcc.Realm.ClinicRealm;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    @Bean
    @ConditionalOnMissingBean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAAP = new DefaultAdvisorAutoProxyCreator();
        defaultAAP.setProxyTargetClass(true);
        return defaultAAP;
    }

    // 自带密码管理器
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        //Shiro自带加密
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        //散列算法使用md5
        credentialsMatcher.setHashAlgorithmName("md5");
        //散列次数，2表示md5加密两次
        credentialsMatcher.setHashIterations(2);
        credentialsMatcher.setStoredCredentialsHexEncoded(true);
        return credentialsMatcher;
    }

    //将自己的验证方式加入容器
    @Bean
    public ClinicRealm myShiroRealm() {
        ClinicRealm clinicRealm = new ClinicRealm();
        //加入密码管理
        clinicRealm.setCredentialsMatcher(hashedCredentialsMatcher());//Shiro自带密码管理器
        return clinicRealm;
    }

    //权限管理，配置主要是Realm的管理认证
    @Bean
    public DefaultWebSecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(myShiroRealm());
        return securityManager;
    }

    @Bean
    public ShiroDialect getShiroDialect(){
        return new ShiroDialect();
    }

    //Filter工厂，设置对应的过滤条件和跳转条件
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        Map<String, String> map = new HashMap<>();

        // 配置拦截的路由
        //登出
        map.put("/logout", "logout");
        // 静态资源
        map.put("/api/**","anon");// 匿名访问
        map.put("/css/**","anon");// 匿名访问
        map.put("/images/**","anon");// 匿名访问
        map.put("/js/**","anon");// 匿名访问
        map.put("/lib/**","anon");// 匿名访问
        map.put("/loginPage","anon");// 匿名访问
        map.put("/loginForm","anon");// 匿名访问
        map.put("/registerStaffPage","anon");
        map.put("/selectAllDp","anon");
        map.put("/registerStaffInfo","anon");
        //对所有用户认证，其余的
        map.put("/**", "authc");

        //登录认证路径的路径
        shiroFilterFactoryBean.setLoginUrl("/login");
        //首页
        shiroFilterFactoryBean.setSuccessUrl("/index");
        //错误页面，认证不通过跳转
        shiroFilterFactoryBean.setUnauthorizedUrl("/erro");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);

        return shiroFilterFactoryBean;
    }

    //加入注解的使用，不加入这个注解不生效
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

}
