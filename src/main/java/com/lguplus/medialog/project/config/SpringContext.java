package com.lguplus.medialog.project.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * POJO에서 스프링 빈을 얻기위한 용도.
 * 예) MyBeanClass myBeanInstance = SpringContext.getBean(MyBeanClass.class); 
 */
@Component
public class SpringContext implements ApplicationContextAware {
	private static ApplicationContext context;

	public static <T extends Object> T getBean(Class<T> beanClass) {
		return context.getBean(beanClass);
	}

	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		SpringContext.context = context;
	}
}
