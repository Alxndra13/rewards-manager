package majestatyczne.bestie.frontend.model.view;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;
import majestatyczne.bestie.frontend.model.RewardCategory;

public class RewardCategoryView {

    @Getter
    private int id;

    private StringProperty name;

    public RewardCategoryView(int id, String name) {
        this.id = id;
        this.name = new SimpleStringProperty(name);
    }

    public RewardCategoryView(RewardCategory rewardCategory) {
        this(rewardCategory.getId(), rewardCategory.getName());
    }

    public String getName() {
        return name.get();
    }

    public StringProperty getNameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    @Override
    public String toString() {
        return getName();
    }
}
