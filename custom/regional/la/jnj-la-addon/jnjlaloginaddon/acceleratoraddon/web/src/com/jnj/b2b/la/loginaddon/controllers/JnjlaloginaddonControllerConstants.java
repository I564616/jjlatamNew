/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *  
 */
package com.jnj.b2b.la.loginaddon.controllers;

import java.util.ArrayList;
import java.util.List;

/**
 */
public interface JnjlaloginaddonControllerConstants
{
    String ADDON_PREFIX = "addon:/jnjlaloginaddon/";

    public static final String CONTACT_NUMBER_SULFIX = "_contactNumber";
    public static final String BR_DEPUY_SYNTHES_PREFIX = "depuy_synthes_";
    public static final String DEPUY_SYNTHES = "br.franchise.depuy.synthes";
    public static final String NO_CONTACT_NUMBER = "misc.controllers.noContactNumber";

    interface Views {

        interface Pages{

            interface Home {
                String home = "pages/home/home";
            }

            interface MyBid {
                String myBidPage = "pages/bid/myBidPage";
            }

            interface Account {
                String AccountLoginPage = "pages/account/accountLoginPage";
            }
            interface Help
            {
                String ContactUsPopUpPage =  "pages/help/contactUs";
                String HelpPage = "pages/help/helpPage";
            }
            
            interface Password
   			{
   				String PasswordResetChangePage = "pages/password/passwordResetChangePage";
   			}
        }
    }

    public interface Groups {

        public static final String GROUP_PLACE_ORDER = "placeOrderBuyerGroup";
        public static final String GROUP_ORDER_HISTORY =  "orderHistoryGroup";
        public static final String GROUP_CATALOG = "catalogGroup";
    }

    public interface Faq {

        public static final String FAQ_COUNT_NEW_PORTAL = "faqCountNewPortal";
        public static final String FAQ_COUNT_PLACE_ORDER = "faqCountPlaceOrder";
        public static final String FAQ_COUNT_ORDER_STATUS = "faqCountOrderStatus";
        public static final String FAQ_COUNT_BIDS = "faqCountBids";
        public static final String COUNT_USEFUL_LINKS = "countUsefulLinks";

    }

}