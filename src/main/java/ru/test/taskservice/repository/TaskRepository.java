package ru.test.taskservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.test.taskservice.model.Task;

@Transactional(readOnly = true)
public interface TaskRepository extends JpaRepository<Task, String> {

    @Transactional
    @Query(value = "UPDATE tasks " +
            "SET status='PROCESSING'::status, updated_at = now() " +
            "WHERE name IN (SELECT name" +
            "               FROM tasks t" +
            "               WHERE t.status = 'NEW'::status" +
            "                  OR ( t.status = 'PROCESSING'::status" +
            "                       AND now() - t.updated_at > INTERVAL '10' second)" +
            "                  ORDER BY t.created_at ASC" +
            "                  LIMIT 1 FOR UPDATE SKIP LOCKED)" +
            "RETURNING name, duration_ms, status, created_at, updated_at", nativeQuery = true)
    Task updateForWorker();

    @Transactional
    @Modifying
    @Query(value="UPDATE tasks" +
            " SET status='DONE'::status," +
            "    updated_at = now() " +
            "WHERE name = :taskName", nativeQuery = true)
   void setDone(@Param("taskName") String name);

}
