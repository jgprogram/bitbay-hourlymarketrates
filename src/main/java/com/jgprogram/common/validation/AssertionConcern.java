/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jgprogram.common.validation;

import java.util.Objects;

/**
 *
 * @author jgprogram
 */
public class AssertionConcern {

    protected AssertionConcern() {
        super();
    }

    protected final void assertArgumentEquals(Object anObject1, Object anObject2, String aMessage) {
        if (!Objects.equals(anObject1, anObject2)) {
            throw new IllegalArgumentException(aMessage);
        }
    }

    protected final void assertArgumentFalse(boolean aBoolean, String aMessage) {
        if (aBoolean) {
            throw new IllegalArgumentException(aMessage);
        }
    }

    protected final void assertArgumentLength(String aString, int aMaximum, String aMessage) {
        if (aString != null) {
            int length = aString.trim().length();
            if (length > aMaximum) {
                throw new IllegalArgumentException(aMessage);
            }
        }
    }

    protected final void assertArgumentLength(String aString, int aMinimum, int aMaximum, String aMessage) {
        if (aString != null) {
            int length = aString.trim().length();
            if (length < aMinimum || length > aMaximum) {
                throw new IllegalArgumentException(aMessage);
            }
        }
    }

    protected final void assertArgumentMatchesToPattern(String aString, String aPattern, String aMessage) {
        if (aString != null && !aString.matches(aPattern)) {
            throw new IllegalArgumentException(aMessage);
        }
    }

    protected final void assertArgumentNotEmpty(String aString, String aMessage) {
        if (aString == null || aString.trim().isEmpty()) {
            throw new IllegalArgumentException(aMessage);
        }
    }

    protected final void assertArgumentNotEquals(Object anObject1, Object anObject2, String aMessage) {
        if (Objects.equals(anObject1, anObject2)) {
            throw new IllegalArgumentException(aMessage);
        }
    }

    protected final void assertArgumentNotNull(Object anObject, String aMessage) {
        if (anObject == null) {
            throw new IllegalArgumentException(aMessage);
        }
    }

    protected final void assertArgumentRange(double aValue, double aMinimum, double aMaximum, String aMessage) {
        if (aValue < aMinimum || aValue > aMaximum) {
            throw new IllegalArgumentException(aMessage);
        }
    }

    protected final void assertArgumentRange(float aValue, float aMinimum, float aMaximum, String aMessage) {
        if (aValue < aMinimum || aValue > aMaximum) {
            throw new IllegalArgumentException(aMessage);
        }
    }

    protected final void assertArgumentRange(int aValue, int aMinimum, int aMaximum, String aMessage) {
        if (aValue < aMinimum || aValue > aMaximum) {
            throw new IllegalArgumentException(aMessage);
        }
    }

    protected final void assertArgumentRange(long aValue, long aMinimum, long aMaximum, String aMessage) {
        if (aValue < aMinimum || aValue > aMaximum) {
            throw new IllegalArgumentException(aMessage);
        }
    }

    protected final void assertArgumentTrue(boolean aBoolean, String aMessage) {
        if (!aBoolean) {
            throw new IllegalArgumentException(aMessage);
        }
    }

    protected final void assertStateFalse(boolean aBoolean, String aMessage) {
        if (aBoolean) {
            throw new IllegalStateException(aMessage);
        }
    }

    protected final void assertStateTrue(boolean aBoolean, String aMessage) {
        if (!aBoolean) {
            throw new IllegalStateException(aMessage);
        }
    }
}
