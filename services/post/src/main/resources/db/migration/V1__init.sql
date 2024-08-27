CREATE TABLE post (
                        id BIGINT PRIMARY KEY,
                        user_id UUID NOT NULL,
                        title VARCHAR(255),
                        content TEXT NOT NULL,
                        language VARCHAR(50),
                        created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                        expires_at TIMESTAMP WITH TIME ZONE,
                        is_public BOOLEAN DEFAULT true,
                        view_count INT DEFAULT 0,
                        hash VARCHAR(64)
);