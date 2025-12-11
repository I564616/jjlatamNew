/**
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */

package com.jnj.core.util;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class CalendarBlockUtils {

    private static final int QUANTITY_MONTHS = 12;
    private static final int FIRST_DAY_MONTH = 1;

    private final int blocksQuantity;
    private final int blockSize;
    private final Map<Integer, Interval<Integer>> intervalOfMonthsPerBlock;

    public CalendarBlockUtils(final int blocksQuantity) {
        this.blocksQuantity = blocksQuantity;
        this.blockSize = calculateBlockSize();
        this.intervalOfMonthsPerBlock = createIntervalOfMonthsPerBlock();
    }

    private int calculateBlockSize() {
        return QUANTITY_MONTHS / blocksQuantity;
    }

    private Map<Integer, Interval<Integer>> createIntervalOfMonthsPerBlock() {
        final Map<Integer, Interval<Integer>> result = new HashMap<>();

        int startInterval;
        int endInterval;

        int currentBlock = 1;
        while (currentBlock <= blocksQuantity) {
            startInterval = ((currentBlock - 1) * blockSize) + 1;
            endInterval = (currentBlock * blockSize);

            result.put(currentBlock, new Interval<>(startInterval, endInterval));
            currentBlock++;
        }

        return result;
    }

    public Interval<Calendar> getPreviousBlockFromNow() {
        return getPreviousBlock(Calendar.getInstance());
    }

    public Interval<Calendar> getPreviousBlock(final Calendar date) {
        shiftBlocks(date, -1);
        return extractIntervalFromDate(date);
    }

    void shiftBlocks(final Calendar date, final int numberOfBlocks) {
        date.add(Calendar.MONTH, numberOfBlocks * blockSize);
    }

    @SuppressWarnings("MagicConstant")
    private Interval<Calendar> extractIntervalFromDate(final Calendar shiftedDate) {
        final int block = getBlock(shiftedDate);
        final int year = shiftedDate.get(Calendar.YEAR);
        final Interval<Integer> monthInterval = intervalOfMonthsPerBlock.get(block);

        final Calendar startDate;
        final Calendar endDate;

        startDate = new GregorianCalendar(year, monthInterval.getStart() - 1, FIRST_DAY_MONTH);

        endDate = new GregorianCalendar(year, monthInterval.getEnd() - 1, FIRST_DAY_MONTH);
        endDate.set(Calendar.DATE, endDate.getActualMaximum(Calendar.DATE));

        return new Interval<>(startDate, endDate);
    }

    private int getBlock(final Calendar date) {
        return (date.get(Calendar.MONTH) / blockSize) + 1;
    }


}