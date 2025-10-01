package com.web2.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import com.web2.model.Categoria;
import com.web2.model.Curso;
import com.web2.repository.CategoriaRepository;
import com.web2.repository.CursoRepository;

@Controller
public class HomeController {
	
	@Autowired
	CategoriaRepository categoriaRepository;
	
	@Autowired
	CursoRepository cursoRepository;
	
	@GetMapping("/")
	public ModelAndView index() {
		ModelAndView mv = new ModelAndView("index");
		List<Categoria> categorias = categoriaRepository.findAll();
		List<Curso> cursosDestaque = cursoRepository.findAll();
		mv.addObject("categorias", categorias);
		mv.addObject("cursosDestaque", cursosDestaque);
		return mv;
	}
	
	@GetMapping("/home")
	public ModelAndView home() {
		ModelAndView mv = new ModelAndView("index");
		List<Categoria> categorias = categoriaRepository.findAll();
		List<Curso> cursosDestaque = cursoRepository.findAll();
		mv.addObject("categorias", categorias);
		mv.addObject("cursosDestaque", cursosDestaque);
		return mv;
	}
	

	
	@GetMapping("/categoria/{id}")
	public ModelAndView cursosPorCategoria(@PathVariable("id") int id) {
		ModelAndView mv = new ModelAndView("cursos-categoria");
		Categoria categoria = categoriaRepository.findById(id).orElse(null);
		if (categoria != null) {
			List<Curso> cursos = cursoRepository.findCursoByCategoria(categoria);
			mv.addObject("categoria", categoria);
			mv.addObject("cursos", cursos);
		}
		List<Categoria> categorias = categoriaRepository.findAll();
		mv.addObject("categorias", categorias);
		return mv;
	}
	
	@GetMapping("/curso/{id}")
	public ModelAndView detalhesCurso(@PathVariable("id") int id) {
		ModelAndView mv = new ModelAndView("curso-detalhe");
		Curso curso = cursoRepository.findById(id).orElse(null);
		if (curso != null) {
			mv.addObject("curso", curso);
		}
		List<Categoria> categorias = categoriaRepository.findAll();
		mv.addObject("categorias", categorias);
		return mv;
	}
}
