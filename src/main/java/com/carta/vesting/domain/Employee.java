package com.carta.vesting.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Employee {

	private String id;
	private String fullName;
	private final List<Award> awards;

}
