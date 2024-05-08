package mateus.jdbc.test;

import lombok.extern.log4j.Log4j2;
import mateus.jdbc.domain.Producer;
import mateus.jdbc.service.ProducerService;

import java.util.List;

@Log4j2
public class ConnectionFactoryTest {
    public static void main(String[] args) {
        Producer producer = Producer.builder()
                .name("mmd")
                .build();
        ProducerService.save(producer);

        List<Producer> producers = ProducerService.findAll();
        log.info("Producers found '{}'", producers);

        ProducerService.showTypeScrollWorking();
    }
}
