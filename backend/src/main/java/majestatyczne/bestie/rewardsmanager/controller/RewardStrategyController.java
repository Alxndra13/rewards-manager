package majestatyczne.bestie.rewardsmanager.controller;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import majestatyczne.bestie.rewardsmanager.dto.RewardStrategyDTO;
import majestatyczne.bestie.rewardsmanager.dto.RewardStrategyParameterDTO;
import majestatyczne.bestie.rewardsmanager.model.RewardStrategy;
import majestatyczne.bestie.rewardsmanager.model.RewardStrategyParameter;
import majestatyczne.bestie.rewardsmanager.service.RewardCategoryService;
import majestatyczne.bestie.rewardsmanager.service.RewardStrategyParameterService;
import majestatyczne.bestie.rewardsmanager.service.RewardStrategyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("reward-strategies")
@RequiredArgsConstructor
public class RewardStrategyController {

    private final RewardStrategyService rewardStrategyService;

    private final RewardStrategyParameterService rewardStrategyParameterService;

    private final RewardCategoryService rewardCategoryService;

    @PostMapping
    public ResponseEntity<String> addWithParameters(@RequestBody RewardStrategyDTO rewardStrategyDTO) {
        try {
            RewardStrategy rewardStrategy = rewardStrategyService.add(rewardStrategyDTO.quizDTO().id(),
                    rewardStrategyDTO.rewardStrategyType());

            List<RewardStrategyParameter> rewardStrategyParameters =
                    RewardStrategyParameterDTO.convertAllFromDTO(rewardStrategyDTO.rewardStrategyParameterDTOs(),
                            rewardStrategy, rewardCategoryService);

            rewardStrategyParameterService.addAll(rewardStrategyParameters);

            rewardStrategyService.addParametersToStrategy(rewardStrategy, rewardStrategyParameters);

            rewardStrategyService.insertRewards(rewardStrategy);

            return ResponseEntity.status(HttpStatus.OK).build();

        } catch (EntityExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
        }
    }

    @PutMapping
    public ResponseEntity<String> updateWithParameters(@RequestBody RewardStrategyDTO rewardStrategyDTO) {
        try {
            RewardStrategy rewardStrategy = rewardStrategyService.update(rewardStrategyDTO.quizDTO().id(),
                    rewardStrategyDTO.rewardStrategyType());

            List<RewardStrategyParameter> rewardStrategyParameters =
                    RewardStrategyParameterDTO.convertAllFromDTO(rewardStrategyDTO.rewardStrategyParameterDTOs(),
                            rewardStrategy, rewardCategoryService);

            rewardStrategyParameterService.updateAll(rewardStrategyParameters, rewardStrategy);

            rewardStrategyService.addParametersToStrategy(rewardStrategy, rewardStrategyParameters);

            rewardStrategyService.insertRewards(rewardStrategy);

            return ResponseEntity.status(HttpStatus.OK).build();

        } catch (EntityExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
        }
    }

    @GetMapping("/{quizId}")
    public ResponseEntity<?> getByQuizId(@PathVariable int quizId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(
                    RewardStrategyDTO.convertToDTO(rewardStrategyService.findByQuizId(quizId)));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
