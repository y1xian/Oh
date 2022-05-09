package com.yyxnb.lib.andorid.log;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * 实现ILog
 *
 * @author yyx
 * @date 2022/4/6
 */
public class LoggerImpl implements ILog {

	/**
	 * Android一个日志条目不能超过4076字节，
	 * 这里设置以2000字节来计算
	 */
	private static final int CHUNK_SIZE = 2000;
	/**
	 * log 配置
	 */
	private static final LogConfig CONFIG = new LogConfig();
	/**
	 * 样式
	 */
	private static final char TOP_LEFT_CORNER = '┏';
	private static final char BOTTOM_LEFT_CORNER = '┗';
	private static final char MIDDLE_CORNER = '┠';
	private static final char HORIZONTAL_DOUBLE_LINE = '┃';
	private static final String DOUBLE_DIVIDER = "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━";
	private static final String SINGLE_DIVIDER = "──────────────────────────────────────────────";
	private static final String TOP_BORDER = TOP_LEFT_CORNER + DOUBLE_DIVIDER;
	private static final String BOTTOM_BORDER = BOTTOM_LEFT_CORNER + DOUBLE_DIVIDER;
	private static final String MIDDLE_BORDER = MIDDLE_CORNER + SINGLE_DIVIDER;

	/**
	 * 获取操作系统对应的换行符
	 * <p>
	 * java中的转义符"\r\n":
	 * windows下的文本文件换行符:\r\n
	 * linux/unix下的文本文件换行符:\r
	 * Mac下的文本文件换行符:\n
	 */
	public static String LINE_SEPARATOR = System.getProperty("line.separator");

	private final StringBuilder logStr = new StringBuilder();

	/**
	 * 初始化
	 */
	@Override
	public LogConfig init() {
		return CONFIG;
	}

	/**
	 * 返回最后一次格式化的打印结果样式
	 *
	 * @return 内容
	 */
	@Override
	public String getLastLog() {
		return logStr.toString();
	}

	@Override
	public void d(String message, Object... args) {
		log(Log.DEBUG, message, args);
	}

	@Override
	public void e(String message, Object... args) {
		e(null, message, args);
	}

	@Override
	public void e(Throwable throwable, String message, Object... args) {
		if (throwable != null && message != null) {
			message += " : " + throwable.toString();
		}
		if (throwable != null && message == null) {
			message = throwable.toString();
		}
		if (message == null) {
			message = "message/exception 为空！";
		}
		log(Log.ERROR, message, args);
	}

	@Override
	public void w(String message, Object... args) {
		log(Log.WARN, message, args);
	}

	@Override
	public void i(String message, Object... args) {
		log(Log.INFO, message, args);
	}

	@Override
	public void v(String message, Object... args) {
		log(Log.VERBOSE, message, args);
	}

	@Override
	public void a(String message, Object... args) {
		log(Log.ASSERT, message, args);
	}

	/**
	 * 格式化json
	 */
	@Override
	public void json(String json) {
		if (TextUtils.isEmpty(json)) {
			d("json 数据为空！");
			return;
		}
		try {
			String message = "";
			if (json.startsWith("{")) {
				JSONObject jo = new JSONObject(json);
				message = jo.toString(4);
			} else if (json.startsWith("[")) {
				JSONArray ja = new JSONArray(json);
				message = ja.toString(4);
			}
			d(message);
		} catch (Exception e) {
			e(e.getCause().getMessage() + LINE_SEPARATOR + json);
		}
	}

	/**
	 * 格式化xml
	 */
	@Override
	public void xml(String xml) {
		if (TextUtils.isEmpty(xml)) {
			d("xml 数据为空！");
			return;
		}
		try {
			Source xmlInput = new StreamSource(new StringReader(xml));
			StreamResult xmlOutput = new StreamResult(new StringWriter());
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			transformer.transform(xmlInput, xmlOutput);
			String message = xmlOutput.getWriter().toString().replaceFirst(">", ">" + LINE_SEPARATOR);
			d(message);
		} catch (TransformerException e) {
			e(e.getCause().getMessage() + LINE_SEPARATOR + xml);
		}
	}

	/**
	 * 格式化Map集合
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void map(Map<?, ?> map) {
		if (map != null) {
			StringBuilder stringBuilder = new StringBuilder();
			for (Object entry : map.entrySet()) {
				stringBuilder.append("[key] → ");
				stringBuilder.append(((Map.Entry) entry).getKey());
				stringBuilder.append(",[value] → ");
				stringBuilder.append(((Map.Entry) entry).getValue());
				stringBuilder.append(LINE_SEPARATOR);
			}
			d(stringBuilder.toString());
		}
	}

	/**
	 * 格式化List集合
	 */
	@Override
	public void list(List<?> list) {
		if (list != null) {
			StringBuilder stringBuilder = new StringBuilder();
			for (int i = 0; i < list.size(); i++) {
				stringBuilder.append("[").append(i).append("] → ");
				stringBuilder.append(list.get(i));
				stringBuilder.append(LINE_SEPARATOR);
			}
			d(stringBuilder.toString());
		}
	}

	/**
	 * 同步日志打印顺序
	 */
	private synchronized void log(int priority, String msg, Object... args) {
		logStr.delete(0, logStr.length());
		String message = args.length == 0 ? msg : String.format(msg, args);
		if (!CONFIG.isShowLog()) {
			return;
		}
		if (CONFIG.isSimpleLog()) {
			//得到系统的默认字符集的信息字节（UTF-8）
			byte[] bytes = message.getBytes();
			int length = bytes.length;
			if (length <= CHUNK_SIZE) {
				logContent(priority, message);
				return;
			}
			for (int i = 0; i < length; i += CHUNK_SIZE) {
				int count = Math.min(length - i, CHUNK_SIZE);
				//创建系统的默认字符集的一个新的字符串（UTF-8）
				logContent(priority, new String(bytes, i, count));
			}
		} else {
			logChunk(priority, TOP_BORDER);
			if (CONFIG.isShowThreadInfo()) {
				//打印线程
				getStackInfo(priority);
			}
			//得到系统的默认字符集的信息字节（UTF-8）
			byte[] bytes = message.getBytes();
			int length = bytes.length;
			if (length <= CHUNK_SIZE) {
				logContent(priority, message);
				logChunk(priority, BOTTOM_BORDER);
				return;
			}
			for (int i = 0; i < length; i += CHUNK_SIZE) {
				int count = Math.min(length - i, CHUNK_SIZE);
				//创建系统的默认字符集的一个新的字符串（UTF-8）
				logContent(priority, new String(bytes, i, count));
			}
			logChunk(priority, BOTTOM_BORDER);
		}

	}

	/**
	 * 日志主体
	 *
	 * @param priority 优先级
	 * @param chunk    内容
	 */
	private void logContent(int priority, String chunk) {
		String[] lines = chunk.split(LINE_SEPARATOR);
		for (String line : lines) {
			logChunk(priority, CONFIG.isSimpleLog() ? line : (HORIZONTAL_DOUBLE_LINE + " " + line));
		}
	}

	/**
	 * 打印
	 *
	 * @param priority 优先级
	 * @param chunk    内容
	 */
	private void logChunk(int priority, String chunk) {
		logStr.append(LINE_SEPARATOR);
		logStr.append(chunk);
		String tag = CONFIG.getTag();
		switch (priority) {
			case Log.ERROR:
				Log.e(tag, chunk);
				break;
			case Log.INFO:
				Log.i(tag, chunk);
				break;
			case Log.VERBOSE:
				Log.v(tag, chunk);
				break;
			case Log.WARN:
				Log.w(tag, chunk);
				break;
			case Log.ASSERT:
				Log.wtf(tag, chunk);
				break;
			case Log.DEBUG:
			default:
				Log.d(tag, chunk);
				break;
		}
	}

	/**
	 * 打印堆栈信息.
	 */
	private void getStackInfo(int priority) {
		logChunk(priority, HORIZONTAL_DOUBLE_LINE + "[Thread] → " + Thread.currentThread().getName());
		logChunk(priority, MIDDLE_BORDER);
		String str = "";
		StackTraceElement[] traces = Thread.currentThread().getStackTrace();

		for (StackTraceElement element : traces) {
			StringBuilder perTrace = new StringBuilder(str);
			if (element.isNativeMethod()) {
				continue;
			}
			String className = element.getClassName();
			if (className.startsWith("android.")
					|| className.contains("com.android")
					|| className.contains("androidx.")
					|| className.contains("java.lang")
			) {
				continue;
			}
			perTrace.append(element.getClassName())
					.append('.')
					.append(element.getMethodName())
					.append("  (")
					.append(element.getFileName())
					.append(':')
					.append(element.getLineNumber())
					.append(")");
			str += "  ";
			logContent(priority, perTrace.toString());
		}
		logChunk(priority, MIDDLE_BORDER);
	}
}
