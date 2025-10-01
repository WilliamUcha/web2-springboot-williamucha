package com.web2.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
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

import com.web2.dto.CategoriaDTO;
import com.web2.model.Categoria;
import com.web2.repository.CategoriaRepository;
import com.web2.repository.CursoRepository;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/categoria")
public class CategoriaController {
	
	@Autowired
	CategoriaRepository repository;
	
	@Autowired
	CursoRepository cursoRepository;
	
	@GetMapping("/inserir")
	public ModelAndView inserir() {
		ModelAndView mv = new ModelAndView("categoria/inserir");
		mv.addObject("categoria", new Categoria());
		return mv;
	}
	
	@PostMapping("/inserir")
	public String inserido(
			@ModelAttribute @Valid CategoriaDTO dto, 
			BindingResult result, 
			RedirectAttributes msg) {
		if(result.hasErrors()) {
			msg.addFlashAttribute("erro", "Erro ao inserir categoria!");
			return "redirect:/categoria/listar";
		}
		var categoria = new Categoria();		
		BeanUtils.copyProperties(dto, categoria);
		repository.save(categoria);
		msg.addFlashAttribute("sucesso", "Categoria inserida!");
		return "redirect:/categoria/listar";
	}
	
	@GetMapping("/listar")
	public ModelAndView listar() {
		ModelAndView mv = new ModelAndView("categoria/listar");
		List<Categoria> lista = repository.findAll();
		mv.addObject("categorias", lista);
		return mv;
	}
	
	@GetMapping("/excluir/{id}")
	public String excluir(@PathVariable(value="id") int id, RedirectAttributes msg) {
		Optional<Categoria> categoria = repository.findById(id);
		if(categoria.isEmpty()) {
			msg.addFlashAttribute("erro", "Categoria não encontrada!");
			return "redirect:/categoria/listar";			
		}
		
		long cursosVinculados = cursoRepository.countByCategoria(categoria.get());
		if(cursosVinculados > 0) {
			msg.addFlashAttribute("erro", 
				"Não é possível excluir esta categoria pois ela possui " + cursosVinculados + 
				" curso(s) vinculado(s). Exclua primeiro os cursos ou mude a categoria deles.");
			return "redirect:/categoria/listar";
		}
		
		repository.deleteById(id);
		msg.addFlashAttribute("sucesso", "Categoria excluída!");
		return "redirect:/categoria/listar";					
	}
	
	@GetMapping("/editar/{id}")
	public ModelAndView editar(@PathVariable(value="id") int id) {
		ModelAndView mv = new ModelAndView("categoria/editar");
		Optional<Categoria> categoria = repository.findById(id);
		if(categoria.isPresent()) {
			mv.addObject("categoria", categoria.get());
		}
		return mv;
	}
	
	@PostMapping("/editar/{id}")
	public String editado(
			@ModelAttribute @Valid CategoriaDTO dto, 
			BindingResult result, 
			RedirectAttributes msg,
			@PathVariable(value="id") int id) {
		if(result.hasErrors()) {
			msg.addFlashAttribute("erro", "Erro ao editar categoria!");
			return "redirect:/categoria/listar";
		}
		Optional<Categoria> categoria = repository.findById(id);
		if(categoria.isPresent()) {
			var categoria2 = categoria.get();
			BeanUtils.copyProperties(dto, categoria2);
			repository.save(categoria2);
			msg.addFlashAttribute("sucesso", "Categoria editada!");
		}
		return "redirect:/categoria/listar";
	}
}
