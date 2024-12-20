package features;

import com.rifftech.temporal.collections.BiTemporalCollection;
import io.cucumber.java.en.Given;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

public class BiTemporalCollectionOfIntegerStep {
    BiTemporalScenarioContext context;

    @Given("^an empty bi-temporal collection of Integer$")
    public void anEmptyBiTemporalCollectionOfInteger() {
        BiTemporalCollection<Integer> collection = new BiTemporalCollection<>();
    }
}
