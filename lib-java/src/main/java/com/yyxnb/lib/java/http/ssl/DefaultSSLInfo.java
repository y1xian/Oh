package com.yyxnb.lib.java.http.ssl;

import com.yyxnb.lib.java.StrUtil;

import javax.net.ssl.SSLSocketFactory;

/**
 * 默认的全局SSL配置，当用户未设置相关信息时，使用默认设置，默认设置为单例模式。
 *
 * @author yyx
 */
public class DefaultSSLInfo {
	/**
	 * 默认信任全部的域名校验器
	 */
	public static final TrustAnyHostnameVerifier TRUST_ANY_HOSTNAME_VERIFIER;
	/**
	 * 默认的SSLSocketFactory，区分安卓
	 */
	public static final SSLSocketFactory DEFAULT_SSF;

	static {
		TRUST_ANY_HOSTNAME_VERIFIER = new TrustAnyHostnameVerifier();
		if (StrUtil.equalsIgnoreCase("dalvik", System.getProperty("java.vm.name"))) {
			// 兼容android低版本SSL连接
			DEFAULT_SSF = new AndroidSupportSSLFactory();
		} else {
			DEFAULT_SSF = new DefaultSSLFactory();
		}
	}
}
