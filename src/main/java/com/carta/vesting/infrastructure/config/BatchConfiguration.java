package com.carta.vesting.infrastructure.config;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Comparator;
import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import com.carta.vesting.application.cli.EmployeeSumarizeProcessor;
import com.carta.vesting.application.cli.JobCompletionNotificationListener;
import com.carta.vesting.application.data.VestingRequest;
import com.carta.vesting.application.data.VestingResponse;
import com.carta.vesting.application.util.CommandLineUtils;
import com.carta.vesting.application.util.WriteableResourceAdapter;
import com.carta.vesting.domain.Employee;
import com.carta.vesting.domain.service.EmployeeService;
import com.carta.vesting.infrastructure.batch.SimpleSortFileTasklet;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Autowired
	public ApplicationArguments applicationArguments;

	@Autowired
	public EmployeeSumarizeProcessor employeeSumarizeProcessor;

	@Autowired
	public EmployeeService employeeService;
	
	@Bean
	public File orderedSourceCsvFile() throws IOException {
		return File.createTempFile("ordered-", ".csv");
	}
	
	@Bean
	public FlatFileItemReader<VestingRequest> csvReader() {
		return new FlatFileItemReaderBuilder<VestingRequest>() //
				.name("vestingItemReader") //
				.resource(new FileSystemResource(CommandLineUtils
						.parseFileNameArgument(applicationArguments.getNonOptionArgs()).getAbsolutePath()))
				.delimited() //
				.names(VestingRequest.csvFieldNames()) //
				.fieldSetMapper(new BeanWrapperFieldSetMapper<VestingRequest>() {
					{
						setTargetType(VestingRequest.class);
					}
				}).build();
	}
	
	@Bean
	public FlatFileItemWriter<VestingRequest> orderedSourceCsvWriter(File orderedSourceCsvFile) {
		return new FlatFileItemWriterBuilder<VestingRequest>() //
				.name("sortedVestingItemWriter") //
				.shouldDeleteIfExists(true) //
				.resource(new FileSystemResource(orderedSourceCsvFile)) //
				.delimited() //
				.delimiter(",") //
				.names(VestingRequest.csvFieldNames()) //
				.build();
	}
	
	@Bean
	public Comparator<VestingRequest> comparator() {
		return Comparator //
				.comparing(VestingRequest::getEmployeeId) //
				.thenComparing(VestingRequest::getAwardId) //
				.thenComparing(VestingRequest::getAwardDate);
	}
	
	@Bean
	public SimpleSortFileTasklet<VestingRequest> tasklet(FlatFileItemReader<VestingRequest> csvReader, FlatFileItemWriter<VestingRequest> orderedSourceCsvWriter, Comparator<VestingRequest> comparator) {
		return new SimpleSortFileTasklet<>(csvReader,  orderedSourceCsvWriter, comparator);
	}

	@Bean
	public ItemWriter<Employee> empWriter() {
		return employees -> {
			log.info("Saving Employee Records: " + employees);
			employeeService.addAll(employees);
		};
	}

	@Bean
	public ItemWriter<List<VestingResponse>> sysOutWriter() {
		// Create writer instance
		FlatFileItemWriter<List<VestingResponse>> writer = new FlatFileItemWriter<>();

		// All job repetitions should "append" to same output file
		writer.setAppendAllowed(true);

		writer.setResource(new WriteableResourceAdapter() {

			@Override
			public OutputStream getOutputStream() throws IOException {
				return System.out;
			}

		});

		// Name field values sequence based on object properties
		writer.setLineAggregator(new DelimitedLineAggregator<List<VestingResponse>>() {
			{
				setDelimiter(",");
				setFieldExtractor(new BeanWrapperFieldExtractor<List<VestingResponse>>() {
					{
						setNames(VestingResponse.csvFieldNames());
					}
				});
			}
		});
		return writer;
	}

	@Bean
	public Job importUserJob(JobCompletionNotificationListener listener, Step stepLoad, Step stepSummarize) {
		return jobBuilderFactory.get("Summarize Vesting Job") //
				.incrementer(new RunIdIncrementer()) //
				.listener(listener) //
				.start(stepLoad) //
				.next(stepSummarize) //
				.build();
	}

	@Bean
	public Step stepLoad(SimpleSortFileTasklet<VestingRequest> tasklet) {
		return stepBuilderFactory.get("step load") //
				.tasklet(tasklet) //
				.build();
	}

	@Bean
	public Step stepSummarize(ItemReader<Employee> empReader, ItemWriter<List<VestingResponse>> sysOutWriter) {
		return stepBuilderFactory.get("step summarize") //
				.<Employee, List<VestingResponse>>chunk(10) //
				.reader(empReader) //
				.processor(employeeSumarizeProcessor) //
				.writer(sysOutWriter) //
				.build();

	}
}
