package mateus.jdbc.repository;

import lombok.extern.log4j.Log4j2;
import mateus.jdbc.conn.ConnectionFactory;
import mateus.jdbc.domain.Producer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public class ProducerRepository {
    public static void save(Producer producer) {
        String sql = "INSERT INTO `anime_store`.`producer` (`name`) VALUES ('%s');".formatted(producer.getName());
        try (Connection conn = ConnectionFactory.getConnection(); Statement stmt = conn.createStatement()) {
            int rowsAffected = stmt.executeUpdate(sql);
            log.info("Inserted producer in the database, rows affected '{}'", rowsAffected);
        } catch (SQLException e) {
            log.error("Error while trying to insert producer '{}'", producer.getName(), e);
        }
    }

    public static void delete(Integer id) {
        String sql = "DELETE FROM `anime_store`.`producer` WHERE (`id` = %d);".formatted(id);
        try (Connection conn = ConnectionFactory.getConnection(); Statement stmt = conn.createStatement()) {
            int rowsAffected = stmt.executeUpdate(sql);
            log.info("Deleted producer in the database, id '{}', rows affected '{}'", id, rowsAffected);
        } catch (SQLException e) {
            log.error("Error while trying to delete producer with id: {}", id, e);
        }
    }

    public static void update(Producer producer) {
        String sql = "UPDATE `anime_store`.`producer` SET `name` = '%s' WHERE (`id` = '%d');".formatted(producer.getName(), producer.getId());
        try (Connection conn = ConnectionFactory.getConnection(); Statement stmt = conn.createStatement()) {
            int rowsAffected = stmt.executeUpdate(sql);
            log.info("Updated producer in the database, id '{}', rows affected '{}'", producer.getId(), rowsAffected);
        } catch (SQLException e) {
            log.error("Error while trying to update producer with id: {}", producer.getId(), e);
        }
    }

    public static List<Producer> findAll() {
        log.info("Finding all producers");
        return findByName("");
    }

    public static List<Producer> findByName(String name) {
        String sql = "SELECT * FROM anime_store.producer WHERE NAME LIKE '%s';".formatted("%" + name + "%");
        List<Producer> producers = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Producer producer = Producer.builder()
                        .id(rs.getInt("id"))
                        .name(rs.getString("name"))
                        .build();
                producers.add(producer);
            }
        } catch (SQLException e) {
            log.error("Error while trying to find producer");
        }
        return producers;
    }

    public static void showProducerMetaData() {
        String sql = "SELECT * FROM anime_store.producer;";
        try (Connection conn = ConnectionFactory.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            ResultSetMetaData rsMetaData = rs.getMetaData();
            int columnMetaData = rsMetaData.getColumnCount();
            for (int i = 1; i <= columnMetaData; i++) {
                log.info("------------------------"); // To indicate where the column metadata starts
                log.info("Table name: {}", rsMetaData.getTableName(i));
                log.info("Column name: {}", rsMetaData.getColumnName(i));
                log.info("Column type: {}", rsMetaData.getColumnTypeName(i));
                log.info("Column size: {}", rsMetaData.getColumnDisplaySize(i));
            }
        } catch (SQLException e) {
            log.error("Error while trying to show producer metadata");
        }
    }
}
