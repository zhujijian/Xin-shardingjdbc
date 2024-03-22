package com.snowalker.shardingjdbc.snowalker.demo.util.util;


import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 数学工具类
 */
public class MathUtils {
    private MathUtils() {
    }

    public static boolean equalsZero(BigDecimal d) {
        BigDecimal bd = toBigDecimal(d);
        return BigDecimal.ZERO.compareTo(bd) == 0;
    }

    public static BigDecimal min(BigDecimal d1, BigDecimal d2) {
        BigDecimal bd1 = toBigDecimal(d1);
        BigDecimal bd2 = toBigDecimal(d2);
        return bd1.compareTo(bd2) < 0 ? bd1 : bd2;
    }

    public static BigDecimal max(BigDecimal d1, BigDecimal d2) {
        BigDecimal bd1 = toBigDecimal(d1);
        BigDecimal bd2 = toBigDecimal(d2);
        return bd1.compareTo(bd2) < 0 ? bd2 : bd1;
    }

    public static BigDecimal add(BigDecimal d1, BigDecimal d2) {
        BigDecimal bd1 = toBigDecimal(d1);
        BigDecimal bd2 = toBigDecimal(d2);
        return bd1.add(bd2);
    }

    public static BigDecimal subtract(BigDecimal d1, BigDecimal d2) {
        BigDecimal bd1 = toBigDecimal(d1);
        BigDecimal bd2 = toBigDecimal(d2);
        return bd1.subtract(bd2);
    }

    public static BigDecimal multiply(BigDecimal d1, BigDecimal d2) {
        BigDecimal bd1 = toBigDecimal(d1);
        BigDecimal bd2 = toBigDecimal(d2);
        return bd1.multiply(bd2);
    }

    public static BigDecimal divide(BigDecimal d1, BigDecimal d2) {
        BigDecimal bd1 = toBigDecimal(d1);
        BigDecimal bd2 = toBigDecimal(d2);
        if (equalsZero(bd2)) {
            return BigDecimal.ZERO;
        }
        
        int scale = Math.max(bd1.scale(), bd2.scale());
        
        return bd1.divide(bd2, scale, BigDecimal.ROUND_HALF_UP);
    }
    
    public static BigDecimal divide(BigDecimal d1, BigDecimal d2, int scale) {
        BigDecimal bd1 = toBigDecimal(d1);
        BigDecimal bd2 = toBigDecimal(d2);
        if (equalsZero(bd2)) {
            return BigDecimal.ZERO;
        }
        return bd1.divide(bd2, scale, BigDecimal.ROUND_HALF_UP);
    }
    public static BigDecimal scale(BigDecimal bd, int scale){
        BigDecimal b1 = toBigDecimal(bd);
        return b1.divide(new BigDecimal(1), scale, BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal scale(BigDecimal bd, int scale, int roundingMode){
        BigDecimal b1 = toBigDecimal(bd);
        return b1.divide(new BigDecimal(1), scale, roundingMode);
    }

    public static BigDecimal divideRoundDown(BigDecimal d1, BigDecimal d2) {
        BigDecimal bd1 = toBigDecimal(d1);
        BigDecimal bd2 = toBigDecimal(d2);
        if (equalsZero(bd2)) {
            return BigDecimal.ZERO;
        }

        BigDecimal divide = bd1.divide(bd2, 0, BigDecimal.ROUND_DOWN);
        long longValue = divide.longValue();
        return new BigDecimal(longValue);
    }
    
    public static BigDecimal divideRoundUp(BigDecimal d1, BigDecimal d2) {
        BigDecimal bd1 = toBigDecimal(d1);
        BigDecimal bd2 = toBigDecimal(d2);
        if (equalsZero(bd2)) {
            return BigDecimal.ZERO;
        }

        BigDecimal divide = bd1.divide(bd2, 0, BigDecimal.ROUND_UP);
        long longValue = divide.longValue();
        return new BigDecimal(longValue);
    }

    
    public static BigDecimal divideRoundUp(BigDecimal d1, BigDecimal d2, int scale) {
        BigDecimal bd1 = toBigDecimal(d1);
        BigDecimal bd2 = toBigDecimal(d2);
        if (equalsZero(bd2)) {
            return BigDecimal.ZERO;
        }

        return bd1.divide(bd2, scale, BigDecimal.ROUND_UP);
    }


    public static BigDecimal mode(BigDecimal d1, BigDecimal d2) {
        BigDecimal bd1 = toBigDecimal(d1);
        BigDecimal bd2 = toBigDecimal(d2);
        if (equalsZero(bd2)) {
            return d1;
        }

        BigDecimal divide = bd1.divide(bd2, BigDecimal.ROUND_DOWN);
        long longValue = divide.longValue();
        return bd1.subtract(new BigDecimal(longValue).multiply(bd2));
    }

    public static int compareTo(BigDecimal d1, BigDecimal d2) {
        return toBigDecimal(d1).compareTo(toBigDecimal(d2));
    }

    public static int compareTo(Integer d1, Integer d2) {
        return toBigDecimal(d1).compareTo(toBigDecimal(d2));
    }

    public static int compareTo(BigDecimal d1, Integer d2) {
        return toBigDecimal(d1).compareTo(toBigDecimal(d2));
    }

    public static BigDecimal toBigDecimal(BigDecimal d) {
        return d != null ? d : BigDecimal.ZERO;
    }

    public static BigDecimal toBigDecimal(Long d) {
        return d != null ? new BigDecimal(d) : BigDecimal.ZERO;
    }

    public static BigDecimal toBigDecimal(Integer d) {
        return d != null ? new BigDecimal(d) : BigDecimal.ZERO;
    }
    
    public static BigDecimal toBigDecimal(String d) {
        return StringUtils.isNotBlank(d) ? new BigDecimal(d) : BigDecimal.ZERO;
    }

    public static Integer toInteger(Integer d) {
        return d != null ? d : 0;
    }
    public static BigDecimal toBigDecimal(Object d) {
        return d != null ? new BigDecimal(d.toString()) : null;
    }
    public static Long toLong(BigDecimal d) {
        return d != null ? Long.valueOf(d.longValue()) : 0L;
    }
    
    public static int compareTo(Long d1, Long d2) {
        return toBigDecimal(d1).compareTo(toBigDecimal(d2));
    }

	public static List<BigDecimal> avgQty(BigDecimal qty, Integer count) {// 均摊量
		if (count <= 0 || compareTo(qty, BigDecimal.ZERO) <= 0) {
			return new ArrayList<BigDecimal>();
		}
		count = toInteger(count);
		qty = toBigDecimal(qty);
		List<BigDecimal> avgList = new ArrayList<BigDecimal>();
		BigDecimal size = new BigDecimal(count);
		BigDecimal total = qty;
		BigDecimal avg = MathUtils.divide(total, size, 3);
		BigDecimal multiply = MathUtils.multiply(avg, MathUtils.subtract(size, BigDecimal.ONE));
		BigDecimal subtract = MathUtils.subtract(total, multiply);
		for (int i = 0; i < (count - 1); i++) {
			avgList.add(avg);
		}
		avgList.add(subtract);
		return avgList;
	}

    /**
     * 将阿拉伯数字  转成  中文的大写汉字
     * @param numberOfMoney
     * @return
     */
    public static String number2CNMontrayUnit(BigDecimal numberOfMoney) {
        final String[] CN_UPPER_NUMBER = { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };
        String[] CN_UPPER_MONETRAY_UNIT = { "分", "角", "元", "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿", "拾", "佰", "仟", "兆","拾", "佰", "仟" };
        String CN_FULL = "整";
        String CN_NEGATIVE = "负";
        int MONEY_PRECISION = 2;
        String CN_ZEOR_FULL = "零元" + CN_FULL;

        StringBuffer sb = new StringBuffer();
        // -1, 0, or 1 as the value of this BigDecimal is negative, zero, or
        // positive.
        int signum = numberOfMoney.signum();
        // 零元整的情况
        if (signum == 0) {
            return "人民币"+CN_ZEOR_FULL;
        }
        // 这里会进行金额的四舍五入
        long number = numberOfMoney.movePointRight(MONEY_PRECISION).setScale(0, BigDecimal.ROUND_HALF_UP).abs().longValue();
        // 得到小数点后两位值
        long scale = number % 100;
        int numUnit = 0;
        int numIndex = 0;
        boolean getZero = false;
        // 判断最后两位数，一共有四中情况：00 = 0, 01 = 1, 10, 11
        if (!(scale > 0)) {
            numIndex = 2;
            number = number / 100;
            getZero = true;
        }
        if ((scale > 0) && (!(scale % 10 > 0))) {
            numIndex = 1;
            number = number / 10;
            getZero = true;
        }
        int zeroSize = 0;
        while (true) {
            if (number <= 0) {
                break;
            }
            // 每次获取到最后一个数
            numUnit = (int) (number % 10);
            if (numUnit > 0) {
                if ((numIndex == 9) && (zeroSize >= 3)) {
                    sb.insert(0, CN_UPPER_MONETRAY_UNIT[6]);
                }
                if ((numIndex == 13) && (zeroSize >= 3)) {
                    sb.insert(0, CN_UPPER_MONETRAY_UNIT[10]);
                }
                sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
                sb.insert(0, CN_UPPER_NUMBER[numUnit]);
                getZero = false;
                zeroSize = 0;
            } else {
                ++zeroSize;
                if (!(getZero)) {
                    sb.insert(0, CN_UPPER_NUMBER[numUnit]);
                }
                if (numIndex == 2) {
                    if (number > 0) {
                        sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
                    }
                } else if (((numIndex - 2) % 4 == 0) && (number % 1000 > 0)) {
                    sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
                }
                getZero = true;
            }
            // 让number每次都去掉最后一个数
            number = number / 10;
            ++numIndex;
        }
        // 如果signum == -1，则说明输入的数字为负数，就在最前面追加特殊字符：负
        if (signum == -1) {
            sb.insert(0, CN_NEGATIVE);
        }
        // 输入的数字小数点后两位为"00"的情况，则要在最后追加特殊字符：圆整
        //if (!(scale > 0)) {
        sb.append(CN_FULL);
        //}
        if ( sb.toString().endsWith("分整")  ){
            return "人民币"+sb.toString().replace("分整", "分");
        }
        return "人民币"+sb.toString();
    }

    /**
     * 格式数字
     * @param d1
     * @param format
     * @return
     */
    public static BigDecimal toDecimal(BigDecimal d1,String format){
        DecimalFormat nf =new  DecimalFormat(format);
        String d2 = nf.format(d1);
        return new BigDecimal(d2);
    }
}
