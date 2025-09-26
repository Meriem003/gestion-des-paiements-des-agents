-- Table Département

CREATE TABLE IF NOT EXISTS departement (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(100) NOT NULL
);

-- Table Agent

CREATE TABLE IF NOT EXISTS agent (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    mot_de_passe VARCHAR(255) NOT NULL,
    type_agent ENUM(
        'OUVRIER',
        'RESPONSABLE_DEPARTEMENT',
        'DIRECTEUR',
        'STAGIAIRE'
    ) NOT NULL,
    departement_id INT,
    est_responsable_departement BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (departement_id) REFERENCES departement (id) ON DELETE SET NULL
);

-- Table Paiement

CREATE TABLE IF NOT EXISTS paiement (
    id INT PRIMARY KEY AUTO_INCREMENT,
    type_paiement ENUM(
        'SALAIRE',
        'PRIME',
        'BONUS',
        'INDEMNITE'
    ) NOT NULL,
    montant DECIMAL(10, 2) NOT NULL CHECK (montant >= 0),
    date_paiement DATE NOT NULL,
    motif VARCHAR(255),
    condition_validee BOOLEAN DEFAULT FALSE,
    agent_id INT NOT NULL,
    FOREIGN KEY (agent_id) REFERENCES agent (id) ON DELETE CASCADE
);

-- Départements
INSERT INTO departement (nom) VALUES
('Informatique'),
('Ressources Humaines'),
('Comptabilité'),
('Production');

-- Agents
INSERT INTO agent (nom, prenom, email, mot_de_passe, type_agent, departement_id, est_responsable_departement) VALUES
('Dupont', 'Jean', 'jean.dupont@example.com', 'pass123', 'OUVRIER', 4, FALSE),
('Martin', 'Sophie', 'sophie.martin@example.com', 'pass123', 'RESPONSABLE_DEPARTEMENT', 1, TRUE),
('Durand', 'Paul', 'paul.durand@example.com', 'pass123', 'DIRECTEUR', 2, FALSE),
('Leroy', 'Claire', 'claire.leroy@example.com', 'pass123', 'STAGIAIRE', 3, FALSE),
('Moreau', 'Karim', 'karim.moreau@example.com', 'pass123', 'OUVRIER', 1, FALSE);

-- Paiements
INSERT INTO paiement (type_paiement, montant, date_paiement, motif, condition_validee, agent_id) VALUES
('SALAIRE', 5000.00, '2025-09-01', 'Salaire mensuel', TRUE, 1),
('PRIME', 800.00, '2025-09-10', 'Prime de performance', TRUE, 2),
('BONUS', 1200.00, '2025-09-15', 'Bonus projet terminé', TRUE, 3),
('INDEMNITE', 300.00, '2025-09-20', 'Indemnité transport', TRUE, 4),
('SALAIRE', 4500.00, '2025-09-01', 'Salaire mensuel', TRUE, 5);