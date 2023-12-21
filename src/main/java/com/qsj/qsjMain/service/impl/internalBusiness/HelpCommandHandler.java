package com.qsj.qsjMain.service.impl.internalBusiness;

import org.springframework.stereotype.Service;

@Service
public class HelpCommandHandler extends BaseCommandHandler{

    @Override
    public String handle(String msg, String fromUser) {
        return """
                可用命令：
                开始营业: 将所属店铺的状态变更为营业中
                停止营业: 将所属店铺的状态变更为已休息
                锁定门店状态:  禁止门店自动开关业，用于长时间休业等场景
                解除门店锁定:  恢复门店自动开关业
                研发命令：
                发券 [userId] [batchId]: 为指定用户发放批次券
                设置停止营业 [shopId]: 将指定店铺设置为停止营业
                设置开始营业 [shopId]: 将指定店铺设置为开始营业
                help：查看帮助
                """;
    }

    @Override
    public String getPattern() {
        return "help";
    }
}
