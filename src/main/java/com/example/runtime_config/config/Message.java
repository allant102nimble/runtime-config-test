package com.example.runtime_config.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
public class Message implements Serializable {
    public static final String EVENT_TYPE = "EVENT_TYPE";
    @JsonProperty
    private Map<String, String> attributes;

    public static class MessageBuilder {

        private final Map<String, String> attributeMap;

        private MessageBuilder() {
            attributeMap = new HashMap<>();
        }

        private MessageBuilder(String type) {
            attributeMap = new HashMap<>();
            attributeMap.put(EVENT_TYPE, type);
        }

        public static MessageBuilder forType(@NonNull String type) {
            return new MessageBuilder(type);
        }

        public MessageBuilder withAttribute(@NonNull String key, @NonNull String value) {
            attributeMap.put(key, value);
            return this;
        }

        public Message build() {
            return new Message(attributeMap);
        }

    }
}
