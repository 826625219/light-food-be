package com.qsj.qsjMain.model.vo.ewx;


import lombok.Data;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement(name = "xml")
public class EWXTextCallbackMsg{
    @XmlElement
    private String Content;
    @XmlElement
    private String ToUserName;
    @XmlElement
    private String FromUserName;
    @XmlElement
    private Long CreateTime;
    @XmlElement
    private String MsgType;
    @XmlElement
    private Long MsgId;
}
