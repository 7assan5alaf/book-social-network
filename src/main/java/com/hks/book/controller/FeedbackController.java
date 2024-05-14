package com.hks.book.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hks.book.request.FeedbackRequest;
import com.hks.book.service.FeedbackService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("feedbacks")
@Tag(name = "Feedback")
public class FeedbackController {

	private final FeedbackService feedbackService;
    @PostMapping("")
	public ResponseEntity<Long> insertFeedback(@Valid @RequestBody FeedbackRequest request, Authentication auth) {
		return ResponseEntity.ok(feedbackService.insertFeedback(request, auth));
	}
    
    @GetMapping("/book/{bookId}")
    public ResponseEntity<?>findAllFeedbackByUser(
    		@PathVariable Long bookId,
    		@RequestParam(defaultValue = "0", required = false) int page,
			@RequestParam(defaultValue = "10", required = false) int size, Authentication auth
    		){
    	return ResponseEntity.ok(feedbackService.findAllFeedbacksOnlyBook(bookId, page, size, auth));
    	
    }
    

}
