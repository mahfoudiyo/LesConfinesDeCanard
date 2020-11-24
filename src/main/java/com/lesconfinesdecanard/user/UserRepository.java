package com.lesconfinesdecanard.user;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository <User, Integer> {

    User findByPseudoAndPassword (String pseudo, String password);

}
