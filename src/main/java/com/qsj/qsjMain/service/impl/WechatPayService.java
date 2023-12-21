/*
 * *
 *  * Created on ${YEAR}-${MONTH}-${DAY}
 *
 */

package com.qsj.qsjMain.service.impl;

import com.qsj.qsjMain.config.ProfileVariableConfig;
import com.qsj.qsjMain.remote.service.WXApiRemoteService;
import com.qsj.qsjMain.remote.service.model.dto.PreOrderSignResult;
import com.qsj.qsjMain.utils.RandomStringUtils;
import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAConfig;
import com.wechat.pay.java.core.notification.NotificationConfig;
import com.wechat.pay.java.core.notification.NotificationParser;
import com.wechat.pay.java.core.notification.RSANotificationConfig;
import com.wechat.pay.java.core.util.PemUtil;
import com.wechat.pay.java.service.certificate.CertificateService;
import com.wechat.pay.java.service.payments.jsapi.JsapiService;
import com.wechat.pay.java.service.payments.jsapi.model.Amount;
import com.wechat.pay.java.service.payments.jsapi.model.Payer;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayRequest;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayResponse;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.List;

/**
 * 微信支付服务
 */
@Service
@Data
public class WechatPayService {
    private final String merchantId = "1634050072";
    private final String merchantSerialNumber = "5C5FA92B43E842060AC75FF8CE53E8CC15B3CFDA";
    private final String apiV3Key = "27ed343c85837eb4d145f533e22b5317";
    private final String appId = "wxfbfc7fd709d9253b";

    private final ProfileVariableConfig profileVariableConfig;



    private final Config config = new RSAConfig.Builder()
            .merchantId(merchantId)
            .merchantSerialNumber(merchantSerialNumber)
            .privateKeyFromPath(getPrivateKeyPath())
            .wechatPayCertificatesFromPath(getWechatPayCertificatePath())
            .build();

    private final PrivateKey privateKey = PemUtil.loadPrivateKeyFromPath(getPrivateKeyPath());
    private final CertificateService certificateService = new CertificateService.Builder().config(config).build();
    private final NotificationConfig rsaNotificationConfig = new RSANotificationConfig.Builder()
            .apiV3Key(apiV3Key)
            .certificatesFromPath(getWechatPayCertificatePath())
            .build();

    private final NotificationParser notificationParser = new NotificationParser(rsaNotificationConfig);
    private final JsapiService jsapiService = new JsapiService.Builder().config(config).build();

    public WechatPayService(ProfileVariableConfig profileVariableConfig) {
        this.profileVariableConfig = profileVariableConfig;
    }

    public List<X509Certificate> getCertificateList() {
        return certificateService.downloadCertificate(apiV3Key.getBytes(StandardCharsets.UTF_8));
    }

    public PrepayResponse prepay(Integer payAmount, String openId, String orderId, String description) {
        PrepayRequest request = new PrepayRequest();
        Amount amount = new Amount();
        amount.setTotal(payAmount);
        request.setAmount(amount);
        request.setNotifyUrl(profileVariableConfig.getPayCallbackURL());
        request.setAppid(appId);
        request.setDescription(description);
        request.setOutTradeNo(orderId);
        request.setMchid(merchantId);
        Payer payer = new Payer();
        payer.setOpenid(openId);
        request.setPayer(payer);
        return jsapiService.prepay(request);
    }

    public PreOrderSignResult prepayAndSign(Integer payAmount, String openId, String orderId, String description) {
        try {
            PrepayResponse resp = prepay(payAmount, openId, orderId, description);
            String prePayId = resp.getPrepayId();
            String appId = WXApiRemoteService.APP_ID;
            String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
            String nonceStr = RandomStringUtils.randomAlphanumeric(32);
            String packageStr = "prepay_id=" + prePayId;
            String signType = "RSA";
            String message = appId + "\n" + timestamp + "\n" + nonceStr + "\n" + packageStr + "\n";
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(getPrivateKey());
            signature.update(message.getBytes());
            String sign = Base64.getEncoder().encodeToString(signature.sign());
            return new PreOrderSignResult(timestamp, nonceStr, packageStr, signType, sign);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    private String getPrivateKeyPath() {
        String os = System.getProperty("os.name");
        if (os.toLowerCase().startsWith("win")) {
            return "C:\\key\\apiclient_key.pem";
        } else {
            return "/data/key/apiclient_key.pem";
        }
    }

    private String getWechatPayCertificatePath() {
        String os = System.getProperty("os.name");
        if (os.toLowerCase().startsWith("win")) {
            return "C:\\key\\wechatpay_cert.pem";
        } else {
            return "/data/key/wechatpay_cert.pem";
        }
    }
}
