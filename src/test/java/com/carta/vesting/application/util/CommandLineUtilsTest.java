package com.carta.vesting.application.util;

import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class CommandLineUtilsTest {

	@ParameterizedTest(name="#{index} - testParseFileNameArgument with Argument={0}")
	@MethodSource("com.carta.vesting.util.CommandLineUtilsTest#argumentListsProvider")
	void testParseFileNameArgument(List<String> nonOptionalArgs) {
		CommandLineUtils.parseFileNameArgument(nonOptionalArgs);
	}

	@ParameterizedTest(name="#{index} - testParseDateArgument with Argument={0}")
	@MethodSource("com.carta.vesting.util.CommandLineUtilsTest#argumentListsProvider")
	void testParseDateArgument(List<String> nonOptionalArgs) {
		CommandLineUtils.parseDateArgument(nonOptionalArgs);
	}

	static Stream<Arguments> argumentListsProvider() {
		return Stream.of( //
				arguments(List.of("exemplo.csv", "2022-01-01")), //
				arguments(List.of("flat.csv", "2022-02-04")), //
				arguments(List.of("otimo.csv", "2025-09-04")), //
				arguments(List.of("bom.csv", "2024-11-04")));
	}

}
