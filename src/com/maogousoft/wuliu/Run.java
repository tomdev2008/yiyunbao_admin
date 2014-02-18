/**
 * @filename Run.java
 */
package com.maogousoft.wuliu;

import com.jfinal.core.JFinal;

/**
 * @description 运行程序
 * @author shevliu
 * @email shevliu@gmail.com
 * Jul 26, 2012 9:38:13 PM
 */
public class Run {

	/**
	 * @description 运行程序
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * Jul 26, 2012 9:38:13 PM
	 * @param args
	 */
	public static void main(String[] args) {
		JFinal.start("WebRoot", 8888, "/admin",5);
	}

}
