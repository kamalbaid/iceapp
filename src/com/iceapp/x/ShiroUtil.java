package com.iceapp.x;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

public class ShiroUtil {

    public static boolean hasAdminRole(){
        Subject currentUser = SecurityUtils.getSubject();
        return currentUser.hasRole(FileBasedRealm.ADMIN_ROLE);
    }
    
    public static boolean hasRole(String role){
        Subject currentUser = SecurityUtils.getSubject();
        return currentUser.hasRole(role);
    }
    
    public static String getCurrentUserName() {
        Subject currentUser = SecurityUtils.getSubject();
        String userName = currentUser.getPrincipal().toString();
        return userName;
    }
}
