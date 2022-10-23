package com.carta.vesting.infrastructure.batch;


import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.PeekableItemReader;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.repeat.RepeatContext;
import org.springframework.batch.repeat.policy.SimpleCompletionPolicy;

import com.carta.vesting.application.data.VestingResponse;

public class AwardPeekingCompletionPolicyReader extends SimpleCompletionPolicy implements ItemReader<VestingResponse> {

    private PeekableItemReader<? extends VestingResponse> delegate;

    private VestingResponse currentReadItem = null;
	
	@Override
	public VestingResponse read()
			throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        currentReadItem = delegate.read();
        return currentReadItem;
	}

    @Override
    public RepeatContext start(final RepeatContext context) {
        return new ComparisonPolicyTerminationContext(context);
    }
    
    protected class ComparisonPolicyTerminationContext extends SimpleTerminationContext {

        public ComparisonPolicyTerminationContext(final RepeatContext context) {
            super(context);
        }

        @Override
        public boolean isComplete() {
            VestingResponse nextReadItem;
			try {
				nextReadItem = delegate.peek();

	            // logic to check if same awardID
	            if (nextReadItem.getAwardId().equals(currentReadItem.getAwardId())) {
	                return false;
	            }

	            return true;
			} catch (Exception e) {
				return true;
			}
        }
    }


}
