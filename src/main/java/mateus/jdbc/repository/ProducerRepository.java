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

    public static void updatePreparedStatement(Producer producer) {
        try (Connection conn = ConnectionFactory.getConnection(); var ps = preparedStatementUpdate(conn,producer)) {
            int rowsAffected = ps.executeUpdate();
            log.info("Updated producer in the database, id '{}', rows affected '{}'", producer.getId(), rowsAffected);
        } catch (SQLException e) {
            log.error("Error while trying to update producer with id: {}", producer.getId(), e);
        }
    }

    public static PreparedStatement preparedStatementUpdate(Connection conn, Producer producer) throws SQLException{
        String sql = "UPDATE `anime_store`.`producer` SET `name` = ? WHERE (`id` = ?);";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, producer.getName());
        ps.setInt(2, producer.getId());
        return ps;
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

    public static void showDriverMetaData() {
        log.info("Showing driver metadata");
        try (Connection conn = ConnectionFactory.getConnection()) {
            DatabaseMetaData dbMetaData = conn.getMetaData();
            var updatable = "And supports CONCUR_UPDATABLE";

            if (dbMetaData.supportsResultSetType(ResultSet.TYPE_FORWARD_ONLY)) {
                log.info("Supports TYPE_FORWARD_ONLY");
                if (dbMetaData.supportsResultSetConcurrency(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE)) {
                    log.info(updatable);
                }
            }

            if (dbMetaData.supportsResultSetType(ResultSet.TYPE_SCROLL_INSENSITIVE)) {
                log.info("Supports TYPE_SCROLL_INSENSITIVE");
                if (dbMetaData.supportsResultSetConcurrency(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
                    log.info(updatable);
                }
            }

            if (dbMetaData.supportsResultSetType(ResultSet.TYPE_SCROLL_SENSITIVE)) {
                log.info("Supports TYPE_SCROLL_SENSITIVE");
                if (dbMetaData.supportsResultSetConcurrency(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
                    log.info(updatable);
                }
            }
        } catch (SQLException e) {
            log.error("Error trying to show driver metadata");
        }
    }

    public static void showTypeScrollWorking() {
        String sql = "SELECT * FROM anime_store.producer;";
        try (Connection conn = ConnectionFactory.getConnection();
             Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
             ResultSet rs = stmt.executeQuery(sql)) {
            log.info("Last row? '{}'", rs.last());
            log.info("Row number '{}'", rs.getRow());
            log.info(getProducer(rs));
            //
            log.info("First row? '{}'", rs.first());
            log.info("Row number '{}'", rs.getRow());
            log.info(getProducer(rs));

            rs.afterLast();
            while (rs.previous()) {
                log.info(getProducer(rs));
            }
        } catch (SQLException e) {
            log.error("Error while trying to show type scroll working");
        }
    }

    public static List<Producer> findAll() {
        log.info("Finding all producers");
        return findByName("");
    }

    public static List<Producer> findByName(String name) {
        log.info("Finding by name");
        String sql = "SELECT * FROM anime_store.producer WHERE NAME LIKE '%s';".formatted("%" + name + "%");
        List<Producer> producers = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                producers.add(getProducer(rs));
            }
        } catch (SQLException e) {
            log.error("Error while trying to find producer");
        }
        return producers;
    }

    public static List<Producer> findByNamePreparedStatement(String name) {
        log.info("Finding by name");
        List<Producer> producers = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             var ps = preparedStatementFindByName(conn, name);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                producers.add(getProducer(rs));
            }
        } catch (SQLException e) {
            log.error("Error while trying to find producer");
        }
        return producers;
    }

    public static PreparedStatement preparedStatementFindByName(Connection conn, String name) throws SQLException {
        String sql = "SELECT * FROM anime_store.producer WHERE NAME LIKE ?;";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, name);
        return ps;
    }

    public static List<Producer> findByNameAndUpdateToUpperCase(String name) {
        log.info("Finding by name");
        String sql = "SELECT * FROM anime_store.producer WHERE NAME LIKE '%s';".formatted("%" + name + "%");
        List<Producer> producers = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                rs.updateString("name", rs.getString("name").toUpperCase());
                rs.updateRow();
                producers.add(getProducer(rs));
            }
        } catch (SQLException e) {
            log.error("Error while trying to find and update producer");
        }
        return producers;
    }

    public static Producer findByNameAndInsertWhenNotFound(String name) {
        log.info("Finding by name");
        String sql = "SELECT * FROM anime_store.producer WHERE NAME LIKE '%s';".formatted("%" + name + "%");
        Producer newProducer = null;
        try (Connection conn = ConnectionFactory.getConnection();
             Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return newProducer;
            insertNewProducer(name, rs);
            rs.first();
            newProducer = getProducer(rs);
        } catch (SQLException e) {
            log.error("Error while trying to insert when not found a producer");
        }
        return newProducer;
    }

    public static void findByNameAndDelete(String name) {
        log.info("Finding by name");
        String sql = "SELECT * FROM anime_store.producer WHERE NAME LIKE '%s';".formatted("%" + name + "%");
        try (Connection conn = ConnectionFactory.getConnection();
             Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                log.info("Deleting producer '{}'", rs.getString("name"));
                rs.deleteRow();
            }
        } catch (SQLException e) {
            log.error("Error trying to find and delete producer");
        }
    }

    private static void insertNewProducer(String name, ResultSet rs) throws SQLException {
        rs.moveToInsertRow();
        rs.updateString("name", name);
        rs.insertRow();
    }

    private static Producer getProducer(ResultSet rs) throws SQLException {
        return Producer.builder().id(rs.getInt("id")).name(rs.getString("name")).build();
    }
}
