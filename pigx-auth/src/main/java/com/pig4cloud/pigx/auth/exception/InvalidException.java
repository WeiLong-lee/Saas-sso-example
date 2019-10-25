
package com.pig4cloud.pigx.auth.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pig4cloud.pigx.auth.component.PigxAuth2ExceptionSerializer;

/**
 * @author lengleng
 * @date 2018/7/8
 */
@JsonSerialize(using = PigxAuth2ExceptionSerializer.class)
public class InvalidException extends PigxAuth2Exception {

	public InvalidException(String msg, Throwable t) {
		super(msg);
	}

	@Override
	public String getOAuth2ErrorCode() {
		return "invalid_exception";
	}

	@Override
	public int getHttpErrorCode() {
		return 426;
	}

}
