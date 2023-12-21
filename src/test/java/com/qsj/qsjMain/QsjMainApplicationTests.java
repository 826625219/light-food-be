package com.qsj.qsjMain;

import com.qsj.qsjMain.remote.service.DaDaDeliveryRemoteService;
import com.qsj.qsjMain.remote.service.EWXRobotRemoteService;
import com.qsj.qsjMain.remote.service.JuDanKeDeliveryRemoteService;
import com.qsj.qsjMain.remote.service.WXApiRemoteService;
import com.qsj.qsjMain.remote.service.model.dto.*;
import com.qsj.qsjMain.remote.service.model.vo.*;
import com.qsj.qsjMain.service.OrderService;
import com.qsj.qsjMain.service.impl.WechatPayService;
import com.qsj.qsjMain.utils.CredentialUtils;
import com.qsj.qsjMain.utils.SerialUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.security.cert.X509Certificate;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.regex.Pattern;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class QsjMainApplicationTests {

    @Autowired
    EWXRobotRemoteService ewxRobotRemoteService;

    @Autowired
    DaDaDeliveryRemoteService daDaDeliveryRemoteService;


    @Autowired
    JuDanKeDeliveryRemoteService juDanKeDeliveryRemoteService;

    @Autowired
    WechatPayService wechatPayService;

    @Autowired
    OrderService orderService;

    @Autowired
    WXApiRemoteService wxApiRemoteService;

    @Test
    void contextLoads() {
    }

    @Test
    void testEncrypt() {
        String content = "wuweiliang+zhegnmingsheng";
        String encrypt = CredentialUtils.encrypt(content);
        System.out.println(encrypt);
        String decrypt = CredentialUtils.decrypt(encrypt);
        System.out.println(decrypt);
    }

    @Test
    void testGenSn() {
        Set<String> snSet = new java.util.HashSet<>();
        Integer duplicateCount = 0;
        for (int i = 0; i < 10; i++) {
            String sn = SerialUtils.generateSN();
            if (snSet.contains(sn)) {
                duplicateCount++;
            }
            snSet.add(sn);
            System.out.println(sn);
        }
        System.out.println(duplicateCount);

    }

    @Test
    void testEWX() throws IOException {
        SendEWXRobotMessageDTO dto = new SendEWXRobotMessageDTO();
        dto.setMsgType("text");
        dto.setText(new SendEWXRobotMessageText("content"));
        ewxRobotRemoteService.getService().sendMessage(dto).execute();


    }

    @Test
    void testWeChatPayDownloadCert() {
        List<X509Certificate> certs = wechatPayService.getCertificateList();
        System.out.println(certs);
    }

    @Test
    void testDadaApi() throws IOException {
        QueryDeliveryFee queryDeliveryFee = new QueryDeliveryFee();
        queryDeliveryFee.setShopNo("1779b889a8fb46e0").setOriginId("QSJ_TEST_1234556453342").setCityCode("0755").
                setCargoPrice(10.00).setIsPrepay(1).setReceiverName("测试员").setReceiverAddress("测试地址").
                setReceiverLng(113.93).setReceiverLat(22.52).setCargoWeight(1.0).setCallback("http://127.0.0.1:80/api")
                .setReceiverPhone("13000000000");
        DaDaCommonReq<QueryDeliveryFee> req = new DaDaCommonReq<>();
        req.setBody(queryDeliveryFee);
        req.prepareBody();
        DaDaCommonResp<QueryDeliveryFeeResult> resp = daDaDeliveryRemoteService.getService().queryDeliverFee(req).execute().body();
        System.out.println(resp);
    }

    @Test
    void testJuDanKeApi() throws IOException {
        JuDanKeQueryDeliveryFee queryDeliveryFee = new JuDanKeQueryDeliveryFee();
        queryDeliveryFee.setShopId("1362").setThirdOrderNo("010100").setReceiverName("测试员")
                .setReceiverPhone("13000000000").setReceiverProvince("广东").setReceiverCity("深圳").setReceiverDistrict(
                        "南山区").setReceiverLng("113.93").setReceiverLat("22.52").setGoodsPrice(20).setGoodsTypeId(2)
                .setGoodsWeight(1).setGoodsQuantity(1).setDeliveryBrands(Arrays.asList("ddks", "sftc", "gxd", "kfw",
                        "ss", "apt", "fnpt", "mtpt", "uupt", "gxd", "blpt", "ccs")).setDeliveryRemark("").setReceiverAddress(
                        "测试地址");
//                setCargoPrice(10.00).setIsPrepay(1).setReceiverName("测试员").setReceiverAddress("测试地址").
//                setReceiverLng(113.93).setReceiverLat(22.52).set .setCallback("http://127.0.0.1:80/api")
//                .setReceiverPhone("13000000000");
        JuDanKeCommonReq<JuDanKeQueryDeliveryFee> req = new JuDanKeCommonReq<>("/Order/quotation");

        req.setBody(queryDeliveryFee);
        req.prepareBody();
        JuDanKeCommonResp<JuDanKeQueryDeliveryFeeResult> resp =
                juDanKeDeliveryRemoteService.getService().queryDeliverFee(req).execute().body();
        System.out.println(resp);
    }


    @Test
    void testJuDanKeCreateApi() throws IOException {
        JuDanKeCreateShop juDanKeCreateShop = new JuDanKeCreateShop();
        juDanKeCreateShop.setAddress("深圳南山区富利臻大厦三楼(领感空间内)").setCity("深圳").setDistrict("南山区")
                .setName("SAKKAD").setContactPerson("陈君辉").setProvince("广东").setGoodsTypeId("2").
                setLat("22.543006").setLng("113.949167").setPhone("13040860602");

        JuDanKeCommonReq<JuDanKeCreateShop> req = new JuDanKeCommonReq<>("/Shop/create");

        req.setBody(juDanKeCreateShop);
        req.prepareBody();
        JuDanKeCommonResp<JuDanKeCreateShopResult> resp =
                juDanKeDeliveryRemoteService.getService().createShop(req).execute().body();
        System.out.println(resp);
    }

    @Test
    void testGenerateQRCode() throws IOException {
        String qrCode = orderService.getQRCode("AAA", 123L, "");
        System.out.println(qrCode);
    }

    @Test
    void testGenerateDineInQRCode() {
        GetWXCodeUnLimitDTO dto = new GetWXCodeUnLimitDTO();
        dto.setScene("s=3");
        dto.setPage("pages/menu/menu");
        dto.setCheckPath(false);
        dto.setEnvVersion("trial");
        byte[] rawData;
        try {
            rawData = Objects.requireNonNull(wxApiRemoteService.getTokenService().getWXACodeUnLimit(dto).execute().body()).bytes();
        } catch (IOException e) {
            log.error("获取小程序码失败", e);
            return;
        }
        // covert to base64
        System.out.println(Base64.getEncoder().encodeToString(rawData));
    }

    @Test
    void testPaddingZeroId() {
        String testStr = "000000023";
        Integer result = Integer.parseInt(testStr);
        System.out.println(result);
    }

    @Test
    public void getQRCode() throws IOException {
        GetWXCodeUnLimitDTO dto = new GetWXCodeUnLimitDTO();
        dto.setScene("s=1");
        dto.setEnvVersion("release");
        dto.setPage("pages/menu/menu");
        byte[] rawData;
        rawData = Objects.requireNonNull(wxApiRemoteService.getTokenService().
                getWXACodeUnLimit(dto).execute().body()).bytes();

        // covert to base64
        System.out.println(Base64.getEncoder().encodeToString(rawData));
    }

    @Test
    public void testRE() {
        String command="设置停止营业 1\n";
        Pattern pattern = Pattern.compile("设置停止营业 \\d+");
        assert pattern.matcher(command).find();
    }

    @Test
    public void testEpochSecond() {
        Long timestamp = LocalDateTime.of(LocalDate.of(2023,4,6), LocalTime.of(13, 50, 10).minusMinutes(15)).toEpochSecond(ZoneOffset.of(
                "+8")) * 1000;
        System.out.println(timestamp);
    }
}