package junkyu.budget.service;

import junkyu.budget.domain.Spend;
import junkyu.budget.domain.User;
import junkyu.budget.dto.SpendGetRequestDto;
import junkyu.budget.dto.SpendGetResponseDto;
import junkyu.budget.dto.SpendRequestDto;
import junkyu.budget.dto.SpendTodayResponseDto;
import junkyu.budget.enums.Category;
import junkyu.budget.exception.CustomException;
import junkyu.budget.exception.ErrorCode;
import junkyu.budget.repository.SpendRepository;
import junkyu.budget.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SpendService {

    private final SpendRepository spendRepository;
    private final UserRepository userRepository;

    public void createSpend(SpendRequestDto spendRequestDto){
        User findUser = userRepository.findById(spendRequestDto.getUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        spendRepository.save(Spend.builder()
                .user(findUser)
                .category(Category.nameOf(spendRequestDto.getCategory()))
                .amount(spendRequestDto.getAmount())
                .memo(spendRequestDto.getMemo())
                .build());
    }

    public void updateSpend(Long spendId, SpendRequestDto spendRequestDto){
        Spend findSpend = spendRepository.findById(spendId)
                .orElseThrow(() -> new CustomException(ErrorCode.SPEND_NOT_FOUND));

        findSpend.update(spendRequestDto);
    }
    public void deleteSpend(Long spendId){
        Spend findSpend = spendRepository.findById(spendId)
                .orElseThrow(() -> new CustomException(ErrorCode.SPEND_NOT_FOUND));

        spendRepository.delete(findSpend);
    }

    @Transactional(readOnly = true)
    public List<SpendGetResponseDto> getSpendList(SpendGetRequestDto spendGetRequestDto){
        List<SpendGetResponseDto> dtoList = spendRepository.findBySpendListRequestDto(spendGetRequestDto);

        dtoList.add(SpendGetResponseDto.builder()
                .category("총액")
                .amount(dtoList.stream().mapToLong(SpendGetResponseDto::getAmount).sum())
                .build());
        return dtoList;
    }
    @Transactional(readOnly = true)
    public SpendGetResponseDto getSpend(Long spendId){
        Spend findSpend = spendRepository.findById(spendId)
                .orElseThrow(() -> new CustomException(ErrorCode.SPEND_NOT_FOUND));
        return SpendGetResponseDto.from(findSpend);
    }

    @Transactional(readOnly = true)
    public Long getTotal(Long id){
        return spendRepository.thisMonthTotalSpendById(id);
    }

    @Transactional(readOnly = true)
    public List<SpendTodayResponseDto> getTodaySpendList(Long id){
        return spendRepository.getTodaySpendList(id);
    }

}
