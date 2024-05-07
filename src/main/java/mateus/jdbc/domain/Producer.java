package mateus.jdbc.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@Builder
@EqualsAndHashCode
public final class Producer {
    private final Integer id;
    private final String name;
}
