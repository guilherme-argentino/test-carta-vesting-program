package com.carta.vesting.infrastructure.config;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import com.carta.vesting.domain.Employee;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class InMemoryItemReader implements ItemReader<Employee> {

	private final Iterator<Employee> data;

	@Override
	public Employee read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		try {
			Employee next = data.next();
			log.debug("Reading {}", next);
			return next;
		} catch (NoSuchElementException e) {
			return null;
		}
	}
}