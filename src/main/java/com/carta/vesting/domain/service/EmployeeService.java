package com.carta.vesting.domain.service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.carta.vesting.domain.Award;
import com.carta.vesting.domain.AwardOperation;
import com.carta.vesting.domain.Employee;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class EmployeeService {

	/**
	 * para uma escala maior que 100mil registros, o ideal é construir uma
	 * serialização em um sistema noSQL
	 */
	private final Set<Employee> employees = ConcurrentHashMap.newKeySet();

	public void addEmployee(Employee employee) {
		if (!employees.contains(employee)) {
			if (!employees.add(employee)) {
				Employee found = employees.stream() //
						.filter(inserted -> inserted.equals(employee)) //
						.findFirst() //
						.orElseThrow();
				found.getAwards().add(employee.getAwards().get(0));
			}
		}
	}

	public long getAwardSum(Employee employee) {
		return getAwardSum(employee.getAwards().stream());
	}

	/**
	 * Sumariza as operações somando em caso de VEST ou subtraindo em caso de CANCEL
	 * 
	 * @param awards
	 * @return
	 */
	public long getAwardSum(Stream<Award> awards) {
		return awards //
				.map(award -> award.getOperation().equals(AwardOperation.VEST) //
						? award.getQuantity() //
						: award.getQuantity() * -1)
				.reduce(0L, Long::sum).longValue();
	}

	public Set<Employee> getEmployees() {
		return employees;
	}

	public void addAll(List<? extends Employee> employees) {
		employees.stream().forEach(employee -> addEmployee(employee));
	}

}
