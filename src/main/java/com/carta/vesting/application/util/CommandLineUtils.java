package com.carta.vesting.application.util;

import java.io.File;
import java.util.List;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CommandLineUtils {	
	
	public static File parseFileNameArgument(List<String> nonOptionArgs) {
		return new File(nonOptionArgs.stream().findFirst()
				.orElseThrow(() -> new IllegalArgumentException("Arquivo não especificado")));
	}

	
	public static String parseDateArgument(List<String> nonOptionArgs) {
		return nonOptionArgs.stream().skip(1).findFirst()
				.orElseThrow(() -> new IllegalArgumentException("Data não especificada"));
	}

}
