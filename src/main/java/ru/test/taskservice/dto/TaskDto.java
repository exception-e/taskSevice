package ru.test.taskservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Schema(description = "Dto for saving message from kafka to Postgres")
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto implements Serializable {

    @Schema(description = "Unique task name")
    String name;

    @Schema(description = "Task duration in ms", example = "3000")
    Integer duration;
}