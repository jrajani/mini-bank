CREATE TABLE `association_value_entry` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `association_key` varchar(255) NOT NULL,
  `association_value` varchar(255) DEFAULT NULL,
  `saga_id` varchar(255) NOT NULL,
  `saga_type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `IDXk45eqnxkgd8hpdn6xixn8sgft` (`saga_type`,`association_key`,`association_value`),
  KEY `IDXgv5k1v2mh6frxuy5c0hgbau94` (`saga_id`,`saga_type`)
);

CREATE TABLE `domain_event_entry` (
  `global_index` bigint(20) NOT NULL AUTO_INCREMENT,
  `event_identifier` varchar(255) NOT NULL,
  `meta_data` longblob,
  `payload` longblob NOT NULL,
  `payload_revision` varchar(255) DEFAULT NULL,
  `payload_type` varchar(255) NOT NULL,
  `time_stamp` varchar(255) NOT NULL,
  `aggregate_identifier` varchar(255) NOT NULL,
  `sequence_number` bigint(20) NOT NULL,
  `type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`global_index`),
  UNIQUE KEY `UK8s1f994p4la2ipb13me2xqm1w` (`aggregate_identifier`,`sequence_number`),
  UNIQUE KEY `UK_fwe6lsa8bfo6hyas6ud3m8c7x` (`event_identifier`)
) ;

CREATE TABLE `saga_entry` (
  `saga_id` varchar(255) NOT NULL,
  `revision` varchar(255) DEFAULT NULL,
  `saga_type` varchar(255) DEFAULT NULL,
  `serialized_saga` longblob,
  PRIMARY KEY (`saga_id`)
) ;

CREATE TABLE `snapshot_event_entry` (
  `aggregate_identifier` varchar(255) NOT NULL,
  `sequence_number` bigint(20) NOT NULL,
  `type` varchar(255) NOT NULL,
  `event_identifier` varchar(255) NOT NULL,
  `meta_data` longblob,
  `payload` longblob NOT NULL,
  `payload_revision` varchar(255) DEFAULT NULL,
  `payload_type` varchar(255) NOT NULL,
  `time_stamp` varchar(255) NOT NULL,
  PRIMARY KEY (`aggregate_identifier`,`sequence_number`,`type`),
  UNIQUE KEY `UK_e1uucjseo68gopmnd0vgdl44h` (`event_identifier`)
) ;

CREATE TABLE `token_entry` (
  `processor_name` varchar(255) NOT NULL,
  `segment` int(11) NOT NULL,
  `owner` varchar(255) DEFAULT NULL,
  `timestamp` varchar(255) NOT NULL,
  `token` longblob,
  `token_type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`processor_name`,`segment`)
) ;
