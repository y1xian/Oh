package com.yyxnb.lib.java.convert.impl;

import com.yyxnb.lib.java.BooleanUtil;
import com.yyxnb.lib.java.StrUtil;
import com.yyxnb.lib.java.convert.AbstractConverter;

/**
 * 字符转换器
 *
 * @author yyx
 */
public class CharacterConverter extends AbstractConverter<Character> {
	private static final long serialVersionUID = 1L;

	@Override
	protected Character convertInternal(Object value) {
		if (value instanceof Boolean) {
			return BooleanUtil.toCharacter((Boolean) value);
		} else {
			final String valueStr = convertToStr(value);
			if (StrUtil.isNotBlank(valueStr)) {
				return valueStr.charAt(0);
			}
		}
		return null;
	}

}
