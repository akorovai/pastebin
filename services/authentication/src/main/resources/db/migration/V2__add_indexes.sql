CREATE UNIQUE INDEX idx_user_nickname ON _user (nickname);

CREATE INDEX idx_user_created_date ON _user (     created_date  );
CREATE INDEX idx_user_last_modified_date ON _user (last_modified_date);

CREATE INDEX idx_user_roles_user_id ON _user_roles (user_id);
CREATE INDEX idx_user_roles_role_id ON _user_roles (roles_id);