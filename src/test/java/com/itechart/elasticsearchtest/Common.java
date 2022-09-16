package com.itechart.elasticsearchtest;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.testcontainers.containers.wait.strategy.HttpWaitStrategy;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.utility.DockerImageName;

public class Common {

    public static ElasticsearchContainer createContainer() {
        DockerImageName name = DockerImageName.parse("docker.io/library/elasticsearch:8.4.1")
                .asCompatibleSubstituteFor("docker.elastic.co/elasticsearch/elasticsearch");

        return new ElasticsearchContainer(name)
                .withEnv("discovery.type", "single-node")
                .withEnv("xpack.security.enabled", "false")
                .withExposedPorts(9200, 9300)
                .waitingFor(new HttpWaitStrategy().forPort(9200).forPath("/_cluster/health"));
    }

    public static ElasticsearchClient createClient(int port) {
        RestClient client = RestClient.builder(
                new HttpHost("localhost", port)
        ).build();

        ElasticsearchTransport transport = new RestClientTransport(
                client,
                new JacksonJsonpMapper()
        );

        return new ElasticsearchClient(transport);
    }

}
