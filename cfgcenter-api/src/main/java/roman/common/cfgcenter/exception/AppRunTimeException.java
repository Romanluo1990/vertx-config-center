package roman.common.cfgcenter.exception;

public class AppRunTimeException extends RuntimeException {

	private static final int DEFAULT_ERR_CODE = 500;

	private final int errCode;

	public AppRunTimeException() {
		this(DEFAULT_ERR_CODE);
	}

	public AppRunTimeException(final int errCode) {
		this(errCode, (String) null);
	}

	public AppRunTimeException(final String message) {
		this(DEFAULT_ERR_CODE, message);
	}

	public AppRunTimeException(final int errCode, final String message) {
		this(errCode, message, null);
	}

	public AppRunTimeException(final Throwable cause) {
		this(DEFAULT_ERR_CODE, cause);
	}

	public AppRunTimeException(final int errCode, final Throwable cause) {
		this(errCode, (cause == null ? null : cause.toString()), cause);
	}

	public AppRunTimeException(final String message, final Throwable cause) {
		this(DEFAULT_ERR_CODE, message, cause);
	}

	public AppRunTimeException(final int errCode, final String message, final Throwable cause) {
		this(errCode, message, cause, true, true);
	}

	public AppRunTimeException(
			final String message, final Throwable cause, final boolean enableSuppression,
			final boolean writableStackTrace) {
		this(DEFAULT_ERR_CODE, message, cause, enableSuppression, writableStackTrace);
	}

	public AppRunTimeException(final int errCode, final String message, final Throwable cause,
			final boolean enableSuppression, final boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		this.errCode = errCode;
	}

	public int getExceptionCode() {
		return errCode;
	}
}
