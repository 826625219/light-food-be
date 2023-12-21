package com.qsj.qsjMain.service.impl;

import com.qsj.qsjMain.model.vo.ewx.EWXCallbackResp;
import com.qsj.qsjMain.model.vo.ewx.EWXTextCallbackMsg;
import com.qsj.qsjMain.service.impl.internalBusiness.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class InternalBusinessService {

    private final StartShopCommandHandler startShopCommandHandler;
    private final StopShopCommandHandler stopShopCommandHandler;
    private final HelpCommandHandler helpCommandHandler;
    private final FallbackHandler fallbackHandler;
    private final DispatchCouponHandler dispatchCouponHandler;
    private final ShopStartSACommandHandler shopStartSACommandHandler;
    private final ShopStopSACommandHandler shopStopSACommandHandler;

    private final ShopUnLockCommandHandler shopUnLockCommandHandler;

    private final ShopLockCommandHandler shopLockCommandHandler;

    private static List<BaseCommandHandler> commandHandlers = null;

    @PostConstruct
    public void init() {
        commandHandlers = List.of(startShopCommandHandler, stopShopCommandHandler, helpCommandHandler,
                dispatchCouponHandler, shopStartSACommandHandler, shopStopSACommandHandler, shopUnLockCommandHandler,
                shopLockCommandHandler, fallbackHandler);

    }


    public String ewxAppMessageCallback(String xmlString) throws JAXBException {
        StringReader reader = new StringReader(xmlString);
        JAXBContext context = JAXBContext.newInstance(EWXTextCallbackMsg.class);
        EWXTextCallbackMsg msg = (EWXTextCallbackMsg) context.createUnmarshaller().unmarshal(reader);
        String respText ="未知错误";
        try {
            for (BaseCommandHandler handler : commandHandlers) {
                if (handler.isMatch(msg.getContent())) {
                    respText=handler.handle(msg.getContent(), msg.getFromUserName());
                    break;
                }
            }
        }
        catch (IOException e) {
            respText = "服务器通讯错误";
        }


        // 将返回消息打包为xml
        EWXCallbackResp resp = new EWXCallbackResp();
        resp.setFromUserName(msg.getToUserName());
        resp.setToUserName(msg.getFromUserName());
        resp.setCreateTime(System.currentTimeMillis());
        resp.setMsgType("text");
        resp.setContent(respText);
        StringWriter writer = new StringWriter();
        JAXBContext respContext = JAXBContext.newInstance(EWXCallbackResp.class);
        respContext.createMarshaller().marshal(resp, writer);
        return writer.toString();
    }
}
