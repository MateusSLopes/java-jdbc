package mateus.jdbc.service;

import mateus.jdbc.domain.Producer;
import mateus.jdbc.repository.ProducerRepository;

public class ProducerService {
    public static void save(Producer producer) {
        ProducerRepository.save(producer);
    }

    public static void delete(Integer id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid value for id");
        }
        ProducerRepository.delete(id);
    }
}
