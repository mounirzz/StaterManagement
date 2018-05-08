package com.micro.demo.util;

import javax.servlet.http.HttpSession;

import com.micro.demo.adapters.Constants;
import com.micro.demo.models.Admin;

public class AdminUtil {

	public static final String ADMIN = Constants.LOGIN_ADMIN;
	
    public static void saveAdminToSession(HttpSession session, Admin admin) {
        UserUtil.deleteUserFromSession(session);
        session.setAttribute(ADMIN,admin);
    }

    /**
     * Obtenir les informations de gestion actuelles de Session
     *
     * @param session
     * @return
     */
    public static Admin getAdminFromSession(HttpSession session) {
        Object attribute = session.getAttribute(ADMIN);
        return attribute == null ? null : (Admin) attribute;
    }


    public static void deleteAdminFromSession(HttpSession session) {
        session.removeAttribute(ADMIN);
    }
}
