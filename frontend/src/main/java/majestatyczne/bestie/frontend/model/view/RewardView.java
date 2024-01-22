package majestatyczne.bestie.frontend.model.view;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;
import majestatyczne.bestie.frontend.model.Reward;
import majestatyczne.bestie.frontend.model.RewardCategory;

public class RewardView {

    @Getter
    private int id;

    private ObjectProperty<RewardCategoryView> rewardCategory;

    private StringProperty rewardCategoryName;

    private StringProperty name;

    private StringProperty description;

    public RewardView(int id, RewardCategory rewardCategory, String name, String description) {
        this.id = id;
        if (rewardCategory == null) {
            this.rewardCategory = new SimpleObjectProperty<>(null);
            this.rewardCategoryName = new SimpleStringProperty(null);
        } else {
            this.rewardCategory = new SimpleObjectProperty<>(new RewardCategoryView(rewardCategory));
            this.rewardCategoryName = new SimpleStringProperty(rewardCategory.getName());
        }
        this.name = new SimpleStringProperty(name);
        this.description = new SimpleStringProperty(description);
    }

    public RewardView(Reward reward) {
        this(reward.getId(), reward.getRewardCategory(), reward.getName(), reward.getDescription());
    }

    public RewardCategoryView getRewardCategory() {
        return rewardCategory.get();
    }

    public ObjectProperty<RewardCategoryView> getRewardCategoryProperty() {
        return rewardCategory;
    }

    public void setRewardCategory(RewardCategoryView rewardCategory) {
        this.rewardCategory.set(rewardCategory);
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

    public String getDescription() {
        return description.get();
    }

    public StringProperty getDescriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    @Override
    public String toString() {
        return getName();
    }

    public String getRewardCategoryName() {
        return rewardCategoryName.get();
    }

    public StringProperty getRewardCategoryNameProperty() {
        return rewardCategoryName;
    }

    public void setRewardCategoryName(String rewardCategoryName) {
        this.rewardCategoryName.set(rewardCategoryName);
    }
}
