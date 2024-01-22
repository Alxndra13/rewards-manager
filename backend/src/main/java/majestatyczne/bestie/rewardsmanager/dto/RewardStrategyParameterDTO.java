package majestatyczne.bestie.rewardsmanager.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import majestatyczne.bestie.rewardsmanager.model.RewardCategory;
import majestatyczne.bestie.rewardsmanager.model.RewardStrategy;
import majestatyczne.bestie.rewardsmanager.model.RewardStrategyParameter;
import majestatyczne.bestie.rewardsmanager.service.RewardCategoryService;

import java.util.List;

public record RewardStrategyParameterDTO (

    int id,

    int priority,

    int parameterValue,

    @JsonProperty("rewardCategory")
    RewardCategoryDTO rewardCategoryDTO
) {

    public static RewardStrategyParameterDTO convertToDTO(RewardStrategyParameter rewardStrategyParameter) {
        return new RewardStrategyParameterDTO(rewardStrategyParameter.getId(), rewardStrategyParameter.getPriority(),
                rewardStrategyParameter.getParameterValue(), RewardCategoryDTO.convertToDTO(rewardStrategyParameter.getRewardCategory()));
    }

    public static List<RewardStrategyParameterDTO> convertAllToDTO(List<RewardStrategyParameter> rewardStrategyParameters) {
        return rewardStrategyParameters.stream().map(RewardStrategyParameterDTO::convertToDTO).toList();
    }

    public static RewardStrategyParameter convertFromDTO(RewardStrategyParameterDTO rewardStrategyParameterDTO,
                                                  RewardStrategy rewardStrategy, RewardCategory rewardCategory) {
        RewardStrategyParameter rewardStrategyParameter = new RewardStrategyParameter();
        rewardStrategyParameter.setRewardStrategy(rewardStrategy);
        rewardStrategyParameter.setParameterValue(rewardStrategyParameterDTO.parameterValue);
        rewardStrategyParameter.setPriority(rewardStrategyParameterDTO.priority);
        rewardStrategyParameter.setRewardCategory(rewardCategory);

        return rewardStrategyParameter;
    }

    public static List<RewardStrategyParameter> convertAllFromDTO(List<RewardStrategyParameterDTO> rewardStrategyParameterDTOs,
                                                                  RewardStrategy rewardStrategy,
                                                                  RewardCategoryService rewardCategoryService) {
        return rewardStrategyParameterDTOs
                .stream()
                .map(rewardStrategyParameterDTO ->
                {
                    RewardCategory rewardCategory = rewardStrategyParameterDTO.rewardCategoryDTO == null ? null :
                            rewardCategoryService.findById(rewardStrategyParameterDTO.rewardCategoryDTO.id());

                    return RewardStrategyParameterDTO.convertFromDTO(rewardStrategyParameterDTO, rewardStrategy,
                            rewardCategory);
                })
                .toList();
    }
}
