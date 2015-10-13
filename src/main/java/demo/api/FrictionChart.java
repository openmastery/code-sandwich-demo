package demo.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FrictionChart {

        String title;
        List<String> ticks;
        List<Double> conflictSeries = new ArrayList<>();
        List<Double> learningSeries = new ArrayList<>();
        List<Double> reworkSeries = new ArrayList<>();
}
