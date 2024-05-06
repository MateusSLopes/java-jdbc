package mateus.jdbc.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.Objects;

@Value
@Builder
@EqualsAndHashCode
public final class Producer {
    private final Integer id;
    private final String name;
}
