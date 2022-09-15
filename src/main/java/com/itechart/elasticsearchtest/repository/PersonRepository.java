package com.itechart.elasticsearchtest.repository;

import com.itechart.elasticsearchtest.document.Person;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends ElasticsearchRepository<Person, String> {
}
