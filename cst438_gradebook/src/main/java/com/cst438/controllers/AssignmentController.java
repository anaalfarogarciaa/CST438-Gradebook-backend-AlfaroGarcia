package com.cst438.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cst438.domain.Assignment;
import com.cst438.domain.AssignmentAdd;
import com.cst438.domain.AssignmentListDTO;
import com.cst438.domain.AssignmentRepository;

@RestController
@CrossOrigin(origins = {"http://localhost:3000","http://localhost:3001"})
public class AssignmentController {

	@Autowired
	AssignmentRepository assignmentRepository;

	@PostMapping("/assignment")
	public void addAssignment (@RequestBody AssignmentAdd a) {

		Assignment as = new Assignment();
		as.setName(a.getAssignmentName());
		as.setDueDate(a.getDueDate());

		assignmentRepository.save(as);
	}

	@PostMapping("/assignment/{id}/delete")
	public void deleteAssignment (@PathVariable("id") Integer assignmentId ) {

		String email = "dwisneski@csumb.edu";  // user name (should be instructor's email) 
		checkAssignment(assignmentId, email);  // check that user name matches instructor email of the course.

		Assignment as = assignmentRepository.findById(assignmentId).orElse(null);
		if (as == null) {
			throw new ResponseStatusException( HttpStatus.BAD_REQUEST, "Invalid grade primary key. "+assignmentId);
		}
		assignmentRepository.delete(as);


	}

	@PutMapping("/assignment/{id}/rename")
	public void updateAssignmentName (@PathVariable("id") Integer assignmentId, @RequestParam("name") String name) {

		String email = "dwisneski@csumb.edu";  // user name (should be instructor's email) 
		checkAssignment(assignmentId, email);  // check that user name matches instructor email of the course.

		Assignment as = assignmentRepository.findById(assignmentId).orElse(null);
		if (as == null) {
			throw new ResponseStatusException( HttpStatus.BAD_REQUEST, "Invalid grade primary key. "+assignmentId);
		}

		as.setName(name);

		assignmentRepository.save(as);

	}

	private Assignment checkAssignment(int assignmentId, String email) {
		// get assignment 
		Assignment assignment = assignmentRepository.findById(assignmentId).orElse(null);
		if (assignment == null) {
			throw new ResponseStatusException( HttpStatus.BAD_REQUEST, "Assignment not found. "+assignmentId );
		}
		// check that user is the course instructor
		if (!assignment.getCourse().getInstructor().equals(email)) {
			throw new ResponseStatusException( HttpStatus.UNAUTHORIZED, "Not Authorized. " );
		}

		return assignment;
	}

}
