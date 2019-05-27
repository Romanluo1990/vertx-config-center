package roman.common.cfgcenter.controller.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import roman.common.cfgcenter.controller.ApiResponse;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

/**
 * 统一json返回处理
 */
@Slf4j
@ControllerAdvice(basePackages = "roman.common.cfgcenter.roman.common.cfgcenter.controller")
public class ResultJsonResponseAdvice implements ResponseBodyAdvice {

	@Override
	public boolean supports(final MethodParameter returnType, final Class converterType) {
		return true;
	}

	@Override
	public Object beforeBodyWrite(Object body, final MethodParameter returnType, final MediaType selectedContentType,
                                  final Class selectedConverterType, final ServerHttpRequest request, final ServerHttpResponse response) {
		if (body == null) {
			final Class returnClass = (Class) returnType.getGenericParameterType();
			if (returnClass.isArray() || Collection.class.isAssignableFrom(returnClass)) {
				body = Collections.emptyList();
			} else if (String.class.equals(returnClass)) {
				body = "";
			} else {
				body = new HashMap<>();
			}
		}
		return ApiResponse.success(body);
	}
}
