/**
 * Copyright: Copyright © 2018
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.b2b.la.jnjlaprofileaddon.forms;

import com.jnj.b2b.jnjglobalprofile.forms.JnjGTProfileForm;
import com.jnj.la.core.enums.JnJEmailPeriodicity;
import com.jnj.la.core.enums.JnJDayOfTheWeek;
import com.jnj.core.enums.JnjOrderTypesEnum;
import com.jnj.la.core.enums.JnJEmailFrequency;
import java.util.List;

public class JnjLaProfileForm extends JnjGTProfileForm {

    private JnJEmailPeriodicity emailPeriodicity = JnJEmailPeriodicity.NONE;

    private List<JnjOrderTypesEnum> orderTypes;
    private JnJEmailFrequency jnJEmailFrequency;
    private JnJDayOfTheWeek dayOfTheWeek;
    private List<String> daysOfTheMonth;

    public JnJEmailPeriodicity getEmailPeriodicity() {
        return emailPeriodicity;
    }

    public void setEmailPeriodicity(final JnJEmailPeriodicity emailPeriodicity) {
        this.emailPeriodicity = emailPeriodicity;
    }

    public List<JnjOrderTypesEnum> getOrderTypes() {
        return orderTypes;
    }

    public void setOrderTypes(final List<JnjOrderTypesEnum> orderTypes) {
        this.orderTypes = orderTypes;
    }

    public JnJEmailFrequency getJnJEmailFrequency() {
        return jnJEmailFrequency;
    }

    public void setJnJEmailFrequency(final JnJEmailFrequency jnJEmailFrequency) {
        this.jnJEmailFrequency = jnJEmailFrequency;
    }

    public JnJDayOfTheWeek getDayOfTheWeek() {
        return dayOfTheWeek;
    }

    public void setDayOfTheWeek(final JnJDayOfTheWeek dayOfTheWeek) {
        this.dayOfTheWeek = dayOfTheWeek;
    }

    public List<String> getDaysOfTheMonth() {
        return daysOfTheMonth;
    }

    public void setDaysOfTheMonth(final List<String> daysOfTheMonth) {
        this.daysOfTheMonth = daysOfTheMonth;
    }
}
