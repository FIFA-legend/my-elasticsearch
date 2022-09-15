package com.itechart.elasticsearchtest;

import com.itechart.elasticsearchtest.document.Person;
import com.itechart.elasticsearchtest.repository.PersonRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ElasticsearchConnectionTest {

	@Autowired
	private PersonRepository personRepository;

	@Test
	public void elasticsearchSaveTest() {
		String id = "person-id";
		String name = "person-name";
		Person person = new Person();
		person.setId(id);
		person.setName(name);

		personRepository.save(person);
		Person copy = personRepository.findById(id).orElse(null);
		assertThat(person).isEqualTo(copy);
	}

}
