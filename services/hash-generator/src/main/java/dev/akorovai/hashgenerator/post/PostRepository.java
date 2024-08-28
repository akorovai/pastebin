package dev.akorovai.hashgenerator.post;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post,String> {
	boolean existsByHash( String hash );

	Optional<Post> findByHash( String hash);
}
