package com.globalsight.dispatcher.util;


import com.globalsight.dispatcher.bo.Account;
import com.globalsight.dispatcher.bo.AppConstants;

import javax.servlet.http.HttpSession;

public class SessionUtil {
   public static boolean validatePermission(HttpSession session, boolean adminOnly) {
       if(null == session) return false;
       Object accountObj = session.getAttribute("theAccount");
       if(null == accountObj) return false;
       if(!(accountObj instanceof Account)) return false;
       Account account = (Account) accountObj;
       return (adminOnly? AppConstants.USER_TYPE_ADMIN.equals(account.getType()) : true);
   }
   
   public static boolean validateSession(HttpSession session) {
     if(null == session) return false;
     Object accountObj = session.getAttribute("theAccount");
     if(null == accountObj) return false;
     if(!(accountObj instanceof Account)) return false;
     return true;
 }

}
