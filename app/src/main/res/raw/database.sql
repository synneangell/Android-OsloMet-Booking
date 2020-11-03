DROP TABLE Hus;
DROP TABLE Rom;
DROP TABLE Reservasjon;

CREATE TABLE Hus (
    HusID INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    Beskrivelse VARCHAR(30) NOT NULL,
    GateAdresse VARCHAR(30) NOT NULL,
    Antall_etasjer INTEGER NOT NULL,
    Gps_koordinater VARCHAR(50) NOT NULL
);

CREATE TABLE Rom (
	RomId INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL,
	Beskrivelse VARCHAR(30) NOT NULL,
	Etasjenr INTEGER NOT NULL,
	Romnr INTEGER NOT NULL,
	kapasitet INTEGER NOT NULL,
	FOREIGN KEY(ReservasjonId) PREFERENCES Hus(HusID)
);

CREATE TABLE Reservasjon (
    ReservasjonId INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL,
	Navn VARCHAR(15) NOT NULL,
	TidFra VARCHAR(5) NOT NULL,
	TidTil VARCHAR(5) NOT NULL,
	Date VARCHAR(10) NOT NULL,
	FOREIGN KEY(HusID) PREFERENCES Hus(HudID),
	FOREIGN KEY (RomID) PREFERENCES Rom(RomID)
);

INSERT INTO Hus values ('', 'P35', 'Pilestredet 35', 8, '59.920503, 10.735504');
INSERT INTO Hus values ('', 'P52', 'Pilestredet 52', 3, '59.922588, 10.732752');

INSERT INTO Rom values ();
INSERT INTO Rom values ();
INSERT INTO Rom values ();

INSERT INTO Reservasjon values ('', 'Gudrun Pedersen', '10:30', '12:30', '12/12/2020');
INSERT INTO Reservasjon values ('', 'Jakob Anthonsen', '08:00', '11:30', '10/12/2020');
INSERT INTO Reservasjon values ('', 'Storm Andersen', '09:30', '12:30', '04/12/2020');
INSERT INTO Reservasjon values ('', 'Thomas Ruud', '12:00', '16:00', '08/12/2020');
INSERT INTO Reservasjon values ('', 'Stina Pettersen', '09:30', '11:00', '19/12/2020');
INSERT INTO Reservasjon values ('', 'Håkon Gunvaldsen', '10:30', '12:30', '01/12/2020');
INSERT INTO Reservasjon values ('', 'Eirik Gunnarsen', '10:00', '14:00', '15/12/2020');
INSERT INTO Reservasjon values ('', 'Ruben Amundsen', '08:30', '12:00', '16/12/2020');
INSERT INTO Reservasjon values ('', 'Astrid Isaksen', '11:30', '12:30', '03/12/2020');
INSERT INTO Reservasjon values ('', 'Roar Simonsen', '12:30', '15:30', '02/12/2020');
INSERT INTO Reservasjon values ('', 'Gunne Omdahl', '12:30', '15:30', '01/12/2020');
INSERT INTO Reservasjon values ('', 'Baard Christensen', '08:00', '11:00', '18/12/2020');
