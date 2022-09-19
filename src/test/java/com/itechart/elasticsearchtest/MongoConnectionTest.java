package com.itechart.elasticsearchtest;

import com.itechart.elasticsearchtest.document.Person;
import com.mongodb.client.MongoClients;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.ImmutableMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfig;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@EnableAutoConfiguration(exclude = EmbeddedMongoAutoConfiguration.class)
public class MongoConnectionTest {

    public MongodExecutable setupExecutable(String ip, int port) throws UnknownHostException {
        ImmutableMongodConfig mongoConfiguration = MongodConfig
                .builder()
                .version(Version.Main.PRODUCTION)
                .net(new Net(ip, port, Network.localhostIsIPv6()))
                .build();

        MongodStarter starter = MongodStarter.getDefaultInstance();
        return starter.prepare(mongoConfiguration);
    }

    public MongoTemplate setupTemplate(String ip, int port, String dbName) {
        return new MongoTemplate(MongoClients.create(String.format("mongodb://%s:%d", ip, port)), dbName);
    }

    @Test
    public void test() throws IOException {
        String ip = "localhost";
        int port = 27017;

        MongodExecutable executable = setupExecutable(ip, port);
        executable.start();

        MongoTemplate mongoTemplate = setupTemplate(ip, port, "test");

        Person person = new Person();
        person.setId("person-id");
        person.setName("person-name");

        mongoTemplate.save(person, "person");

        assertThat(mongoTemplate.findAll(Person.class, "person")).isEqualTo(List.of(person));

        executable.stop();
    }

}
