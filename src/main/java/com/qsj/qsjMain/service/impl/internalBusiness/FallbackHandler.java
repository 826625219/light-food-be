package com.qsj.qsjMain.service.impl.internalBusiness;

import org.springframework.stereotype.Service;

@Service
public class FallbackHandler extends BaseCommandHandler{
    @Override
    public Boolean isMatch(String msg) {
        return true;
    }

    @Override
    public String handle(String msg, String fromUser) {
        return "您的命令不符合规范，请输入help查看帮助";
    }

    @Override
    public String getPattern() {
        return null;
    }

}
