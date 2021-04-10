package com.margarine.server.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.boot.web.server.LocalServerPort;


@Data
public class ServerInfoDTO {

    @LocalServerPort @JsonProperty("serverPort")
    private int port;

    @JsonProperty("serverIP")
    private String ip;

    @JsonProperty("serverName")
    private String name;

    public ServerInfoDTO () {
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
