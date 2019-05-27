package roman.common.cfgcenter.controller;

import org.springframework.http.HttpStatus;

/**
 * API调用的通用结果
 * Created by roman.luo on 2017/7/18.
 */
public class ApiResponse<T> {

	private int code;

	private String msg;

	private T data;

	public ApiResponse() {
		this(null);
	}

	public ApiResponse(final T data) {
		this(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), data);
	}

	public ApiResponse(final int code, final String msg, final T data) {
		this.code = code;
		this.msg = msg;
		this.data = data;
	}

	public static ApiResponse success(final Object data) {
		return new ApiResponse<>(data);
	}

	public static ApiResponse failure(final String message) {
		return failure(HttpStatus.INTERNAL_SERVER_ERROR.value(), message);
	}

	public static ApiResponse failure(final int statusCode, final String message) {
		return new ApiResponse<>(statusCode, message, null);
	}

	public int getCode() {
		return code;
	}

	public void setCode(final int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(final String msg) {
		this.msg = msg;
	}

	public T getData() {
		return data;
	}

	public void setData(final T data) {
		this.data = data;
	}

}
