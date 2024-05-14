package com.hks.book.response;

import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(value = Include.NON_EMPTY)
public class ExceptionResponse {
	
	private Integer bussniessErrorCode;
	private String bussniessErrorDescription;
	private String error;
	private Set<String>validationError;
	private Map<String,String>errors;

}
