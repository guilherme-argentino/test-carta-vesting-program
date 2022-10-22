package com.carta.vesting.infrastructure.batch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.repeat.RepeatStatus;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SimpleSortFileTasklet<T> implements Tasklet {

	private final FlatFileItemReader<T> itemReader;
	private final FlatFileItemWriter<T> itemWriter;
	private final Comparator<T> comparator;

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

		T record;
		List<T> records = new ArrayList<T>();

		// Execution context
		ExecutionContext context = new ExecutionContext();

		try {
			itemReader.open(context);			
			itemWriter.setTransactional(false);
			itemWriter.open(context);

			while ((record = itemReader.read()) != null) {
				records.add(record);
			}

			// Efetiva a ordenação com o comparador passado
			Collections.sort(records, comparator);

			itemWriter.write(records);
		} catch (Exception e) {
			throw e;
		} finally {
			itemReader.close();
			itemWriter.close();
		}

		return RepeatStatus.FINISHED;

	}

}
