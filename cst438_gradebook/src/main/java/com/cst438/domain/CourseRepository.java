package com.cst438.domain;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface CourseRepository extends CrudRepository <Course, Integer> {

	List<Course> verifyCourseInstructor(int course_id, String email);


	String findByEmail(String email);


	String findById(String email);




}
