/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package evskilo.logic.impl;

import evskilo.logic.VoteLogic;
import evskilo.logic.dto.PollDetailsDto;
import evskilo.logic.dto.PollResultDto;
import evskilo.logic.dto.QuestionDto;
import evskilo.logic.exception.ExceptionType;
import evskilo.logic.exception.PollException;
import evskilo.logic.util.ExceptionUtil;
import evskilo.logic.util.StringUtil;
import evskilo.persistence.dao.PollAccess;
import evskilo.persistence.dao.TokenAccess;
import evskilo.persistence.entities.Item;
import evskilo.persistence.entities.Poll;
import evskilo.persistence.entities.Question;
import evskilo.persistence.entities.Token;
import evskilo.persistence.entities.enums.PollStates;
import java.util.Optional;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author uthar
 */
@Stateless
public class VoteLogicImpl implements VoteLogic {
    
    @Inject
    private TokenAccess tokenAccess;
    
    @Inject
    private PollAccess pollAccess;

    @Override
    public Long addVote(String tokenValue, PollDetailsDto pollInfo) throws PollException {
        Token token = tokenAccess.getTokenByValue(tokenValue);
        Poll poll = token.getPoll();
        for (Question question : poll.getQuestions()) {
            Optional<QuestionDto> foundQuestion = pollInfo.getQuestions()
                    .stream()
                    .filter(inQuestion -> inQuestion.getId().equals(question.getId()))
                    .findFirst();
            if (!foundQuestion.isPresent()) {
                 ExceptionUtil.throwException(ExceptionType.VOTING_DATA_ERROR, StringUtil.getLabel(question.getTitle()), StringUtil.getLabel("votingDataException"));
            }
            Integer numOfAnswers = foundQuestion.get().getSelectedAnswer().size();
            if (numOfAnswers > question.getMaxAnswers()) {
                 ExceptionUtil.throwException(ExceptionType.INVALID_PARAMETER, StringUtil.getLabel(question.getTitle()), StringUtil.getLabel("Exceeds the number of possible answers"));
                
            }
            if (numOfAnswers > 0) {
                for (Long answerId : foundQuestion.get().getSelectedAnswer()) {
                    Optional<Item> foundItem = question.getItems()
                            .stream()
                            .filter(item -> item.getId().equals(answerId))
                            .findFirst();
                    if (!foundItem.isPresent()) {
                        ExceptionUtil.throwException(ExceptionType.VOTING_DATA_ERROR, StringUtil.getLabel(question.getTitle()), StringUtil.getLabel("votingDataException"));
                    }
                    foundItem.get().increaseVotes();
                }
                question.increaseSubmittedVotes();
            }
        }
        poll.increaseSubmittedVotes();
        if (poll.getTokenCount() == poll.getSubmittedVotes()) {
            poll.setState(PollStates.FINISHED);
        }
//        token.setIsValid(false);
        tokenAccess.persist(token);
        pollAccess.persist(poll);
        return poll.getId();
    }
    
    @Override
    public PollResultDto viewResult(String publishKey) throws PollException {
        Optional<Poll> poll = pollAccess.getByPublishKey(publishKey);
        if (!poll.isPresent()) {
            return null;
        }
        return PollResultDto.fromEo(poll.get());
    }

}
