package evskilo.logic.dto;

import evskilo.persistence.entities.Question;
import evskilo.persistence.entities.enums.DecisionMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author uthar
 */
public class QuestionResultDto {

    private String title;
    private Long questionID;
    private DecisionMode decisionMode;
    private Integer participantsCount;
    private Integer votesCount;
    private Integer abstentionsCount;
    private List<ItemResultDto> itemResults = new ArrayList<>();

    public static QuestionResultDto fromEo(Question model) {
        QuestionResultDto questionResultDto = new QuestionResultDto();
        questionResultDto.setTitle(model.getTitle());
        questionResultDto.setQuestionID(model.getId());
        questionResultDto.setDecisionMode(model.getMode());
        questionResultDto.setParticipantsCount(model.getPoll().getTokenCount());
        questionResultDto.setVotesCount(model.getSubmittedVotes());
        questionResultDto.setAbsentionsCount(model.getAbstentions());
        questionResultDto.setItemResults(model.getItems().stream()
                .map(item -> ItemResultDto.fromEo(item))
                .collect(Collectors.toList()));
        return questionResultDto;
    }

    public List<ItemResultDto> getDecisionItems() {
        Double halfParticipants = Double.valueOf(getParticipantsCount()) / 2;
        Double halfVotes = Double.valueOf(getVotesCount()) / 2;

        // Check if there are more participants than votes, change decision mode
        if (getAbsentionsCount() > 0) {
            decisionMode = DecisionMode.RELATIVE_MAJORITY;
        } else {
            decisionMode = DecisionMode.ABSOLUTE_MAJORITY;
        }

        List<ItemResultDto> itemTopResults = getItemResults();

        // Sort the items by vote counts in descending order
        itemTopResults.sort((a, b) -> b.getVoteCounts() - a.getVoteCounts());

        // Find the top-voted item
        ItemResultDto topItem = itemTopResults.get(0);

        switch (decisionMode) {
            case ABSOLUTE_MAJORITY:
                // Find all items with the same number of votes as the top-voted item

                List<ItemResultDto> decisionItems = new ArrayList<>();
                if (topItem.getVoteCounts() > halfParticipants) {
                    decisionItems.add(topItem);
                    for (int i = 1; i < itemTopResults.size(); i++) {
                        ItemResultDto currentItem = itemTopResults.get(i);
                        if (currentItem.getVoteCounts() == topItem.getVoteCounts()) {
                            decisionItems.add(currentItem);
                        }
                    }
                }
                return decisionItems;

            case RELATIVE_MAJORITY:
                List<ItemResultDto> result = new ArrayList<>();
                // Check if the top-voted item has a relative majority of votes
                if (topItem.getVoteCounts() > halfVotes) {

                    result.add(topItem);
                    for (int i = 1; i < itemTopResults.size(); i++) {
                        ItemResultDto currentItem = itemTopResults.get(i);
                        if (currentItem.getVoteCounts() == topItem.getVoteCounts()) {
                            result.add(currentItem);
                        }
                    }
                }
                return result;
            default:
                List<ItemResultDto> itmResult = new ArrayList<>();
                itmResult.add(topItem);
                return itmResult;

        }
    }

    public Integer getParticipantsCount() {
        return participantsCount;
    }

    public Integer getAbsentionsCount() {
        return abstentionsCount;
    }

    public void setAbsentionsCount(Integer absentationsCount) {
        this.abstentionsCount = absentationsCount;
    }

    public void setVotesCount(Integer votesCount) {
        this.votesCount = votesCount;
    }

    public Integer getVotesCount() {
        return votesCount;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setParticipantsCount(Integer participantsCount) {
        this.participantsCount = participantsCount;
    }

    public void setDecisionMode(DecisionMode decisionMode) {
        this.decisionMode = decisionMode;
    }

    public void setItemResults(List<ItemResultDto> itemResults) {
        this.itemResults = itemResults;
    }

    public void setQuestionID(Long questionID) {
        this.questionID = questionID;
    }

    public List<ItemResultDto> getItemResults() {
        return itemResults;
    }

    public String getItemTitles() {
        return itemResults.stream()
                .map(item -> "\"" + item.getName().replace("\\", "\\\\").replace("\"", "\\\"") + "\"")
                .collect(Collectors.joining(", "));
    }

    public String getItemVotes() {
        return itemResults.stream()
                .map(item -> item.getVoteCounts().toString())
                .collect(Collectors.joining(", "));
    }

    public DecisionMode getDecisionMode() {
        return decisionMode;
    }

    public String getTitle() {
        return title;
    }

    public Long getQuestionID() {
        return questionID;
    }
}
