package ru.test.taskservice;

import ru.test.taskservice.dto.TaskDto;
import ru.test.taskservice.dto.TaskRestDto;
import ru.test.taskservice.model.Status;
import ru.test.taskservice.model.Task;

import java.time.LocalDateTime;

public class Util {

    private Util(){
    }

    public static Task TaskFromDto(TaskDto to){
        return new Task(to.getName(), to.getDuration(), Status.NEW, LocalDateTime.now(),LocalDateTime.now());
    }

    public static TaskRestDto TaskRestDtoFromTask(Task task){
        return new TaskRestDto(task.getName(), task.getStatus(), task.getUpdated_at());
    }

    public static boolean valid(TaskDto dto){
        return !dto.getName().trim().isEmpty()
                && dto.getDuration() != null
                && dto.getDuration()<10000
                && dto.getDuration() >0;
    }
}
