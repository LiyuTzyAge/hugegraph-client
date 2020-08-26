package com.baidu.hugegraph.client;

import com.baidu.hugegraph.driver.HugeClient;

/**
 *  @author: liyu04
 *  @date: 2020/8/19
 *  @version: V1.0
 *
 * @Description:
 */
public class UserAccessContext
{
    private static ThreadLocal<User> voucher = new ThreadLocal<>();
    private static boolean isAuth = false;

    private UserAccessContext()
    {
    }

    public static class User
    {
        private String user;
        private String pwd;

        public User(String user, String pwd)
        {
            this.user = user;
            this.pwd = pwd;
        }

        public String getUser()
        {
            return user;
        }

        public void setUser(String user)
        {
            this.user = user;
        }

        public String getPwd()
        {
            return pwd;
        }

        public void setPwd(String pwd)
        {
            this.pwd = pwd;
        }
    }

    public static void setUser(String user, String pwd)
    {
        voucher.set(new User(user, pwd));
    }

    public static User getUser()
    {
        return voucher.get();
    }

    public static boolean isAuth()
    {
        return isAuth;
    }

    private static void setAuth(boolean openAuth)
    {
        isAuth = openAuth;
    }

    public static HugeClient hugeClient(
            String url, String graph, String key, String value)
    {
        setAuth(true);
        setUser(key, value);
        return new HugeClient(url, graph);
    }

    public static HugeClient hugeClient(
            String url,String graph,String key,String value,int timeout)
    {
        setAuth(true);
        setUser(key, value);
        return new HugeClient(url, graph, timeout);
    }

}
