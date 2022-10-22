package com.carta.vesting.application.cli;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.carta.vesting.application.data.VestingResponse;
import com.carta.vesting.domain.Employee;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class EmployeeSumarizeProcessor implements ItemProcessor<Employee, List<VestingResponse>> {

	@Override
	public List<VestingResponse> process(Employee item) throws Exception {
		// TODO Auto-generated method stub
		return from(item);
	}

	/**
	 * Método responsável pela transformação do objeto do domínio para 
	 * saida do sistema e sumarização de <<QUANTITY>> por <<AWARD ID>>
	 * @param employee
	 * @return
	 */
	private List<VestingResponse> from(Employee employee) {
		return employee.getAwards().stream() //
				.map(award -> VestingResponse.builder() //
						.awardId(award.getId()) //
						.awardQuantity(award.getQuantity()) //
						.empId(employee.getId()) //
						.empName(employee.getFullName()) //
						.build())
				.collect(Collectors.collectingAndThen(
						        Collectors.groupingBy(VestingResponse::getAwardId, 
						        		Collectors.collectingAndThen(
										Collectors.reducing((a, b) -> sumMerge(a, b)), Optional::get)),
						m -> new ArrayList<>(m.values())));
	}

	private VestingResponse sumMerge(VestingResponse base, VestingResponse additional) {
		// TODO Auto-generated method stub
		return VestingResponse.builder() //
				.awardId(base.getAwardId()) //
				.awardQuantity(base.getAwardQuantity() + additional.getAwardQuantity()) //
				.empId(base.getEmpId()) //
				.empName(base.getEmpName()) //
				.build();
	}
}
