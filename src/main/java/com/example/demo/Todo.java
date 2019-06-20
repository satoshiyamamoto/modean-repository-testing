package com.example.demo;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "todos")
public class Todo {
	@Id @GeneratedValue
	private Long id;
	private String title;
	private boolean completed;
}
