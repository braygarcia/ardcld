CREATE TABLE ardc_level (
  id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(128) NOT NULL,
  description VARCHAR(128) NOT NULL,
  creation_date DATETIME NOT NULL,
  creator_id INT NOT NULL,
  active BIT NOT NULL DEFAULT 1,
  PRIMARY KEY (id),
  CONSTRAINT fk_ardc_level_user
  FOREIGN KEY (creator_id)
  REFERENCES system_user (user_id)
);

CREATE TABLE professional (
  id INT NOT NULL AUTO_INCREMENT,
  us_number VARCHAR(10) NOT NULL,
  name VARCHAR(128) NOT NULL,
  last_name VARCHAR(128) NOT NULL,
  email VARCHAR(128) NOT NULL,
  level_id INT NOT NULL,
  creation_date DATETIME NOT NULL,
  creator_id INT NOT NULL,
  active BIT NOT NULL DEFAULT 1,
  PRIMARY KEY (id),
  CONSTRAINT fk_professional_user
  FOREIGN KEY (creator_id)
  REFERENCES system_user (user_id),
  CONSTRAINT fk_professional_level
  FOREIGN KEY (level_id)
  REFERENCES ardc_level (id)
);

CREATE TABLE community (
  id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(128) NOT NULL,
  description VARCHAR(256) NOT NULL,
  leader_id INT NOT NULL,
  study_group_leader_id INT NOT NULL,
  creation_date DATETIME NOT NULL,
  creator_id INT NOT NULL,
  active BIT NOT NULL DEFAULT 1,
  PRIMARY KEY (id),
  CONSTRAINT fk_community_user
  FOREIGN KEY (creator_id)
  REFERENCES system_user (user_id),
  CONSTRAINT fk_community_leader
  FOREIGN KEY (leader_id)
  REFERENCES professional (id),
  CONSTRAINT fk_community_study_group_leader
  FOREIGN KEY (study_group_leader_id)
  REFERENCES professional (id)
);

CREATE TABLE community_members (
  id INT NOT NULL AUTO_INCREMENT,
  community_id INT NOT NULL,
  member_id INT NOT NULL,
  CONSTRAINT fk_community_member
  FOREIGN KEY (community_id)
  REFERENCES community (id),
  CONSTRAINT fk_community_member_professional
  FOREIGN KEY (member_id)
  REFERENCES professional (id)
);

CREATE TABLE certification (
  id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(128) NOT NULL,
  description VARCHAR(256) NOT NULL,
  cost DOUBLE NOT NULL,
  creation_date DATETIME NOT NULL,
  creator_id INT NOT NULL,
  active BIT NOT NULL DEFAULT 1,
  PRIMARY KEY (id),
  CONSTRAINT fk_certification_user
  FOREIGN KEY (creator_id)
  REFERENCES system_user (user_id)
);

CREATE TABLE study_group (
  id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(128) NOT NULL,
  community_id INT NOT NULL,
  leader_id INT NOT NULL,
  certification_id INT NOT NULL,
  creation_date DATETIME NOT NULL,
  creator_id INT NOT NULL,
  active BIT NOT NULL DEFAULT 1,
  PRIMARY KEY (id),
  CONSTRAINT fk_study_group_user
  FOREIGN KEY (creator_id)
  REFERENCES system_user (user_id),
  CONSTRAINT fk_study_group_community
  FOREIGN KEY (community_id)
  REFERENCES community (id),
  CONSTRAINT fk_study_group_professional
  FOREIGN KEY (leader_id)
  REFERENCES professional (id),
  CONSTRAINT fk_study_group_certification
  FOREIGN KEY (certification_id)
  REFERENCES certification (id)
);

CREATE TABLE study_group_members (
  id INT NOT NULL AUTO_INCREMENT,
  study_group_id INT NOT NULL,
  member_id INT NOT NULL,
  CONSTRAINT fk_study_group
  FOREIGN KEY (study_group_id)
  REFERENCES study_group (id),
  CONSTRAINT fk_study_group_member
  FOREIGN KEY (member_id)
  REFERENCES professional (id)
);
