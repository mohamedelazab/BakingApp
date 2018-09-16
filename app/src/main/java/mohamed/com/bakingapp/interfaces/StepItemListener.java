package mohamed.com.bakingapp.interfaces;

import java.util.List;

import mohamed.com.bakingapp.model.StepResponse;

public interface StepItemListener {
    public void onStepItemClicked(StepResponse stepResponse, List<StepResponse> steps);
}
