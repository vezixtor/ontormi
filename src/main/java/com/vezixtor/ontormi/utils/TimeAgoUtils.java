package com.vezixtor.ontormi.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

public abstract class TimeAgoUtils implements TimeUnits {
	
	public static Map<String, String> getTimeAgo(long timeAgoMilliseconds) {
		Map<String, String> response = new HashMap<>();
		if (timeAgoMilliseconds <= MINUTE) {
			response.put("type", "SECOND");
			response.put("value", String.valueOf(timeAgoMilliseconds/SECOND));
		}
		else if (timeAgoMilliseconds <= HOUR) {
			response.put("type", "MINUTE");
			response.put("value", String.valueOf(timeAgoMilliseconds/MINUTE));
		}
		else if (timeAgoMilliseconds <= DAY) {
			response.put("type", "HOUR");
			response.put("value", String.valueOf(timeAgoMilliseconds/HOUR));
		}
		else if (timeAgoMilliseconds <= WEEK) {
			response.put("type", "DAY");
			response.put("value", String.valueOf(timeAgoMilliseconds/DAY));
		}
		else if (timeAgoMilliseconds <= MONTH) {
			response.put("type", "WEEK");
			response.put("value", String.valueOf(timeAgoMilliseconds/WEEK));
		}
		else if (timeAgoMilliseconds <= YEAR) {
			response.put("type", "MONTH");
			response.put("value", String.valueOf(timeAgoMilliseconds/MONTH));
		}
		else {
			response.put("type", "YEAR");
			response.put("value", String.valueOf(timeAgoMilliseconds/YEAR));
		}
		return response;
	}
	
	public static long toMillisecond(LocalDateTime createdAt) {
		long ago = createdAt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
		long now = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
		return (now - ago);
	}
	
	public static long toMillisecond(LocalDate date) {
		return toMillisecond(LocalDateTime.of(date, LocalTime.MIN));
	}
}
