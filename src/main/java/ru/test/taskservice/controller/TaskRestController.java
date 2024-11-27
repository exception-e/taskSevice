package ru.test.taskservice.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.test.taskservice.Util;
import ru.test.taskservice.dto.TaskRestDto;
import ru.test.taskservice.model.Task;
import ru.test.taskservice.service.TaskService;

@Slf4j
@Tag(name = "TaskRestController", description = "Returns tasks name and current status")
@RestController
@RequestMapping(value = TaskRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class TaskRestController {
    static final String REST_URL = "/rest/tasks";

    @Autowired
    private TaskService service;

    @GetMapping("/{name}")
    public TaskRestDto get(@PathVariable String name) {

        log.info("get task {}", name);
        return Util.TaskRestDtoFromTask(service.get(name));
    }
}
