package ru.test.taskservice.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.Type;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import java.time.LocalDateTime;

@Entity
@Table(name="tasks")
public class Task {

    @Column(name = "name", nullable = false, unique = true)
    @Id
    String name;
    @Column(name = "duration_ms", nullable = false)
    Integer duration;
    @Enumerated(EnumType.STRING)
    @Column(name="status")
    @JdbcType(PostgreSQLEnumJdbcType.class)
    Status status;
    @Column(name = "created_at", nullable = false)
    LocalDateTime created_at;
    @Column(name = "updated_at")
    LocalDateTime updated_at;

    public Task(String name, Integer duration, Status status, LocalDateTime created_at, LocalDateTime updated_at) {
        this.name = name;
        this.duration = duration;
        this.status = status;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public Task() {
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", duration=" + duration +
                ", status=" + status +
                ", created_at=" + created_at +
                ", updated_at=" + updated_at +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public LocalDateTime getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(LocalDateTime updated_at) {
        this.updated_at = updated_at;
    }
}
