CREATE table Pacient (
                         idpac serial primary key ,
                         f text not null ,
                         i text not null ,
                         o text not null ,
                         polis varchar(16) not null
);
CREATE table Doctor (
                        iddoc serial primary key ,
                        f text not null ,
                        i text not null ,
                        o text not null ,
                        ecp varchar(16) not null
);
CREATE table Preparat (
                          idpre serial primary key ,
                          kolvo int not null ,
                          sppr text not null ,
                          edizm text not null
);
CREATE table Recept (
                        idrec serial primary key ,
                        dateof timestamp not null ,
                        srok int not null ,
                        status text not null ,
                        diagnoz text not null ,
                        qr text not null,
                        iddoc int not null,
                        idpac int not null,
                        constraint fk_iddoc foreign key (iddoc) references Doctor(iddoc) on update cascade,
                        constraint fk_idpac foreign key (idpac) references Pacient(idpac) on update cascade

);
CREATE table PreparatRecept (
                                idprerec serial primary key ,
                                vipisano int not null ,
                                doza int not null ,
                                kolvodoz int not null ,
                                kolvokurs int not null ,
                                kurs text not null,
                                idrec int not null,
                                idpre int not null,
                                constraint fk_idrec foreign key (idrec) references Recept(idrec) on update cascade,
                                constraint fk_idpre foreign key (idpre) references Preparat(idpre) on update cascade
);

insert into pacient(f, i, o, polis) VALUES ('Иванов','Иван','Иванович','0000000000000000');
insert into pacient(f, i, o, polis) VALUES ('Петров','Петр','Петрович','1111111111111111');

insert into doctor(f, i, o, ecp) VALUES ('Докторов','Доктор','Докторович','эцп');

insert into preparat(kolvo, sppr, edizm) VALUES (12,'таблетка','таблетка');

insert into recept(dateof, srok, status, diagnoz, qr, iddoc, idpac) VALUES (current_timestamp,2,'активен','орви','qr',1,1);

insert into preparatrecept(vipisano, doza, kolvodoz, kolvokurs, kurs, idrec, idpre) VALUES (1,1,1,1,'месяц',1,1);
insert into preparatrecept(vipisano, doza, kolvodoz, kolvokurs, kurs, idrec, idpre) VALUES (2,1,1,1,'месяц',1,1);

insert into recept(dateof, srok, status, diagnoz, qr, iddoc, idpac) VALUES (current_timestamp,2,'активен','орви','qr',1,1);
insert into recept(dateof, srok, status, diagnoz, qr, iddoc, idpac) VALUES (current_timestamp,2,'обслужен','орви','qr',1,1);

update recept set status = 'активен' where idrec = 1;