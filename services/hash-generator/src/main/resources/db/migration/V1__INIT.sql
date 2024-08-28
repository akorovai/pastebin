CREATE TABLE post (
                      hash VARCHAR(64) PRIMARY KEY,
                      user_id UUID NOT NULL,
                      s3_url VARCHAR(255),
                      language VARCHAR(50),
                      created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                      expires_at TIMESTAMP NOT NULL,
                      is_public BOOLEAN DEFAULT true,
                      view_count INT DEFAULT 0
);


CREATE UNIQUE INDEX IF NOT EXISTS idx_post_hash ON post (hash);