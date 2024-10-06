package com.example.talent_api.service;

import com.example.talent_api.dto.JobDto;
import com.example.talent_api.model.Job;
import com.example.talent_api.repository.JobRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobService {

	private final JobRepository jobRepository;

	public JobService(JobRepository jobRepository) {
		this.jobRepository = jobRepository;
	}

	public List<JobDto> getAllJobs() {
		return jobRepository.findAll().stream()
				.map(this::convertToDto)
				.collect(Collectors.toList());
	}

	public JobDto getJobById(Long id) {
		return jobRepository.findById(id)
				.map(this::convertToDto)
				.orElseThrow(() -> new RuntimeException("Job not found"));
	}

	private JobDto convertToDto(Job job) {
		JobDto jobDto = new JobDto();
		jobDto.setId(job.getId());
		jobDto.setTitle(job.getTitle());
		jobDto.setDescription(job.getDescription());
		jobDto.setRequirements(job.getRequirements());
		return jobDto;
	}
}