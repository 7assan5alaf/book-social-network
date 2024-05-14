package com.hks.book.mapper;

import java.util.Objects;

import org.springframework.stereotype.Service;

import com.hks.book.entity.Book;
import com.hks.book.entity.Feedback;
import com.hks.book.request.FeedbackRequest;
import com.hks.book.response.FeedbackResponse;


@Service
public class FeedbackMapper {
	
	public Feedback toFeedback(FeedbackRequest request) {
		
		return Feedback.builder()
				.book(Book.builder().id(request.bookId()).build())
				.comment(request.comment())
				.note(request.note())
				.build();
	}

	public FeedbackResponse toFeedbackResponse(Feedback feedback, Long userId) {
		
		return FeedbackResponse.builder()
				.comment(feedback.getComment())
				.note(feedback.getNote())
				.ownFeedback(Objects.equals(feedback.getCreatedBy(),userId))
				.build() ;
	}


	
	
	
}
