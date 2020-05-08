package com.easypay.sdk.vo;

import java.io.Serializable;

/**
 * @ClassName: ApiHeader  
 * @Description: 请求时传入的头信息   
 */
public class ApiHeader extends SdkReqVO implements Serializable {
	private static final long serialVersionUID = 7290404120604370437L;

	@Override
	public String toString() {
		return "ApiHeader [" + super.toString() + "]";
	}

}
