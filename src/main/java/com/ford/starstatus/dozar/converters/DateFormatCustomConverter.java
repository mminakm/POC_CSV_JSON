package com.ford.starstatus.dozar.converters;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.commons.lang3.StringUtils;

public class DateFormatCustomConverter {
	
	public String converterFormat1(String input) throws ParseException {
		if( StringUtils.isNotBlank(input) ) {
			DateFormat from = new SimpleDateFormat("yyyyMMdd");
			DateFormat to = new SimpleDateFormat("ddMMyyyy"); 
			return to.format(from.parse(input));
		}else {
			return "";
		}
	}
}
