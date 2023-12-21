package com.qsj.qsjMain.service.impl.internalBusiness;

import java.io.IOException;

public interface ICommandHandle {
    String handle(String msg, String fromUser) throws IOException;

    Boolean isMatch(String msg);

    Boolean isRegex();
    String getPattern();

}
