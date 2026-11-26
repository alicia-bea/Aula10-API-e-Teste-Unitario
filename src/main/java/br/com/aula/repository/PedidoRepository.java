package br.com.aula.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.aula.model.PedidoModel;

public interface PedidoRepository extends JpaRepository<PedidoModel, Long> {

}
