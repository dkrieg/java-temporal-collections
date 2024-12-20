package features;

import com.rifftech.temporal.collections.BiTemporalCollection;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static lombok.AccessLevel.PRIVATE;

@Data
@FieldDefaults(level = PRIVATE)
@Component
@Scope(scopeName="scenario")
public class BiTemporalScenarioContext {
    BiTemporalCollection<Integer> collection;
}
