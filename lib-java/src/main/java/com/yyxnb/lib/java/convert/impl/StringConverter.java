package com.yyxnb.lib.java.convert.impl;

import com.yyxnb.lib.java.CharsetUtil;
import com.yyxnb.lib.java.XmlUtil;
import com.yyxnb.lib.java.convert.AbstractConverter;
import com.yyxnb.lib.java.convert.ConvertException;
import com.yyxnb.lib.java.io.IoUtil;

import java.io.InputStream;
import java.io.Reader;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.TimeZone;

/**
 * 字符串转换器，提供各种对象转换为字符串的逻辑封装
 *
 * @author yyx
 */
public class StringConverter extends AbstractConverter<String> {
	private static final long serialVersionUID = 1L;

	@Override
	protected String convertInternal(Object value) {
		if (value instanceof TimeZone) {
			return ((TimeZone) value).getID();
		} else if (value instanceof org.w3c.dom.Node) {
			return XmlUtil.toStr((org.w3c.dom.Node) value);
		} else if (value instanceof Clob) {
			return clobToStr((Clob) value);
		} else if (value instanceof Blob) {
			return blobToStr((Blob) value);
//		} else if (value instanceof Type) {
//			return ((Type) value).getTypeName();
		}

		// 其它情况
		return convertToStr(value);
	}

	/**
	 * Clob字段值转字符串
	 *
	 * @param clob {@link Clob}
	 * @return 字符串
	 */
	private static String clobToStr(Clob clob) {
		Reader reader = null;
		try {
			reader = clob.getCharacterStream();
			return IoUtil.read(reader);
		} catch (SQLException e) {
			throw new ConvertException(e);
		} finally {
			IoUtil.close(reader);
		}
	}

	/**
	 * Blob字段值转字符串
	 *
	 * @param blob {@link Blob}
	 * @return 字符串
	 */
	private static String blobToStr(Blob blob) {
		InputStream in = null;
		try {
			in = blob.getBinaryStream();
			return IoUtil.read(in, CharsetUtil.CHARSET_UTF_8);
		} catch (SQLException e) {
			throw new ConvertException(e);
		} finally {
			IoUtil.close(in);
		}
	}
}
