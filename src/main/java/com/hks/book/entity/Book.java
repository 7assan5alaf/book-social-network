package com.hks.book.entity;

import java.util.List;

import com.hks.book.base.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Setter
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Book extends BaseEntity {
	
	private String title;
	private String authorName;
	private String isbn;
	private String synopsis;
	private String bookCover;
	private boolean archived;
	private boolean shareable;
	@ManyToOne
	@JoinColumn(name = "owner_id")
	private User owner;

	@OneToMany(mappedBy = "book")
	private List<Feedback>feedbacks;
	
	@OneToMany(mappedBy = "book")
	private List<BookTransactionHistory>histories;

	@Transient
	public double getRate() {
		if(this.feedbacks==null ||this.feedbacks.isEmpty())return 0.0;
		
		var rate=this.feedbacks.stream()
				.mapToDouble(Feedback::getNote)
				.average().orElse(0.0);
		
		return Math.round(rate*10.0)/10.0;
	}
	
}
