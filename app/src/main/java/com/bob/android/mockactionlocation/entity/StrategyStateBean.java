package com.bob.android.mockactionlocation.entity;

/**
 * @package com.bob.android.mockactionlocation.entity
 * @fileName StrategyStateBean
 * @Author Bob on 2018/12/26 18:08.
 * @Describe TODO
 */
public class StrategyStateBean {
    private int mStrategyCode;
    private boolean mOpen;

    public StrategyStateBean(int strategyCode, boolean open) {
        setStrategyCode(strategyCode);
        setOpen(open);
    }

    public int getStrategyCode() {
        return mStrategyCode;
    }

    public void setStrategyCode(int mStrategyCode) {
        this.mStrategyCode = mStrategyCode;
    }

    public boolean isOpen() {
        return mOpen;
    }

    public void setOpen(boolean mOpen) {
        this.mOpen = mOpen;
    }
}
