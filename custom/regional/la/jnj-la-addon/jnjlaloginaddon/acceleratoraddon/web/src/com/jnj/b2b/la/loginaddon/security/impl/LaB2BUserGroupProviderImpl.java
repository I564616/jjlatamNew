package com.jnj.b2b.la.loginaddon.security.impl;

import com.jnj.b2b.la.loginaddon.security.LaB2BUserGroupProvider;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

/**
 * Created by 588685 on 25/11/2016.
 */
public class LaB2BUserGroupProviderImpl implements LaB2BUserGroupProvider{

    private UserService userService;
    private Set<String> authorizedGroupsToBid;

    @Override
    public boolean isCurrentUserAuthorizedToBid() {
        final UserModel user = getUserService().getCurrentUser();
        return checkIfUserAuthorizedToBid(user);
    }

    @Override
    public boolean checkIfUserAuthorizedToBid(UserModel user){
        boolean isAutorized = false;
        for (final PrincipalGroupModel group : user.getAllGroups()){
            if (getAuthorizedGroupsToBid().contains(group.getUid())){
                isAutorized=true;
                break;
            }
        }
        return isAutorized;
    }

    public UserService getUserService(){
        return userService;
    }

    public void setUserService(UserService userService){
        this.userService = userService;
    }

    public Set<String> getAuthorizedGroupsToBid(){
        return authorizedGroupsToBid;
    }

    public void setAuthorizedGroupsToBid(Set<String> authorizedGroupsToBid){
        this.authorizedGroupsToBid=authorizedGroupsToBid;
    }

    /***********************************************************/

    @Override
    public Set<String> getAllowedUserGroup() {
        return null;
    }

    @Override
    public boolean isCurrentUserAuthorized() {
        return false;
    }

    @Override
    public boolean isUserAuthorized(UserModel user) {
        return false;
    }

    @Override
    public boolean isUserAuthorized(String loginName) {
        return false;
    }

    @Override
    public boolean isUserAuthorizedToCheckOut(String loginName) {
        return false;
    }

    @Override
    public boolean isUserAuthorizedToCheckOut(UserModel user) {
        return false;
    }

    @Override
    public boolean isCurrentUserAuthorizedToCheckOut() {
        return false;
    }

    @Override
    public boolean isUserEnabled(String userId) {
        return false;
    }
}
