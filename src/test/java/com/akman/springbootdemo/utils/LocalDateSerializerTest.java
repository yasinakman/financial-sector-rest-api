package com.akman.springbootdemo.utils;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.time.LocalDate;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class LocalDateSerializerTest {
    @Test
    public void serialises_LocalDateTime() throws JsonProcessingException, IOException {
        Writer jsonWriter = new StringWriter();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(jsonWriter);
        SerializerProvider serializerProvider = new ObjectMapper().getSerializerProvider();
        new LocalDateSerializer().serialize(LocalDate.of(2000, 1, 1), jsonGenerator, serializerProvider);
        jsonGenerator.flush();
        assertThat(jsonWriter.toString(), is(equalTo("\"2000-01-01\"")));
    }
}
