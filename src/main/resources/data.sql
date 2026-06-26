USE desdeaqui;

-- Roles
INSERT INTO roles (nombre) VALUES ('ADMIN');
INSERT INTO roles (nombre) VALUES ('VIAJERO');

-- Usuarios de prueba
-- Contraseña de ambos: 1234  (en texto plano por ahora, en fase de seguridad se encripta)
INSERT INTO usuarios (nombre, correo, contrasena, rol_id) VALUES
    ('Admin Desde Aquí', 'admin@desdeaqui.com', '1234', 1),
    ('Hairon',           'hairon@correo.com',   '1234', 2);

-- Destinos
INSERT INTO destinos (nombre, descripcion, imagen) VALUES
    ('Tamarindo',     'Playa famosa por el surf y los atardeceres en el Pacífico Norte.',     'tamarindo.png'),
    ('Volcán Arenal', 'Volcán activo rodeado de aguas termales y naturaleza exuberante.',     'arenal.png'),
    ('Tortuguero',    'Canales del Caribe y santuario de tortugas marinas.',                  'tortuguero.png'),
    ('Uvita',         'Hogar de la Ballena, playas tranquilas y avistamiento de ballenas.',   'uvita.png'),
    ('Golfito',       'Bahía tranquila en el sur, ideal para pesca y naturaleza.',            'golfito.png');

-- Tips de Tamarindo
INSERT INTO tips (contenido, categoria, destino_id) VALUES
    ('Los buses desde San José salen de la Terminal Alfaro, el viaje dura unas 5 horas.', 'transporte', 1),
    ('El Coconut Cafe tiene el mejor desayuno del pueblo a buen precio.', 'comida', 1),
    ('La zona hotelera de Langosta es más tranquila y económica que el centro.', 'hospedaje', 1),
    ('La Playa Grande, a 10 minutos, es mucho menos concurrida y muy bonita.', 'lugares', 1);

-- Tips de Volcán Arenal
INSERT INTO tips (contenido, categoria, destino_id) VALUES
    ('Alquiler de carro es lo más cómodo, pero hay buses desde San José a La Fortuna.', 'transporte', 2),
    ('El Restaurante Nene en La Fortuna tiene comida típica excelente y barata.', 'comida', 2),
    ('Los hoteles cerca del lago tienen mejor vista del volcán que los del centro.', 'hospedaje', 2),
    ('Las aguas termales Tabacón son las más conocidas, pero Baldi es más accesible.', 'lugares', 2);

-- Tips de Tortuguero
INSERT INTO tips (contenido, categoria, destino_id) VALUES
    ('Solo se llega en bote o avioneta, no hay carretera. Salís desde Caño Blanco.', 'transporte', 3),
    ('El pueblo tiene varios sodas económicas con casado y mariscos frescos.', 'comida', 3),
    ('Las cabinas Miss Junie son muy recomendadas por viajeros locales.', 'hospedaje', 3),
    ('El tour de tortugas es de noche entre julio y octubre, reservá con anticipación.', 'lugares', 3);

-- Tips de Uvita
INSERT INTO tips (contenido, categoria, destino_id) VALUES
    ('Desde San José tomás el bus a Dominical o Palmar y bajás en Uvita.', 'transporte', 4),
    ('En el pueblo hay una feria de agricultores los sábados con frutas frescas.', 'comida', 4),
    ('Los hostels cerca de la entrada al Parque Marino Ballena son los más convenientes.', 'hospedaje', 4),
    ('La cola de ballena solo se puede caminar con marea baja, revisá las tablas de marea.', 'lugares', 4);

-- Tips de Golfito
INSERT INTO tips (contenido, categoria, destino_id) VALUES
    ('Hay vuelos desde San José con Sansa, o bus por la costanera sur.', 'transporte', 5),
    ('El muelle tiene buenas sodas con mariscos recién pescados.', 'comida', 5),
    ('El sector de la zona americana tiene opciones de hospedaje económico.', 'hospedaje', 5),
    ('El Parque Nacional Piedras Blancas está muy cerca y casi no tiene turistas.', 'lugares', 5);