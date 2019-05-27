package roman.common.cfgcenter.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.text.SimpleDateFormat;
import java.util.List;

@Configuration
public class WebMvcConfig {

	@Bean
	public WebMvcConfigurer webMvcConfigurer() {
		final ObjectMapper objectMapper = createMapper();
		MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter(objectMapper);
		return new WebMvcConfigurer() {
			@Override
			public void configureMessageConverters(final List<HttpMessageConverter<?>> converters) {
				converters.add(mappingJackson2HttpMessageConverter);
			}

			@Override
			public void addResourceHandlers(final ResourceHandlerRegistry registry) {
				registry.addResourceHandler("swagger-ui.html")
						.addResourceLocations("classpath:/META-INF/resources/");
				registry.addResourceHandler("/template/**")
						.addResourceLocations("classpath:/static/template/");
			}

			@Override
			public void extendMessageConverters(final List<HttpMessageConverter<?>> converters) {
				converters.add(new FormHttpMessageConverter());
			}

			/** 配置servlet处理 */
			@Override
			public void configureDefaultServletHandling(final DefaultServletHandlerConfigurer configurer) {
				configurer.enable();
			}

		};
	}

	private ObjectMapper createMapper() {
		final ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		return objectMapper;
	}
}
