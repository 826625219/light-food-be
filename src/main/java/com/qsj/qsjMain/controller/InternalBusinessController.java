package com.qsj.qsjMain.controller;

import com.qq.weixin.mp.aes.AesException;
import com.qq.weixin.mp.aes.WXBizMsgCrypt;
import com.qsj.qsjMain.config.ProfileVariableConfig;
import com.qsj.qsjMain.service.impl.InternalBusinessService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.xml.bind.JAXBException;
import java.util.Random;

@RestController
@RequestMapping("api/v1/_internalBiz")
public class InternalBusinessController {

    @Resource
    ProfileVariableConfig profileVariableConfig;

    private final InternalBusinessService internalBusinessService;

    public InternalBusinessController(InternalBusinessService internalBusinessService) {
        this.internalBusinessService = internalBusinessService;
    }

    @GetMapping(path = "/ewxCallback")
    public String ewxAppMessageEchoCallback(@RequestParam(name = "msg_signature") String msgSign,
                                            @RequestParam String timestamp,
                                            @RequestParam String nonce,
                                            @RequestParam String echostr) throws AesException {


        // decrypt echoStr
        WXBizMsgCrypt crypt = new WXBizMsgCrypt(profileVariableConfig.getEwxAppToken()
                , profileVariableConfig.getEncodingAESKey()
                , profileVariableConfig.getEwxCorpId());

        return crypt.VerifyURL(msgSign, timestamp, nonce, echostr);

    }

    @PostMapping(path = "/ewxCallback")
    public String ewxAppMessageCallback(@RequestParam(name = "msg_signature") String msgSign,
                                        @RequestParam String timestamp,
                                        @RequestParam String nonce,
                                        @RequestBody String xmlBody) throws AesException, JAXBException {


        // check signature
        WXBizMsgCrypt crypt = new WXBizMsgCrypt(profileVariableConfig.getEwxAppToken()
                , profileVariableConfig.getEncodingAESKey()
                , profileVariableConfig.getEwxCorpId());

        String decryptedXMLBody = crypt.DecryptMsg(msgSign, timestamp, nonce, xmlBody);
        String respXml = internalBusinessService.ewxAppMessageCallback(decryptedXMLBody);
        String rtNonce = String.valueOf(new Random().nextInt(1000000));
        String rtTimestamp = String.valueOf(System.currentTimeMillis());
        return crypt.EncryptMsg(respXml, rtTimestamp, rtNonce);
    }
}
