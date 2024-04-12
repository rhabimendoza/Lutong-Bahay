package com.example.lutongbahay;

public class DataClass {
    String image_direc;
    String name;
    String budget;
    String difficulty;
    String ingredient;
    String flavor;
    String servings;
    String procedure;
    String creator;

    public DataClass() {}

    public DataClass(String image_direc, String name, String budget, String difficulty, String ingredient, String flavor, String servings, String procedure, String creator) {
        this.image_direc = image_direc;
        this.name = name;
        this.budget = budget;
        this.difficulty = difficulty;
        this.ingredient = ingredient;
        this.flavor = flavor;
        this.servings = servings;
        this.procedure = procedure;
        this.creator = creator;
    }

    public String getImage_direc() {
        return image_direc;
    }

    public void setImage_direc(String image_direc) {
        this.image_direc = image_direc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public String getFlavor() {
        return flavor;
    }

    public void setFlavor(String flavor) {
        this.flavor = flavor;
    }

    public String getServings() {
        return servings;
    }

    public void setServings(String servings) {
        this.servings = servings;
    }

    public String getProcedure() {
        return procedure;
    }

    public void setProcedure(String procedure) {
        this.procedure = procedure;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }
}