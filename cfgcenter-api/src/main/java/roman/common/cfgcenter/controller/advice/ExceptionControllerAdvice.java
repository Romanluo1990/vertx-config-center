package roman.common.cfgcenter.controller.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import roman.common.cfgcenter.controller.ApiResponse;
import roman.common.cfgcenter.exception.AppException;
import roman.common.cfgcenter.exception.AppParamsException;
import roman.common.cfgcenter.exception.AppRunTimeException;

import javax.servlet.http.HttpServletRequest;

/**
 * 统一处理controller类抛出的FinDcRuntimeException异常，转换成标准json输出格式信息
 * 参考 springboot Error Handling
 */
@Slf4j
@ControllerAdvice(basePackages = {"roman.common.cfgcenter.controller.ConfigController"})
public class ExceptionControllerAdvice extends ResponseEntityExceptionHandler {


	@ExceptionHandler(Throwable.class)
	@ResponseBody
	public ResponseEntity handleControllerException(final HttpServletRequest request, Throwable ex) {
		final HttpStatus status = getStatus(request);
		int statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();  // 默认500，服务器内部异常
		ex = findReallyException(ex);
		if (ex instanceof AppParamsException) {
			statusCode = ((AppParamsException) ex).getExceptionCode();
		} else if (ex instanceof AppRunTimeException) {
			statusCode = ((AppRunTimeException) ex).getExceptionCode();
		} else if (ex instanceof AppException) {
			statusCode = ((AppException) ex).getExceptionCode();
		}
		log.error("handle error", ex);
		return new ResponseEntity<>(ApiResponse.failure(statusCode, ex.getMessage()), status);
	}

	private Throwable findReallyException(final Throwable ex) {
		if (ex instanceof AppRunTimeException)
			return ex;
		final Throwable cause = ex.getCause();
		if (cause == null)
			return ex;
		return findReallyException(ex.getCause());
	}

	private HttpStatus getStatus(final HttpServletRequest request) {
		final Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
		if (statusCode == null) {
			return HttpStatus.OK;
		}
		return HttpStatus.valueOf(statusCode);
	}

}
