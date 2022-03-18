package com.lguplus.medialog.project.config;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.context.annotation.Bean;
import org.springframework.format.Formatter;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

@JsonComponent
public class DateFormatConfig {
    @Value("${spring.mvc.format.date-time:yyyyMMddHHmmss}")
    String dateTimeFormat;

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        return builder -> {
            builder.simpleDateFormat(dateTimeFormat);
            builder.serializers(new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(dateTimeFormat)));
            
            // JSON 데이터를 request body로 요청하는 경우 String -> LocalDateTime 변환에 사용
            // request parameter로 요청하는 경우는 spring.mvc.format.date-time 설정에 따라 변환된다.
            builder.deserializers(new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(dateTimeFormat)));
        };
    }
    
    @Bean
    public Formatter<Date> DateFormatter() { // request parameter로 String -> Date 변환에 사용
        return new Formatter<Date>() {
            @Override
            public Date parse(String text, Locale locale) throws ParseException {
            	SimpleDateFormat dt = new SimpleDateFormat(dateTimeFormat, locale); 
            	return dt.parse(text); 
            }

            @Override
            public String print(Date object, Locale locale) {
            	return object.toString(); // 반환값 의미없음. 위의 builder.simpleDateFormat 설정이 적용된다.
            }
        };
    }
    
}
