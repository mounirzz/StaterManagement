package com.micro.demo.util;

import javax.servlet.http.HttpSession;

import com.micro.demo.adapters.Constants;
import com.micro.demo.models.User;

public class UserUtil {

	public static final String USER = Constants.LOGIN_USER;
    /**
     *Réglez l'utilisateur sur session
     *
     * @param session
     * @param user
     */
    public static void saveUserToSession(HttpSession session, User user) {
        AdminUtil.deleteAdminFromSession(session);
        session.setAttribute(USER, user);
    }

    /**
     * Obtenir les informations d'utilisateur actuelles de Session
     *
     * @param session
     * @return
     */
    public static User getUserFromSession(HttpSession session) {
        Object attribute = session.getAttribute(USER);
        return attribute == null ? null : (User) attribute;
    }

    /**
     * Supprimer les informations personnelles de l'utilisateur connecté de la session
     *
     * @param session
     */
    public static void deleteUserFromSession(HttpSession session) {
        session.removeAttribute(USER);
    }

}
