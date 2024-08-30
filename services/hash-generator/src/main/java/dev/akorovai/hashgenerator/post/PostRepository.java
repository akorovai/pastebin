package dev.akorovai.hashgenerator.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post,String> {
	boolean existsByHash( String hash );

	Optional<Post> findByHash( String hash);

	List<Post> findByExpiresDateBeforeAndActiveTrue( LocalDateTime currentTime);
	@Modifying
	@Query("DELETE FROM Post p WHERE p.active = false AND p.expiresDate < :oneWeekAgo")
	void deleteInactivePostsOlderThanWeek(@Param("oneWeekAgo") LocalDateTime oneWeekAgo);
}
