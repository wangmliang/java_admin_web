package com.wangml.utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * 数字金额转中文
 * 
 * <pre>
 * <b>Title：</b>SimpleMoney.java<br/>
 * <b>@author：</b>WML<br/>
 * <b>@date：</b>2017年3月31日 - 下午2:27:15<br/>  
 * <b>@version V1.0</b></br/>
 * <b>Copyright (c) 2017 ASPire Tech.</b>
 * </pre>
 */
public class SimpleMoney {
	private static final double MAXNUM = 99999999999d;
	private static final String[] num = { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };
	private static final String[] uti = { "分", "角", "元", "十", "百", "千", "万", "十", "百", "千", "亿" };

	public static void main(String[] args) {
		/**
		 * 格式为中文 打印: 玖亿玖千玖百玖十玖万玖千玖百玖十玖元柒角柒分
		 */
		System.out.println(ToChineseMoney("999999999.99"));

		/**
		 * 格式化指定格式 打印: 168,866,886.159
		 */
		DecimalFormat myformat = new DecimalFormat();
		myformat.applyPattern("##,###.000");
		System.out.println(myformat.format(168866886.15897));
	}

	public static String ToChineseMoney(String str) {

		int inum = 0; // 用于记数，用来取单位
		// 转换成整数，处理到小数点后面2位，用ceil进位，强制转换为int。
		double ia = Math.ceil(Double.parseDouble(str) * 100);

		if (ia > MAXNUM) {
			return "数值过大，小数点前最大为9位（换成金额为亿）";
		}

		int ib = 0; // 保存每一位的数据
		StringBuffer sb = new StringBuffer(""); // 保存转换后的中文金额
		List<String> array = new ArrayList<String>(); // 保存转换后的中文金额
		while (ia >= 1) { // 判断a是否还有位数
			ib = (int) ia % 10; // 取出当前a的最后一位
			if (ib == 0 && uti[inum] != "万" && uti[inum] != "元") {
				array.add(num[ib]); // 当数值为零，并且单位不是万或元的时候只拼接数据
			} else if (ib == 0 && (uti[inum] == "万" || uti[inum] == "元")) {
				array.add(uti[inum]);// 当数值为单位是万或元的时候只拼接单位
			} else {
				array.add(num[ib] + uti[inum]); // 拼接上数据和单位
			}
			inum++;
			ia = ia / 10; // 去除a当前的最后一位
		}

		// 对上面取得的数组取反，得到平常所说的金额顺序，从大到小
		Collections.reverse(array);
		for (Iterator<String> iter = array.iterator(); iter.hasNext();) {
			sb.append(iter.next());
		}
		String temp = sb.toString();

		temp = formatMonet(temp);

		return temp;
	}

	// 格式化
	public static String formatMonet(String str) {
		// 把零零替换掉。
		while (str.indexOf("零零") != -1) {
			str = str.replace("零零", "零");
		}
		// 判断最后一个分的值是否为0，如果是替换为整
		if (str.charAt(str.length() - 1) == '零') {
			str = str.substring(0, str.length() - 1) + "整";
		}

		return str;
	}

}
