package pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Ingredient {

    @JsonProperty("_id")
    private String id;

    private String name;
    private String type;
    private int proteins;
    private int fat;
    private int carbohydrates;
    private int calories;
    private int price;
    private String image;
    private String imageMobile;
    private String imageLarge;

    @JsonProperty("__v")
    private int v;

    // Геттеры
    public String getId() { return id; }
    public String getName() { return name; }
    public String getType() { return type; }
    public int getProteins() { return proteins; }
    public int getFat() { return fat; }
    public int getCarbohydrates() { return carbohydrates; }
    public int getCalories() { return calories; }
    public int getPrice() { return price; }
    public String getImage() { return image; }
    public String getImageMobile() { return imageMobile; }
    public String getImageLarge() { return imageLarge; }
    public int getV() { return v; }
}