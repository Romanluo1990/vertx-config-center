package roman.common.cfgcenter.exception;

public class AppParamsException extends AppRunTimeException {

	private static final int EXCEPTION_CODE = 400;

	public AppParamsException(final String message) {
		super(EXCEPTION_CODE, message);
	}

}
