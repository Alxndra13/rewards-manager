package majestatyczne.bestie.frontend.model.view;

import javafx.beans.property.*;
import majestatyczne.bestie.frontend.model.RewardCategory;
import majestatyczne.bestie.frontend.model.RewardStrategyParameter;

public class RewardStrategyParameterView {
    private int id;

    private ObjectProperty<Integer> priority;

    private ObjectProperty<Integer> parameterValue;

    private ObjectProperty<RewardCategory> rewardCategory;

    private StringProperty rewardCategoryName;

    public RewardStrategyParameterView(int id, int priority, int parameterValue, RewardCategory rewardCategory) {
        this.id = id;
        this.priority = new SimpleObjectProperty<>(priority);
        this.parameterValue = new SimpleObjectProperty<>(parameterValue);
        this.rewardCategory = new SimpleObjectProperty<>(rewardCategory);
        if (rewardCategory == null) {
            this.rewardCategoryName = null;
        } else {
            this.rewardCategoryName = new SimpleStringProperty(rewardCategory.getName());
        }
    }

    public RewardStrategyParameterView(RewardStrategyParameter parameter) {
        this(parameter.getId(), parameter.getPriority(), parameter.getParameterValue(), parameter.getRewardCategory());
    }

    public int getPriority() {
        return priority.get();
    }

    public int getParameterValue() {
        return parameterValue.get();
    }

    public RewardCategory getRewardCategory() {
        return rewardCategory.get();
    }

    public ObjectProperty<RewardCategory> getRewardCategoryProperty() {
        return rewardCategory;
    }

    public ObjectProperty<Integer> getPriorityProperty() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority.set(priority);
    }

    public ObjectProperty<Integer> getParameterValueProperty() {
        return parameterValue;
    }

    public void setParameterValue(Integer parameterValue) {
        this.parameterValue.set(parameterValue);
    }

    public void setRewardCategory(RewardCategory rewardCategory) {
        this.rewardCategory.set(rewardCategory);
    }

    public RewardStrategyParameter toRewardStrategyParameter() {
        return new RewardStrategyParameter(id, priority.get(), parameterValue.get(), rewardCategory.get());
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
