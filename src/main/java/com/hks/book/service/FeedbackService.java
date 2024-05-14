package com.hks.book.service;

import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hks.book.entity.Book;
import com.hks.book.entity.Feedback;
import com.hks.book.entity.User;
import com.hks.book.exception.OperationPermittedException;
import com.hks.book.exception.RecordNotFound;
import com.hks.book.mapper.FeedbackMapper;
import com.hks.book.repository.BookRepo;
import com.hks.book.repository.FeedBackRepo;
import com.hks.book.request.FeedbackRequest;
import com.hks.book.response.FeedbackResponse;
import com.hks.book.response.PageResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FeedbackService {

	private final FeedBackRepo feedBackRepo;
	private final BookRepo bookRepo;
	private final FeedbackMapper feedbackMapper;

	public Long insertFeedback(FeedbackRequest request, Authentication auth) {
		Book book = bookRepo.findById(request.bookId())
				.orElseThrow(() -> new RecordNotFound("no found book id " + request.bookId()));
		User user = (User) auth.getPrincipal();
		if (book.isArchived() || !book.isShareable())
			throw new OperationPermittedException(
					"You cannot give a feedback for and archived or not shareable book");
		if (Objects.equals(book.getOwner().getId(), user.getId())) {
			throw new OperationPermittedException("You can not give feedback to your own book");
		}
		
		Feedback feedback=feedbackMapper.toFeedback(request);
		return feedBackRepo.save(feedback).getId();
	}

	@Transactional
	public PageResponse<FeedbackResponse> findAllFeedbacksOnlyBook(Long bookId,int page,int size, Authentication auth) {
		Book book = bookRepo.findById(bookId)
				.orElseThrow(() -> new RecordNotFound("no found book id " +bookId));
		User user = (User) auth.getPrincipal();
		Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
		Page<Feedback> feedback = feedBackRepo.findAllFeedbackByBook(pageable, book.getId());
		List<FeedbackResponse>feedbacks=feedback.stream()
				.map(f->feedbackMapper.toFeedbackResponse(f, user.getId()))
				.toList()
				;

		return new PageResponse<>(feedbacks, feedback.getNumber(),feedback.getSize(),
			feedback.getTotalElements(), feedback.getTotalPages(),feedback.isFirst(), feedback.isLast());
	}
	
	
	
}
