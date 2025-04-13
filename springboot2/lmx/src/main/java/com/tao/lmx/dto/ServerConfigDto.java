package com.tao.lmx.dto;

import lombok.Data;

@Data
public class ServerConfigDto {
    private String serverIp;
    private String serverPort;
    private String xmlContent;
    private String target;
    private String daemonInfo;
    private String etcInfo1;
    private String etcInfo2;
    private String etcInfo3;
}