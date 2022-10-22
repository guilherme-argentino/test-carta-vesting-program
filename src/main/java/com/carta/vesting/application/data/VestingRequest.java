package com.carta.vesting.application.data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.carta.vesting.domain.Award;
import com.carta.vesting.domain.AwardOperation;
import com.carta.vesting.domain.Employee;

import lombok.Data;

@Data
public class VestingRequest {

	private String operation;
	private String employeeId;
	private String employeeName;
	private String awardId;
	private String awardDate;
	private long awardQuantity;

	public static String[] csvFieldNames() {
		return new String[] { "OPERATION", "EMPLOYEE ID", "EMPLOYEE NAME", "AWARD ID", "AWARD DATE", "QUANTITY" };
	}

	public Employee toEmployee() {
		// TODO Auto-generated method stub
		return Employee.builder() //
				.id(employeeId) //
				.fullName(employeeName) //
				.awards(List.of( //
						Award.builder() //
								.id(awardId) //
								.date(LocalDate.parse(awardDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"))) //
								.operation(AwardOperation.valueOf(operation)) //
								.quantity(awardQuantity) //
								.build())) //
				.build();
	}

}
