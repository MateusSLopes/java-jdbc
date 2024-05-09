package mateus.jdbc.test;

import lombok.extern.log4j.Log4j2;
import mateus.jdbc.service.ProducerServiceRowSet;
import mateus.jdbc.domain.Producer;

import java.util.List;

@Log4j2
public class ConnectionTest02 {
    public static void main(String[] args) {
        List<Producer> producers = ProducerServiceRowSet.findByNameJdbcRowSet("Pierrot");
        log.info(producers);
    }
}
