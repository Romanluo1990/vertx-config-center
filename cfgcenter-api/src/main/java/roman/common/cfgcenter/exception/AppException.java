package roman.common.cfgcenter.exception;

public class AppException extends Exception {

	private static final int DEFAULT_ERR_CODE = 500;

	private final int errCode;

	public AppException() {
		this(DEFAULT_ERR_CODE);
	}

	public AppException(final int errCode) {
		this(errCode, (String) null);
	}

	public AppException(final String message) {
		this(DEFAULT_ERR_CODE, message);
	}

	public AppException(final int errCode, final String message) {
		this(errCode, message, null);
	}

	public AppException(final Throwable cause) {
		this(DEFAULT_ERR_CODE, cause);
	}

	public AppException(final int errCode, final Throwable cause) {
		this(errCode, (cause == null ? null : cause.toString()), cause);
	}

	public AppException(final String message, final Throwable cause) {
		this(DEFAULT_ERR_CODE, message, cause);
	}

	public AppException(final int errCode, final String message, final Throwable cause) {
		this(errCode, message, cause, true, true);
	}

	public AppException(final String message, final Throwable cause, final boolean enableSuppression,
			final boolean writableStackTrace) {
		this(DEFAULT_ERR_CODE, message, cause, enableSuppression, writableStackTrace);
	}

	public AppException(final int errCode, final String message, final Throwable cause,
			final boolean enableSuppression, final boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		this.errCode = errCode;
	}

	public int getExceptionCode() {
		return errCode;
	}
}
