package com.web2.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.web2.dto.CursoDTO;
import com.web2.model.Categoria;
import com.web2.model.Curso;
import com.web2.model.Professor;
import com.web2.repository.CategoriaRepository;
import com.web2.repository.CursoRepository;
import com.web2.repository.ProfessorRepository;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/curso")
public class CursoController {
	
	@Autowired
	CursoRepository repository;
	
	@Autowired
	CategoriaRepository categoriaRepository;
	
	@Autowired
	ProfessorRepository professorRepository;
	
	@GetMapping("/inserir")
	public ModelAndView inserir() {
		var mv = new ModelAndView("curso/inserir");
		mv.addObject("curso", new CursoDTO(null, null, null, null, null, null, null, null, null));
		mv.addObject("categorias", categoriaRepository.findAll());
		mv.addObject("professores", professorRepository.findAll());
		return mv;
	}
	
	@PostMapping("/inserir")
	public String inserir(@Valid @ModelAttribute("curso") CursoDTO dto, BindingResult result,
			RedirectAttributes msg) {
		
		if(result.hasErrors()) {
			return "curso/inserir";
		}
		
		Optional<Categoria> categoria = categoriaRepository.findById(dto.categoriaId());
		Optional<Professor> professor = professorRepository.findById(dto.professorId());
		
		if(categoria.isEmpty() || professor.isEmpty()) {
			msg.addFlashAttribute("erro", "Categoria ou Professor não encontrados!");
			return "redirect:/curso/listar";
		}
		
		var curso = new Curso();
		curso.setNome(dto.nome());
		curso.setDescricao(dto.descricao());
		curso.setDescricaoCompleta(dto.descricaoCompleta());
		curso.setCargaHoraria(dto.cargaHoraria());
		curso.setPreco(dto.preco());
		curso.setDataInicio(dto.dataInicio());
		curso.setDataFinal(dto.dataFinal());
		curso.setCategoria(categoria.get());
		curso.setProfessor(professor.get());
		
		repository.save(curso);
		msg.addFlashAttribute("sucesso", "Curso inserido!");
		return "redirect:/curso/listar";
	}
	

	
	@GetMapping("/listar")
	public ModelAndView listar() {
		var mv = new ModelAndView("curso/listar");
		mv.addObject("cursos", repository.findAll());
		return mv;
	}
	
	@GetMapping("/editar/{id}")
	public ModelAndView editar(@PathVariable Integer id) {
		System.out.println("DEBUG: Tentando editar curso com ID: " + id);
		
		try {
			var curso = repository.findById(id);
			if(curso.isEmpty()) {
				System.out.println("DEBUG: Curso não encontrado, redirecionando");
				return new ModelAndView("redirect:/curso/listar");
			}
			
		var c = curso.get();
		System.out.println("DEBUG: Curso encontrado: " + c.getNome());
		
		Integer categoriaId = (c.getCategoria() != null) ? c.getCategoria().getId() : null;
		Integer professorId = (c.getProfessor() != null) ? c.getProfessor().getId() : null;			System.out.println("DEBUG: CategoriaId: " + categoriaId + ", ProfessorId: " + professorId);
			
			var dto = new CursoDTO(c.getNome(), c.getDescricao(), c.getDescricaoCompleta(),
					c.getCargaHoraria(), c.getPreco(), c.getDataInicio(), c.getDataFinal(),
					categoriaId, professorId);
			
			var mv = new ModelAndView("curso/editar");
			mv.addObject("curso", dto);
			mv.addObject("id", id);
			mv.addObject("categorias", categoriaRepository.findAll());
			mv.addObject("professores", professorRepository.findAll());
			
			System.out.println("DEBUG: Retornando ModelAndView para curso/editar");
			return mv;
			
		} catch (Exception e) {
			System.out.println("DEBUG: Erro ao editar curso: " + e.getMessage());
			e.printStackTrace();
			return new ModelAndView("redirect:/curso/listar");
		}
	}
	
	@PostMapping("/editar/{id}")
	public String editar(@PathVariable Integer id, @Valid @ModelAttribute("curso") CursoDTO dto,
			BindingResult result, RedirectAttributes msg) {
		
		if(result.hasErrors()) {
			return "curso/editar";
		}
		
		Optional<CursoDTO> cursoOpt = repository.findById(id).map(c -> 
			new CursoDTO(c.getNome(), c.getDescricao(), c.getDescricaoCompleta(),
					c.getCargaHoraria(), c.getPreco(), c.getDataInicio(), c.getDataFinal(),
					c.getCategoria().getId(), c.getProfessor().getId())
		);
		
		if(cursoOpt.isEmpty()) {
			msg.addFlashAttribute("erro", "Curso não encontrado!");
			return "redirect:/curso/listar";
		}
		
		Optional<Categoria> categoria = categoriaRepository.findById(dto.categoriaId());
		Optional<Professor> professor = professorRepository.findById(dto.professorId());
		
		if(categoria.isEmpty() || professor.isEmpty()) {
			msg.addFlashAttribute("erro", "Categoria ou Professor não encontrados!");
			return "redirect:/curso/listar";
		}
		
		var curso = repository.findById(id).get();
		curso.setNome(dto.nome());
		curso.setDescricao(dto.descricao());
		curso.setDescricaoCompleta(dto.descricaoCompleta());
		curso.setCargaHoraria(dto.cargaHoraria());
		curso.setPreco(dto.preco());
		curso.setDataInicio(dto.dataInicio());
		curso.setDataFinal(dto.dataFinal());
		curso.setCategoria(categoria.get());
		curso.setProfessor(professor.get());
		
		repository.save(curso);
		msg.addFlashAttribute("sucesso", "Curso editado!");
		return "redirect:/curso/listar";
	}
	
	@GetMapping("/excluir/{id}")
	public String excluir(@PathVariable(value="id") int id, RedirectAttributes msg) {
		Optional<Curso> curso = repository.findById(id);
		if(curso.isEmpty()) {
			msg.addFlashAttribute("erro", "Curso não encontrado!");
			return "redirect:/curso/listar";			
		}
		
		repository.deleteById(id);
		msg.addFlashAttribute("sucesso", "Curso excluído!");
		return "redirect:/curso/listar";					
	}
}
