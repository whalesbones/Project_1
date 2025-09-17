package pojo;


import io.qameta.allure.internal.shadowed.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class IngredientsRequest {

    private List<String> ingredients;

    public IngredientsRequest() {}

    public IngredientsRequest ingredientsRequest(List<String> ingredients) {
        this.ingredients = ingredients;
        return this;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public IngredientsRequest setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
        return this;
    }
}