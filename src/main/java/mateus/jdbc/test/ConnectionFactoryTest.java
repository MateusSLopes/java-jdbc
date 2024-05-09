package mateus.jdbc.test;

import lombok.extern.log4j.Log4j2;
import mateus.jdbc.domain.Producer;
import mateus.jdbc.service.ProducerService;

@Log4j2
public class ConnectionFactoryTest {
    public static void main(String[] args) {
        Producer producer = ProducerService.findByNameAndInsertWhenNotFound("MMD");
        log.info("Producer inserted '{}'", producer);
        log.info(ProducerService.findByNameCallableStatement("Pierrot"));
    }
}
