package mateus.jdbc.test;

import lombok.extern.log4j.Log4j2;
import mateus.jdbc.domain.Producer;
import mateus.jdbc.service.ProducerService;

@Log4j2
public class ConnectionFactoryTest {
    public static void main(String[] args) {
        Producer producer = Producer.builder()
                .name("NHK")
                .id(1).build();
        ProducerService.update(producer);
    }
}
