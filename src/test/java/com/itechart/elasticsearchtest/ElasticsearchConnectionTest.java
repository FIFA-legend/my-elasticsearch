package com.itechart.elasticsearchtest;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.itechart.elasticsearchtest.document.Person;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.elasticsearch.ElasticsearchContainer;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ElasticsearchConnectionTest {

	private static ElasticsearchContainer container;

	private static ElasticsearchClient elasticsearchClient;

	@BeforeAll
	public static void setUp() {
		container = Common.createContainer();
		container.start();

		elasticsearchClient = Common.createClient(container.getMappedPort(9200));
	}

	@Test
	public void elasticsearchSaveTest() {
		String id = "person-id";
		String name = "person-name";
		Person person = new Person();
		person.setId(id);
		person.setName(name);

		String index = "person";

		try {
			IndexResponse response = elasticsearchClient.index(i -> i
					.index(index)
					.id(id)
					.document(person)
			);

			assertThat(response.index()).isEqualTo(index);
			assertThat(response.id()).isEqualTo(id);

			SearchResponse<Person> search = elasticsearchClient.search(s -> s
							.index(index)
							.query(q -> q
									.match(t -> t
											.field("id")
											.query(id)
									)
							),
					Person.class);

			for (Hit<Person> hit: search.hits().hits()) {
				assertThat(person).isEqualTo(hit.source());
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@AfterAll
	public static void shutdown() {
		elasticsearchClient.shutdown();
		container.stop();
		container.close();
	}

}
