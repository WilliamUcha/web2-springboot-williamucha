package com.web2.controller;

import java.io.IOException;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.web2.dto.ProfessorDTO;
import com.web2.model.Professor;
import com.web2.repository.ProfessorRepository;
import com.web2.service.FileUploadService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/professor")
public class ProfessorController {
	
	@Autowired
	ProfessorRepository repository;
	
	@Autowired
	FileUploadService fileUploadService;
	
	@Autowired
	com.web2.repository.CursoRepository cursoRepository;
	
	@GetMapping("/inserir")
	public ModelAndView inserir() {
		ModelAndView mv = new ModelAndView("professor/inserir");
		mv.addObject("professor", new Professor());
		return mv;
	}
	
	@PostMapping("/inserir")
	public String inserido(
			@ModelAttribute @Valid ProfessorDTO dto, 
			BindingResult result, 
			@RequestParam(value = "imagemFile", required = false) MultipartFile imagemFile,
			RedirectAttributes msg) {
		if(result.hasErrors()) {
			msg.addFlashAttribute("erro", "Erro ao inserir professor!");
			return "redirect:/professor/listar";
		}
		
		try {
		var professor = new Professor();		
		BeanUtils.copyProperties(dto, professor);
		
		if (imagemFile != null && !imagemFile.isEmpty()) {
			String imagemPath = fileUploadService.uploadFile(imagemFile);
			professor.setImagem(imagemPath);
		}			repository.save(professor);
			msg.addFlashAttribute("sucesso", "Professor inserido!");
		} catch (IOException e) {
			msg.addFlashAttribute("erro", "Erro ao fazer upload da imagem!");
			return "redirect:/professor/listar";
		}
		return "redirect:/professor/listar";
	}
	
	@GetMapping("/listar")
	public ModelAndView listar() {
		ModelAndView mv = new ModelAndView("professor/listar");
		List<Professor> lista = repository.findAll();
		mv.addObject("professores", lista);
		return mv;
	}
	
	@GetMapping("/excluir/{id}")
	public String excluir(@PathVariable(value="id") int id, RedirectAttributes msg) {
		Optional<Professor> professor = repository.findById(id);
		if(professor.isEmpty()) {
			msg.addFlashAttribute("erro", "Professor não encontrado!");
			return "redirect:/professor/listar";			
		}
		
		long cursosVinculados = cursoRepository.countByProfessor(professor.get());
		if(cursosVinculados > 0) {
			msg.addFlashAttribute("erro", 
				"Não é possível excluir este professor pois ele possui " + cursosVinculados + 
				" curso(s) vinculado(s). Exclua primeiro os cursos ou mude o professor deles.");
			return "redirect:/professor/listar";
		}
		
		if (professor.get().getImagem() != null) {
			fileUploadService.deleteFile(professor.get().getImagem());
		}
		
		repository.deleteById(id);
		msg.addFlashAttribute("sucesso", "Professor excluído!");
		return "redirect:/professor/listar";					
	}
	
	@GetMapping("/editar/{id}")
	public ModelAndView editar(@PathVariable(value="id") int id) {
		ModelAndView mv = new ModelAndView("professor/editar");
		Optional<Professor> professor = repository.findById(id);
		if(professor.isPresent()) {
			mv.addObject("id", professor.get().getId());
			mv.addObject("nome", professor.get().getNome());
			mv.addObject("email", professor.get().getEmail());
			mv.addObject("telefone", professor.get().getTelefone());
			mv.addObject("especializacao", professor.get().getEspecializacao());
			mv.addObject("curriculo", professor.get().getCurriculo());
			mv.addObject("imagem", professor.get().getImagem());
		}
		return mv;
	}
	
	@PostMapping("/editar/{id}")
	public String editado(
			@ModelAttribute @Valid ProfessorDTO dto, 
			BindingResult result, 
			@RequestParam(value = "imagemFile", required = false) MultipartFile imagemFile,
			RedirectAttributes msg,
			@PathVariable(value="id") int id) {
		if(result.hasErrors()) {
			msg.addFlashAttribute("erro", "Erro ao editar professor!");
			return "redirect:/professor/listar";
		}
		
		try {
			Optional<Professor> professor = repository.findById(id);
			if(professor.isPresent()) {
				var professor2 = professor.get();
				String imagemAnterior = professor2.getImagem();
				
				BeanUtils.copyProperties(dto, professor2);
				
				if (imagemFile != null && !imagemFile.isEmpty()) {
					if (imagemAnterior != null) {
						fileUploadService.deleteFile(imagemAnterior);
					}
					String novaImagem = fileUploadService.uploadFile(imagemFile);
					professor2.setImagem(novaImagem);
				} else if (imagemAnterior != null) {
					professor2.setImagem(imagemAnterior);
				}
				
				repository.save(professor2);
				msg.addFlashAttribute("sucesso", "Professor editado!");
			}
		} catch (IOException e) {
			msg.addFlashAttribute("erro", "Erro ao fazer upload da imagem!");
		}
		return "redirect:/professor/listar";
	}
}
