package generator;

import pojo.Ingredient;

import java.util.List;

public class IngredientsResponse {

    private boolean success;
    private List<Ingredient> data;
    private String message;

    public boolean isSuccess() {
        return success;
    }

    public List<Ingredient> getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "IngredientsResponse{" +
                "success=" + success +
                ", data=" + data +
                '}';
    }
}