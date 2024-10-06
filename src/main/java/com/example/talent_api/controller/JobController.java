package com.example.talent_api.controller;

import com.example.talent_api.dto.JobDto;
import com.example.talent_api.service.JobService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

	private final JobService jobService;

	public JobController(JobService jobService) {
		this.jobService = jobService;
	}

	@GetMapping
	public ResponseEntity<List<JobDto>> getAllJobs() {
		return ResponseEntity.ok(jobService.getAllJobs());
	}

	@GetMapping("/{id}")
	public ResponseEntity<JobDto> getJobById(@PathVariable Long id) {
		return ResponseEntity.ok(jobService.getJobById(id));
	}
}