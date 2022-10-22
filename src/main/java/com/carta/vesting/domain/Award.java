package com.carta.vesting.domain;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Award {
	
	private AwardOperation operation;
	private String id;
	private LocalDate date;
	private long quantity;

}
