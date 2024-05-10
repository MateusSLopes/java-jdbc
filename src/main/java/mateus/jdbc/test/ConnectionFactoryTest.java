package mateus.jdbc.test;

import lombok.extern.log4j.Log4j2;
import mateus.jdbc.domain.Producer;
import mateus.jdbc.service.ProducerService;

import java.util.List;

@Log4j2
public class ConnectionFactoryTest {
    public static void main(String[] args) {
        Producer producer = ProducerService.findByNameAndInsertWhenNotFound("MMD");
        log.info("Producer inserted '{}'", producer);
        log.info(ProducerService.findByNameCallableStatement("Pierrot"));

        Producer p1 = Producer.builder().name("MS").build();
        Producer p2 = Producer.builder().name("MLP").build();
        ProducerService.saveTransactions(List.of(p1,p2));
    }
}
