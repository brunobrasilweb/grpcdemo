package br.com.inoovexa.grpcdemo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
}
