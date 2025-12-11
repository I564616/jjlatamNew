package com.jnj.b2b.la.loginaddon.security;

import com.jnj.b2b.storefront.security.B2BUserGroupProvider;
import de.hybris.platform.core.model.user.UserModel;

/**
 * Created by 588685 on 25/11/2016.
 */
public interface LaB2BUserGroupProvider extends B2BUserGroupProvider{

    boolean isCurrentUserAuthorizedToBid();

    boolean checkIfUserAuthorizedToBid(UserModel user);
}
