package org.bohdanzhuvak.nicoai.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "prompts_data")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PromptData {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String prompt;
  private String negativePrompt;
  private Integer height;
  private Integer width;
  private Integer numInterferenceSteps;
  private Integer guidanceScale;
}
