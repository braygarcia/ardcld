package com.ardc.ld.service;

import com.ardc.ld.api.InvalidArgumentException;
import com.ardc.ld.api.NotFoundException;
import com.ardc.ld.model.Student;
import com.ardc.ld.model.User;
import com.ardc.ld.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;

    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    public Student findOne(Long id) {
        Optional<Student> student = studentRepository.findById(id);
        if (!student.isPresent()) {
            throw new NotFoundException();
        }
        return student.get();
    }

    public Student save(Student student, User creator) {
        student.setActive(true);
        student.setCreator(creator);
        student.setCreationDate(new Date());
        return studentRepository.save(student);
    }

    public Student edit(Student student, Long id) {
        Student exist = findOne(id);
        if (exist == null) {
            throw new NotFoundException("STUDENT not found.");
        }
        student.setActive(exist.getActive());
        student.setCreator(exist.getCreator());
        student.setCreationDate(exist.getCreationDate());
        return studentRepository.save(student);
    }

    public void delete(Long id) {
        Student exist = findOne(id);
        if (exist == null) {
            throw new NotFoundException("STUDENT not found.");
        }
        try {
            studentRepository.deleteById(id);
        } catch (Exception e) {
            throw new InvalidArgumentException("STUDENT can not delete, parent key found.");
        }
    }
}
