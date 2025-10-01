package com.web2.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CursoDTO(
		@NotBlank String nome,
		String descricao,
		String descricaoCompleta,
		Integer cargaHoraria,
		BigDecimal preco,
		LocalDate dataInicio,
		LocalDate dataFinal,
		@NotNull Integer categoriaId,
		@NotNull Integer professorId
		) {
}
