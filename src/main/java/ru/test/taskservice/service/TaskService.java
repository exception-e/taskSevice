package ru.test.taskservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.test.taskservice.Util;
import ru.test.taskservice.dto.TaskDto;
import ru.test.taskservice.model.Task;
import ru.test.taskservice.repository.TaskDataRepository;

@Slf4j
@Service
public class TaskService {
    private final ObjectMapper objectMapper;
    private final TaskDataRepository repository;
    private final TaskExecutor taskExecutor;

    @Autowired
    public TaskService(
            KafkaTemplate<String, TaskDto> kafkaTaskTemplate,
            ObjectMapper objectMapper,
            TaskDataRepository repository,
            TaskExecutor taskExecutor) {
        this.objectMapper = objectMapper;
        this.repository = repository;
        this.taskExecutor = taskExecutor;

        Thread pollerThread = new Thread(new TaskPoller());
        pollerThread.start();
    }

    public Task get(String name){
        return repository.get(name);
    }


    @KafkaListener(id = "Task", topics = {"${kafka.group.id}"}, containerFactory = "singleFactory")
    public void consume(TaskDto dto) {
        log.info("=> consumed {}", writeValueAsString(dto));
        if (!Util.valid(dto)) {
            log.info("Invalid task skipped: " + dto);
            return;
        }

            Task task = Util.TaskFromDto(dto);

            try {
                repository.save(task);
                log.info("Task created: " + task.getName());
            } catch (org.springframework.dao.DataIntegrityViolationException e) {
                log.info("Task skipped (duplicate): " + task.getName());
            }
    }

    private class TaskPoller implements Runnable {
        public void run() {
            log.info("TaskPoller: started ...");
            while (true) {
                Task task = repository.updateForWorker();
                if (task == null) {
                    try {
                        Thread.sleep(500);
                    }
                    catch (Exception e) {
                        log.warn("TaskPoller: thread.sleep exception:");
                        e.printStackTrace();
                    }
                    continue;
                }

                log.info("TaskPoller: task polled: " + task.getName());
                try {
                    taskExecutor.execute(new TaskWorker(task));
                }
                catch (Exception e) {
                    log.warn("Task not accepted by the threadExecutor:");
                    e.printStackTrace();
                }
            }
        }
    }

    private class TaskWorker implements Runnable {
        private final Task task;

        public TaskWorker(Task task) {
            this.task = task;
        }

        public void run() {
            log.info("Execution starts:  task " + task.getName());
            try {
                Thread.sleep(task.getDuration());
            }
            catch (Exception e) {
                log.warn("TaskWorker: thread.sleep exception:");
                e.printStackTrace();
            }
            log.info("Execution done:  task " + task.getName());
            repository.setDone(task);
        }
    }

    private String writeValueAsString(TaskDto dto) {
        try {
            return objectMapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException("Writing value to JSON failed: " + dto.toString());
        }
    }
}
