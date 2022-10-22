package com.carta.vesting;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.carta.vesting.application.util.CommandLineUtils;
import com.carta.vesting.infrastructure.config.BatchConfiguration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class VestingProgramApplication implements ApplicationRunner {

	@Autowired
	BatchConfiguration batchConfiguration;

	public static void main(String[] args) {
		SpringApplication.run(VestingProgramApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		log.debug("Application started with command-line arguments: {}", Arrays.toString(args.getSourceArgs()));
		log.debug("NonOptionArgs: {}", args.getNonOptionArgs());
		log.debug("OptionNames: {}", args.getOptionNames());

		for (String name : args.getOptionNames()) {
			log.debug("arg-" + name + "=" + args.getOptionValues(name));
		}

		// Bloco que verifica os argumentos antes de iniciar a aplicação
		try {

			log.info("Processamento será realizado com os seguintes argumentos: [{}] [{}]",
					CommandLineUtils.parseFileNameArgument(args.getNonOptionArgs()).getPath(), CommandLineUtils.parseDateArgument(args.getNonOptionArgs()));

		} catch (Exception e) {
			// TODO: handle exception
			System.err.print(e);

			throw e;
		}
	}

}
