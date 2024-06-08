package org.bohdanzhuvak.nicoai.model;

import jakarta.persistence.*;
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
  @Column(columnDefinition = "TEXT")
  private String prompt;
  @Column(columnDefinition = "TEXT")
  private String negativePrompt;
  private Integer height;
  private Integer width;
  private Integer numInterferenceSteps;
  private Integer guidanceScale;
}
