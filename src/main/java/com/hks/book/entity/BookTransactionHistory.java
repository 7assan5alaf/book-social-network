package com.hks.book.entity;

import com.hks.book.base.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class BookTransactionHistory extends BaseEntity {

	private boolean returned;
	private boolean returnedApproved;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;
	@ManyToOne 
	@JoinColumn(name="book_id")
	private Book book;

}
