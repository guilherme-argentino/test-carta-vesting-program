package com.carta.vesting.application.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class VestingResponse {

	private String empId;
	private String empName;
	private String awardId;
	private long awardQuantity;

	public static String[] csvFieldNames() {
		return new String[] { "EMPLOYEE ID", "EMPLOYEE NAME", "AWARD ID", "QUANTITY" };
	}

}
