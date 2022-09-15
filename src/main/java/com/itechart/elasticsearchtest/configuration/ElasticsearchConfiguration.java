package com.itechart.elasticsearchtest.configuration;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.RestHighLevelClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@ComponentScan(basePackages = "com.itechart.elasticsearchtest")
@EnableElasticsearchRepositories(basePackages = "com.itechart.elasticsearchtest.repository")
public class ElasticsearchConfiguration extends AbstractElasticsearchConfiguration {

    @Bean
    public RestClient lowLevelClient() {
        return RestClient.builder(
                new HttpHost("localhost", 9200)
        ).build();
    }

    @Bean
    @Override
    public RestHighLevelClient elasticsearchClient() {
        return new RestHighLevelClientBuilder(lowLevelClient())
                .setApiCompatibilityMode(true)
                .build();
    }

    @Bean
    public ElasticsearchClient javaClient() {
        ElasticsearchTransport transport = new RestClientTransport(
                lowLevelClient(),
                new JacksonJsonpMapper()
        );

        return new ElasticsearchClient(transport);
    }

}
