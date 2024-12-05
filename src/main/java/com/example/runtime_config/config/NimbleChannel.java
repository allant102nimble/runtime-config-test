package com.example.runtime_config.config;

import lombok.Getter;
import org.springframework.data.redis.listener.ChannelTopic;

@Getter
public enum NimbleChannel {
    TEST_CHANNEL (ChannelTopic.of("TEST_CHANNEL"));

    ChannelTopic channelTopic;

    NimbleChannel(ChannelTopic channelTopic) {
        this.channelTopic = channelTopic;
    }
}
