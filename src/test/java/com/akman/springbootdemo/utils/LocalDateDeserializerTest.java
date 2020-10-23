package com.akman.springbootdemo.utils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class LocalDateDeserializerTest {
    private ObjectMapper mapper;
    private LocalDateDeserializer deserializer;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
        deserializer = new LocalDateDeserializer();
    }

    @Test
    public void floating_point_string_deserialize_to_Double_value() {
        String json = String.format("{\"value\":%s}", "\"2020-07-01\"");
        LocalDate deserializeNumber = deserializeNumber(json);
        assertThat(deserializeNumber, instanceOf(LocalDate.class));
        assertThat(deserializeNumber, is(equalTo(LocalDate.of(2020, 7, 1))));
    }

    @SneakyThrows({JsonParseException.class, IOException.class})
    private LocalDate deserializeNumber(String json) {
        InputStream stream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
        JsonParser parser = mapper.getFactory().createParser(stream);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        return deserializer.deserialize(parser, ctxt);
    }
}
