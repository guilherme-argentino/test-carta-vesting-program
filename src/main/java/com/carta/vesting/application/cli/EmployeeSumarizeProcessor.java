package com.carta.vesting.application.cli;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.carta.vesting.application.data.VestingRequest;
import com.carta.vesting.application.data.VestingResponse;

import lombok.NoArgsConstructor;

@Component
@NoArgsConstructor
public class EmployeeSumarizeProcessor implements ItemProcessor<VestingRequest, VestingResponse> {
	
	private VestingResponse vestingResponse = null;

	/**
	 * Método responsável pela transformação do objeto do domínio para 
	 * saida do sistema e sumarização de <<QUANTITY>> por <<AWARD ID>>
	 * @param vestingRequest
	 * @return
	 */
	@Override
	public VestingResponse process(VestingRequest item) throws Exception {
		if(vestingResponse == null) {
			vestingResponse = de(item);			
		} else {
			vestingResponse.setAwardQuantity(vestingResponse.getAwardQuantity() + item.getAwardQuantity());			
		}
		return vestingResponse;
	}

	private VestingResponse de(VestingRequest item) {
		return VestingResponse.builder() //
				.awardId(item.getAwardId())
				.awardQuantity(item.getAwardQuantity())
				.empId(item.getEmployeeId())
				.empName(item.getEmployeeName())
				.build();
	}
	
}
