package com.hks.book.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hks.book.entity.Feedback;

@Repository
public interface FeedBackRepo extends JpaRepository<Feedback, Long> {

	
	@Query("""
			select feedback 
			from Feedback feedback
			where feedback.book.id=:bookId
			""")
	Page<Feedback> findAllFeedbackByBook(Pageable pageable, Long bookId);

}
