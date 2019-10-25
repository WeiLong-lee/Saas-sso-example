
package com.pig4cloud.pigx.auth.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pig4cloud.pigx.auth.component.PigxAuth2ExceptionSerializer;
import lombok.Getter;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

/**
 * @author lengleng
 * @date 2018/7/8
 * 自定义OAuth2Exception
 */
@JsonSerialize(using = PigxAuth2ExceptionSerializer.class)
public class PigxAuth2Exception extends OAuth2Exception {
	@Getter
	private String errorCode;

	public PigxAuth2Exception(String msg) {
		super(msg);
	}

	public PigxAuth2Exception(String msg, String errorCode) {
		super(msg);
		this.errorCode = errorCode;
	}
}
