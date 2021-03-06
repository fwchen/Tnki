package com.tnki.core.memox;

import com.tnki.core.common.MemoDateUtil;
import com.tnki.core.memox.command.CreateLearnItemCommand;
import com.tnki.core.memox.command.LearnItemCommand;
import com.tnki.core.memox.model.*;
import com.tnki.core.memox.repository.*;
import com.tnki.core.statistics.StatisticsApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class MemoxApplicationService {
    final private MemoItemFactory memoItemFactory;
    final private MemoItemRepository memoItemRepository;
    final private MemoUserSettingRepository memoUserSettingRepository;
    final private UserDailyCheckInRecordRepository userDailyCheckInRecordRepository;
    final private MemoLearnItemRepository memoLearnItemRepository;
    final private SuperMemoX superMemoX;
    final private StatisticsApplicationService statisticsApplicationService;

    @Autowired
    public MemoxApplicationService(
            MemoItemFactory memoItemFactory,
            MemoItemRepository memoItemRepository,
            MemoUserSettingRepository memoUserSettingRepository,
            UserDailyCheckInRecordRepository userDailyCheckInRecordRepository,
            MemoLearnItemRepository memoLearnItemRepository,
            SuperMemoX superMemoX,
            StatisticsApplicationService statisticsApplicationService
    ) {
        this.memoItemFactory = memoItemFactory;
        this.memoItemRepository = memoItemRepository;
        this.memoUserSettingRepository = memoUserSettingRepository;
        this.userDailyCheckInRecordRepository = userDailyCheckInRecordRepository;
        this.memoLearnItemRepository = memoLearnItemRepository;
        this.superMemoX = superMemoX;
        this.statisticsApplicationService = statisticsApplicationService;
    }

    long createLearnItem(CreateLearnItemCommand command, long userID) {
        MemoItem item = memoItemFactory.createMemoItemFromCommand(command);
        memoItemRepository.add(item, userID);
        log.info("Created learn item [{}].", item.getID());
        return item.getID();
    }

    void checkIn(long userID) {
        if (memoUserSettingRepository.findOne(userID).isEmpty()) {
            memoUserSettingRepository.save(UserLearnSetting.initUserLearnSetting(userID));
        }

        UserDailyCheckInRecord userDailyCheckInRecord = userDailyCheckInRecordRepository.find(userID, MemoDateUtil.today());
        superMemoX.fillItemToLearn(userID);

        if (userDailyCheckInRecord == null) {
            userDailyCheckInRecordRepository.add(userID, MemoDateUtil.today());
        } else {
            userDailyCheckInRecord.increaseTime();
            userDailyCheckInRecordRepository.update(userDailyCheckInRecord);
        }
    }

    void learnItem(LearnItemCommand learnItemCommand, long userID) {
        MemoLearningItem memoLearningItem = memoLearnItemRepository.findOne(learnItemCommand.getItemID(), userID);
        superMemoX.learnItem(memoLearningItem, learnItemCommand.getMemoQuality());
        this.statisticsApplicationService.increaseDailyLearned(userID, 1);
    }

    List<MemoLearningItem> getDailyLearnItem(long userID) {
        return superMemoX.getLearningItem(userID);
    }

    List<MemoLearningItem> getLearningItems(long userID, int offset, int limit) {
        return memoLearnItemRepository.find(userID, offset, limit);
    }

}
