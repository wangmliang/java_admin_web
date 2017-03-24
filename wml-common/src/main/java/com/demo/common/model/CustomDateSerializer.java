package com.demo.common.model;

import java.io.IOException;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.springframework.stereotype.Component;

/**
 * 
 * <pre>
 * <b>Title：</b>CustomDateSerializer.java<br/>
 * <b>@author： </b>WML<br/>
 * <b>@date：</b>2016年10月27日 下午2:29:01<br/>  
 * <b>Copyright (c) 2016 ASPire Tech.</b>   
 *  </pre>
 */
@Component
public class CustomDateSerializer extends JsonSerializer<Date> {
	@Override
	public void serialize(Date value, JsonGenerator jgen,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String formattedDate = formatter.format(value);
		jgen.writeString(formattedDate);
	}
}
