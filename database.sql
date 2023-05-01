CREATE DATABASE balloons;

USE balloons;

CREATE TABLE `player_balloons` (
  `player_uuid` varchar(255) NOT NULL,
  `balloon_type` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Indexes for table `player_balloons`
--
ALTER TABLE `player_balloons`
  ADD PRIMARY KEY (`player_uuid`);
COMMIT;
