package mateus.jdbc.repository;

import lombok.extern.log4j.Log4j2;
import mateus.jdbc.conn.ConnectionFactory;
import mateus.jdbc.domain.Producer;

import javax.sql.rowset.JdbcRowSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static mateus.jdbc.repository.ProducerRepository.getProducer;

@Log4j2
public class ProducerRepositoryRowSet {
    public static List<Producer> findByNameJdbcRowSet(String name) {
        String sql = "SELECT * FROM anime_store.producer WHERE name LIKE ?;";
        List<Producer> producers = new ArrayList<Producer>();
        try (JdbcRowSet jrs = ConnectionFactory.getJdbcRowSet()) {
            jrs.setCommand(sql);
            jrs.setString(1, name);
            jrs.execute(); // Execute is only for read
            while (jrs.next()) {
                Producer producer = getProducer(jrs);
                producers.add(producer);
            }
        } catch (SQLException e) {
            log.error("Error trying to find producer by name", e);
        }
        return producers;
    }
}
