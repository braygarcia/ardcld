CREATE TABLE system_user (
  user_id INT NOT NULL AUTO_INCREMENT,
  username varchar(255) DEFAULT NULL,
  password varchar(255) DEFAULT NULL,
  name varchar(255) DEFAULT NULL,
  email varchar(255) DEFAULT NULL,
  phone_number varchar(10) DEFAULT NULL,
  creation_date DATETIME NULL,
  active BIT NOT NULL DEFAULT 0,
  creator_id bigint(20) NOT NULL,
  PRIMARY KEY (user_id)
);

INSERT INTO system_user
(user_id, username, password, name, email, phone_number, creation_date, active, creator_id)
VALUES
(1,'admin','$2a$10$s3xehPFBFciatObtXiLMse.xRh0RT.BUeKLqvslGrIX0NQVMlxg/u','Administrador','admin@ardc.com',NULL,now(),1,1),
(2,'user','$2a$10$yy9.pZ2ok1JwES3QUn0lXONEdJGFrku9YeCHNX3ZCiwPhHxaVbSt2','User','user@ardc.com',NULL,now(),1,1);

CREATE TABLE system_role (
  role_id INT NOT NULL AUTO_INCREMENT,
  name varchar(64) DEFAULT NULL,
  description varchar(512) DEFAULT NULL,
  PRIMARY KEY (role_id)
);

INSERT INTO system_role ( role_id, name, description )
	VALUES
	( 1, 'admin', 'Administrator'),
	( 2, 'user', 'User');

CREATE TABLE system_user_role (
  role_id INT NOT NULL,
  user_id INT NOT NULL,
  PRIMARY KEY (role_id, user_id),
  CONSTRAINT fk_role_user_role
    FOREIGN KEY (role_id)
    REFERENCES system_role (role_id),
  CONSTRAINT fk_user_user_role
    FOREIGN KEY (user_id)
    REFERENCES system_user (user_id)
);

INSERT INTO system_user_role ( role_id, user_id )
	VALUES ( 1, 1 ), ( 2, 2 );

