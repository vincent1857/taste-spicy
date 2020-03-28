/*
 * Copyright (c) 2015 by vincent.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package cn.vincent.taste.spicy.helper.math;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 精确的浮点数运算
 *
 * @author vincent
 * @version 1.0 2019/9/23 15:00
 */
public class Arith {

    /**
     * 默认除法运算精度
     */
    private static final int DEF_DIV_SCALE = 10;

    /**
     * 这个类不能实例化
     */
    private Arith() { }

    public static double subtract(double x, double y) {
        BigDecimal d1 = new BigDecimal(x);
        BigDecimal d2 = new BigDecimal(y);
        return d1.subtract(d2).doubleValue();
    }

    public static double subtractUp(double x, double y) {
        double value = subtract(x, y);
        return roundUp(value);
    }

    public static double subtractDown(double x, double y) {
        double value = subtract(x, y);
        return roundDown(value);
    }

    public static double add(double x, double y) {
        BigDecimal d1 = new BigDecimal(x);
        BigDecimal d2 = new BigDecimal(y);
        return d1.add(d2).doubleValue();
    }

    public static double multiply(double x, double y) {
        BigDecimal d1 = new BigDecimal(x);
        BigDecimal d2 = new BigDecimal(y);
        return d1.multiply(d2).doubleValue();
    }

    public static double divide(double x, double y) {
        return divide(x, y, DEF_DIV_SCALE);
    }

    public static double divide(double x, double y, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal d1 = new BigDecimal(x);
        BigDecimal d2 = new BigDecimal(y);
        return d1.divide(d2, scale).doubleValue();
    }

    public static double roundUp(double val) {
        return roundUp(val, 0);
    }

    public static double roundUp(double val, int scale) {
        BigDecimal dec = new BigDecimal(val);
        return dec.setScale(scale, RoundingMode.UP).doubleValue();
    }

    public static double roundDown(double val) {
        return roundDown(val, 0);
    }

    public static double roundDown(double val, int scale) {
        BigDecimal dec = new BigDecimal(val);
        return dec.setScale(scale, RoundingMode.DOWN).doubleValue();
    }

    public static double roundHalfUp(double val) {
        return roundHalfUp(val, 0);
    }

    public static double roundHalfUp(double val, int scale) {
        BigDecimal dec = new BigDecimal(val);
        return dec.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
