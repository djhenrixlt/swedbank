INSERT INTO roles (id, name) VALUES
                                 (1, 'ROLE_USER'),
                                 (2, 'ROLE_ADMIN');

INSERT INTO swed_user (id, name, last_name, nick_name, password, email, is_active) VALUES
    (1, 'John', 'Doe', 'johndoe', '$2a$10$V3SUYuAGRjuhMyA2dUG1UOPjdx4oYxC.4xWVMBvOWhSv1YcYmu/cy', 'john.doe@example.com', true);

INSERT INTO user_roles (user_id, role_id) VALUES
    (1, 1);
