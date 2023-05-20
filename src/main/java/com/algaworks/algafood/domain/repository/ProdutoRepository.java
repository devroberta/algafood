package com.algaworks.algafood.domain.repository;

import com.algaworks.algafood.domain.model.Produto;
import com.algaworks.algafood.domain.model.Restaurante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    @Query(value = "SELECT * FROM produto " +
            "INNER JOIN restaurante ON produto.restaurante_id = restaurante.id " +
            "WHERE produto.restaurante_id = :restauranteId and produto.id = :produtoId", nativeQuery = true)
    Optional<Produto> findById(@Param("restauranteId") Long restauranteId,
                               @Param("produtoId") Long produtoId);

//    @Query(value = "SELECT * FROM produto " +
//            "INNER JOIN restaurante ON produto.restaurante_id = restaurante.id " +
//            "WHERE produto.restaurante_id = :restauranteId;", nativeQuery = true)
    List<Produto> findTodosByRestaurante(Restaurante restaurante);

    @Query("from Produto p where p.ativo = true and p.restaurante = :restaurante")
    List<Produto> findAtivosByRestaurante(Restaurante restaurante);
}
