package com.qsj.qsjMain.service.impl.internalBusiness;

public abstract class BaseCommandHandler implements ICommandHandle{

    @Override
    public Boolean isMatch(String msg) {
        if (isRegex()) {
            return msg.matches(getPattern());
        } else {
            return msg.equals(getPattern());
        }
    }

    @Override
    public Boolean isRegex() {
        return true;
    }

}
