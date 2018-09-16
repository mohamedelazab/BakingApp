package mohamed.com.bakingapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class BakedResponse implements Serializable{

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("servings")
    private int servings;

    @SerializedName("image")
    private String image;

    @SerializedName("ingredients")
    private List<IngredientResponse> ingredientResponses;

    @SerializedName("steps")
    private List<StepResponse> stepResponses;

    public BakedResponse(){

    }

    public BakedResponse(int id, String name, int servings, String image, List<IngredientResponse> ingredientResponses, List<StepResponse> stepResponses) {
        this.id = id;
        this.name = name;
        this.servings = servings;
        this.image = image;
        this.ingredientResponses = ingredientResponses;
        this.stepResponses = stepResponses;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<IngredientResponse> getIngredientResponses() {
        return ingredientResponses;
    }

    public void setIngredientResponses(List<IngredientResponse> ingredientResponses) {
        this.ingredientResponses = ingredientResponses;
    }

    public List<StepResponse> getStepResponses() {
        return stepResponses;
    }

    public void setStepResponses(List<StepResponse> stepResponses) {
        this.stepResponses = stepResponses;
    }
}