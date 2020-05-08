package com.easypay.sdk.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
	public static final String SENDTIME = "yyyy-MM-dd HH:mm:ss.SSS";
	public static final String SENDDATE = "yyyyMMdd";

	public static String getApiSendTime() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(SENDTIME);
		Date date = new Date();
		return simpleDateFormat.format(date);
	}

	public static String geth5SendTime() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(SENDTIME);
		Date date = new Date();
		return simpleDateFormat.format(date);
	}

	public static String getNowDate() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(SENDDATE);
		Date date = new Date();
		return simpleDateFormat.format(date);
	}

	public static String formatDate(String format, Date date) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		return simpleDateFormat.format(date);
	}
}
