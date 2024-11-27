package ru.test.taskservice.repository;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.test.taskservice.exception.NotFoundException;
import ru.test.taskservice.model.Task;

@Slf4j
@Repository
public class TaskDataRepository {
    final TaskRepository taskRepository;

    @Autowired
    public TaskDataRepository(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Autowired
    EntityManager entityManager;
    @Transactional
    public void save(Task t){
        entityManager.persist(t);
    }

    public Task get(String name){
        return taskRepository.findById(name).orElseThrow(()->new NotFoundException(name));
    }

    public Task updateForWorker(){
        return taskRepository.updateForWorker();
    }

    public void setDone(Task t){
         taskRepository.setDone(t.getName());
    }
}
