package com.example.demo.tests;

import com.example.demo.backend.domain.Patient;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PatientControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  private static Long patientId;

  @Test
  @Order(1)
  public void findAll() throws Exception {
    mockMvc.perform(get("/patient/all"))
            .andExpect(status().isOk());
  }

  @Test
  @Order(2)
  public void add() throws Exception {
    Patient patient = new Patient();
    patient.setName("Alexander");
    patient.setSurname("Pishulev");

    MvcResult result = this.mockMvc.perform(post("/patient")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(patient)))
            .andExpect(status().isOk())
            .andReturn();

    Pattern p = Pattern.compile("id ([0-9]+)");
    Matcher m = p.matcher(result.getResponse().getContentAsString());
    if (m.find()) {
      patientId = Long.parseLong(m.group(1));
    }
  }

  @Test
  @Order(3)
  public void find() throws Exception {
    mockMvc.perform(get("/patient/" + patientId))
            .andExpect(status().isOk())
            .andExpect(content().json("{'surname':'Pishulev','name':'Alexander'}"));
  }

  @Test
  @Order(4)
  public void update() throws Exception {
    Patient patient = new Patient();
    patient.setName("Dmitriy");
    patient.setSurname("Osinov");

    MvcResult result = this.mockMvc.perform(put("/patient/" + patientId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(patient)))
            .andExpect(status().isOk())
            .andReturn();

    Pattern p = Pattern.compile("id ([0-9]+)");
    Matcher m = p.matcher(result.getResponse().getContentAsString());
    if (m.find()) {
      patientId = Long.parseLong(m.group(1));
    }
  }

  @Test
  @Order(5)
  public void edit() throws Exception {
    Patient patient = new Patient();
    patient.setName("Misha");
    mockMvc.perform(patch("/patient/" + patientId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(patient)))
            .andExpect(status().isOk());
  }

  @Test
  @Order(6)
  public void del() throws Exception {
    mockMvc.perform(delete("/patient/" + patientId))
            .andExpect(status().isOk());
  }

}
