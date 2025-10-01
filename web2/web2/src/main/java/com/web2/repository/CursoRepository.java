package com.web2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.web2.model.Categoria;
import com.web2.model.Curso;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Integer>{
	List<Curso> findCursoByNomeLike(String nome);
	List<Curso> findCursoByCategoria(Categoria categoria);
	long countByCategoria(Categoria categoria);
	long countByProfessor(com.web2.model.Professor professor);
}
