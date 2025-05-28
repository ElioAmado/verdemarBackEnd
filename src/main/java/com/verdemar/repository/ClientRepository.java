package com.verdemar.repository;

import com.verdemar.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Integer> {
    // Aquí puedes agregar métodos personalizados si lo necesitas
}
