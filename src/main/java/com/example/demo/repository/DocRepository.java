package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.DocDto;

public interface DocRepository extends JpaRepository<DocDto, Integer> {

}
