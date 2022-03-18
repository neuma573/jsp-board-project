package com.lguplus.medialog.project.base.auth;

import java.util.Arrays;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.lguplus.medialog.project.config.consts.Const;

/**
 * Apache에 reverse proxy를 설정하지 않고 tomcat에서 직접 CORS 기능을 제공하는 경우 사용한다.
 * 이 필터를 등록하면 http.cors() 설정을 한 것과 같다.
 */
public class CustomCorsFilter extends CorsFilter {
	
    public CustomCorsFilter() {
        super(configurationSource());
    }

    private static UrlBasedCorsConfigurationSource configurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
//        config.addAllowedOrigin("*");
        config.setAllowedOriginPatterns(Arrays.asList("*"));
        config.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type", "Origin", "Accept"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "OPTIONS", "DELETE", "PATCH", "HEAD"));
        config.setMaxAge(36000L);
        
        // https://stackoverflow.com/questions/4369987/jquery-getresponseheader-always-returns-undefined
        config.addExposedHeader(Const.ACCESS_TOKEN_HEADER);
        config.addExposedHeader(Const.REFRESH_TOKEN_HEADER);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", config);
        return source;
    }
}
