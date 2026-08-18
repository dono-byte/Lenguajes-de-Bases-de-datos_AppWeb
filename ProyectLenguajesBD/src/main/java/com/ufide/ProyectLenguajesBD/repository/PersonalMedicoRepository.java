package com.ufide.ProyectLenguajesBD.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ufide.ProyectLenguajesBD.entity.PersonalMedico;
import java.util.Optional;

/**
 * Repository de PersonalMedico.
 */
public interface PersonalMedicoRepository extends JpaRepository<PersonalMedico, Integer> {
    Optional<PersonalMedico> findByCodigoMedico(String codigoMedico);
    Optional<PersonalMedico> findByCorreoElectronico(String correoElectronico);
}
