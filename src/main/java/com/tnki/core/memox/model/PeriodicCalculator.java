package com.tnki.core.memox.model;

import java.util.Date;

public interface PeriodicCalculator {
    Date calcNextLearnDate(MemoLearningItem item);
    Date getStartLearnDate();
    double calcNextEF(double lastEF, int memoQuality);
}
